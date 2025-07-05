package UI;

import javax.swing.*;
import java.awt.*;

public class BingoBallVisualizer extends JPanel {
    private int number = -1;

    public void setNumber(int number) {
        this.number = number;
        repaint();
    }

    public BingoBallVisualizer() {
        setPreferredSize(new Dimension(120, 120));
        setOpaque(false); // Fondo transparente
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (number == -1) return;

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int diameter = Math.min(getWidth(), getHeight()) - 10;
        int x = (getWidth() - diameter) / 2;
        int y = (getHeight() - diameter) / 2;

        // Fondo con gradiente
        GradientPaint gradient = new GradientPaint(
                x, y, new Color(255, 140, 0),
                x + diameter, y + diameter, new Color(255, 200, 0)
        );
        g2.setPaint(gradient);
        g2.fillOval(x, y, diameter, diameter);

        // Doble borde blanco
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(4));
        g2.drawOval(x, y, diameter, diameter);
        g2.setStroke(new BasicStroke(2));
        g2.drawOval(x + 10, y + 10, diameter - 20, diameter - 20);

        // Texto (número)
        String text = String.valueOf(number);
        g2.setFont(new Font("Comic Sans MS", Font.BOLD, 36));
        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getAscent();

        g2.setColor(new Color(255, 87, 34)); // Naranja fuerte
        g2.drawString(text,
                getWidth() / 2 - textWidth / 2,
                getHeight() / 2 + textHeight / 4);

        g2.dispose();
    }
}
