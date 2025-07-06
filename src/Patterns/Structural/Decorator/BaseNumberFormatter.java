package Patterns.Structural.Decorator;

/**
 * Clase BaseNumberFormatter
 *
 * Esta clase es una implementación concreta del componente base para el patrón Decorator.
 * Proporciona el formato predeterminado y sin adornos para un número de cartón de Bingo.
 * Es el punto de partida sobre el cual se pueden añadir decoraciones adicionales.
 *
 * Rol en el patrón Decorator: Concrete Component (Componente Concreto)
 * - Implementa la interfaz {@link ICardNumberFormatter Componente}.
 * - Es el objeto original al que se le añade comportamiento (formato visual) de forma dinámica.
 */
public class BaseNumberFormatter implements ICardNumberFormatter {
    /**
     * Provee el formato por defecto para un número de cartón.
     * Este método es la implementación básica del comportamiento de formato.
     * Los decoradores subsiguientes añadirán funcionalidad a este formato.
     *
     * @param number El número del cartón a formatear.
     * @param row La fila del número en el cartón.
     * @param col La columna del número en el cartón.
     * @param isMarked Booleano que indica si el número está marcado.
     * @param isLastCalled Booleano que indica si el número fue la última bola cantada.
     * @param inWinPattern Booleano que indica si el número forma parte de un patrón de victoria.
     * @return Una cadena formateada que representa el número, con padding para dos dígitos.
     */
    @Override
    public String format(int number, int row, int col, boolean isMarked, boolean isLastCalled, boolean inWinPattern) {
        // Si el número es 0 (que representa la casilla FREE en un cartón 5x5), se muestra como "FR".
        // De lo contrario, se formatea el número con un padding para que tenga al menos dos caracteres.
        return (number == 0) ? "FR" : String.format("%2d", number);
    }
}