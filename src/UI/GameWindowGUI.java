package UI;


import Players.Player;
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

        this.cardNumberFormatter = new MarkedNumberDecorator(
                                        new LastCalledNumberDecorator(
                                            new WinningNumberDecorator(
                                                new BaseNumberFormatter())));

        for (Player player : facade.getPlayers()) {
            player.setCardNumberFormatter(cardNumberFormatter);
        }

        ballLabels = new ArrayList<>(); 

        initUI(); 

        gameTimer = new Timer(ROUND_DELAY_MS, this::playNextRound);

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
        mainPanel = new JPanel(new BorderLayout(15, 15)); 
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15)); 

        JPanel topControlPanel = new JPanel(new BorderLayout());
        topControlPanel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2)); 
        
        winStrategyDisplayPane = new JTextPane();
        winStrategyDisplayPane.setEditable(false);
        winStrategyDisplayPane.setContentType("text/html");
        winStrategyDisplayPane.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
        winStrategyDisplayPane.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane strategyScrollPane = new JScrollPane(winStrategyDisplayPane);
        strategyScrollPane.setPreferredSize(new Dimension(400, 60)); 
        
        topControlPanel.add(strategyScrollPane, BorderLayout.WEST); 
        
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        JButton startGameButton = new JButton("Iniciar Partida");
        startGameButton.setFont(new Font("Arial", Font.BOLD, 18));
        startGameButton.addActionListener(e -> {
            startGameButton.setEnabled(false); 
            logTextArea.append("¡Comienza la partida!\n");
            gameTimer.start(); 
        });
        buttonsPanel.add(startGameButton);

        JButton exitButton = new JButton("Salir");
        exitButton.setFont(new Font("Arial", Font.BOLD, 18));
        exitButton.addActionListener(this::volverAlMenu); 
        buttonsPanel.add(exitButton);
        topControlPanel.add(buttonsPanel, BorderLayout.EAST); 
        
        mainPanel.add(topControlPanel, BorderLayout.NORTH); 

        centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createCompoundBorder( 
                                BorderFactory.createLineBorder(Color.BLUE.darker(), 3), 
                                BorderFactory.createEmptyBorder(10, 10, 10, 10))); 
        centerPanel.setPreferredSize(new Dimension(750, 500)); 
        centerPanel.setAlignmentX(Component.CENTER_ALIGNMENT); 
        
        currentBallLabel = new JLabel("Última Bola: --");
        currentBallLabel.setFont(new Font("Arial", Font.BOLD, 72)); 
        currentBallLabel.setForeground(Color.RED); 
        currentBallLabel.setAlignmentX(Component.CENTER_ALIGNMENT); 
        centerPanel.add(currentBallLabel);
        
        centerPanel.add(Box.createRigidArea(new Dimension(0, 30))); 

        calledBallsDisplayPanel = new JPanel(new GridLayout(8, 10, 2, 2)); 
        calledBallsDisplayPanel.setBorder(BorderFactory.createTitledBorder( 
                                        BorderFactory.createEtchedBorder(), "Bolas Cantadas", 
                                        TitledBorder.CENTER, TitledBorder.TOP)); 
        
        for (int i = 1; i <= 75; i++) { 
            JLabel ballNumLabel = new JLabel(String.valueOf(i), SwingConstants.CENTER);
            ballNumLabel.setOpaque(true); 
            ballNumLabel.setBackground(Color.LIGHT_GRAY); 
            ballNumLabel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY)); 
            ballNumLabel.setFont(new Font("Arial", Font.PLAIN, 12));
            ballLabels.add(ballNumLabel); 
            calledBallsDisplayPanel.add(ballNumLabel); 
        }
        
        JScrollPane ballScrollPane = new JScrollPane(calledBallsDisplayPanel);
        ballScrollPane.setPreferredSize(new Dimension(720, 300)); 
        ballScrollPane.setMinimumSize(new Dimension(720, 300));
        ballScrollPane.setMaximumSize(new Dimension(720, 300));
        centerPanel.add(ballScrollPane);
        
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10))); 

        logTextArea = new JTextArea();
        logTextArea.setEditable(false);
        logTextArea.setLineWrap(true);
        logTextArea.setWrapStyleWord(true);
        JScrollPane logScrollPane = new JScrollPane(logTextArea);
        logScrollPane.setPreferredSize(new Dimension(720, 100)); 
        logScrollPane.setMinimumSize(new Dimension(720, 100));
        logScrollPane.setMaximumSize(new Dimension(720, 100));
        logScrollPane.setBorder(BorderFactory.createTitledBorder("Registro de Eventos")); 
        
        centerPanel.add(logScrollPane); 

        mainPanel.add(centerPanel, BorderLayout.CENTER); 

        setContentPane(mainPanel);
    }

    private void createAndDisplayPlayerWindows() {
        List<Player> players = facade.getPlayers();
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            // PASAR 'this' (la referencia a GameWindowGUI) al constructor de PlayerCardWindow
            PlayerCardWindow playerWindow = new PlayerCardWindow(player, this); 
            playerWindow.setLocationBasedOnIndex(i); 
            playerWindow.setVisible(true);
            playerWindows.add(playerWindow); 
        }
    }

    private void playNextRound(ActionEvent e) {
        int calledBall = facade.playRound(); 
        
        if (calledBall == -1 || !facade.areBallsLeft()) { 
            gameTimer.stop(); 
            logTextArea.append("No quedan más bolas. Fin del juego.\n");
            logTextArea.setCaretPosition(logTextArea.getDocument().getLength()); 
            JOptionPane.showMessageDialog(this, "¡Juego terminado! No quedan más bolas.");
            closePlayerWindows(); 
            return;
        }

        currentBallLabel.setText("Última Bola: " + calledBall); 
        logTextArea.append("Número cantado: " + calledBall + "\n");
        logTextArea.setCaretPosition(logTextArea.getDocument().getLength());
        
        if (calledBall >= 1 && calledBall <= 75) { 
            JLabel ballLabelToUpdate = ballLabels.get(calledBall - 1); 
            ballLabelToUpdate.setBackground(Color.RED); 
            ballLabelToUpdate.setForeground(Color.WHITE); 
            ballLabelToUpdate.setFont(new Font("Arial", Font.BOLD, 14)); 
        }

        boolean bingoAchieved = false;
        for (Player player : facade.getPlayers()) {
            for (PlayerCardWindow pw : playerWindows) {
                if (pw.getTitle().contains(player.getName())) { 
                    pw.updateCardsDisplay(calledBall);
                    break;
                }
            }
            
            if (player.checkBingoGUI()) {
                gameTimer.stop(); 
                logTextArea.append("¡BINGO! " + player.getName() + " ha ganado con la estrategia: " + facade.getGame().getWinStrategy().getName() + "!\n");
                logTextArea.setCaretPosition(logTextArea.getDocument().getLength());
                JOptionPane.showMessageDialog(this, "¡BINGO! " + player.getName() + " ha ganado!");
                bingoAchieved = true;
                closePlayerWindows(); 
                break; 
            }
        }
        
        if (!bingoAchieved && !facade.areBallsLeft()) { 
            gameTimer.stop();
            logTextArea.append("Todas las bolas han sido cantadas y nadie hizo Bingo. Juego terminado.\n");
            JOptionPane.showMessageDialog(this, "Todas las bolas han sido cantadas. ¡Nadie hizo Bingo!");
            closePlayerWindows(); 
        }
    }

    private void closePlayerWindows() {
        for (PlayerCardWindow pw : playerWindows) {
            pw.dispose(); 
        }
        playerWindows.clear(); 
    }

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