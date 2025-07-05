package UI;

import Players.Player;
import Patterns.Behavioral.Strategy.WinStrategy;
import Patterns.Structural.Decorator.*;
import Patterns.Structural.Facade.GameFacade;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.List;
import java.util.ArrayList;
import javax.swing.border.TitledBorder;

public class GameWindowGUI extends JFrame {

    private GameFacade facade;
    private ICardNumberFormatter cardNumberFormatter;
    private BingoBallVisualizer ballVisualizer;

    private JPanel mainPanel;
    private JPanel centerPanel;
    private JTextPane winStrategyDisplayPane;
    private JPanel calledBallsDisplayPanel;
    private List<JLabel> ballLabels;
    private Timer gameTimer;
    private final int ROUND_DELAY_MS = 3000;
    private List<PlayerCardWindow> playerWindows;

    public GameWindowGUI(GameFacade facade) {
        this.facade = facade;
        this.playerWindows = new ArrayList<>();

        setTitle("Juego de Bingo - Principal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(880, 600);
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

        SwingUtilities.invokeLater(this::createAndDisplayPlayerWindows);
    }

    private void initUI() {
        mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(220, 255, 220));

        JPanel topControlPanel = new JPanel(new BorderLayout());
        topControlPanel.setBorder(BorderFactory.createLineBorder(new Color(0, 150, 136), 3));
        topControlPanel.setBackground(new Color(178, 223, 223));

        winStrategyDisplayPane = new JTextPane();
        winStrategyDisplayPane.setEditable(false);
        winStrategyDisplayPane.setContentType("text/html");
        winStrategyDisplayPane.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
        winStrategyDisplayPane.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
        winStrategyDisplayPane.setBackground(new Color(255, 255, 220));
        JScrollPane strategyScrollPane = new JScrollPane(winStrategyDisplayPane);
        strategyScrollPane.setPreferredSize(new Dimension(400, 60));

        topControlPanel.add(strategyScrollPane, BorderLayout.WEST);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        buttonsPanel.setOpaque(false);

        JButton startGameButton = new JButton("Iniciar Partida");
        startGameButton.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
        startGameButton.setBackground(new Color(76, 175, 80));
        startGameButton.setForeground(Color.WHITE);
        startGameButton.setBorder(BorderFactory.createRaisedBevelBorder());
        startGameButton.addActionListener(e -> {
            startGameButton.setEnabled(false);
            gameTimer.start();
        });
        buttonsPanel.add(startGameButton);

        JButton exitButton = new JButton("Salir");
        exitButton.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
        exitButton.setBackground(new Color(244, 67, 54));
        exitButton.setForeground(Color.WHITE);
        exitButton.setBorder(BorderFactory.createRaisedBevelBorder());
        exitButton.addActionListener(this::volverAlMenu);
        buttonsPanel.add(exitButton);

        topControlPanel.add(buttonsPanel, BorderLayout.EAST);
        mainPanel.add(topControlPanel, BorderLayout.NORTH);

        centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLUE.darker(), 3),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        centerPanel.setPreferredSize(new Dimension(750, 400));
        centerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.setBackground(new Color(230, 240, 255));

        // Panel para el título + letras + bola
        JPanel bolaCantadaPanel = new JPanel();
        bolaCantadaPanel.setLayout(new BoxLayout(bolaCantadaPanel, BoxLayout.Y_AXIS));
        bolaCantadaPanel.setOpaque(false);
        bolaCantadaPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel tituloBolaLabel = new JLabel("Bola Cantada:");
        tituloBolaLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 26));
        tituloBolaLabel.setForeground(new Color(0, 100, 0));
        tituloBolaLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        bolaCantadaPanel.add(tituloBolaLabel);
        bolaCantadaPanel.add(Box.createVerticalStrut(10));

        bolaCantadaPanel.add(createBingoBallRow());
        centerPanel.add(bolaCantadaPanel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        // Panel de bolas cantadas (cuadrícula del 1 al 75)
        calledBallsDisplayPanel = new JPanel(new GridLayout(8, 10, 2, 2));
        calledBallsDisplayPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Bolas Cantadas",
                TitledBorder.CENTER, TitledBorder.TOP,
                new Font("Comic Sans MS", Font.BOLD, 14), new Color(0, 100, 0)));
        calledBallsDisplayPanel.setBackground(new Color(240, 255, 240));

        ballLabels = new ArrayList<>();

        for (int i = 1; i <= 75; i++) {
            JLabel ballNumLabel = new JLabel(String.valueOf(i), SwingConstants.CENTER);
            ballNumLabel.setOpaque(true);
            ballNumLabel.setBackground(new Color(200, 230, 201));
            ballNumLabel.setBorder(BorderFactory.createLineBorder(new Color(129, 199, 132), 1));
            ballNumLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
            ballLabels.add(ballNumLabel);
            calledBallsDisplayPanel.add(ballNumLabel);
        }

        JScrollPane ballScrollPane = new JScrollPane(calledBallsDisplayPanel);
        ballScrollPane.setPreferredSize(new Dimension(780, 320));
        centerPanel.add(ballScrollPane);

        // Añadir centerPanel al mainPanel
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        setContentPane(mainPanel);
    }

    private JPanel createBingoBallRow() {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        row.setOpaque(false);

        row.add(createLetterBall("B", new Color(244, 67, 54)));
        row.add(createLetterBall("I", new Color(33, 150, 243)));
        row.add(createLetterBall("N", new Color(76, 175, 80)));

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new OverlayLayout(centerPanel));
        centerPanel.setOpaque(false);
        centerPanel.setPreferredSize(new Dimension(100, 100));

        JLabel gLabel = new JLabel("G", SwingConstants.CENTER);
        gLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 32));
        gLabel.setForeground(Color.WHITE);
        gLabel.setAlignmentX(0.5f);
        gLabel.setAlignmentY(0.05f);

        ballVisualizer = new BingoBallVisualizer();
        ballVisualizer.setAlignmentX(0.5f);
        ballVisualizer.setAlignmentY(0.95f);

        centerPanel.add(ballVisualizer);
        centerPanel.add(gLabel);

        row.add(centerPanel);
        row.add(createLetterBall("O", new Color(156, 39, 176)));

        return row;
    }

    private JPanel createLetterBall(String letra, Color colorFondo) {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int size = Math.min(getWidth(), getHeight()) - 8;
                int x = (getWidth() - size) / 2;
                int y = (getHeight() - size) / 2;

                g2.setColor(colorFondo);
                g2.fillOval(x, y, size, size);

                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(4));
                g2.drawOval(x, y, size, size);

                g2.setFont(new Font("Comic Sans MS", Font.BOLD, 28));
                FontMetrics fm = g2.getFontMetrics();
                int textWidth = fm.stringWidth(letra);
                int textHeight = fm.getAscent();
                g2.setColor(Color.WHITE);
                g2.drawString(letra, getWidth() / 2 - textWidth / 2, getHeight() / 2 + textHeight / 4);

                g2.dispose();
            }
        };
        panel.setPreferredSize(new Dimension(60, 60));
        panel.setOpaque(false);
        return panel;
    }

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

    private void playNextRound(ActionEvent e) {
        int calledBall = facade.playRound();

        if (calledBall == -1 || !facade.areBallsLeft()) {
            gameTimer.stop();
            JOptionPane.showMessageDialog(this, "¡Juego terminado! No quedan más bolas.");
            closePlayerWindows();
            return;
        }

        ballVisualizer.setNumber(calledBall); // 👈 Solo usamos esto

        if (calledBall >= 1 && calledBall <= 75) {
            JLabel ballLabelToUpdate = ballLabels.get(calledBall - 1);
            ballLabelToUpdate.setBackground(new Color(255, 152, 0));
            ballLabelToUpdate.setForeground(Color.WHITE);
            ballLabelToUpdate.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
        }

        boolean bingoAchieved = false;
        WinStrategy currentWinStrategy = facade.getGame().getWinStrategy();

        for (Player player : facade.getPlayers()) {
            for (PlayerCardWindow pw : playerWindows) {
                if (pw.getTitle().contains(player.getName())) {
                    pw.updateCardsDisplay(calledBall);
                    break;
                }
            }

            if (player.checkBingoGUI(currentWinStrategy)) {
                gameTimer.stop();
                showBingoImage();
                showWinnerDialog(player.getName());
                closePlayerWindows();
                this.dispose();
                new MainMenuGUI().setVisible(true);
                bingoAchieved = true;
                break;
            }
        }

        if (!bingoAchieved && !facade.areBallsLeft()) {
            gameTimer.stop();
            JOptionPane.showMessageDialog(this, "Todas las bolas han sido cantadas. ¡Nadie hizo Bingo!");
            closePlayerWindows();
        }
    }

    private void showBingoImage() {
        JDialog dialog = new JDialog(this, "¡BINGO!", true);
        dialog.setSize(500, 400); // Más ALTO y proporcionado
        dialog.setLocationRelativeTo(this);
        dialog.setUndecorated(true);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(255, 255, 220));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(244, 67, 54), 4),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        URL imageUrl = getClass().getClassLoader().getResource("imagen/bingo3.png");
        JLabel imageLabel;

        if (imageUrl != null) {
            ImageIcon originalIcon = new ImageIcon(imageUrl);
            // Estiramos de forma balanceada: más alto (400px) para evitar distorsión
            Image scaledImage = originalIcon.getImage().getScaledInstance(520, 260, Image.SCALE_SMOOTH);
            imageLabel = new JLabel(new ImageIcon(scaledImage));
        } else {
            imageLabel = new JLabel("¡BINGO!");
            imageLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 60));
            imageLabel.setForeground(new Color(255, 69, 0));
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        }

        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(imageLabel, BorderLayout.CENTER);

        JButton okButton = new JButton("OK");
        okButton.setFont(new Font("Comic Sans MS", Font.BOLD, 18));
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

    private void closePlayerWindows() {
        for (PlayerCardWindow pw : playerWindows) {
            pw.dispose();
        }
        playerWindows.clear();
    }

    private void volverAlMenu(ActionEvent e) {
        gameTimer.stop();
        int confirm = showStyledConfirmation("¿Deseas volver al menú principal?", "Juego Terminado");
        if (confirm == JOptionPane.YES_OPTION) {
            closePlayerWindows();
            this.dispose();
            new MainMenuGUI().setVisible(true);
        } else {
            gameTimer.start();
        }
    }

    private int showStyledConfirmation(String mensaje, String titulo) {
        JDialog dialog = new JDialog(this, titulo, true);
        dialog.setSize(400, 180);
        dialog.setLocationRelativeTo(this);
        dialog.setUndecorated(true);

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

        final int[] result = {JOptionPane.CLOSED_OPTION};

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

        return result[0];
    }
}
