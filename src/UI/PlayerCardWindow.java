package UI;

import Core.Card; 
import Players.Player; 

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Clase PlayerCardWindow
 *
 * Esta clase representa una ventana separada en la interfaz gráfica de usuario
 * que muestra los cartones de Bingo de un jugador específico.
 * Utiliza JEditorPane para renderizar los cartones como HTML, permitiendo estilos
 * y resaltados dinámicos (gracias al patrón Decorator implementado en el formateador
 * de números de cartón del jugador).
 *
 * No es un participante directo en un patrón de diseño GoF como rol principal,
 * pero es un componente de la capa de presentación que colabora con el patrón Decorator
 * (a través de {@link Players.Player#getCardDisplayString}) y es orquestado por el
 * {@link GameWindowGUI}.
 */
public class PlayerCardWindow extends JFrame {

    /**
     * El jugador al que pertenecen los cartones mostrados en esta ventana.
     */
    private Player player;

    /**
     * Un mapa que asocia cada objeto Card del jugador con su correspondiente JEditorPane
     * en la interfaz, para facilitar la actualización.
     */
    private Map<Card, JEditorPane> cardDisplayPanes;

    /**
     * Una referencia a la ventana principal del juego (GameWindowGUI),
     * utilizada para posicionar las ventanas de los jugadores de forma relativa.
     */
    private JFrame parentFrame;

    // Dimensiones base para el tamaño de cada JEditorPane que representa un cartón.
    // Se han ajustado para ser un tamaño "medio" que visualmente funciona bien.
    private static final int CARD_WIDTH_BASE = 200;
    private static final int CARD_HEIGHT_BASE = 200;

    // Espaciado interno y externo para los componentes.
    private static final int PADDING = 10;
    // Espacio reservado para el título de la ventana y el nombre del jugador.
    private static final int TITLE_HEIGHT = 45;

    // Ajuste de altura adicional si solo hay una fila de cartones, para centrar mejor.
    private static final int SINGLE_ROW_HEIGHT_ADJUSTMENT = 30;

    /**
     * Constructor para PlayerCardWindow.
     *
     * @param player El objeto {@link Player} cuyos cartones se mostrarán.
     * @param parentFrame La ventana principal del juego ({@link GameWindowGUI}) para posicionamiento relativo.
     */
    public PlayerCardWindow(Player player, JFrame parentFrame) {
        this.player = player;
        this.parentFrame = parentFrame;
        this.cardDisplayPanes = new HashMap<>(); // Inicializa el mapa de JEditorPanes.

        setTitle("Cartones de " + player.getName()); // Título de la ventana.
        // Evita que la ventana se cierre individualmente, solo se cerrará con la ventana principal.
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        calculateAndSetWindowSize(); // Calcula y establece el tamaño de la ventana según el número de cartones.
        initUI(); // Inicializa los componentes de la interfaz de usuario.
    }

    /**
     * Calcula el tamaño óptimo de la ventana basándose en la cantidad de cartones
     * del jugador y establece las dimensiones de la ventana.
     */
    private void calculateAndSetWindowSize() {
        int numCards = player.getCards().size();
        int rows = 1;
        int cols = numCards;

        // Si hay más de 2 cartones, organiza en 2 columnas para una mejor visualización.
        if (numCards > 2) {
            cols = 2;
            rows = (int) Math.ceil((double) numCards / cols); // Calcula las filas necesarias.
        }

        // Calcula el ancho total: columnas * ancho base del cartón + padding entre columnas y en los bordes.
        int totalWidth = cols * CARD_WIDTH_BASE + (cols + 1) * PADDING;
        // Calcula la altura total: filas * alto base del cartón + padding entre filas y en los bordes + altura del título.
        int totalHeight = rows * CARD_HEIGHT_BASE + (rows + 1) * PADDING + TITLE_HEIGHT;

        // Ajuste adicional para el caso de una sola fila, para que el contenido se vea mejor centrado.
        if (rows == 1) {
            totalHeight += SINGLE_ROW_HEIGHT_ADJUSTMENT;
        }

        setSize(totalWidth, totalHeight); // Establece el tamaño de la ventana.
        setResizable(false); // La ventana no es redimensionable.
    }

    /**
     * Inicializa y organiza los componentes de la interfaz de usuario dentro de esta ventana.
     * Crea un JEditorPane para cada cartón del jugador.
     */
    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING));
        mainPanel.setBackground(new Color(255, 240, 200)); // Fondo de color crema/naranja muy suave.

        // JLabel para mostrar el nombre del jugador.
        JLabel playerNameLabel = new JLabel(player.getName(), SwingConstants.CENTER);
        playerNameLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 22)); // Fuente estilo cartoon.
        playerNameLabel.setForeground(new Color(139, 69, 19)); // Color marrón oscuro.
        playerNameLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, PADDING, 0)); // Espacio inferior.
        mainPanel.add(playerNameLabel, BorderLayout.NORTH); // Añade el nombre en la parte superior.

        // Panel que contendrá los cartones, usando GridBagLayout para una disposición flexible.
        JPanel cardsPanel = new JPanel(new GridBagLayout());
        cardsPanel.setOpaque(false); // Transparente para que se vea el fondo del mainPanel.
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(PADDING, PADDING, PADDING, PADDING); // Padding alrededor de cada cartón.
        gbc.fill = GridBagConstraints.BOTH; // Los componentes se estiran en ambas direcciones.
        gbc.weightx = 1.0; // Los cartones pueden crecer horizontalmente.
        gbc.weighty = 0.01; // Pequeño peso vertical para permitir un poco de crecimiento.

        int currentCardIndex = 0;
        // Determina el número máximo de columnas (1 o 2).
        int maxCols = (player.getCards().size() > 2) ? 2 : player.getCards().size();

        // Itera sobre cada cartón del jugador para crear su JEditorPane correspondiente.
        for (Card card : player.getCards()) {
            JEditorPane cardPane = new JEditorPane();
            cardPane.setContentType("text/html"); // Importante: para renderizar HTML.
            cardPane.setEditable(false); // No editable.
            cardPane.setOpaque(true);
            cardPane.setBackground(Color.WHITE); // Fondo blanco para el cartón.

            // Establece el tamaño preferido del JEditorPane.
            // Aunque el contenido HTML de la tabla será más pequeño, el JEditorPane en sí
            // debe tener el tamaño suficiente para contener el HTML sin cortar bordes.
            cardPane.setPreferredSize(new Dimension(CARD_WIDTH_BASE - (2 * PADDING), CARD_HEIGHT_BASE - (2 * PADDING)));

            // Borde del cartón.
            cardPane.setBorder(BorderFactory.createLineBorder(new Color(0, 150, 136), 4)); // Color teal, grosor 4px.

            cardDisplayPanes.put(card, cardPane); // Almacena el JEditorPane asociado a la Card.

            // Posiciona el JEditorPane en el GridBagLayout.
            gbc.gridx = currentCardIndex % maxCols; // Columna.
            gbc.gridy = currentCardIndex / maxCols; // Fila.
            cardsPanel.add(cardPane, gbc);

            currentCardIndex++;
        }
        mainPanel.add(cardsPanel, BorderLayout.CENTER); // Añade el panel de cartones al panel principal.
        add(mainPanel); // Añade el panel principal a la ventana.

        // Inicializa la visualización de los cartones. Pasa 'null' ya que al inicio no hay bola cantada.
        updateCardsDisplay(null);
    }

    /**
     * Actualiza la visualización de todos los cartones del jugador.
     * Este método se llama en cada ronda del juego para reflejar los números marcados
     * y el último número cantado.
     *
     * @param lastCalledBall El último número de bola que ha sido cantado. Puede ser `null`
     * para la actualización inicial.
     */
    public void updateCardsDisplay(Integer lastCalledBall) {
        for (Map.Entry<Card, JEditorPane> entry : cardDisplayPanes.entrySet()) {
            Card card = entry.getKey();
            JEditorPane pane = entry.getValue();
            // Obtiene la representación HTML del cartón del jugador, que aplica los decoradores.
            pane.setText(player.getCardDisplayString(card, lastCalledBall));
            pane.revalidate(); // Revalida el layout del componente.
            pane.repaint(); // Repinta el componente para mostrar los cambios.
        }
    }

    /**
     * Establece la posición de esta ventana de jugador en la pantalla,
     * basada en el índice del jugador y la posición de la ventana principal del juego.
     * Las ventanas de los jugadores se apilan verticalmente a la derecha de la ventana principal.
     *
     * @param index El índice del jugador (0 para el primer jugador, 1 para el segundo, etc.).
     */
    public void setLocationBasedOnIndex(int index) {
        if (parentFrame == null) {
            return; // No se puede posicionar si no hay ventana padre.
        }

        // Obtiene la posición y tamaño de la ventana principal.
        int parentX = parentFrame.getX();
        int parentY = parentFrame.getY();
        int parentWidth = parentFrame.getWidth();

        // Obtiene el tamaño de esta ventana del jugador.
        int windowWidth = getWidth();
        int windowHeight = getHeight();

        int x, y;

        // Posiciona las ventanas a la derecha de la ventana principal, con un pequeño margen.
        x = parentX + parentWidth + 10;
        // Apila las ventanas verticalmente, con un espacio de 10px entre ellas.
        y = parentY + (index * (windowHeight + 10));

        // Ajusta la posición si la ventana se sale de los límites de la pantalla.
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        if (x + windowWidth > screen.width - 10) {
            x = screen.width - windowWidth - 10; // Mueve a la izquierda si se sale por la derecha.
            if (x < 0) {
                x = 0; // Asegura que no se salga por la izquierda.
            }
        }
        if (y + windowHeight > screen.height - 10) {
            y = screen.height - windowHeight - 10; // Mueve hacia arriba si se sale por abajo.
            if (y < 0) {
                y = 0; // Asegura que no se salga por arriba.
            }
        }

        setLocation(x, y); // Establece la ubicación final de la ventana.
    }

    /**
     * Obtiene el nombre del jugador asociado a esta ventana.
     *
     * @return El nombre del jugador.
     */
    public String getPlayerName() {
        return player.getName();
    }
}