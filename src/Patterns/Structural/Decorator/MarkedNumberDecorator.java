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
        // Obtiene el formato base de los decoradores anteriores
        String base = super.format(number, row, col, isMarked, isLastCalled, inWinPattern);
        
        if (isMarked) {
            // Añade el asterisco y el color verde usando HTML
            return "<font color='green'>" + base + "*</font>";
        }
        // Si no está marcado, retorna el formato base más un espacio para mantener el alineamiento
        return base + " "; 
    }
}