package UI;

import Patterns.Behavioral.Command.Command; 
import Patterns.Behavioral.Command.ExitCommand;
import Patterns.Behavioral.Command.ShowInstructionsCommand;
import Patterns.Behavioral.Command.StartGameCommand;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.net.URL;

/**
 * Clase MainMenuGUI
 *
 * Esta clase representa la ventana principal del menú del juego de Bingo.
 * Permite al usuario iniciar un nuevo juego, ver las instrucciones o salir de la aplicación.
 *
 * Implementa el patrón de diseño Command para las acciones de los botones,
 * desacoplando la lógica de la acción del componente de la interfaz de usuario que la invoca.
 *
 * Roles en el patrón Command:
 * - Invoker: Los JButtons del menú, que invocan los métodos `execute()` de los objetos `Command`.
 * - Cliente: La propia clase MainMenuGUI, que crea los objetos `Command` concretos
 * y los asocia con los JButtons.
 */
public class MainMenuGUI extends JFrame {

    /**
     * Constructor para MainMenuGUI.
     * Configura la ventana principal del menú con un título, operación de cierre y tamaño.
     */
    public MainMenuGUI() {
        setTitle("Menú Principal - Bingo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Cierra la aplicación al cerrar la ventana.
        setSize(550, 550); // Tamaño aumentado para una mejor estética.
        setLocationRelativeTo(null); // Centra la ventana en la pantalla.
        initUI(); // Inicializa los componentes de la interfaz de usuario.
    }

    /**
     * Inicializa y organiza todos los componentes de la interfaz de usuario del menú principal.
     */
    private void initUI() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(20, 20)); // BorderLayout con espacio entre componentes.
        mainPanel.setBackground(new Color(255, 255, 220)); // Fondo amarillo muy claro.
        mainPanel.setBorder(createLightBorder()); // Aplica un borde personalizado con luces.

        // --- Título del Juego (Imagen "BINGO!") ---
        // Intenta cargar la imagen del logo de "BINGO!" desde los recursos.
        URL imageUrl = getClass().getClassLoader().getResource("imagen/bingo1.png");
        JLabel titleLabel;

        if (imageUrl == null) {
            // Si la imagen no se encuentra, usa un JLabel de texto simple.
            titleLabel = new JLabel("BINGO!");
            titleLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 70));
            titleLabel.setForeground(new Color(255, 69, 0)); // Color naranja.
        } else {
            // Si la imagen se carga correctamente, la escala y la usa.
            ImageIcon originalIcon = new ImageIcon(imageUrl);

            // Escala la imagen proporcionalmente para que su altura máxima sea 150px.
            int maxHeight = 150;
            int originalWidth = originalIcon.getIconWidth();
            int originalHeight = originalIcon.getIconHeight();
            int newWidth = (int) ((double) maxHeight / originalHeight * originalWidth);

            Image scaledImage = originalIcon.getImage().getScaledInstance(
                    newWidth,
                    maxHeight,
                    Image.SCALE_SMOOTH // Usa un algoritmo de escalado suave.
            );

            titleLabel = new JLabel(new ImageIcon(scaledImage));
            // Añade un borde de biselado suave a la imagen para un efecto 3D.
            titleLabel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createEmptyBorder(5, 5, 5, 5), // Un pequeño margen.
                    BorderFactory.createSoftBevelBorder(BevelBorder.RAISED) // Borde biselado.
            ));
        }

        titleLabel.setHorizontalAlignment(SwingConstants.CENTER); // Centra el JLabel.
        titleLabel.setBorder(new EmptyBorder(20, 0, 5, 0)); // Margen superior para el título.
        mainPanel.add(titleLabel, BorderLayout.NORTH); // Añade el título en la parte superior.

        // --- Panel para los Botones ---
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridBagLayout()); // Usa GridBagLayout para centrar y espaciar los botones.
        buttonsPanel.setOpaque(false); // Hace el panel transparente para que se vea el fondo principal.
        buttonsPanel.setBorder(new EmptyBorder(20, 50, 20, 50)); // Margen alrededor de los botones.

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; // Columna 0.
        gbc.gridy = GridBagConstraints.RELATIVE; // Cada componente se coloca en la siguiente fila.
        gbc.insets = new Insets(10, 0, 10, 0); // Margen vertical entre botones.
        gbc.fill = GridBagConstraints.HORIZONTAL; // Los botones se expanden horizontalmente.
        gbc.weightx = 1.0; // Los botones ocupan todo el ancho disponible.

        // Creación de los objetos Command (patrón Command).
        Command startCommand = new StartGameCommand(this); // Command para iniciar el juego.
        Command instructionsCommand = new ShowInstructionsCommand(this); // Command para mostrar instrucciones.
        Command exitCommand = new ExitCommand(); // Command para salir de la aplicación.

        // Creación de los JButtons con estilo personalizado.
        JButton startButton = createStyledButton("Comenzar juego", new Color(76, 175, 80)); // Verde.
        JButton instructionsButton = createStyledButton("Instrucciones", new Color(33, 150, 243)); // Azul.
        JButton exitButton = createStyledButton("Salir", new Color(244, 67, 54)); // Rojo.

        // Asignación de los Commands a los ActionListeners de los botones (Invoker).
        startButton.addActionListener(e -> startCommand.execute());
        instructionsButton.addActionListener(e -> instructionsCommand.execute());
        exitButton.addActionListener(e -> exitCommand.execute());

        // Añade los botones al panel de botones usando GridBagConstraints.
        buttonsPanel.add(startButton, gbc);
        buttonsPanel.add(instructionsButton, gbc);
        buttonsPanel.add(exitButton, gbc);

        mainPanel.add(buttonsPanel, BorderLayout.CENTER); // Añade el panel de botones al centro.
        add(mainPanel); // Añade el panel principal a la ventana.
    }

    /**
     * Crea un borde personalizado con un efecto de "luces" de feria para la ventana.
     * Este es un ejemplo de un componente decorativo.
     *
     * @return Una instancia de Border que dibuja el efecto de luces.
     */
    private Border createLightBorder() {
        return new Border() {
            // Colores y tamaños para el borde.
            private final Color BORDER_COLOR = new Color(244, 67, 54); // Rojo.
            private final Color LIGHT_COLOR = new Color(255, 215, 0); // Oro/Amarillo brillante.
            private final int BORDER_WIDTH = 10; // Ancho del borde principal.
            private final int LIGHT_SIZE = 6; // Tamaño de cada "luz".
            private final int LIGHT_SPACING = 15; // Espacio entre luces.

            @Override
            public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // Suavizado de bordes.

                // Dibuja el borde principal rectangular.
                g2d.setColor(BORDER_COLOR);
                g2d.setStroke(new BasicStroke(BORDER_WIDTH));
                g2d.drawRect(
                        x + BORDER_WIDTH / 2,
                        y + BORDER_WIDTH / 2,
                        width - BORDER_WIDTH,
                        height - BORDER_WIDTH
                );

                // Dibuja las luces.
                g2d.setColor(LIGHT_COLOR);
                int innerX = x + BORDER_WIDTH;
                int innerY = y + BORDER_WIDTH;
                int innerWidth = width - 2 * BORDER_WIDTH;
                int innerHeight = height - 2 * BORDER_WIDTH;

                // Dibuja luces en los bordes horizontales y verticales.
                drawLights(g2d, innerX, innerY, innerWidth, innerHeight, true); // Luces horizontales.
                drawLights(g2d, innerX, innerY, innerWidth, innerHeight, false); // Luces verticales.

                g2d.dispose();
            }

            /**
             * Dibuja las luces individuales a lo largo de un lado del borde.
             *
             * @param g2d El contexto Graphics2D.
             * @param x Coordenada X de inicio del área de dibujo.
             * @param y Coordenada Y de inicio del área de dibujo.
             * @param w Ancho del área de dibujo.
             * @param h Altura del área de dibujo.
             * @param horizontal Booleano que indica si se dibujan luces horizontalmente (true) o verticalmente (false).
             */
            private void drawLights(Graphics2D g2d, int x, int y, int w, int h, boolean horizontal) {
                int length = horizontal ? w : h; // Longitud del lado a lo largo del cual se dibujarán las luces.
                int lightsCount = length / LIGHT_SPACING; // Número de luces a dibujar.

                for (int i = 0; i < lightsCount; i++) {
                    int pos = i * LIGHT_SPACING + LIGHT_SPACING / 2; // Posición de cada luz.

                    if (horizontal) {
                        // Dibuja luces en los bordes superior e inferior.
                        g2d.fillOval(x + pos - LIGHT_SIZE / 2, y - LIGHT_SIZE / 2, LIGHT_SIZE, LIGHT_SIZE);
                        g2d.fillOval(x + pos - LIGHT_SIZE / 2, y + h - LIGHT_SIZE / 2, LIGHT_SIZE, LIGHT_SIZE);
                    } else {
                        // Dibuja luces en los bordes izquierdo y derecho.
                        g2d.fillOval(x - LIGHT_SIZE / 2, y + pos - LIGHT_SIZE / 2, LIGHT_SIZE, LIGHT_SIZE);
                        g2d.fillOval(x + w - LIGHT_SIZE / 2, y + pos - LIGHT_SIZE / 2, LIGHT_SIZE, LIGHT_SIZE);
                    }
                }
            }

            @Override
            public Insets getBorderInsets(Component c) {
                // Define los márgenes que el borde consume del componente.
                return new Insets(BORDER_WIDTH + 5, BORDER_WIDTH + 5, BORDER_WIDTH + 5, BORDER_WIDTH + 5);
            }

            @Override
            public boolean isBorderOpaque() {
                return true; // Indica que el borde es opaco y llena su área.
            }
        };
    }

    /**
     * Crea un JButton con un estilo predefinido para el menú.
     * Incluye fuente "Comic Sans MS", colores de fondo y texto, y un efecto de hover.
     *
     * @param text El texto a mostrar en el botón.
     * @param bgColor El color de fondo del botón.
     * @return El JButton estilizado.
     */
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Comic Sans MS", Font.BOLD, 22)); // Fuente estilo cartoon.
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        // Borde compuesto: un LineBorder y un EmptyBorder para padding.
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bgColor.darker(), 2), // Borde más oscuro que el fondo.
                BorderFactory.createEmptyBorder(5, 15, 5, 15) // Padding interno.
        ));
        button.setFocusPainted(false); // No pinta el borde de foco.

        // Añade un MouseListener para el efecto de "hover".
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                // Cuando el ratón entra, aclara el color de fondo.
                button.setBackground(bgColor.brighter());
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                // Cuando el ratón sale, restaura el color de fondo original.
                button.setBackground(bgColor);
            }
        });

        return button;
    }
}