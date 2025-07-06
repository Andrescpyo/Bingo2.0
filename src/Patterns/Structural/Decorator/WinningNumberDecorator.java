package Patterns.Structural.Decorator;

/**
 * Clase WinningNumberDecorator
 *
 * Esta es una clase {@link ConcreteDecorator Decorador Concreto}
 * en el patrón Decorator. Su función es añadir una decoración visual específica:
 * resaltar los números que forman parte de un patrón de victoria (línea, diagonal, etc.)
 * en el cartón de Bingo con color azul y negrita.
 *
 * Rol en el patrón Decorator: Concrete Decorator (Decorador Concreto)
 * - Extiende {@link NumberFormatterDecorator Decorator abstracto} e implementa {@link ICardNumberFormatter Componente}.
 * - Envuelve un componente (o otro decorador) y le añade una nueva responsabilidad de formateo.
 * - Su comportamiento se activa dinámicamente cuando un número forma parte de un patrón ganador.
 */
public class WinningNumberDecorator extends NumberFormatterDecorator {

    /**
     * Constructor para WinningNumberDecorator.
     * Recibe la instancia de {@link ICardNumberFormatter} a la que se va a añadir esta decoración.
     * Esto permite construir una cadena de responsabilidades de formato.
     *
     * @param decoratedFormatter La instancia del formateador (componente o decorador previo)
     * al que se le añade esta nueva responsabilidad de resaltar números ganadores.
     */
    public WinningNumberDecorator(ICardNumberFormatter decoratedFormatter) {
        super(decoratedFormatter); // Llama al constructor de la clase padre (NumberFormatterDecorator)
    }

    /**
     * Formatea un número, aplicando un estilo específico si el número forma parte de un patrón de victoria.
     * Primero invoca el método {@code format} del objeto decorado ({@code super.format()})
     * para obtener el formato base o los formatos ya aplicados por decoradores anteriores.
     * Luego, si el número está dentro de un patrón de victoria (`inWinPattern` es true),
     * envuelve la representación HTML con etiquetas para aplicar color azul y negrita.
     *
     * @param number El número entero del cartón a formatear.
     * @param row La fila del número.
     * @param col La columna del número.
     * @param isMarked Booleano que indica si el número está marcado.
     * @param isLastCalled Booleano que indica si el número fue la última bola cantada.
     * @param inWinPattern Booleano que indica si el número forma parte de un patrón de victoria.
     * @return La cadena (HTML) formateada del número, con estilo de número ganador si aplica.
     */
    @Override
    public String format(int number, int row, int col, boolean isMarked, boolean isLastCalled, boolean inWinPattern) {
        // Obtiene el formato base del número (o el formato ya aplicado por decoradores anteriores).
        String base = super.format(number, row, col, isMarked, isLastCalled, inWinPattern);

        // Si el número forma parte de un patrón de victoria, envuelve el formato base
        // con etiquetas HTML para aplicar color azul brillante y negrita.
        if (inWinPattern) {
            return "<font color='#2196F3'><b>" + base + "</b></font>";
        }
        // Si el número no forma parte de un patrón de victoria, devuelve el formato base sin esta decoración.
        return base;
    }
}