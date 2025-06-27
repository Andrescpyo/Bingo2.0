// Carpeta: UI
// Archivo: PlayerCardWindow.java
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
    // Mantenemos los últimos valores, pero nos enfocaremos en la altura de la ventana.
    private static final int CARD_WIDTH_BASE = 180;
    private static final int CARD_HEIGHT_BASE = 210;
    
    private static final int PADDING = 8; 
    private static final int TITLE_HEIGHT = 40; 

    // Nuevo: Un ajuste extra de altura para ventanas de una sola fila
    private static final int SINGLE_ROW_HEIGHT_ADJUSTMENT = 20; // Añadir 20 píxeles extra si es una sola fila

    public PlayerCardWindow(Player player, JFrame parentFrame) {
        this.player = player;
        this.parentFrame = parentFrame;
        this.cardDisplayPanes = new HashMap<>();

        setTitle("Cartones de " + player.getName());
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); 
        
        calculateAndSetWindowSize();
        initUI();
    }

    private void calculateAndSetWindowSize() {
        int numCards = player.getCards().size();
        int rows = 1;
        int cols = numCards;

        if (numCards > 2) { 
            cols = 2; 
            rows = (int) Math.ceil((double) numCards / cols);
        }

        int totalWidth = cols * CARD_WIDTH_BASE + (cols + 1) * PADDING;
        int totalHeight = rows * CARD_HEIGHT_BASE + (rows + 1) * PADDING + TITLE_HEIGHT;
        
        // ¡Ajuste clave aquí!
        if (rows == 1) { // Si solo hay una fila de cartones (1 o 2 cartones)
            totalHeight += SINGLE_ROW_HEIGHT_ADJUSTMENT; // Añadir altura extra
        }

        setSize(totalWidth, totalHeight);
        setResizable(false);
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING)); 

        JLabel playerNameLabel = new JLabel(player.getName(), SwingConstants.CENTER);
        playerNameLabel.setFont(new Font("Arial", Font.BOLD, 18)); 
        playerNameLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, PADDING, 0)); 
        mainPanel.add(playerNameLabel, BorderLayout.NORTH);

        JPanel cardsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(PADDING, PADDING, PADDING, PADDING); 
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
            cardPane.setBackground(Color.WHITE); 

            // El tamaño preferido para cada JEditorPane puede necesitar un ligero ajuste aquí si los números se cortan
            // Mantendremos la misma lógica de antes: CARD_BASE - (2 * PADDING)
            cardPane.setPreferredSize(new Dimension(CARD_WIDTH_BASE - (2 * PADDING), CARD_HEIGHT_BASE - (2 * PADDING)));
            
            cardPane.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2)); 

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

        x = parentX + parentWidth + 10; 
        y = parentY + (index * (windowHeight / 6)); 

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        if (x + windowWidth > screen.width - 10) { 
            x = screen.width - windowWidth - 10;
            if (x < 0) x = 0;
        }
        if (y + windowHeight > screen.height - 10) { 
            y = screen.height - windowHeight - 10;
            if (y < 0) y = 0;
        }

        setLocation(x, y);
    }
}