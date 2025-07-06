package UI;

import javax.swing.*;
import java.awt.*;

/**
 * Clase BingoBallVisualizer
 *
 * Este componente JPanel personalizado se encarga de dibujar visualmente
 * una bola de Bingo con un número dentro, utilizando gráficos 2D de Java.
 * Está diseñado para mostrar la "última bola cantada" de una manera atractiva
 * y con una estética cartoon.
 *
 * No implementa directamente un patrón de diseño GoF, pero es un componente
 * de la capa de Presentación (UI) que es utilizado por el {@link GameWindowGUI}
 * para visualizar el estado del juego. Su diseño encapsula la lógica de dibujo
 * de una bola, haciendo el código de la GUI principal más limpio.
 */
public class BingoBallVisualizer extends JPanel {
    /**
     * El número actual que se mostrará en la bola.
     * Un valor de -1 indica que no hay ningún número para mostrar (estado inicial).
     */
    private int number = -1;

    /**
     * Establece el número que se mostrará en la bola y solicita un repintado del componente.
     *
     * @param number El número de la bola a visualizar.
     */
    public void setNumber(int number) {
        this.number = number;
        repaint(); // Solicita a Swing que repinte este componente para mostrar el nuevo número.
    }

    /**
     * Constructor para BingoBallVisualizer.
     * Configura el tamaño preferido del panel y lo hace transparente por defecto,
     * permitiendo que el fondo del contenedor se vea a través de él.
     */
    public BingoBallVisualizer() {
        setPreferredSize(new Dimension(120, 120)); // Establece un tamaño preferido para la bola.
        setOpaque(false); // Hace que el JPanel sea transparente, permitiendo que se vea el fondo de su padre.
    }

    /**
     * Sobrescribe el método paintComponent para realizar el dibujo personalizado de la bola de Bingo.
     * Este método es invocado automáticamente por Swing cuando el componente necesita ser dibujado.
     *
     * @param g El contexto gráfico en el que se dibujará el componente.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Asegura que el fondo y los bordes del JPanel se dibujen correctamente.

        // No dibujar nada si no hay un número válido establecido.
        if (number == -1) return;

        // Crea una copia del contexto Graphics para evitar modificar el original y habilita el antialiasing.
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Calcula el diámetro y la posición de la bola para que esté centrada.
        int diameter = Math.min(getWidth(), getHeight()) - 10; // El diámetro de la bola, con un pequeño margen.
        int x = (getWidth() - diameter) / 2; // Coordenada X para centrar la bola.
        int y = (getHeight() - diameter) / 2; // Coordenada Y para centrar la bola.

        // Dibuja el fondo de la bola con un gradiente de color naranja a amarillo.
        GradientPaint gradient = new GradientPaint(
                x, y, new Color(255, 140, 0), // Color inicial (naranja oscuro)
                x + diameter, y + diameter, new Color(255, 200, 0) // Color final (amarillo anaranjado)
        );
        g2.setPaint(gradient); // Establece el pincel para usar el gradiente.
        g2.fillOval(x, y, diameter, diameter); // Dibuja la forma ovalada (círculo).

        // Dibuja un doble borde blanco alrededor de la bola para darle un efecto de relieve.
        g2.setColor(Color.WHITE); // Color del borde.
        g2.setStroke(new BasicStroke(4)); // Grosor del primer borde.
        g2.drawOval(x, y, diameter, diameter); // Dibuja el círculo exterior.
        g2.setStroke(new BasicStroke(2)); // Grosor del segundo borde (más delgado).
        g2.drawOval(x + 10, y + 10, diameter - 20, diameter - 20); // Dibuja el círculo interior.

        // Dibuja el número de la bola.
        String text = String.valueOf(number);
        g2.setFont(new Font("Comic Sans MS", Font.BOLD, 36)); // Fuente estilo cartoon, negrita, tamaño grande.
        FontMetrics fm = g2.getFontMetrics(); // Obtiene métricas de la fuente para centrar el texto.
        int textWidth = fm.stringWidth(text); // Ancho del texto.
        int textHeight = fm.getAscent(); // Altura de la línea base del texto.

        g2.setColor(new Color(255, 87, 34)); // Color del texto (naranja fuerte).
        g2.drawString(text,
                getWidth() / 2 - textWidth / 2, // Centra el texto horizontalmente.
                getHeight() / 2 + textHeight / 4); // Centra el texto verticalmente (ajuste fino).

        g2.dispose(); // Libera los recursos gráficos asociados con el contexto Graphics2D.
    }
}