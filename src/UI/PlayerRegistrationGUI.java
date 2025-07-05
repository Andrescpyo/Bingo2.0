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

public class PlayerRegistrationGUI {

    private int totalPlayers;
    private int currentPlayerIndex = 0;
    private List<Player> players = new ArrayList<>();
    private JFrame mainFrame;
    private JPanel mainContentPane;

    private PlayerFactory[] factories = {
        new Player1Factory(), new Player2Factory(),
        new Player3Factory(), new Player4Factory()
    };

    private final Color BACKGROUND_COLOR = new Color(255, 255, 220);
    private final Color BORDER_COLOR = new Color(244, 67, 54);
    private final Color LIGHT_COLOR = new Color(255, 215, 0);
    private final Color BUTTON_COLOR = new Color(76, 175, 80);

    public PlayerRegistrationGUI() {
        SwingUtilities.invokeLater(() -> {
            createMainFrame();
            showPlayerCountDialog();
            mainFrame.setVisible(true);
        });
    }

    private void createMainFrame() {
        mainFrame = new JFrame("Registro de Jugadores - Bingo");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(500, 400);
        mainFrame.setLocationRelativeTo(null);
        mainContentPane = new JPanel(new BorderLayout());
        mainContentPane.setBackground(BACKGROUND_COLOR);
        mainContentPane.setBorder(createLightBorder());
        mainFrame.setContentPane(mainContentPane);
    }

    private void showPlayerCountDialog() {
        mainContentPane.removeAll();

        JPanel panel = createDialogPanel("¿Cuántos jugadores jugarán? (2-4)");
        JPanel innerPanel = new JPanel();
        innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS));
        innerPanel.setOpaque(false);
        innerPanel.setBorder(new EmptyBorder(10, 50, 10, 50));

        JTextField input = new JTextField();
        styleTextField(input);
        input.setMaximumSize(new Dimension(200, 28));
        input.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton nextButton = createCompactButton("Siguiente");
        nextButton.setMaximumSize(new Dimension(180, 35));
        nextButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        nextButton.addActionListener(e -> {
            try {
                int cantidad = Integer.parseInt(input.getText().trim());
                if (cantidad < 2 || cantidad > 4) {
                    showStyledError("Debe haber entre 2 y 4 jugadores.");
                    return;
                }
                this.totalPlayers = cantidad;
                currentPlayerIndex = 0;
                showPlayerInfoDialog();
            } catch (NumberFormatException ex) {
                showStyledError("Número inválido.");
            }
        });

        innerPanel.add(Box.createVerticalStrut(10));
        innerPanel.add(input);
        innerPanel.add(Box.createVerticalStrut(15));
        innerPanel.add(nextButton);

        panel.add(innerPanel, BorderLayout.CENTER);
        mainContentPane.add(panel, BorderLayout.CENTER);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    private void showPlayerInfoDialog() {
        if (currentPlayerIndex >= totalPlayers) {
            iniciarJuego();
            return;
        }

        mainContentPane.removeAll();

        // Panel principal
        JPanel panel = createDialogPanel("Jugador " + (currentPlayerIndex + 1) + " de " + totalPlayers);

        // Panel de contenido del formulario
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

        JButton nextButton = createCompactButton(currentPlayerIndex == totalPlayers - 1 ? "Comenzar Juego" : "Siguiente");
        nextButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        nextButton.addActionListener(e -> {
            String nombre = nameInput.getText().trim();
            if (nombre.isEmpty()) {
                showStyledError("El nombre no puede estar vacío.");
                return;
            }

            try {
                int cantidad = Integer.parseInt(cardsInput.getText().trim());
                if (cantidad < 1 || cantidad > 3) {
                    showStyledError("Debe tener entre 1 y 3 cartones.");
                    return;
                }

                PlayerFactory factory = factories[currentPlayerIndex];
                Player player = factory.createPlayer(nombre);
                player.setTempCardCount(cantidad);
                players.add(player);

                currentPlayerIndex++;
                showPlayerInfoDialog();
            } catch (NumberFormatException ex) {
                showStyledError("Número inválido.");
            }
        });

        // Agrega componentes
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

        // Asegúrate que esté en el centro del panel principal
        panel.add(contentPanel, BorderLayout.CENTER);

        mainContentPane.add(panel, BorderLayout.CENTER);
        mainFrame.revalidate();
        mainFrame.repaint();

        // 💡 Evita llamar a pack() si fijas tamaño, o usa solo pack()
        mainFrame.pack();
        // mainFrame.setSize(500, 400); // puedes comentar esto si ya usaste pack()
    }

    private JPanel createDialogPanel(String title) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(new EmptyBorder(15, 20, 15, 20));

        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setOpaque(false);

        URL imageUrl = getClass().getClassLoader().getResource("imagen/bingo2.png");
        if (imageUrl != null) {
            ImageIcon titleIcon = new ImageIcon(imageUrl);
            Image scaledImage = titleIcon.getImage().getScaledInstance(300, -1, Image.SCALE_SMOOTH);
            JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
            imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            headerPanel.add(imageLabel);
        }

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(Box.createVerticalStrut(5));
        headerPanel.add(titleLabel);

        // ✅ Se agrega el header al panel
        panel.add(headerPanel, BorderLayout.NORTH);

        return panel; // Este panel tiene el encabezado y está listo para que le agregues contenido
    }

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

    private void styleTextField(JTextField textField) {
        textField.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
        textField.setHorizontalAlignment(JTextField.CENTER);
        textField.setBackground(Color.WHITE); // Fondo visible
        textField.setOpaque(true); // Asegura que se pinte el fondo
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(120, 120, 120), 1),
                BorderFactory.createEmptyBorder(4, 6, 4, 6)
        ));
    }

    private JButton createCompactButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
        button.setBackground(BUTTON_COLOR);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BUTTON_COLOR.darker(), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return button;
    }

    private void showStyledError(String message) {
        JDialog errorDialog = new JDialog(mainFrame, "Error", true);
        errorDialog.setSize(350, 150);
        errorDialog.setLocationRelativeTo(mainFrame);
        errorDialog.setUndecorated(true);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(createLightBorder());

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(BACKGROUND_COLOR);
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel messageLabel = new JLabel(message);
        messageLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        contentPanel.add(messageLabel, BorderLayout.CENTER);

        JButton okButton = createCompactButton("OK");
        okButton.addActionListener(e -> errorDialog.dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        buttonPanel.add(okButton);

        contentPanel.add(buttonPanel, BorderLayout.SOUTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        errorDialog.setContentPane(mainPanel);
        errorDialog.setVisible(true);
    }

    private void iniciarJuego() {
        mainFrame.dispose();

        GameBuilder builder = new DefaultGameBuilder();
        GameDirector director = new GameDirector(builder);
        Game game = director.constructGame(new ArrayList<>(), 0);

        GameFacade facade = new GameFacade(game);
        facade.registerPlayers(players);
        facade.initializeGameSettings();

        SwingUtilities.invokeLater(() -> {
            GameWindowGUI window = new GameWindowGUI(facade);
            window.setVisible(true);
        });
    }
}
