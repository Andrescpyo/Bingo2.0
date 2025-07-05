package UI;

import Core.Card;
import Players.Player;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class PlayerCardWindow extends JFrame {

    private Player player;
    private Map<Card, JEditorPane> cardDisplayPanes;
    private JFrame parentFrame;

    // Restablecer estas dimensiones base a un tamaño "medio", manteniendo las ventanas grandes.
    // Esto significa que el JEditorPane será más grande que la tabla HTML interna.
    private static final int CARD_WIDTH_BASE = 200; // Volvemos a un tamaño que te gustaba para la ventana.
    private static final int CARD_HEIGHT_BASE = 200; // Volvemos a un tamaño que te gustaba para la ventana.

    // Mantenemos el padding razonable para la interfaz.
    private static final int PADDING = 10;
    // Espacio para el título y el nombre del jugador.
    private static final int TITLE_HEIGHT = 45;

    // Ajuste extra de altura si solo hay una fila de cartones.
    private static final int SINGLE_ROW_HEIGHT_ADJUSTMENT = 30;

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

        if (rows == 1) {
            totalHeight += SINGLE_ROW_HEIGHT_ADJUSTMENT;
        }

        setSize(totalWidth, totalHeight);
        setResizable(false);
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING));
        mainPanel.setBackground(new Color(255, 240, 200));

        JLabel playerNameLabel = new JLabel(player.getName(), SwingConstants.CENTER);
        playerNameLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 22)); // Volvemos a la fuente original de 22pt.
        playerNameLabel.setForeground(new Color(139, 69, 19));
        playerNameLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, PADDING, 0));
        mainPanel.add(playerNameLabel, BorderLayout.NORTH);

        JPanel cardsPanel = new JPanel(new GridBagLayout());
        cardsPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(PADDING, PADDING, PADDING, PADDING);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 0.01;

        int currentCardIndex = 0;
        int maxCols = (player.getCards().size() > 2) ? 2 : player.getCards().size();

        for (Card card : player.getCards()) {
            JEditorPane cardPane = new JEditorPane();
            cardPane.setContentType("text/html");
            cardPane.setEditable(false);
            cardPane.setOpaque(true);
            cardPane.setBackground(Color.WHITE);

            // ¡IMPORTANTE! El tamaño preferido del JEditorPane es ahora más grande
            // que el contenido HTML real, dando espacio para que la tabla no se corte.
            cardPane.setPreferredSize(new Dimension(CARD_WIDTH_BASE - (2 * PADDING), CARD_HEIGHT_BASE - (2 * PADDING)));

            cardPane.setBorder(BorderFactory.createLineBorder(new Color(0, 150, 136), 4)); // Volvemos al borde de 4px.

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
        if (parentFrame == null) {
            return;
        }

        int parentX = parentFrame.getX();
        int parentY = parentFrame.getY();
        int parentWidth = parentFrame.getWidth();

        int windowWidth = getWidth();
        int windowHeight = getHeight();

        int x, y;

        x = parentX + parentWidth + 10;
        y = parentY + (index * (windowHeight + 10)); // Espacio de 10px entre cada ventana

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        if (x + windowWidth > screen.width - 10) {
            x = screen.width - windowWidth - 10;
            if (x < 0) {
                x = 0;
            }
        }
        if (y + windowHeight > screen.height - 10) {
            y = screen.height - windowHeight - 10;
            if (y < 0) {
                y = 0;
            }
        }

        setLocation(x, y);
    }

    public String getPlayerName() {
        return player.getName();
    }

}
