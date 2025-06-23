package Patterns.Structural.Decorator;

/**
 * Implementación base de ICardNumberFormatter.
 * Provee el formato por defecto para un número, sin ninguna decoración.
 */
public class BaseNumberFormatter implements ICardNumberFormatter {
    @Override
    public String format(int number, int row, int col, boolean isMarked, boolean isLastCalled, boolean inWinPattern) {
        // Formato base: número con padding para dos dígitos
        return String.format("%2d", number);
    }
}