package UI;

import Patterns.Behavioral.Command.Command;
import Patterns.Behavioral.Command.ExitCommand;
import Patterns.Behavioral.Command.ShowInstructionsCommand;
import Patterns.Behavioral.Command.StartGameCommand;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.net.URL;

public class MainMenuGUI extends JFrame {

    public MainMenuGUI() {
        setTitle("Menú Principal - Bingo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(550, 550); // Aumentado para espacio visual
        setLocationRelativeTo(null);
        initUI();
    }

    private void initUI() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(20, 20));
        mainPanel.setBackground(new Color(255, 255, 220));
        mainPanel.setBorder(createLightBorder());

        // --- Título del Juego (Imagen "BINGO!") ---
        URL imageUrl = getClass().getClassLoader().getResource("imagen/bingo1.png");
        JLabel titleLabel;

        if (imageUrl == null) {
            titleLabel = new JLabel("BINGO!");
            titleLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 70));
            titleLabel.setForeground(new Color(255, 69, 0));
        } else {
            ImageIcon originalIcon = new ImageIcon(imageUrl);

            // Escalado por altura máxima
            int maxHeight = 150;
            int originalWidth = originalIcon.getIconWidth();
            int originalHeight = originalIcon.getIconHeight();
            int newWidth = (int) ((double) maxHeight / originalHeight * originalWidth);

            Image scaledImage = originalIcon.getImage().getScaledInstance(
                newWidth,
                maxHeight,
                Image.SCALE_SMOOTH
            );

            titleLabel = new JLabel(new ImageIcon(scaledImage));
            titleLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(5, 5, 5, 5),
                BorderFactory.createSoftBevelBorder(BevelBorder.RAISED)
            ));
        }

        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(new EmptyBorder(20, 0, 5, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // --- Panel para los Botones ---
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridBagLayout());
        buttonsPanel.setOpaque(false);
        buttonsPanel.setBorder(new EmptyBorder(20, 50, 20, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        Command startCommand = new StartGameCommand(this);
        Command instructionsCommand = new ShowInstructionsCommand(this);
        Command exitCommand = new ExitCommand();

        JButton startButton = createStyledButton("Comenzar juego", new Color(76, 175, 80));
        JButton instructionsButton = createStyledButton("Instrucciones", new Color(33, 150, 243));
        JButton exitButton = createStyledButton("Salir", new Color(244, 67, 54));

        startButton.addActionListener(e -> startCommand.execute());
        instructionsButton.addActionListener(e -> instructionsCommand.execute());
        exitButton.addActionListener(e -> exitCommand.execute());

        buttonsPanel.add(startButton, gbc);
        buttonsPanel.add(instructionsButton, gbc);
        buttonsPanel.add(exitButton, gbc);

        mainPanel.add(buttonsPanel, BorderLayout.CENTER);
        add(mainPanel);
    }

    private Border createLightBorder() {
        return new Border() {
            private final Color BORDER_COLOR = new Color(244, 67, 54);
            private final Color LIGHT_COLOR = new Color(255, 215, 0);
            private final int BORDER_WIDTH = 10;
            private final int LIGHT_SIZE = 6;
            private final int LIGHT_SPACING = 15;

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

                drawLights(g2d, innerX, innerY, innerWidth, innerHeight, true);
                drawLights(g2d, innerX, innerY, innerWidth, innerHeight, false);

                g2d.dispose();
            }

            private void drawLights(Graphics2D g2d, int x, int y, int w, int h, boolean horizontal) {
                int length = horizontal ? w : h;
                int lightsCount = length / LIGHT_SPACING;

                for (int i = 0; i < lightsCount; i++) {
                    int pos = i * LIGHT_SPACING + LIGHT_SPACING / 2;

                    if (horizontal) {
                        g2d.fillOval(x + pos - LIGHT_SIZE / 2, y - LIGHT_SIZE / 2, LIGHT_SIZE, LIGHT_SIZE);
                        g2d.fillOval(x + pos - LIGHT_SIZE / 2, y + h - LIGHT_SIZE / 2, LIGHT_SIZE, LIGHT_SIZE);
                    } else {
                        g2d.fillOval(x - LIGHT_SIZE / 2, y + pos - LIGHT_SIZE / 2, LIGHT_SIZE, LIGHT_SIZE);
                        g2d.fillOval(x + w - LIGHT_SIZE / 2, y + pos - LIGHT_SIZE / 2, LIGHT_SIZE, LIGHT_SIZE);
                    }
                }
            }

            @Override
            public Insets getBorderInsets(Component c) {
                return new Insets(BORDER_WIDTH + 5, BORDER_WIDTH + 5, BORDER_WIDTH + 5, BORDER_WIDTH + 5);
            }

            @Override
            public boolean isBorderOpaque() {
                return true;
            }
        };
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Comic Sans MS", Font.BOLD, 22));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(bgColor.darker(), 2),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        button.setFocusPainted(false);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.brighter());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }
}
