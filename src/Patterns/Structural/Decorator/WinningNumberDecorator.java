package Patterns.Structural.Decorator;

/**
 * Decorador concreto que resalta con color azul y negrita los números que forman una línea ganadora.
 */
public class WinningNumberDecorator extends NumberFormatterDecorator {
    public WinningNumberDecorator(ICardNumberFormatter decoratedFormatter) {
        super(decoratedFormatter);
    }

    @Override
    public String format(int number, int row, int col, boolean isMarked, boolean isLastCalled, boolean inWinPattern) {
        // Obtiene el formato base de los decoradores anteriores
        String base = super.format(number, row, col, isMarked, isLastCalled, inWinPattern);
        
        if (inWinPattern) {
            // Envuelve el formato base con etiquetas HTML para negrita y color azul
            return "<font color='blue'><b>" + base + "</b></font>";
        }
        return base;
    }
}