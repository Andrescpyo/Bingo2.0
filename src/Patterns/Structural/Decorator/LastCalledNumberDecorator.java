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
        String base = super.format(number, row, col, isMarked, isLastCalled, inWinPattern);
        
        if (isLastCalled) {
            // Un rojo más intenso. Simplificado el span y estilos de fondo/bordes.
            return "<font color='#FF5722'><b>" + base + "</b></font>"; 
        }
        return base;
    }
}