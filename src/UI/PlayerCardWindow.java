package UI;

import Core.Card;
import Players.Player;
import Patterns.Structural.Decorator.ICardNumberFormatter;

import javax.swing.*;
import javax.swing.border.Border; // Importar Border
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerCardWindow extends JFrame {

    private Player player;
    private Map<Card, JEditorPane> cardDisplayPanes;
    private JFrame parentFrame; 

    public PlayerCardWindow(Player player, JFrame parentFrame) {
        this.player = player;
        this.parentFrame = parentFrame; 
        this.cardDisplayPanes = new HashMap<>();

        setTitle("Cartones de " + player.getName());
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        // Ajusta el tamaño inicial para acomodar el nuevo diseño y bordes
        int cardWidth = 180;
        int cardHeight = 150;
        int padding = 15; // Espacio interno del panel principal
        int cardMargin = 10; // Margen entre cartones y bordes del panel GridBag
        
        if (player.getCards().size() <= 2) {
            setSize(cardWidth + (padding * 2) + (cardMargin * 2), 
                    (cardHeight * player.getCards().size()) + (padding * 2) + (cardMargin * 2) + 50); // +50 para el nombre del jugador
        } else { // 3 cartones (2 columnas)
            setSize((cardWidth * 2) + (padding * 2) + (cardMargin * 4), // Doble ancho + márgenes extra
                    (cardHeight * 2) + (padding * 2) + (cardMargin * 2) + 50); // Alto para 2 cartones apilados
        }
        setResizable(false); 

        initUI();
    }

    private void initUI() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel playerNameLabel = new JLabel(player.getName());
        playerNameLabel.setFont(new Font("Arial", Font.BOLD, 18));
        playerNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(playerNameLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JPanel cardsContainerPanel = new JPanel(new GridBagLayout()); 
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Márgenes externos para los cartones
        gbc.fill = GridBagConstraints.BOTH; 

        List<Card> playerCards = player.getCards();

        // Calcular el tamaño preferido de un solo cartón
        Dimension singleCardDim = new Dimension(180, 150); 
        // Definir un borde para cada cartón
        Border cardBorder = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY.darker(), 2), // Borde grueso para visibilidad
            BorderFactory.createEmptyBorder(5, 5, 5, 5) // Espacio interno del borde
        );

        for (int i = 0; i < playerCards.size(); i++) {
            Card card = playerCards.get(i);
            JEditorPane cardPane = new JEditorPane();
            cardPane.setEditable(false);
            cardPane.setContentType("text/html");
            cardPane.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
            cardPane.setFont(new Font("Monospaced", Font.PLAIN, 14)); // Podrías ajustar el tamaño si es muy grande
            cardPane.setPreferredSize(singleCardDim);
            cardPane.setMinimumSize(singleCardDim); // Asegura que no se haga más pequeño
            cardPane.setMaximumSize(singleCardDim); // Asegura que no se haga más grande de lo necesario
            cardPane.setBorder(cardBorder); // <--- APLICA EL BORDE AQUÍ

            // NOTA: El padding interno del contenido HTML se manejará dentro de getCardDisplayString
            // o directamente en el HTML que se genera.

            String cardHtmlContent = "<html><body style='font-family:Monospaced; font-size:14pt; padding: 5px;'>" + // <--- AÑADE PADDING AL BODY
                                     player.getCardDisplayString(card, null) + 
                                     "</body></html>";
            cardPane.setText(cardHtmlContent);
            cardDisplayPanes.put(card, cardPane); 

            if (i == 0) { 
                gbc.gridx = 0;
                gbc.gridy = 0;
            } else if (i == 1) { 
                gbc.gridx = 0;
                gbc.gridy = 1;
            } else if (i == 2) { 
                gbc.gridx = 1; 
                gbc.gridy = 0; 
            }
            cardsContainerPanel.add(cardPane, gbc);
        }

        JScrollPane scrollPane = new JScrollPane(cardsContainerPanel); 
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        // Ajuste más preciso del tamaño del scroll pane para evitar scrollbars innecesarios
        if (playerCards.size() <= 2) {
            scrollPane.setPreferredSize(new Dimension(singleCardDim.width + (gbc.insets.left + gbc.insets.right) * 2, 
                                                     (singleCardDim.height * playerCards.size()) + (gbc.insets.top + gbc.insets.bottom) * playerCards.size())); 
        } else { // 3 cartones
            scrollPane.setPreferredSize(new Dimension((singleCardDim.width * 2) + (gbc.insets.left + gbc.insets.right) * 3, 
                                                     (singleCardDim.height * 2) + (gbc.insets.top + gbc.insets.bottom) * 2)); 
        }

        mainPanel.add(scrollPane);
        setContentPane(mainPanel);
    }

    public void updateCardsDisplay(Integer lastCalledBall) {
        for (Map.Entry<Card, JEditorPane> entry : cardDisplayPanes.entrySet()) {
            Card card = entry.getKey();
            JEditorPane cardPane = entry.getValue();
            // Asegúrate de que el padding se aplica también al actualizar
            String cardHtmlContent = "<html><body style='font-family:Monospaced; font-size:14pt; padding: 5px;'>" + 
                                     player.getCardDisplayString(card, lastCalledBall) +
                                     "</body></html>";
            cardPane.setText(cardHtmlContent);
        }
    }

    // ... (El método setLocationBasedOnIndex() permanece igual que en la última versión) ...
    public void setLocationBasedOnIndex(int index) {
        if (parentFrame == null) {
            super.setLocation(index * 50, index * 50); 
            return;
        }

        Point parentLoc = parentFrame.getLocationOnScreen(); 
        Dimension parentSize = parentFrame.getSize();       
        Dimension playerWinSize = this.getSize();           

        int x = parentLoc.x;
        int y = parentLoc.y;
        
        int horizontalMargin = 10; 
        int verticalMargin = 10;
        
        switch (index) {
            case 0: // Jugador 1: Izquierda de la ventana principal
                x = parentLoc.x - playerWinSize.width - horizontalMargin;
                y = parentLoc.y;
                break;
            case 1: // Jugador 2: Izquierda de la ventana principal, debajo del jugador 1
                x = parentLoc.x - playerWinSize.width - horizontalMargin;
                y = parentLoc.y + playerWinSize.height + verticalMargin; 
                break;
            case 2: // Jugador 3: Derecha de la ventana principal
                x = parentLoc.x + parentSize.width + horizontalMargin;
                y = parentLoc.y;
                break;
            case 3: // Jugador 4: Derecha de la ventana principal, debajo del jugador 3
                x = parentLoc.x + parentSize.width + horizontalMargin;
                y = parentLoc.y + playerWinSize.height + verticalMargin; 
                break;
            default: // Para más de 4 jugadores (si los permitieras)
                x = parentLoc.x + parentSize.width + horizontalMargin;
                y = parentLoc.y + (index - 2) * (playerWinSize.height + verticalMargin);
                break;
        }
        
        x = Math.max(0, x);
        y = Math.max(0, y);

        super.setLocation(x, y);
    }
}