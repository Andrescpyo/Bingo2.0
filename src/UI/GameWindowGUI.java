package UI;

import Core.Card;
import Players.Player;
import Patterns.Structural.Decorator.*;
import Patterns.Structural.Facade.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GameWindowGUI extends JFrame {

    private GameFacade facade;
    private ICardNumberFormatter cardNumberFormatter;

    // Componentes de la GUI
    private JPanel mainPanel;
    private JPanel headerPanel; // Para el número cantado y la estrategia
    private JPanel cardsPanel;  // Para los cartones de los jugadores
    private JPanel controlPanel; // Para botones como "Iniciar Partida" y "Salir"
    private JPanel logPanel; // Para mensajes de texto generales

    private JLabel currentBallLabel; // Muestra la bola actual
    // CAMBIO: JTextPane para mostrar la estrategia de victoria
    private JTextPane winStrategyDisplayPane; // Muestra la estrategia de victoria

    // Mapa para mantener la referencia a cada JEditorPane de cada cartón
    private Map<Card, JEditorPane> cardDisplayPanes;
    private JTextArea logTextArea; // Para mensajes de texto simple (no HTML)

    // Nuevo: Timer para la simulación automática de rondas
    private Timer gameTimer;
    private final int ROUND_DELAY_MS = 2000; // Retraso de 2 segundos entre jugadas

    public GameWindowGUI(GameFacade facade) {
        this.facade = facade;

        setTitle("Juego de Bingo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 750);
        setLocationRelativeTo(null);

        this.cardNumberFormatter = new MarkedNumberDecorator(
                                   new LastCalledNumberDecorator(
                                   new WinningNumberDecorator(
                                   new BaseNumberFormatter())));

        for (Player player : facade.getPlayers()) {
            player.setCardNumberFormatter(cardNumberFormatter);
        }

        cardDisplayPanes = new HashMap<>();

        initUI(); // Construir la interfaz de usuario

        // Configurar el Timer para jugar rondas automáticamente
        gameTimer = new Timer(ROUND_DELAY_MS, this::playNextRound); // Llama playNextRound cada ROUND_DELAY_MS

        // Inicializar la estrategia de victoria y mostrarla en su panel
        String strategyName = facade.getGame().getWinStrategy().getName();
        winStrategyDisplayPane.setText("<html><body><b>Estrategia de Victoria:</b> <br>" + strategyName + "</body></html>");
        
        logTextArea.append("--- ¡Juego listo para iniciar! --- \n");
        logTextArea.append("Haz clic en 'Iniciar Partida' para comenzar.\n\n");

        displayAllPlayerCards(); // Mostrar los cartones iniciales
    }

    private void initUI() {
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- Panel Superior (Bola Actual y Estrategia) ---
        headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBorder(BorderFactory.createTitledBorder("Información del Juego"));

        currentBallLabel = new JLabel("Última Bola: --");
        currentBallLabel.setFont(new Font("Arial", Font.BOLD, 36));
        currentBallLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(currentBallLabel);
        
        // CAMBIO: Panel para mostrar la estrategia de victoria
        winStrategyDisplayPane = new JTextPane();
        winStrategyDisplayPane.setEditable(false);
        winStrategyDisplayPane.setContentType("text/html");
        winStrategyDisplayPane.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
        winStrategyDisplayPane.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        JScrollPane strategyScrollPane = new JScrollPane(winStrategyDisplayPane);
        strategyScrollPane.setPreferredSize(new Dimension(800, 60)); // Tamaño fijo
        headerPanel.add(strategyScrollPane);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // --- Panel Central (Cartones de Jugadores) ---
        cardsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        cardsPanel.setBorder(BorderFactory.createTitledBorder("Cartones de Jugadores"));
        mainPanel.add(cardsPanel, BorderLayout.CENTER);

        // --- Panel de Mensajes (Log) ---
        logPanel = new JPanel(new BorderLayout());
        logPanel.setBorder(BorderFactory.createTitledBorder("Mensajes del Juego"));
        logTextArea = new JTextArea();
        logTextArea.setEditable(false);
        logTextArea.setLineWrap(true);
        logTextArea.setWrapStyleWord(true);
        JScrollPane logScrollPane = new JScrollPane(logTextArea);
        logScrollPane.setPreferredSize(new Dimension(800, 100));
        logPanel.add(logScrollPane, BorderLayout.CENTER);
        mainPanel.add(logPanel, BorderLayout.SOUTH);

        // --- Panel de Control (Botones) ---
        controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        
        // CAMBIO: Botón "Iniciar Partida"
        JButton startGameButton = new JButton("Iniciar Partida");
        startGameButton.setFont(new Font("Arial", Font.BOLD, 18));
        startGameButton.addActionListener(e -> {
            startGameButton.setEnabled(false); // Deshabilitar el botón una vez que la partida inicia
            logTextArea.append("¡Comienza la partida!\n");
            gameTimer.start(); // Iniciar el timer para las rondas automáticas
        });
        controlPanel.add(startGameButton);

        JButton exitButton = new JButton("Salir");
        exitButton.setFont(new Font("Arial", Font.BOLD, 18));
        exitButton.addActionListener(this::volverAlMenu);
        controlPanel.add(exitButton);
        
        mainPanel.add(controlPanel, BorderLayout.EAST);

        setContentPane(mainPanel);
    }

    /**
     * Muestra todos los cartones de los jugadores en el panel central.
     * Crea un JEditorPane para cada cartón y lo agrega a un contenedor por jugador.
     */
    private void displayAllPlayerCards() {
        cardsPanel.removeAll();

        for (Player player : facade.getPlayers()) {
            JPanel playerCardContainer = new JPanel();
            playerCardContainer.setLayout(new BoxLayout(playerCardContainer, BoxLayout.Y_AXIS));
            playerCardContainer.setBorder(BorderFactory.createTitledBorder(player.getName()));

            for (Card card : player.getCards()) {
                JEditorPane cardPane = new JEditorPane();
                cardPane.setEditable(false);
                cardPane.setContentType("text/html");
                cardPane.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
                cardPane.setFont(new Font("Monospaced", Font.PLAIN, 14));
                
                String cardHtmlContent = "<html><body style='font-family:Monospaced; font-size:14pt;'>" +
                                         player.getCardDisplayString(card, null) +
                                         "</body></html>";
                cardPane.setText(cardHtmlContent);
                cardPane.setPreferredSize(new Dimension(180, 150));
                cardDisplayPanes.put(card, cardPane);
                playerCardContainer.add(cardPane);
            }
            cardsPanel.add(playerCardContainer);
        }
        cardsPanel.revalidate();
        cardsPanel.repaint();
    }

    /**
     * Maneja la acción de una nueva ronda (llamada por el Timer).
     * Canta una bola y actualiza la GUI.
     */
    private void playNextRound(ActionEvent e) {
        int calledBall = facade.playRound(); // Obtener la bola cantada
        if (calledBall == -1) { // No hay más bolas disponibles
            gameTimer.stop(); // Detener el timer
            logTextArea.append("No quedan más bolas. Fin del juego.\n");
            logTextArea.setCaretPosition(logTextArea.getDocument().getLength());
            JOptionPane.showMessageDialog(this, "¡Juego terminado! No quedan más bolas.");
            // Opcional: Re-habilitar botón de inicio o mostrar botón de "Nuevo Juego"
            return;
        }

        currentBallLabel.setText("Última Bola: " + calledBall); // Actualizar la etiqueta de la última bola
        logTextArea.append("Número cantado: " + calledBall + "\n");
        logTextArea.setCaretPosition(logTextArea.getDocument().getLength());

        boolean bingoAchieved = false;
        for (Player player : facade.getPlayers()) {
            // Actualizar la vista de cada cartón del jugador
            for (Card card : player.getCards()) {
                JEditorPane cardPane = cardDisplayPanes.get(card);
                if (cardPane != null) {
                    // Regenerar el HTML completo del cartón con la nueva bola cantada y el estado
                    String cardHtmlContent = "<html><body style='font-family:Monospaced; font-size:14pt;'>" +
                                             player.getCardDisplayString(card, calledBall) +
                                             "</body></html>";
                    cardPane.setText(cardHtmlContent);
                }
            }
            
            // Verificar Bingo para cada jugador DESPUÉS de actualizar los cartones
            if (player.checkBingoGUI()) {
                gameTimer.stop(); // Detener el timer si alguien ganó
                logTextArea.append("¡BINGO! " + player.getName() + " ha ganado con la estrategia: " + facade.getGame().getWinStrategy().getName() + "!\n");
                logTextArea.setCaretPosition(logTextArea.getDocument().getLength());
                bingoAchieved = true;
                JOptionPane.showMessageDialog(this, "¡BINGO! " + player.getName() + " ha ganado!");
                // Opcional: Deshabilitar el botón de inicio o mostrar botón de "Nuevo Juego"
                break;
            }
        }
    }

    /**
     * Vuelve al menú principal o sale de la aplicación.
     */
    private void volverAlMenu(ActionEvent e) {
        gameTimer.stop(); // Asegurarse de detener el timer al salir
        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Deseas volver al menú principal?",
                "Juego Terminado",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            this.dispose();
            new MainMenuGUI().setVisible(true);
        } else {
            System.exit(0);
        }
    }
}