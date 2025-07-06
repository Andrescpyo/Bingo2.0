package Patterns.Structural.Decorator;

/**
 * Clase MarkedNumberDecorator
 *
 * Esta es una clase {@link ConcreteDecorator Decorador Concreto}
 * en el patrón Decorator. Su propósito es modificar la representación visual de los números
 * en un cartón de Bingo añadiendo un estilo específico (color verde y negrita) a aquellos
 * números que ya han sido marcados.
 *
 * Rol en el patrón Decorator: Concrete Decorator (Decorador Concreto)
 * - Extiende {@link NumberFormatterDecorator Decorator abstracto} e implementa {@link ICardNumberFormatter Componente}.
 * - Añade responsabilidades específicas (formato de números marcados) al objeto envuelto
 * (ya sea el componente base o un decorador previamente aplicado).
 * - Permite componer el formato de los números de manera flexible en tiempo de ejecución.
 */
public class MarkedNumberDecorator extends NumberFormatterDecorator {

    /**
     * Constructor para MarkedNumberDecorator.
     * Recibe la instancia de {@link ICardNumberFormatter} que será decorada.
     * Esto permite apilar o encadenar múltiples decoradores para aplicar diferentes estilos.
     *
     * @param decoratedFormatter La instancia del formateador (componente o decorador previo)
     * a la que se le añade esta nueva decoración.
     */
    public MarkedNumberDecorator(ICardNumberFormatter decoratedFormatter) {
        super(decoratedFormatter); // Llama al constructor de la clase padre (NumberFormatterDecorator)
    }

    /**
     * Formatea un número, aplicando un estilo específico si el número está marcado.
     * Primero llama al método {@code format} del objeto decorado ({@code super.format()})
     * para obtener la representación base o las decoraciones ya aplicadas.
     * Luego, si el número está marcado, envuelve esa representación con etiquetas HTML
     * para aplicar un color verde y negrita, indicando su estado.
     *
     * @param number El número entero del cartón a formatear.
     * @param row La fila del número.
     * @param col La columna del número.
     * @param isMarked Booleano que indica si el número está marcado.
     * @param isLastCalled Booleano que indica si el número fue la última bola cantada.
     * @param inWinPattern Booleano que indica si el número forma parte de un patrón de victoria.
     * @return La cadena (HTML) formateada del número, con estilo de marcado si aplica.
     */
    @Override
    public String format(int number, int row, int col, boolean isMarked, boolean isLastCalled, boolean inWinPattern) {
        // Obtiene el formato base del número (o el formato ya aplicado por decoradores anteriores).
        String base = super.format(number, row, col, isMarked, isLastCalled, inWinPattern);

        // Si el número está marcado, envuelve el formato base con etiquetas HTML para aplicar color verde y negrita.
        if (isMarked) {
            return "<font color='#4CAF50'><b>" + base + "</b></font>";
        }
        // Si el número no está marcado, devuelve el formato base sin esta decoración específica.
        return base;
    }
}