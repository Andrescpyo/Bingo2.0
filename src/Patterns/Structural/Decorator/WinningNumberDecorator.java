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
        String base = super.format(number, row, col, isMarked, isLastCalled, inWinPattern);
        
        if (inWinPattern) {
            // Un azul más brillante y negrita. Simplificado el subrayado si causa problemas.
            return "<font color='#2196F3'><b>" + base + "</b></font>"; 
        }
        return base;
    }
}