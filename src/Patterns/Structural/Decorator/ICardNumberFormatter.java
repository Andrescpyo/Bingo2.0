package Patterns.Structural.Decorator;

/**
 * Interfaz ICardNumberFormatter
 *
 * Esta interfaz define el contrato para formatear la representación visual de un solo número del cartón de Bingo.
 * Es el componente principal en el patrón de diseño Decorator.
 * Tanto el componente concreto base ({@link BaseNumberFormatter}) como todos los decoradores concretos
 * (ej., {@link LastCalledNumberDecorator}, {@link MarkedNumberDecorator}, {@link WinningNumberDecorator})
 * implementan esta interfaz, permitiendo que se encadenen de forma flexible.
 *
 * Rol en el patrón Decorator: Component (Interfaz de Componente)
 * - Define la interfaz para los objetos a los que se puede añadir responsabilidades adicionales.
 * - Garantiza que los decoradores y el componente base tengan la misma interfaz,
 * permitiendo que el cliente los trate de manera uniforme.
 */
public interface ICardNumberFormatter {
    /**
     * Formatea un número del cartón con base en su estado y contexto.
     * Este método es el que los clientes invocarán para obtener la representación formateada
     * de un número. Los decoradores añadirán o modificarán el comportamiento de este método.
     *
     * @param number El número entero del cartón a formatear.
     * @param row La fila en la que se encuentra el número dentro del cartón (0-indexado).
     * @param col La columna en la que se encuentra el número dentro del cartón (0-indexado).
     * @param isMarked Booleano que indica si el número ha sido marcado en el cartón.
     * @param isLastCalled Booleano que indica si este número fue la última bola cantada.
     * @param inWinPattern Booleano que indica si este número forma parte de un patrón de victoria
     * actual (línea, diagonal, etc.).
     * @return Una cadena (posiblemente HTML para estilos) que representa el número formateado,
     * incorporando las decoraciones aplicadas.
     */
    String format(int number, int row, int col, boolean isMarked, boolean isLastCalled, boolean inWinPattern);
}