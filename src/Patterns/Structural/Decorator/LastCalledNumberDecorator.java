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
            // Un rojo más intenso y un efecto de "burbuja" o un fondo si es posible
            return "<span style='background-color: #FF5722; color: white; border-radius: 5px; padding: 2px 5px;'><b>" + base + "</b></span>"; // <-- CAMBIO: Fondo naranja, texto blanco, bordes redondeados
        }
        return base;
    }
}