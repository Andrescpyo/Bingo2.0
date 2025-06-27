package Patterns.Structural.Decorator;

/**
 * Decorador concreto que añade un asterisco y color verde a los números marcados.
 */
public class MarkedNumberDecorator extends NumberFormatterDecorator {
    public MarkedNumberDecorator(ICardNumberFormatter decoratedFormatter) {
        super(decoratedFormatter);
    }

    @Override
    public String format(int number, int row, int col, boolean isMarked, boolean isLastCalled, boolean inWinPattern) {
        String base = super.format(number, row, col, isMarked, isLastCalled, inWinPattern);
        
        if (isMarked) {
            // Un verde más brillante y un asterisco más visible o un ícono si usas imágenes
            return "<font color='#4CAF50'><b>" + base + " &#10003;</b></font>"; // <-- CAMBIO: Verde más brillante y un checkmark
        }
        return base + " "; // Mantener el espacio para alineación
    }
}