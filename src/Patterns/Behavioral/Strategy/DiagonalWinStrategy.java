// Carpeta: Patterns/Behavioral/Strategy
// Archivo: DiagonalWinStrategy.java
package Patterns.Behavioral.Strategy;

import Core.Card;

/**
 * Clase DiagonalWinStrategy
 *
 * Implementación concreta de la interfaz {@link WinStrategy}.
 * Define un algoritmo específico para determinar si un cartón de Bingo
 * ha ganado al tener todos los números marcados en cualquiera de sus diagonales (principal o secundaria).
 *
 * Rol en el patrón Strategy: Concrete Strategy (Estrategia Concreta)
 * - Implementa el algoritmo definido por la interfaz {@link WinStrategy}.
 * - Este algoritmo es intercambiable con otras estrategias de victoria (ej., {@link HorizontalWinStrategy}).
 */
public class DiagonalWinStrategy implements WinStrategy {

    /**
     * Comprueba si el {@link Card cartón} proporcionado ha logrado una victoria
     * marcando todos los números en alguna de sus diagonales (principal o secundaria).
     *
     * @param card El cartón de Bingo a verificar.
     * @return true si el cartón ha logrado una victoria por diagonal, false en caso contrario.
     * Retorna true si todos los números en la diagonal principal (0,0 -> N-1,N-1)
     * o en la diagonal secundaria (0,N-1 -> N-1,0) están marcados.
     */
    @Override
    public boolean checkWin(Card card) {
        // Suponemos que el cartón es cuadrado (ej., 5x5) para las diagonales.
        // El tamaño se obtiene del número de filas, asumiendo que filas == columnas.
        int size = card.getRows();
        boolean[][] marked = card.getMarked();

        // Bandera para la diagonal principal (de arriba izquierda a abajo derecha)
        boolean primaryDiagonal = true;
        // Bandera para la diagonal secundaria (de arriba derecha a abajo izquierda)
        boolean secondaryDiagonal = true;

        // Itera a través del cartón para verificar ambas diagonales
        for (int i = 0; i < size; i++) {
            // Verifica la diagonal principal: (0,0), (1,1), (2,2), etc.
            if (!marked[i][i]) {
                primaryDiagonal = false; // Si un número no está marcado, la diagonal principal no está completa
            }
            // Verifica la diagonal secundaria: (0, size-1), (1, size-2), etc.
            if (!marked[i][size - 1 - i]) {
                secondaryDiagonal = false; // Si un número no está marcado, la diagonal secundaria no está completa
            }
        }

        // Retorna true si al menos una de las diagonales está completamente marcada
        return primaryDiagonal || secondaryDiagonal;
    }

    /**
     * Devuelve el nombre descriptivo de esta estrategia de victoria.
     *
     * @return La cadena "Diagonal", representando el tipo de victoria.
     */
    @Override
    public String getName() {
        return "Diagonal";
    }
}