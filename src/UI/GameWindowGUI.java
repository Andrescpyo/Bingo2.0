package UI;

import Players.Player;
import Patterns.Behavioral.Strategy.WinStrategy;
import Patterns.Structural.Decorator.*;
import Patterns.Structural.Facade.GameFacade;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.ArrayList;
import javax.swing.border.TitledBorder;

public class GameWindowGUI extends JFrame {

    private GameFacade facade;
    private ICardNumberFormatter cardNumberFormatter;

    private JPanel mainPanel;
    private JPanel centerPanel; 

    private JLabel currentBallLabel; 
    private JTextPane winStrategyDisplayPane; 
    
    private JPanel calledBallsDisplayPanel; 
    private List<JLabel> ballLabels; 

    private JTextArea logTextArea; 

    private Timer gameTimer;
    private final int ROUND_DELAY_MS = 3000; 

    private List<PlayerCardWindow> playerWindows;

    public GameWindowGUI(GameFacade facade) {
        this.facade = facade;
        this.playerWindows = new ArrayList<>(); 

        setTitle("Juego de Bingo - Principal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 650); 
        setLocationRelativeTo(null); 

        // Configuración del decorador para formatear los números del cartón
        this.cardNumberFormatter = new MarkedNumberDecorator(
                                        new LastCalledNumberDecorator(
                                            new WinningNumberDecorator(
                                                new BaseNumberFormatter())));

        // Asignar el formateador a cada jugador
        for (Player player : facade.getPlayers()) {
            player.setCardNumberFormatter(cardNumberFormatter);
        }

        ballLabels = new ArrayList<>(); 

        initUI(); // Inicializar la interfaz de usuario

        // Configurar el temporizador para las rondas del juego
        gameTimer = new Timer(ROUND_DELAY_MS, this::playNextRound);

        // Mostrar la estrategia de victoria actual
        String strategyName = facade.getGame().getWinStrategy().getName();
        winStrategyDisplayPane.setText("<html><body><b>Estrategia de Victoria:</b> <br>" + strategyName + "</body></html>");
        
        logTextArea.append("--- ¡Juego listo para iniciar! ---\n");
        logTextArea.append("Haz clic en 'Iniciar Partida' para comenzar.\n\n");

        // Asegúrate de que la ventana principal sea visible antes de crear las ventanas secundarias
        // para que getLocationOnScreen() devuelva valores correctos.
        SwingUtilities.invokeLater(() -> {
            createAndDisplayPlayerWindows(); 
        });
    }

    private void initUI() {
        // Panel principal con BorderLayout y bordes vacíos para espacio
        mainPanel = new JPanel(new BorderLayout(15, 15)); 
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15)); 
        mainPanel.setBackground(new Color(220, 255, 220)); // Fondo verde pastel para un toque cartoon
        
        // Panel superior para controles (estrategia y botones)
        JPanel topControlPanel = new JPanel(new BorderLayout());
        topControlPanel.setBorder(BorderFactory.createLineBorder(new Color(0, 150, 136), 3)); // Borde más grueso y color vibrante
        topControlPanel.setBackground(new Color(178, 223, 223)); // Fondo azul verdoso

        // JTextPane para mostrar la estrategia de victoria
        winStrategyDisplayPane = new JTextPane();
        winStrategyDisplayPane.setEditable(false);
        winStrategyDisplayPane.setContentType("text/html");
        winStrategyDisplayPane.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
        winStrategyDisplayPane.setFont(new Font("Comic Sans MS", Font.PLAIN, 16)); // Fuente cartoon
        winStrategyDisplayPane.setBackground(new Color(255, 255, 220)); // Fondo amarillo muy claro
        JScrollPane strategyScrollPane = new JScrollPane(winStrategyDisplayPane);
        strategyScrollPane.setPreferredSize(new Dimension(400, 60)); 
        
        topControlPanel.add(strategyScrollPane, BorderLayout.WEST); 
        
        // Panel para botones (Iniciar Partida, Salir)
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        buttonsPanel.setOpaque(false); // Transparente para mostrar el fondo del topControlPanel

        JButton startGameButton = new JButton("Iniciar Partida");
        startGameButton.setFont(new Font("Comic Sans MS", Font.BOLD, 20)); // Fuente cartoon y tamaño
        startGameButton.setBackground(new Color(76, 175, 80)); // Verde vibrante
        startGameButton.setForeground(Color.WHITE); // Texto blanco
        startGameButton.setBorder(BorderFactory.createRaisedBevelBorder()); // Borde con relieve para un toque 3D
        startGameButton.addActionListener(e -> {
            startGameButton.setEnabled(false); // Deshabilitar el botón al iniciar
            logTextArea.append("¡Comienza la partida!\n");
            gameTimer.start(); // Iniciar el temporizador del juego
        });
        buttonsPanel.add(startGameButton);

        JButton exitButton = new JButton("Salir");
        exitButton.setFont(new Font("Comic Sans MS", Font.BOLD, 20)); // Fuente cartoon y tamaño
        exitButton.setBackground(new Color(244, 67, 54)); // Rojo vibrante
        exitButton.setForeground(Color.WHITE); // Texto blanco
        exitButton.setBorder(BorderFactory.createRaisedBevelBorder()); // Borde con relieve
        exitButton.addActionListener(this::volverAlMenu); 
        buttonsPanel.add(exitButton);
        topControlPanel.add(buttonsPanel, BorderLayout.EAST); 
        
        mainPanel.add(topControlPanel, BorderLayout.NORTH); 

        // Panel central para la bola actual y el historial de bolas
        centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createCompoundBorder( 
                                 BorderFactory.createLineBorder(Color.BLUE.darker(), 3), 
                                 BorderFactory.createEmptyBorder(10, 10, 10, 10))); 
        centerPanel.setPreferredSize(new Dimension(750, 500)); 
        centerPanel.setAlignmentX(Component.CENTER_ALIGNMENT); 
        centerPanel.setBackground(new Color(230, 240, 255)); // Fondo azul claro suave

        // Etiqueta para la última bola cantada
        currentBallLabel = new JLabel("Última Bola: --");
        currentBallLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 80)); // Fuente cartoon, tamaño grande
        currentBallLabel.setForeground(new Color(255, 69, 0)); // Naranja vibrante
        currentBallLabel.setAlignmentX(Component.CENTER_ALIGNMENT); 
        centerPanel.add(currentBallLabel);
        
        centerPanel.add(Box.createRigidArea(new Dimension(0, 30))); 

        // Panel para mostrar las bolas cantadas
        calledBallsDisplayPanel = new JPanel(new GridLayout(8, 10, 2, 2)); 
        calledBallsDisplayPanel.setBorder(BorderFactory.createTitledBorder( 
                                             BorderFactory.createEtchedBorder(), "Bolas Cantadas", 
                                             TitledBorder.CENTER, TitledBorder.TOP,
                                             new Font("Comic Sans MS", Font.BOLD, 14), new Color(0, 100, 0))); // Título cartoon
        calledBallsDisplayPanel.setBackground(new Color(240, 255, 240)); // Fondo muy claro
        
        // Inicializar etiquetas para todas las bolas
        for (int i = 1; i <= 75; i++) { 
            JLabel ballNumLabel = new JLabel(String.valueOf(i), SwingConstants.CENTER);
            ballNumLabel.setOpaque(true); 
            ballNumLabel.setBackground(new Color(200, 230, 201)); // Verde claro suave
            ballNumLabel.setBorder(BorderFactory.createLineBorder(new Color(129, 199, 132), 1)); // Borde verde
            ballNumLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, 14)); // Fuente cartoon
            ballLabels.add(ballNumLabel); 
            calledBallsDisplayPanel.add(ballNumLabel); 
        }
        
        JScrollPane ballScrollPane = new JScrollPane(calledBallsDisplayPanel);
        ballScrollPane.setPreferredSize(new Dimension(720, 300)); 
        ballScrollPane.setMinimumSize(new Dimension(720, 300));
        ballScrollPane.setMaximumSize(new Dimension(720, 300));
        centerPanel.add(ballScrollPane);
        
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10))); 

        // Área de texto para el registro de eventos
        logTextArea = new JTextArea();
        logTextArea.setEditable(false);
        logTextArea.setLineWrap(true);
        logTextArea.setWrapStyleWord(true);
        logTextArea.setFont(new Font("Arial", Font.PLAIN, 12)); // Fuente estándar para el log
        logTextArea.setBackground(new Color(255, 250, 240)); // Fondo crema suave
        JScrollPane logScrollPane = new JScrollPane(logTextArea);
        logScrollPane.setPreferredSize(new Dimension(720, 100)); 
        logScrollPane.setMinimumSize(new Dimension(720, 100));
        logScrollPane.setMaximumSize(new Dimension(720, 100));
        logScrollPane.setBorder(BorderFactory.createTitledBorder("Registro de Eventos")); 
        
        centerPanel.add(logScrollPane); 

        mainPanel.add(centerPanel, BorderLayout.CENTER); 

        setContentPane(mainPanel);
    }

    // Crea y muestra las ventanas de los cartones de los jugadores
    private void createAndDisplayPlayerWindows() {
        List<Player> players = facade.getPlayers();
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            PlayerCardWindow playerWindow = new PlayerCardWindow(player, this); 
            playerWindow.setLocationBasedOnIndex(i); 
            playerWindow.setVisible(true);
            playerWindows.add(playerWindow); 
        }
    }

    // Lógica para jugar la siguiente ronda
    private void playNextRound(ActionEvent e) {
        int calledBall = facade.playRound(); 
        
        // Si no quedan bolas, detener el juego
        if (calledBall == -1 || !facade.areBallsLeft()) { 
            gameTimer.stop(); 
            logTextArea.append("No quedan más bolas. Fin del juego.\n");
            logTextArea.setCaretPosition(logTextArea.getDocument().getLength()); 
            JOptionPane.showMessageDialog(this, "¡Juego terminado! No quedan más bolas.");
            closePlayerWindows(); 
            return;
        }

        // Actualizar la interfaz con la bola cantada
        currentBallLabel.setText("Última Bola: " + calledBall); 
        logTextArea.append("Número cantado: " + calledBall + "\n");
        logTextArea.setCaretPosition(logTextArea.getDocument().getLength());
        
        // Resaltar la bola en el panel de bolas cantadas
        if (calledBall >= 1 && calledBall <= 75) { 
            JLabel ballLabelToUpdate = ballLabels.get(calledBall - 1); 
            ballLabelToUpdate.setBackground(new Color(255, 152, 0)); // Naranja para bolas cantadas
            ballLabelToUpdate.setForeground(Color.WHITE); 
            ballLabelToUpdate.setFont(new Font("Comic Sans MS", Font.BOLD, 16)); // Fuente cartoon
        }

        boolean bingoAchieved = false;
        WinStrategy currentWinStrategy = facade.getGame().getWinStrategy(); 

        // Actualizar cartones y verificar si algún jugador ganó
        for (Player player : facade.getPlayers()) {
            for (PlayerCardWindow pw : playerWindows) {
                if (pw.getTitle().contains(player.getName())) {
                    pw.updateCardsDisplay(calledBall);
                    break;
                }
            }
            
            if (player.checkBingoGUI(currentWinStrategy)) {
                gameTimer.stop(); 
                logTextArea.append("¡BINGO! " + player.getName() + " ha ganado con la estrategia: " + facade.getGame().getWinStrategy().getName() + "!\n");
                logTextArea.setCaretPosition(logTextArea.getDocument().getLength());
                JOptionPane.showMessageDialog(this, "¡BINGO! " + player.getName() + " ha ganado!");
                bingoAchieved = true;
                closePlayerWindows(); 
                break; 
            }
        }
        
        // Si no hubo bingo y no quedan bolas
        if (!bingoAchieved && !facade.areBallsLeft()) { 
            gameTimer.stop();
            logTextArea.append("Todas las bolas han sido cantadas y nadie hizo Bingo. Juego terminado.\n");
            JOptionPane.showMessageDialog(this, "Todas las bolas han sido cantadas. ¡Nadie hizo Bingo!");
            closePlayerWindows(); 
        }
    }

    // Cierra todas las ventanas de los cartones de los jugadores
    private void closePlayerWindows() {
        for (PlayerCardWindow pw : playerWindows) {
            pw.dispose(); 
        }
        playerWindows.clear(); 
    }

    // Vuelve al menú principal o sale de la aplicación
    private void volverAlMenu(ActionEvent e) {
        gameTimer.stop(); 
        int confirm = JOptionPane.showConfirmDialog(this,
                                "Deseas volver al menú principal?",
                                "Juego Terminado",
                                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            closePlayerWindows(); 
            this.dispose(); 
            new MainMenuGUI().setVisible(true); 
        } else {
            System.exit(0); 
        }
    }
}