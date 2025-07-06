package Patterns.Structural.Decorator;

/**
 * Clase LastCalledNumberDecorator
 *
 * Esta es una clase {@link ConcreteDecorator Decorador Concreto}
 * en el patrón Decorator. Su responsabilidad es añadir una funcionalidad específica de formato:
 * resaltar el último número cantado en el cartón de Bingo con color rojo y negrita.
 *
 * Rol en el patrón Decorator: Concrete Decorator (Decorador Concreto)
 * - Extiende {@link NumberFormatterDecorator Decorator abstracto} e implementa {@link ICardNumberFormatter Componente}.
 * - Añade responsabilidades al componente original (o a otro decorador) envolviéndolo.
 * - Su comportamiento se añade de forma dinámica en tiempo de ejecución.
 */
public class LastCalledNumberDecorator extends NumberFormatterDecorator {

    /**
     * Constructor para LastCalledNumberDecorator.
     * Recibe la instancia de {@link ICardNumberFormatter} a la que se va a añadir la decoración.
     * Esto permite encadenar múltiples decoradores.
     *
     * @param decoratedFormatter La instancia del formateador (componente o decorador previo)
     * al que se le añade esta nueva responsabilidad.
     */
    public LastCalledNumberDecorator(ICardNumberFormatter decoratedFormatter) {
        super(decoratedFormatter); // Llama al constructor de la clase padre (NumberFormatterDecorator)
    }

    /**
     * Formatea un número, añadiendo un resaltado especial si es el último número cantado.
     * Primero invoca el método {@code format} del objeto decorado ({@code super.format()})
     * para obtener el formato base o los formatos ya aplicados por decoradores anteriores,
     * y luego añade su propia decoración si se cumple la condición.
     *
     * @param number El número entero del cartón a formatear.
     * @param row La fila del número.
     * @param col La columna del número.
     * @param isMarked Booleano que indica si el número está marcado.
     * @param isLastCalled Booleano que indica si el número fue la última bola cantada.
     * @param inWinPattern Booleano que indica si el número forma parte de un patrón de victoria.
     * @return La cadena (HTML) formateada del número, con estilo de último número cantado si aplica.
     */
    @Override
    public String format(int number, int row, int col, boolean isMarked, boolean isLastCalled, boolean inWinPattern) {
        // Obtiene el formato base (o el formato ya aplicado por los decoradores anteriores).
        String base = super.format(number, row, col, isMarked, isLastCalled, inWinPattern);

        // Si el número es la última bola cantada, envuelve el formato base con etiquetas HTML
        // para aplicar color rojo intenso y negrita.
        if (isLastCalled) {
            return "<font color='#FF5722'><b>" + base + "</b></font>";
        }
        // Si no es la última bola cantada, devuelve el formato base sin esta decoración.
        return base;
    }
}