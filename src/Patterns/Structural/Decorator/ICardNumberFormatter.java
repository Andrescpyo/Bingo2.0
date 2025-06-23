package Patterns.Structural.Decorator;

/**
 * Interfaz para formatear la representación visual de un solo número del cartón.
 * Este es el componente base del patrón Decorator.
 */
public interface ICardNumberFormatter {
    /**
     * Formatea un número del cartón con base en su estado.
     * @param number El número a formatear.
     * @param row La fila en la que se encuentra el número.
     * @param col La columna en la que se encuentra el número.
     * @param isMarked Indica si el número está marcado.
     * @param isLastCalled Indica si el número es el último cantado.
     * @param inWinPattern Indica si el número forma parte de una línea ganadora.
     * @return La cadena HTML formateada para el número.
     */
    String format(int number, int row, int col, boolean isMarked, boolean isLastCalled, boolean inWinPattern);
}