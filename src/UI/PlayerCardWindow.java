package UI;

import Core.Card;
import Players.Player;
import Patterns.Structural.Decorator.ICardNumberFormatter;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class PlayerCardWindow extends JFrame {

    private Player player;
    private Map<Card, JEditorPane> cardDisplayPanes;
    private JFrame parentFrame;

    // Dimensiones base para un solo cartón
    // ¡¡AJUSTA ESTOS VALORES HASTA QUE SE VEAN BIEN!!
    // Vamos a ser más generosos esta vez.
    private static final int CARD_WIDTH_BASE = 280; // Aumentado significativamente
    private static final int CARD_HEIGHT_BASE = 320; // Aumentado significativamente
    
    private static final int PADDING = 20; // Aumentado para más espacio entre elementos y bordes
    private static final int TITLE_HEIGHT = 70; // Más espacio para el título

    public PlayerCardWindow(Player player, JFrame parentFrame) {
        this.player = player;
        this.parentFrame = parentFrame;
        this.cardDisplayPanes = new HashMap<>();

        setTitle("Cartones de " + player.getName());
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // GameWindowGUI se encarga de cerrar

        calculateAndSetWindowSize();
        initUI();
    }

    private void calculateAndSetWindowSize() {
        int numCards = player.getCards().size();
        int rows = 1;
        int cols = numCards;

        if (numCards > 2) { // Si hay 3 o 4 cartones, usar 2 columnas
            cols = 2;
            rows = (int) Math.ceil((double) numCards / cols);
        }

        // Calcular el ancho y alto total de la ventana
        int totalWidth = cols * CARD_WIDTH_BASE + (cols + 1) * PADDING;
        int totalHeight = rows * CARD_HEIGHT_BASE + (rows + 1) * PADDING + TITLE_HEIGHT;
        
        setSize(totalWidth, totalHeight);
        setResizable(false);
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING)); // Padding general para la ventana

        JLabel playerNameLabel = new JLabel(player.getName(), SwingConstants.CENTER);
        playerNameLabel.setFont(new Font("Arial", Font.BOLD, 24)); // Fuente aún más grande para el nombre
        playerNameLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, PADDING, 0)); 
        mainPanel.add(playerNameLabel, BorderLayout.NORTH);

        JPanel cardsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(PADDING, PADDING, PADDING, PADDING); // Espacio entre cada JEditorPane
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        int currentCardIndex = 0;
        int maxCols = (player.getCards().size() > 2) ? 2 : player.getCards().size();

        for (Card card : player.getCards()) {
            JEditorPane cardPane = new JEditorPane();
            cardPane.setContentType("text/html");
            cardPane.setEditable(false);
            cardPane.setOpaque(true);
            cardPane.setBackground(Color.WHITE); // Fondo blanco para el cartón

            // Establecer el tamaño preferido para cada JEditorPane
            // Es CRUCIAL que este tamaño sea SUFICIENTE para el HTML renderizado.
            cardPane.setPreferredSize(new Dimension(CARD_WIDTH_BASE - (2 * PADDING), CARD_HEIGHT_BASE - (2 * PADDING)));
            // Resto el padding del contenedor del tamaño base para que el JEditorPane
            // quede del tamaño "esperado" para la tabla HTML.

            // Añadir un borde visual alrededor de cada JEditorPane para que se vea como un cartón individual
            cardPane.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2)); // Borde negro de 2px de grosor

            cardDisplayPanes.put(card, cardPane);
            gbc.gridx = currentCardIndex % maxCols;
            gbc.gridy = currentCardIndex / maxCols;
            cardsPanel.add(cardPane, gbc);

            currentCardIndex++;
        }
        mainPanel.add(cardsPanel, BorderLayout.CENTER);
        add(mainPanel);

        updateCardsDisplay(null);
    }

    public void updateCardsDisplay(Integer lastCalledBall) {
        for (Map.Entry<Card, JEditorPane> entry : cardDisplayPanes.entrySet()) {
            Card card = entry.getKey();
            JEditorPane pane = entry.getValue();
            pane.setText(player.getCardDisplayString(card, lastCalledBall));
            pane.revalidate();
            pane.repaint();
        }
    }

    public void setLocationBasedOnIndex(int index) {
        if (parentFrame == null) return;

        int parentX = parentFrame.getX();
        int parentY = parentFrame.getY();
        int parentWidth = parentFrame.getWidth();
        int parentHeight = parentFrame.getHeight();

        int windowWidth = getWidth();
        int windowHeight = getHeight();

        int x, y;

        x = parentX + parentWidth + 20; // Margen fijo desde la ventana principal
        y = parentY + (index * (windowHeight / 3));

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        if (x + windowWidth > screen.width - 20) {
            x = screen.width - windowWidth - 20;
            if (x < 0) x = 0;
        }
        if (y + windowHeight > screen.height - 20) {
            y = screen.height - windowHeight - 20;
            if (y < 0) y = 0;
        }

        setLocation(x, y);
    }
}