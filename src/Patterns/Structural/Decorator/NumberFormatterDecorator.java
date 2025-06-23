package Patterns.Structural.Decorator;

/**
 * Clase abstracta que implementa ICardNumberFormatter y sirve como base para los decoradores concretos.
 * Delega la llamada al formateador que está decorando.
 */
public abstract class NumberFormatterDecorator implements ICardNumberFormatter {
    protected ICardNumberFormatter decoratedFormatter;

    public NumberFormatterDecorator(ICardNumberFormatter decoratedFormatter) {
        this.decoratedFormatter = decoratedFormatter;
    }

    @Override
    public String format(int number, int row, int col, boolean isMarked, boolean isLastCalled, boolean inWinPattern) {
        // Delega el formateo al componente envuelto
        return decoratedFormatter.format(number, row, col, isMarked, isLastCalled, inWinPattern);
    }
}