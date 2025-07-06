package UI;

import Players.Player; 
import Patterns.Behavioral.Strategy.WinStrategy; 
import Patterns.Structural.Decorator.*; 
import Patterns.Structural.Facade.GameFacade; 

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.net.URL; // Para cargar imágenes desde recursos
import java.util.List;
import java.util.ArrayList;
import javax.swing.border.TitledBorder;

/**
 * Clase GameWindowGUI
 *
 * Esta clase representa la ventana principal de la interfaz gráfica de usuario (GUI) del juego de Bingo.
 * Es responsable de coordinar la visualización del estado del juego, incluyendo la bola cantada,
 * el historial de bolas y la estrategia de victoria.
 *
 * Utiliza el patrón Facade ({@link GameFacade}) para interactuar con la lógica del juego de una manera simplificada.
 * Emplea el patrón Decorator para formatear la visualización de los números en los cartones de los jugadores.
 * También utiliza un Timer para simular el progreso del juego por rondas.
 */
public class GameWindowGUI extends JFrame {

    /**
     * Instancia del Facade que simplifica la interacción con la lógica del juego.
     * Rol en el patrón Facade: Cliente
     */
    private GameFacade facade;

    /**
     * El formateador de números de cartón, que es una composición de decoradores.
     * Rol en el patrón Decorator: Cliente que construye la cadena de decoradores.
     */
    private ICardNumberFormatter cardNumberFormatter;

    /**
     * Componente visual para mostrar la última bola de Bingo cantada.
     */
    private BingoBallVisualizer ballVisualizer;

    /**
     * Paneles principales de la GUI.
     */
    private JPanel mainPanel;
    private JPanel centerPanel;

    /**
     * Componente para mostrar la estrategia de victoria actual.
     */
    private JTextPane winStrategyDisplayPane;

    /**
     * Panel donde se muestran todas las bolas cantadas hasta el momento.
     */
    private JPanel calledBallsDisplayPanel;
    private List<JLabel> ballLabels; // Lista de JLabels para cada número de bola (del 1 al 75).

    /**
     * Temporizador para controlar el flujo de las rondas del juego.
     */
    private Timer gameTimer;
    private final int ROUND_DELAY_MS = 3000; // Retraso entre rondas en milisegundos.

    /**
     * Lista de ventanas individuales para los cartones de cada jugador.
     */
    private List<PlayerCardWindow> playerWindows;

    /**
     * Constructor para GameWindowGUI.
     *
     * @param facade La instancia de {@link GameFacade} que esta ventana utilizará para interactuar con el juego.
     */
    public GameWindowGUI(GameFacade facade) {
        this.facade = facade;
        this.playerWindows = new ArrayList<>();

        setTitle("Juego de Bingo - Principal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(880, 600);
        setLocationRelativeTo(null); // Centra la ventana en la pantalla.

        // Configuración del formateador de números de cartón usando el patrón Decorator.
        // Se encadenan los decoradores: Marked, LastCalled, Winning sobre el BaseFormatter.
        this.cardNumberFormatter = new MarkedNumberDecorator(
                new LastCalledNumberDecorator(
                        new WinningNumberDecorator(
                                new BaseNumberFormatter())));

        // Asigna el formateador decorado a todos los jugadores.
        // Esto permite que los cartones de cada jugador se visualicen con los estilos aplicados.
        for (Player player : facade.getPlayers()) {
            player.setCardNumberFormatter(cardNumberFormatter);
        }

        ballLabels = new ArrayList<>(); // Inicializa la lista para los labels de las bolas cantadas.
        initUI(); // Inicializa los componentes de la interfaz de usuario.

        // Configura el Timer que controlará el paso de cada ronda.
        gameTimer = new Timer(ROUND_DELAY_MS, this::playNextRound);

        // Muestra la estrategia de victoria elegida al inicio del juego.
        String strategyName = facade.getGame().getWinStrategy().getName();
        winStrategyDisplayPane.setText("<html><body><b>Estrategia de Victoria:</b> <br>" + strategyName + "</body></html>");

        // Crea y muestra las ventanas de los jugadores en un hilo aparte para evitar bloqueos.
        SwingUtilities.invokeLater(this::createAndDisplayPlayerWindows);
    }

    /**
     * Inicializa y organiza todos los componentes de la interfaz de usuario de la ventana principal del juego.
     */
    private void initUI() {
        mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(220, 255, 220)); // Fondo verde claro para el panel principal.

        // Panel superior para controles y estrategia de victoria.
        JPanel topControlPanel = new JPanel(new BorderLayout());
        topControlPanel.setBorder(BorderFactory.createLineBorder(new Color(0, 150, 136), 3)); // Borde teal.
        topControlPanel.setBackground(new Color(178, 223, 223)); // Fondo azul verdoso.

        // JTextPane para mostrar la estrategia de victoria.
        winStrategyDisplayPane = new JTextPane();
        winStrategyDisplayPane.setEditable(false);
        winStrategyDisplayPane.setContentType("text/html"); // Permite contenido HTML para formato.
        winStrategyDisplayPane.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
        winStrategyDisplayPane.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
        winStrategyDisplayPane.setBackground(new Color(255, 255, 220)); // Fondo amarillo claro.
        JScrollPane strategyScrollPane = new JScrollPane(winStrategyDisplayPane);
        strategyScrollPane.setPreferredSize(new Dimension(400, 60)); // Tamaño preferido para el scroll.

        topControlPanel.add(strategyScrollPane, BorderLayout.WEST);

        // Panel para botones de control.
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        buttonsPanel.setOpaque(false); // Hace que el panel sea transparente.

        JButton startGameButton = new JButton("Iniciar Partida");
        startGameButton.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
        startGameButton.setBackground(new Color(76, 175, 80)); // Verde.
        startGameButton.setForeground(Color.WHITE);
        startGameButton.setBorder(BorderFactory.createRaisedBevelBorder());
        startGameButton.addActionListener(e -> {
            startGameButton.setEnabled(false); // Deshabilita el botón al iniciar.
            gameTimer.start(); // Inicia el temporizador del juego.
        });
        buttonsPanel.add(startGameButton);

        JButton exitButton = new JButton("Salir");
        exitButton.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
        exitButton.setBackground(new Color(244, 67, 54)); // Rojo.
        exitButton.setForeground(Color.WHITE);
        exitButton.setBorder(BorderFactory.createRaisedBevelBorder());
        exitButton.addActionListener(this::volverAlMenu); // Listener para volver al menú principal.
        buttonsPanel.add(exitButton);

        topControlPanel.add(buttonsPanel, BorderLayout.EAST);
        mainPanel.add(topControlPanel, BorderLayout.NORTH);

        // Panel central para la bola cantada y el historial.
        centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLUE.darker(), 3), // Borde azul oscuro.
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        centerPanel.setPreferredSize(new Dimension(750, 400));
        centerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.setBackground(new Color(230, 240, 255)); // Fondo azul claro.

        // Panel para el título "Bola Cantada" y las letras B-I-N-G-O.
        JPanel bolaCantadaPanel = new JPanel();
        bolaCantadaPanel.setLayout(new BoxLayout(bolaCantadaPanel, BoxLayout.Y_AXIS));
        bolaCantadaPanel.setOpaque(false);
        bolaCantadaPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel tituloBolaLabel = new JLabel("Bola Cantada:");
        tituloBolaLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 26));
        tituloBolaLabel.setForeground(new Color(0, 100, 0)); // Verde oscuro.
        tituloBolaLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        bolaCantadaPanel.add(tituloBolaLabel);
        bolaCantadaPanel.add(Box.createVerticalStrut(10)); // Espacio vertical.

        bolaCantadaPanel.add(createBingoBallRow()); // Agrega la fila de letras BINGO y la bola visualizadora.
        centerPanel.add(bolaCantadaPanel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 30))); // Espacio vertical.

        // Panel para mostrar el historial de bolas cantadas (cuadrícula del 1 al 75).
        calledBallsDisplayPanel = new JPanel(new GridLayout(8, 10, 2, 2)); // Cuadrícula de 8x10.
        calledBallsDisplayPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Bolas Cantadas", // Título del borde.
                TitledBorder.CENTER, TitledBorder.TOP,
                new Font("Comic Sans MS", Font.BOLD, 14), new Color(0, 100, 0))); // Fuente y color del título.
        calledBallsDisplayPanel.setBackground(new Color(240, 255, 240)); // Fondo verde muy claro.

        // Crea los JLabels para cada número del 1 al 75.
        for (int i = 1; i <= 75; i++) {
            JLabel ballNumLabel = new JLabel(String.valueOf(i), SwingConstants.CENTER);
            ballNumLabel.setOpaque(true);
            ballNumLabel.setBackground(new Color(200, 230, 201)); // Color de fondo inicial.
            ballNumLabel.setBorder(BorderFactory.createLineBorder(new Color(129, 199, 132), 1)); // Borde.
            ballNumLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
            ballLabels.add(ballNumLabel);
            calledBallsDisplayPanel.add(ballNumLabel);
        }

        JScrollPane ballScrollPane = new JScrollPane(calledBallsDisplayPanel);
        ballScrollPane.setPreferredSize(new Dimension(780, 320)); // Tamaño preferido para el scroll.
        centerPanel.add(ballScrollPane);

        mainPanel.add(centerPanel, BorderLayout.CENTER); // Añade el panel central al panel principal.
        setContentPane(mainPanel); // Establece el panel principal como el contenido de la ventana.
    }

    /**
     * Crea un panel con las letras B-I-N-G-O y el visualizador de la bola cantada en el centro.
     *
     * @return Un JPanel que contiene la disposición visual de las letras de Bingo y la bola actual.
     */
    private JPanel createBingoBallRow() {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        row.setOpaque(false);

        // Agrega las bolas de letras B, I, N.
        row.add(createLetterBall("B", new Color(244, 67, 54))); // Rojo.
        row.add(createLetterBall("I", new Color(33, 150, 243))); // Azul.
        row.add(createLetterBall("N", new Color(76, 175, 80))); // Verde.

        // Panel central para la bola visualizadora, usando OverlayLayout para superponer.
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new OverlayLayout(centerPanel));
        centerPanel.setOpaque(false);
        centerPanel.setPreferredSize(new Dimension(100, 100)); // Tamaño para la bola visualizadora.

        // La letra 'G' se dibuja sobre la bola principal.
        JLabel gLabel = new JLabel("G", SwingConstants.CENTER);
        gLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 32));
        gLabel.setForeground(Color.WHITE);
        gLabel.setAlignmentX(0.5f); // Alineación X al centro.
        gLabel.setAlignmentY(0.05f); // Ajuste fino para la alineación Y.

        ballVisualizer = new BingoBallVisualizer(); // Instancia el visualizador de bola personalizado.
        ballVisualizer.setAlignmentX(0.5f);
        ballVisualizer.setAlignmentY(0.95f);

        centerPanel.add(ballVisualizer); // Añade el visualizador de la bola.
        centerPanel.add(gLabel); // Añade la letra 'G' (superpuesta).

        row.add(centerPanel); // Agrega el panel central a la fila.
        row.add(createLetterBall("O", new Color(156, 39, 176))); // Morado.

        return row;
    }

    /**
     * Crea un JPanel personalizado que dibuja una bola con una letra dentro.
     * Utilizado para las letras B, I, N, G, O.
     *
     * @param letra El String de la letra a dibujar.
     * @param colorFondo El color de fondo de la bola.
     * @return Un JPanel con la bola de letra dibujada.
     */
    private JPanel createLetterBall(String letra, Color colorFondo) {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int size = Math.min(getWidth(), getHeight()) - 8; // Tamaño de la bola.
                int x = (getWidth() - size) / 2;
                int y = (getHeight() - size) / 2;

                g2.setColor(colorFondo); // Color de fondo de la bola.
                g2.fillOval(x, y, size, size); // Dibuja la bola.

                g2.setColor(Color.WHITE); // Borde blanco.
                g2.setStroke(new BasicStroke(4));
                g2.drawOval(x, y, size, size);

                g2.setFont(new Font("Comic Sans MS", Font.BOLD, 28)); // Fuente de la letra.
                FontMetrics fm = g2.getFontMetrics();
                int textWidth = fm.stringWidth(letra);
                int textHeight = fm.getAscent();
                g2.setColor(Color.WHITE);
                g2.drawString(letra, getWidth() / 2 - textWidth / 2, getHeight() / 2 + textHeight / 4); // Dibuja la letra centrada.

                g2.dispose();
            }
        };
        panel.setPreferredSize(new Dimension(60, 60)); // Tamaño preferido para cada bola de letra.
        panel.setOpaque(false);
        return panel;
    }

    /**
     * Crea y muestra las ventanas individuales para los cartones de cada jugador.
     * Estas ventanas se organizan en la pantalla según su índice.
     */
    private void createAndDisplayPlayerWindows() {
        List<Player> players = facade.getPlayers();
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            PlayerCardWindow playerWindow = new PlayerCardWindow(player, this); // Pasa esta ventana como padre.
            playerWindow.setLocationBasedOnIndex(i); // Establece la posición de la ventana.
            playerWindow.setVisible(true);
            playerWindows.add(playerWindow); // Añade la ventana a la lista.
        }
    }

    /**
     * Maneja la lógica de cada ronda del juego. Es invocado por el Timer.
     * Llama a una nueva bola, actualiza la visualización y verifica si hay un ganador.
     *
     * @param e El ActionEvent generado por el Timer.
     */
    private void playNextRound(ActionEvent e) {
        int calledBall = facade.playRound(); // Llama a la siguiente bola a través de la Facade.

        // Si no quedan más bolas o la llamada devuelve -1, el juego ha terminado.
        if (calledBall == -1 || !facade.areBallsLeft()) {
            gameTimer.stop(); // Detiene el temporizador.
            JOptionPane.showMessageDialog(this, "¡Juego terminado! No quedan más bolas.");
            closePlayerWindows(); // Cierra las ventanas de los jugadores.
            return;
        }

        // Actualiza el visualizador de la bola cantada.
        ballVisualizer.setNumber(calledBall);

        // Resalta la bola cantada en el panel de historial.
        if (calledBall >= 1 && calledBall <= 75) {
            JLabel ballLabelToUpdate = ballLabels.get(calledBall - 1);
            ballLabelToUpdate.setBackground(new Color(255, 152, 0)); // Color naranja.
            ballLabelToUpdate.setForeground(Color.WHITE);
            ballLabelToUpdate.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
        }

        boolean bingoAchieved = false;
        WinStrategy currentWinStrategy = facade.getGame().getWinStrategy(); // Obtiene la estrategia de victoria actual.

        // Actualiza los cartones de los jugadores y verifica si alguno ha ganado.
        for (Player player : facade.getPlayers()) {
            for (PlayerCardWindow pw : playerWindows) {
                if (pw.getTitle().contains(player.getName())) { // Encuentra la ventana del jugador.
                    pw.updateCardsDisplay(calledBall); // Actualiza la visualización de los cartones del jugador.
                    break;
                }
            }

            // Verifica si el jugador actual ha ganado.
            if (player.checkBingoGUI(currentWinStrategy)) {
                gameTimer.stop(); // Detiene el temporizador.
                showBingoImage(); // Muestra una imagen de BINGO.
                showWinnerDialog(player.getName()); // Muestra un diálogo de ganador.
                closePlayerWindows(); // Cierra todas las ventanas de los jugadores.
                this.dispose(); // Cierra la ventana principal del juego.
                new MainMenuGUI().setVisible(true); // Vuelve al menú principal.
                bingoAchieved = true;
                break; // Sale del bucle de jugadores una vez que se encuentra un ganador.
            }
        }

        // Si no se logró un Bingo y no quedan más bolas, notifica que nadie ganó.
        if (!bingoAchieved && !facade.areBallsLeft()) {
            gameTimer.stop();
            JOptionPane.showMessageDialog(this, "Todas las bolas han sido cantadas. ¡Nadie hizo Bingo!");
            closePlayerWindows();
        }
    }

    /**
     * Muestra un diálogo con una imagen de "BINGO!" cuando un jugador gana.
     */
    private void showBingoImage() {
        JDialog dialog = new JDialog(this, "¡BINGO!", true);
        dialog.setSize(500, 400); // Tamaño del diálogo.
        dialog.setLocationRelativeTo(this); // Centra respecto a la ventana principal.
        dialog.setUndecorated(true); // Sin bordes ni barra de título.

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(255, 255, 220)); // Fondo amarillo muy claro.
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(244, 67, 54), 4), // Borde rojo.
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        URL imageUrl = getClass().getClassLoader().getResource("imagen/bingo3.png"); // Carga la imagen.
        JLabel imageLabel;

        if (imageUrl != null) {
            ImageIcon originalIcon = new ImageIcon(imageUrl);
            // Escala la imagen para que se ajuste al tamaño del diálogo.
            Image scaledImage = originalIcon.getImage().getScaledInstance(520, 260, Image.SCALE_SMOOTH);
            imageLabel = new JLabel(new ImageIcon(scaledImage));
        } else {
            // Si la imagen no se encuentra, muestra un texto alternativo.
            imageLabel = new JLabel("¡BINGO!");
            imageLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 60));
            imageLabel.setForeground(new Color(255, 69, 0));
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        }

        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(imageLabel, BorderLayout.CENTER);

        JButton okButton = new JButton("OK");
        okButton.setFont(new Font("Comic Sans MS", Font.BOLD, 18));
        okButton.setBackground(new Color(76, 175, 80)); // Verde.
        okButton.setForeground(Color.WHITE);
        okButton.setFocusPainted(false);
        okButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(56, 142, 60), 2),
                BorderFactory.createEmptyBorder(5, 20, 5, 20)
        ));

        okButton.addActionListener(e -> dialog.dispose()); // Cierra el diálogo al presionar OK.

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(255, 255, 220));
        buttonPanel.add(okButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setContentPane(panel);
        dialog.setVisible(true);
    }

    /**
     * Muestra un diálogo anunciando al jugador ganador.
     *
     * @param playerName El nombre del jugador que ha ganado.
     */
    private void showWinnerDialog(String playerName) {
        JDialog dialog = new JDialog(this, "Ganador", true);
        dialog.setSize(400, 200);
        dialog.setLocationRelativeTo(this);
        dialog.setUndecorated(true);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(255, 255, 220));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(244, 67, 54), 4),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel messageLabel = new JLabel("<html><center><b>" + playerName + " ha ganado el juego.</b></center></html>");
        messageLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(messageLabel, BorderLayout.CENTER);

        JButton okButton = new JButton("OK");
        okButton.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
        okButton.setBackground(new Color(76, 175, 80));
        okButton.setForeground(Color.WHITE);
        okButton.setFocusPainted(false);
        okButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(56, 142, 60), 2),
                BorderFactory.createEmptyBorder(5, 20, 5, 20)
        ));

        okButton.addActionListener(e -> dialog.dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(255, 255, 220));
        buttonPanel.add(okButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setContentPane(panel);
        dialog.setVisible(true);
    }

    /**
     * Cierra todas las ventanas individuales de los cartones de los jugadores.
     */
    private void closePlayerWindows() {
        for (PlayerCardWindow pw : playerWindows) {
            pw.dispose(); // Libera los recursos de cada ventana.
        }
        playerWindows.clear(); // Limpia la lista de ventanas.
    }

    /**
     * Maneja la acción de volver al menú principal. Muestra una confirmación al usuario.
     *
     * @param e El ActionEvent.
     */
    private void volverAlMenu(ActionEvent e) {
        gameTimer.stop(); // Detiene el juego mientras se pide confirmación.
        int confirm = showStyledConfirmation("¿Deseas volver al menú principal?", "Juego Terminado");
        if (confirm == JOptionPane.YES_OPTION) {
            closePlayerWindows(); // Cierra las ventanas de los jugadores.
            this.dispose(); // Cierra la ventana principal.
            new MainMenuGUI().setVisible(true); // Abre el menú principal.
        } else {
            gameTimer.start(); // Si el usuario cancela, reanuda el juego.
        }
    }

    /**
     * Muestra un diálogo de confirmación personalizado con estilo.
     *
     * @param mensaje El mensaje a mostrar en el diálogo.
     * @param titulo El título del diálogo.
     * @return El resultado de la confirmación (JOptionPane.YES_OPTION o JOptionPane.NO_OPTION).
     */
    private int showStyledConfirmation(String mensaje, String titulo) {
        JDialog dialog = new JDialog(this, titulo, true); // Diálogo modal.
        dialog.setSize(400, 180);
        dialog.setLocationRelativeTo(this);
        dialog.setUndecorated(true); // Sin decoraciones del sistema.

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(255, 255, 220));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(244, 67, 54), 4),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));

        JLabel label = new JLabel("<html><center><b>" + mensaje + "</b></center></html>");
        label.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(label, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(255, 255, 220));

        // Botones "Sí" y "No" con estilo.
        JButton yesButton = new JButton("Sí");
        yesButton.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
        yesButton.setBackground(new Color(76, 175, 80));
        yesButton.setForeground(Color.WHITE);
        yesButton.setFocusPainted(false);

        JButton noButton = new JButton("No");
        noButton.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
        noButton.setBackground(new Color(244, 67, 54));
        noButton.setForeground(Color.WHITE);
        noButton.setFocusPainted(false);

        final int[] result = {JOptionPane.CLOSED_OPTION}; // Almacena el resultado de la elección del usuario.

        yesButton.addActionListener(ev -> {
            result[0] = JOptionPane.YES_OPTION;
            dialog.dispose();
        });

        noButton.addActionListener(ev -> {
            result[0] = JOptionPane.NO_OPTION;
            dialog.dispose();
        });

        buttonPanel.add(yesButton);
        buttonPanel.add(noButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setContentPane(panel);
        dialog.setVisible(true);

        return result[0]; // Devuelve la opción seleccionada.
    }
}