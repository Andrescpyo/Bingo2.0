package Patterns.Behavioral.Strategy;

import Core.Card;

/**
 * Clase HorizontalWinStrategy
 *
 * Implementación concreta de la interfaz {@link WinStrategy}.
 * Define un algoritmo específico para determinar si un cartón de Bingo
 * ha ganado al tener todos los números marcados en alguna de sus filas horizontales.
 *
 * Rol en el patrón Strategy: Concrete Strategy (Estrategia Concreta)
 * - Implementa el algoritmo definido por la interfaz {@link WinStrategy}.
 * - Este algoritmo es intercambiable con otras estrategias de victoria (ej., {@link DiagonalWinStrategy}).
 */
public class HorizontalWinStrategy implements WinStrategy {

    /**
     * Comprueba si el {@link Card cartón} proporcionado ha logrado una victoria
     * marcando todos los números en al menos una de sus filas horizontales.
     *
     * @param card El cartón de Bingo a verificar.
     * @return true si el cartón ha logrado una victoria por línea horizontal, false en caso contrario.
     */
    @Override
    public boolean checkWin(Card card) {
        int rows = card.getRows(); // Obtiene el número de filas del cartón
        int cols = card.getCols(); // Obtiene el número de columnas del cartón
        boolean[][] marked = card.getMarked(); // Obtiene la matriz de estados de marcado del cartón

        // Itera sobre cada fila del cartón
        for (int i = 0; i < rows; i++) {
            boolean complete = true; // Bandera para indicar si la fila actual está completa
            // Itera sobre cada columna en la fila actual
            for (int j = 0; j < cols; j++) {
                // Si algún número en la fila no está marcado, la fila no está completa
                if (!marked[i][j]) {
                    complete = false;
                    break; // No es necesario revisar el resto de la fila
                }
            }
            // Si la fila actual está completa (todos los números marcados), el cartón ha ganado
            if (complete) {
                return true;
            }
        }
        // Si ninguna fila horizontal está completamente marcada después de revisar todas, el cartón no ha ganado
        return false;
    }

    /**
     * Devuelve el nombre descriptivo de esta estrategia de victoria.
     *
     * @return La cadena "Línea horizontal", representando el tipo de victoria.
     */
    @Override
    public String getName() {
        return "Línea horizontal";
    }
}