package Patterns.Structural.Decorator;

/**
 * Decorador concreto que resalta con color rojo y negrita el último número cantado.
 */
public class LastCalledNumberDecorator extends NumberFormatterDecorator {
    public LastCalledNumberDecorator(ICardNumberFormatter decoratedFormatter) {
        super(decoratedFormatter);
    }

    @Override
    public String format(int number, int row, int col, boolean isMarked, boolean isLastCalled, boolean inWinPattern) {
        // Obtiene el formato base de los decoradores anteriores
        String base = super.format(number, row, col, isMarked, isLastCalled, inWinPattern);
        
        if (isLastCalled) {
            // Envuelve el formato base con etiquetas HTML para negrita y color rojo
            return "<font color='red'><b>" + base + "</b></font>";
        }
        return base;
    }
}