package Patterns.Behavioral.Command;

import UI.MainMenuGUI;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.net.URL;

/**
 * Clase ShowInstructionsCommand
 *
 * Implementación concreta de la interfaz Command.
 * Este comando encapsula la acción de mostrar una ventana de instrucciones
 * para el juego de Bingo.
 *
 * Rol en el patrón Command: Concrete Command (Comando Concreto)
 * - Define un enlace entre una acción específica (mostrar instrucciones) y su receptor (la lógica de UI que crea la ventana).
 * - Almacena el receptor (en este caso, implícitamente, la lógica de creación de la UI y el `parentFrame` para su disposición).
 */
public class ShowInstructionsCommand implements Command {

    /**
     * El JFrame padre que se va a cerrar antes de mostrar las instrucciones.
     * Este es el "receptor" parcial de este comando, ya que interactúa con él.
     */
    private JFrame parentFrame;

    /**
     * Constructor para ShowInstructionsCommand.
     *
     * @param parentFrame El JFrame actual que debe cerrarse antes de mostrar la ventana de instrucciones.
     * Este es el contexto sobre el cual opera el comando.
     */
    public ShowInstructionsCommand(JFrame parentFrame) {
        this.parentFrame = parentFrame;
    }

    /**
     * Ejecuta el comando para mostrar la ventana de instrucciones del juego.
     * La lógica de creación y visualización de la ventana está encapsulada dentro de este método,
     * desacoplando al "invocador" (por ejemplo, un botón) de los detalles de cómo se muestran las instrucciones.
     */
    @Override
    public void execute() {
        parentFrame.dispose();

        JFrame instructionsFrame = new JFrame("Instrucciones del Bingo");
        instructionsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        instructionsFrame.setSize(850, 800);
        instructionsFrame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(255, 255, 220));
        mainPanel.setBorder(createWindowBorder());

        // Panel del título
        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setBorder(new EmptyBorder(30, 0, 10, 0));

        URL imageUrl = getClass().getClassLoader().getResource("imagen/jugar.png");
        if (imageUrl != null) {
            ImageIcon titleIcon = new ImageIcon(imageUrl);
            Image scaledImage = titleIcon.getImage().getScaledInstance(450, -1, Image.SCALE_SMOOTH);
            JLabel titleLabel = new JLabel(new ImageIcon(scaledImage));
            titlePanel.add(titleLabel);
        } else {
            JLabel titleLabel = new JLabel("INSTRUCCIONES DEL BINGO");
            titleLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 36));
            titleLabel.setForeground(new Color(210, 50, 45));
            titlePanel.add(titleLabel);
        }
        mainPanel.add(titlePanel);

        // Contenedor del texto con borde rojo
        JPanel textContainer = new JPanel(new BorderLayout());
        textContainer.setBorder(createTextBorder());
        textContainer.setBackground(Color.WHITE);
        textContainer.setAlignmentX(Component.CENTER_ALIGNMENT);
        textContainer.setMaximumSize(new Dimension(700, Integer.MAX_VALUE)); // ancho limitado, alto dinámico
        textContainer.setOpaque(true);

        JTextArea instructionsText = new JTextArea(
                          "-El juego se basa en el clásico Bingo con 75 bolas.\n"
                        + "- Cada jugador tendrá uno o más cartones con números del 1 al 75.\n"
                        + "- Se selecciona una forma de victoria al azar: línea, diagonal, L, etc.\n"
                        + "- Se van cantando bolas aleatorias.\n"
                        + "- El primer jugador que cumpla la condición de victoria gana.");
        instructionsText.setFont(new Font("Comic Sans MS", Font.PLAIN, 22));
        instructionsText.setForeground(Color.BLACK);
        instructionsText.setBackground(Color.WHITE);
        instructionsText.setEditable(false);
        instructionsText.setLineWrap(true);
        instructionsText.setWrapStyleWord(true);
        instructionsText.setMargin(new Insets(20, 20, 20, 20));
        instructionsText.setOpaque(false); // fondo se hereda del panel

        textContainer.add(instructionsText, BorderLayout.CENTER);

        JPanel textWrapper = new JPanel();
        textWrapper.setOpaque(false);
        textWrapper.setBorder(new EmptyBorder(10, 50, 10, 50));
        textWrapper.setLayout(new BoxLayout(textWrapper, BoxLayout.X_AXIS));
        textWrapper.add(textContainer);
        mainPanel.add(textWrapper);

        // Botón OK
        JButton okButton = new JButton("OK");
        okButton.setFont(new Font("Comic Sans MS", Font.BOLD, 24));
        okButton.setBackground(new Color(76, 175, 80));
        okButton.setForeground(Color.WHITE);
        okButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(50, 120, 50), 3),
                BorderFactory.createEmptyBorder(8, 40, 8, 40)
        ));
        okButton.addActionListener(e -> {
            instructionsFrame.dispose();
            new MainMenuGUI().setVisible(true);
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(new EmptyBorder(30, 0, 30, 0));
        buttonPanel.add(okButton);
        mainPanel.add(buttonPanel);

        instructionsFrame.add(mainPanel);
        instructionsFrame.setVisible(true);
    }

    /**
     * Crea y devuelve un borde personalizado para la ventana de instrucciones.
     * Este método es una parte auxiliar de la implementación visual del comando.
     *
     * @return Un objeto Border con un diseño gráfico personalizado.
     */
    private Border createWindowBorder() {
        return new Border() {
            private final Color BORDER_COLOR = new Color(244, 67, 54);
            private final Color LIGHT_COLOR = new Color(255, 215, 0);
            private final int BORDER_WIDTH = 12;
            private final int LIGHT_SIZE = 8;
            private final int LIGHT_SPACING = 25;

            @Override
            public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(BORDER_COLOR);
                g2d.setStroke(new BasicStroke(BORDER_WIDTH));
                g2d.drawRect(
                        x + BORDER_WIDTH / 2,
                        y + BORDER_WIDTH / 2,
                        width - BORDER_WIDTH,
                        height - BORDER_WIDTH
                );

                g2d.setColor(LIGHT_COLOR);
                int innerX = x + BORDER_WIDTH;
                int innerY = y + BORDER_WIDTH;
                int innerWidth = width - 2 * BORDER_WIDTH;
                int innerHeight = height - 2 * BORDER_WIDTH;

                for (int i = 0; i < innerWidth; i += LIGHT_SPACING) {
                    g2d.fillOval(innerX + i, innerY - LIGHT_SIZE / 2, LIGHT_SIZE, LIGHT_SIZE);
                    g2d.fillOval(innerX + i, innerY + innerHeight - LIGHT_SIZE / 2, LIGHT_SIZE, LIGHT_SIZE);
                }

                for (int j = 0; j < innerHeight; j += LIGHT_SPACING) {
                    g2d.fillOval(innerX - LIGHT_SIZE / 2, innerY + j, LIGHT_SIZE, LIGHT_SIZE);
                    g2d.fillOval(innerX + innerWidth - LIGHT_SIZE / 2, innerY + j, LIGHT_SIZE, LIGHT_SIZE);
                }

                g2d.dispose();
            }

            @Override
            public Insets getBorderInsets(Component c) {
                return new Insets(BORDER_WIDTH, BORDER_WIDTH, BORDER_WIDTH, BORDER_WIDTH);
            }

            @Override
            public boolean isBorderOpaque() {
                return true;
            }
        };
    }

    /**
     * Crea y devuelve un borde compuesto para el área de texto de instrucciones.
     * Este método es una parte auxiliar de la implementación visual del comando.
     *
     * @return Un objeto Border que combina un borde vacío y un borde de línea.
     */
    private Border createTextBorder() {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(15, 15, 15, 15),
                BorderFactory.createLineBorder(new Color(244, 67, 54), 4)
        );
    }
}
