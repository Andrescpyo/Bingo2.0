package UI;

import Core.Game;
import Patterns.Creational.Builder.DefaultGameBuilder; 
import Patterns.Creational.Builder.GameBuilder; 
import Patterns.Creational.Builder.GameDirector; 
import Patterns.Creational.AbstractFactory.*; 
import Patterns.Structural.Facade.GameFacade; 
import Players.*; 

import javax.swing.*;
import javax.swing.border.*; 
import java.awt.*;
import java.net.URL; 
import java.util.ArrayList;
import java.util.List;

/**
 * Clase PlayerRegistrationGUI
 *
 * Esta clase maneja la interfaz gráfica de usuario para el registro de jugadores
 * antes de que comience el juego de Bingo.
 *
 * Se encarga de:
 * 1. Solicitar el número de jugadores (entre 2 y 4).
 * 2. Solicitar el nombre y la cantidad de cartones (1-3) para cada jugador.
 * 3. Utiliza el patrón Abstract Factory para crear diferentes tipos de jugadores
 * (aunque aquí todas las fábricas crean el mismo tipo de Player, en un diseño más complejo
 * podrían crear Player1, Player2, etc. con características diferentes).
 * 4. Al finalizar el registro, utiliza el patrón Builder para construir el objeto Game
 * y el patrón Facade para inicializar el juego y registrar a los jugadores,
 * y luego lanza la ventana principal del juego (GameWindowGUI).
 *
 * Patrones de Diseño Aplicados:
 * - Abstract Factory: `PlayerFactory` y sus implementaciones concretas (`Player1Factory`, etc.)
 * para la creación de objetos `Player`. El cliente (PlayerRegistrationGUI) interactúa con la
 * interfaz `PlayerFactory` para crear instancias de `Player` sin conocer sus clases concretas.
 * - Builder: `GameBuilder` y `GameDirector` se utilizan para construir el objeto `Game` de forma
 * paso a paso y con una interfaz fluida, desacoplando la construcción de su representación.
 * - Facade: `GameFacade` proporciona una interfaz simplificada al subsistema complejo de
 * inicialización del juego (registro de jugadores, configuración inicial del juego).
 */
public class PlayerRegistrationGUI {

    private int totalPlayers; // Número total de jugadores que se registrarán.
    private int currentPlayerIndex = 0; // Índice del jugador actual en el proceso de registro.
    private List<Player> players = new ArrayList<>(); // Lista para almacenar los objetos Player registrados.

    private JFrame mainFrame; // La ventana principal de registro.
    private JPanel mainContentPane; // El panel de contenido principal de la ventana.

    // Array de fábricas de jugadores.
    // Esto demuestra el patrón Abstract Factory, aunque todas las fábricas aquí
    // instancian la misma clase concreta 'Player' por simplicidad en este ejemplo.
    private PlayerFactory[] factories = {
            new Player1Factory(), new Player2Factory(),
            new Player3Factory(), new Player4Factory()
    };

    // Definición de colores para la interfaz, facilitando la consistencia y el cambio.
    private final Color BACKGROUND_COLOR = new Color(255, 255, 220); // Amarillo muy claro.
    private final Color BORDER_COLOR = new Color(244, 67, 54); // Rojo anaranjado.
    private final Color LIGHT_COLOR = new Color(255, 215, 0); // Oro/Amarillo brillante para las "luces".
    private final Color BUTTON_COLOR = new Color(76, 175, 80); // Verde para los botones.

    /**
     * Constructor para PlayerRegistrationGUI.
     * Inicia el proceso de creación de la interfaz de usuario en el Event Dispatch Thread (EDT).
     */
    public PlayerRegistrationGUI() {
        SwingUtilities.invokeLater(() -> {
            createMainFrame(); // Crea la ventana principal.
            showPlayerCountDialog(); // Muestra el diálogo para el número de jugadores.
            mainFrame.setVisible(true); // Hace visible la ventana.
        });
    }

    /**
     * Configura la ventana principal del registro de jugadores.
     */
    private void createMainFrame() {
        mainFrame = new JFrame("Registro de Jugadores - Bingo");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Cierra la aplicación al cerrar la ventana.
        mainFrame.setSize(500, 400); // Establece un tamaño fijo para el registro.
        mainFrame.setLocationRelativeTo(null); // Centra la ventana en la pantalla.

        mainContentPane = new JPanel(new BorderLayout());
        mainContentPane.setBackground(BACKGROUND_COLOR); // Establece el color de fondo.
        mainContentPane.setBorder(createLightBorder()); // Aplica un borde decorativo.
        mainFrame.setContentPane(mainContentPane); // Establece el panel de contenido.
    }

    /**
     * Muestra el diálogo para que el usuario ingrese la cantidad total de jugadores.
     */
    private void showPlayerCountDialog() {
        mainContentPane.removeAll(); // Limpia el contenido previo del panel principal.

        // Crea un panel de diálogo con un título.
        JPanel panel = createDialogPanel("¿Cuántos jugadores jugarán? (2-4)");
        JPanel innerPanel = new JPanel();
        innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS)); // Layout vertical para los componentes.
        innerPanel.setOpaque(false); // Permite ver el fondo del panel padre.
        innerPanel.setBorder(new EmptyBorder(10, 50, 10, 50)); // Márgenes internos.

        JTextField input = new JTextField();
        styleTextField(input); // Aplica estilos al campo de texto.
        input.setMaximumSize(new Dimension(200, 28)); // Limita el tamaño del campo.
        input.setAlignmentX(Component.CENTER_ALIGNMENT); // Centra el campo horizontalmente.

        JButton nextButton = createCompactButton("Siguiente"); // Crea el botón "Siguiente".
        nextButton.setMaximumSize(new Dimension(180, 35));
        nextButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        nextButton.addActionListener(e -> {
            try {
                int cantidad = Integer.parseInt(input.getText().trim());
                if (cantidad < 2 || cantidad > 4) {
                    showStyledError("Debe haber entre 2 y 4 jugadores."); // Muestra error si el número es inválido.
                    return;
                }
                this.totalPlayers = cantidad; // Guarda la cantidad total de jugadores.
                currentPlayerIndex = 0; // Reinicia el índice de jugador actual.
                showPlayerInfoDialog(); // Procede al registro de información de cada jugador.
            } catch (NumberFormatException ex) {
                showStyledError("Número inválido."); // Muestra error si la entrada no es un número.
            }
        });

        // Añade los componentes al panel interno con espaciadores.
        innerPanel.add(Box.createVerticalStrut(10));
        innerPanel.add(input);
        innerPanel.add(Box.createVerticalStrut(15));
        innerPanel.add(nextButton);

        panel.add(innerPanel, BorderLayout.CENTER); // Añade el panel interno al panel de diálogo.
        mainContentPane.add(panel, BorderLayout.CENTER); // Añade el panel de diálogo al contenido principal.
        mainFrame.revalidate(); // Revalida el layout.
        mainFrame.repaint(); // Repinta la ventana.
    }

    /**
     * Muestra el diálogo para que el usuario ingrese el nombre y la cantidad de cartones
     * para el jugador actual. Se llama recursivamente hasta que todos los jugadores se hayan registrado.
     */
    private void showPlayerInfoDialog() {
        if (currentPlayerIndex >= totalPlayers) {
            iniciarJuego(); // Si todos los jugadores están registrados, inicia el juego.
            return;
        }

        mainContentPane.removeAll(); // Limpia el contenido previo.

        // Panel principal del diálogo con el título del jugador actual.
        JPanel panel = createDialogPanel("Jugador " + (currentPlayerIndex + 1) + " de " + totalPlayers);

        // Panel de contenido para los campos del formulario.
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(new EmptyBorder(10, 40, 10, 40));

        JLabel nameLabel = new JLabel("Nombre:");
        nameLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField nameInput = new JTextField();
        styleTextField(nameInput);
        nameInput.setMaximumSize(new Dimension(250, 28));
        nameInput.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel cardsLabel = new JLabel("Cartones (1-3):");
        cardsLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
        cardsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField cardsInput = new JTextField();
        styleTextField(cardsInput);
        cardsInput.setMaximumSize(new Dimension(250, 28));
        cardsInput.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Cambia el texto del botón si es el último jugador.
        JButton nextButton = createCompactButton(currentPlayerIndex == totalPlayers - 1 ? "Comenzar Juego" : "Siguiente");
        nextButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        nextButton.addActionListener(e -> {
            String nombre = nameInput.getText().trim();
            if (nombre.isEmpty()) {
                showStyledError("El nombre no puede estar vacío."); // Validación de nombre.
                return;
            }

            try {
                int cantidad = Integer.parseInt(cardsInput.getText().trim());
                if (cantidad < 1 || cantidad > 3) {
                    showStyledError("Debe tener entre 1 y 3 cartones."); // Validación de cantidad de cartones.
                    return;
                }

                // Utiliza el Abstract Factory para crear un nuevo jugador.
                // Aunque las fábricas concretas son las mismas aquí, esto mantiene el patrón.
                PlayerFactory factory = factories[currentPlayerIndex]; // O podrías usar currentPlayerIndex % factories.length
                Player player = factory.createPlayer(nombre);
                player.setTempCardCount(cantidad); // Guarda la cantidad de cartones deseada temporalmente.
                players.add(player); // Añade el jugador a la lista.

                currentPlayerIndex++; // Incrementa el índice para el siguiente jugador.
                showPlayerInfoDialog(); // Llama recursivamente para el siguiente jugador o inicia el juego.
            } catch (NumberFormatException ex) {
                showStyledError("Número inválido.");
            }
        });

        // Agrega los componentes al contentPanel con espaciadores.
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(nameLabel);
        contentPanel.add(Box.createVerticalStrut(5));
        contentPanel.add(nameInput);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(cardsLabel);
        contentPanel.add(Box.createVerticalStrut(5));
        contentPanel.add(cardsInput);
        contentPanel.add(Box.createVerticalStrut(15));
        contentPanel.add(nextButton);

        panel.add(contentPanel, BorderLayout.CENTER); // Añade el contentPanel al panel de diálogo.
        mainContentPane.add(panel, BorderLayout.CENTER); // Añade el panel de diálogo al contenido principal.
        mainFrame.revalidate();
        mainFrame.repaint();

        // Asegura que la ventana se ajuste a su contenido y se mantenga en el tamaño deseado.
        // mainFrame.pack(); // Se puede usar para ajustar el tamaño automáticamente, pero podría cambiar el tamaño inicial.
        // Si se usa pack(), el setSize() posterior puede ser redundante o anular el efecto de pack().
        // Para una ventana de registro, es mejor mantener un tamaño fijo y centrado.
    }

    /**
     * Crea un panel básico para diálogos con un título y una imagen opcional.
     *
     * @param title El texto del título para el diálogo.
     * @return Un JPanel configurado como panel de diálogo.
     */
    private JPanel createDialogPanel(String title) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(new EmptyBorder(15, 20, 15, 20));

        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setOpaque(false);

        // Intenta cargar una imagen de logo para el diálogo.
        URL imageUrl = getClass().getClassLoader().getResource("imagen/bingo2.png");
        if (imageUrl != null) {
            ImageIcon titleIcon = new ImageIcon(imageUrl);
            // Escala la imagen proporcionalmente, manteniendo el ancho en 300px.
            Image scaledImage = titleIcon.getImage().getScaledInstance(300, -1, Image.SCALE_SMOOTH);
            JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
            imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            headerPanel.add(imageLabel);
        }

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(Box.createVerticalStrut(5)); // Espacio entre imagen y título.
        headerPanel.add(titleLabel);

        panel.add(headerPanel, BorderLayout.NORTH); // Añade el panel de encabezado al diálogo.

        return panel;
    }

    /**
     * Crea y devuelve un borde personalizado con un efecto de "luces" de feria,
     * similar al usado en MainMenuGUI.
     *
     * @return Una instancia de Border que dibuja el efecto de luces.
     */
    private Border createLightBorder() {
        return new Border() {
            private final int BORDER_WIDTH = 10;
            private final int LIGHT_SIZE = 8;
            private final int LIGHT_SPACING = 20;

            @Override
            public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(BORDER_COLOR);
                g2d.setStroke(new BasicStroke(BORDER_WIDTH));
                g2d.drawRect(x + BORDER_WIDTH / 2, y + BORDER_WIDTH / 2, width - BORDER_WIDTH, height - BORDER_WIDTH);
                g2d.setColor(LIGHT_COLOR);

                int innerX = x + BORDER_WIDTH;
                int innerY = y + BORDER_WIDTH;
                int innerWidth = width - 2 * BORDER_WIDTH;
                int innerHeight = height - 2 * BORDER_WIDTH;

                // Dibuja las luces horizontales (superior e inferior).
                for (int i = 0; i < innerWidth; i += LIGHT_SPACING) {
                    g2d.fillOval(innerX + i, innerY - LIGHT_SIZE / 2, LIGHT_SIZE, LIGHT_SIZE);
                    g2d.fillOval(innerX + i, innerY + innerHeight - LIGHT_SIZE / 2, LIGHT_SIZE, LIGHT_SIZE);
                }

                // Dibuja las luces verticales (izquierda y derecha).
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
     * Aplica un estilo uniforme a los JTextFields.
     *
     * @param textField El JTextField al que aplicar el estilo.
     */
    private void styleTextField(JTextField textField) {
        textField.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
        textField.setHorizontalAlignment(JTextField.CENTER);
        textField.setBackground(Color.WHITE);
        textField.setOpaque(true);
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(120, 120, 120), 1), // Borde gris.
                BorderFactory.createEmptyBorder(4, 6, 4, 6) // Padding interno.
        ));
    }

    /**
     * Crea un botón estilizado para el uso en los diálogos de registro.
     *
     * @param text El texto del botón.
     * @return El JButton estilizado.
     */
    private JButton createCompactButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
        button.setBackground(BUTTON_COLOR);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BUTTON_COLOR.darker(), 1), // Borde ligeramente más oscuro.
                BorderFactory.createEmptyBorder(5, 10, 5, 10) // Padding.
        ));
        // Se puede añadir un MouseListener para el efecto de hover si se desea,
        // similar al MainMenuGUI.
        return button;
    }

    /**
     * Muestra un diálogo de error estilizado en lugar de un JOptionPane.showMessageDialog.
     *
     * @param message El mensaje de error a mostrar.
     */
    private void showStyledError(String message) {
        JDialog errorDialog = new JDialog(mainFrame, "Error", true); // Diálogo modal.
        errorDialog.setSize(350, 150);
        errorDialog.setLocationRelativeTo(mainFrame);
        errorDialog.setUndecorated(true); // Sin barra de título por defecto.

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(createLightBorder()); // Aplica el borde de luces.

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(BACKGROUND_COLOR);
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel messageLabel = new JLabel(message);
        messageLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        contentPanel.add(messageLabel, BorderLayout.CENTER);

        JButton okButton = createCompactButton("OK");
        okButton.addActionListener(e -> errorDialog.dispose()); // Cierra el diálogo al presionar OK.

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        buttonPanel.add(okButton);

        contentPanel.add(buttonPanel, BorderLayout.SOUTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        errorDialog.setContentPane(mainPanel);
        errorDialog.setVisible(true);
    }

    /**
     * Inicia el juego una vez que todos los jugadores han sido registrados.
     * Utiliza los patrones Builder y Facade para la inicialización.
     */
    private void iniciarJuego() {
        mainFrame.dispose(); // Cierra la ventana de registro.

        // Patrón Builder: Construcción del objeto Game.
        GameBuilder builder = new DefaultGameBuilder();
        GameDirector director = new GameDirector(builder);
        // Aquí se construye un juego base; la configuración específica de jugadores
        // se maneja a través de la Fachada.
        // Nota: Los parámetros del constructor de constructGame se pueden refinar
        // si el GameBuilder es más complejo (e.g., configurando la lista de jugadores directamente aquí).
        Game game = director.constructGame(new ArrayList<>(), 0); // Se construye un juego vacío inicialmente

        // Patrón Facade: Provee una interfaz simplificada para la configuración del juego.
        GameFacade facade = new GameFacade(game);
        facade.registerPlayers(players); // Registra los jugadores obtenidos del registro.
        facade.initializeGameSettings(); // Inicializa otras configuraciones del juego.

        // Lanza la ventana principal del juego.
        SwingUtilities.invokeLater(() -> {
            GameWindowGUI window = new GameWindowGUI(facade);
            window.setVisible(true);
        });
    }
}