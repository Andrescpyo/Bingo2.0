package Patterns.Structural.Decorator;

/**
 * Clase abstracta NumberFormatterDecorator
 *
 * Esta clase sirve como el {@link Decorator Decorador abstracto}
 * en el patrón Decorator. Implementa la interfaz {@link ICardNumberFormatter Componente}
 * y mantiene una referencia al objeto {@link ICardNumberFormatter Componente} que decora.
 * Es la base común para todos los {@link ConcreteDecorator Decoradores Concretos}.
 * Su función principal es delegar el comportamiento de formateo a su componente envuelto.
 *
 * Rol en el patrón Decorator: Decorator (Decorador Abstracto)
 * - Mantiene una referencia a un objeto {@link ICardNumberFormatter Componente}.
 * - Implementa la interfaz {@link ICardNumberFormatter Componente} para que los clientes
 * puedan tratar a los decoradores como si fueran el componente original.
 * - Proporciona una implementación por defecto del método {@code format} que simplemente
 * delega la llamada al componente envuelto. Esto permite a los decoradores concretos
 * añadir comportamiento antes o después de la llamada al super.
 */
public abstract class NumberFormatterDecorator implements ICardNumberFormatter {
    /**
     * Referencia al objeto {@link ICardNumberFormatter} que está siendo decorado.
     * Puede ser un {@link BaseNumberFormatter Componente Concreto}
     * o cualquier otro {@link ConcreteDecorator Decorador Concreto}.
     */
    protected ICardNumberFormatter decoratedFormatter;

    /**
     * Constructor para NumberFormatterDecorator.
     * Inicializa el decorador con el componente (o decorador) al que va a añadir funcionalidad.
     *
     * @param decoratedFormatter La instancia de {@link ICardNumberFormatter} que será envuelta
     * por este decorador.
     */
    public NumberFormatterDecorator(ICardNumberFormatter decoratedFormatter) {
        this.decoratedFormatter = decoratedFormatter;
    }

    /**
     * Implementación por defecto del método de formateo.
     * Este método simplemente delega la llamada al método {@code format}
     * del componente {@link #decoratedFormatter} envuelto.
     * Las subclases (decoradores concretos) sobrescribirán este método para
     * añadir su propia lógica de formateo antes o después de esta llamada.
     *
     * @param number El número a formatear.
     * @param row La fila en la que se encuentra el número.
     * @param col La columna en la que se encuentra el número.
     * @param isMarked Indica si el número está marcado.
     * @param isLastCalled Indica si el número es el último cantado.
     * @param inWinPattern Indica si el número forma parte de una línea ganadora.
     * @return La cadena formateada devuelta por el componente decorado.
     */
    @Override
    public String format(int number, int row, int col, boolean isMarked, boolean isLastCalled, boolean inWinPattern) {
        // Delega el formateo al componente envuelto.
        // Este es el punto donde el comportamiento se propaga a través de la cadena de decoradores.
        return decoratedFormatter.format(number, row, col, isMarked, isLastCalled, inWinPattern);
    }
}