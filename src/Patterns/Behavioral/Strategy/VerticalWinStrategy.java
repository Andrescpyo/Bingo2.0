package Patterns.Behavioral.Strategy;

import Core.Card;

/**
 * Clase VerticalWinStrategy
 *
 * Implementación concreta de la interfaz {@link WinStrategy}.
 * Define un algoritmo específico para determinar si un cartón de Bingo
 * ha ganado al tener todos los números marcados en alguna de sus columnas verticales.
 *
 * Rol en el patrón Strategy: Concrete Strategy (Estrategia Concreta)
 * - Implementa el algoritmo definido por la interfaz {@link WinStrategy}.
 * - Este algoritmo es intercambiable con otras estrategias de victoria (ej., {@link HorizontalWinStrategy}).
 */
public class VerticalWinStrategy implements WinStrategy {

    /**
     * Comprueba si el {@link Card cartón} proporcionado ha logrado una victoria
     * marcando todos los números en al menos una de sus columnas verticales.
     *
     * @param card El cartón de Bingo a verificar.
     * @return true si el cartón ha logrado una victoria por línea vertical, false en caso contrario.
     */
    @Override
    public boolean checkWin(Card card) {
        int rows = card.getRows(); // Obtiene el número de filas del cartón
        int cols = card.getCols(); // Obtiene el número de columnas del cartón
        boolean[][] marked = card.getMarked(); // Obtiene la matriz de estados de marcado del cartón

        // Itera sobre cada columna del cartón
        for (int j = 0; j < cols; j++) {
            boolean complete = true; // Bandera para indicar si la columna actual está completa
            // Itera sobre cada fila en la columna actual
            for (int i = 0; i < rows; i++) {
                // Si algún número en la columna no está marcado, la columna no está completa
                if (!marked[i][j]) {
                    complete = false;
                    break; // No es necesario revisar el resto de la columna
                }
            }
            // Si la columna actual está completa (todos los números marcados), el cartón ha ganado
            if (complete) {
                return true;
            }
        }
        // Si ninguna columna vertical está completamente marcada después de revisar todas, el cartón no ha ganado
        return false;
    }

    /**
     * Devuelve el nombre descriptivo de esta estrategia de victoria.
     *
     * @return La cadena "Línea vertical", representando el tipo de victoria.
     */
    @Override
    public String getName() {
        return "Línea vertical";
    }
}