package Patterns.Behavioral.Strategy;

import Core.Card;

/**
 * Clase LShapeWinStrategy
 *
 * Implementación concreta de la interfaz {@link WinStrategy}.
 * Define un algoritmo específico para determinar si un cartón de Bingo
 * ha ganado al tener todos los números marcados formando una "L".
 * Esta estrategia considera dos posibles formas de 'L':
 * 1. Columna izquierda + Fila inferior
 * 2. Columna derecha + Fila superior
 *
 * Rol en el patrón Strategy: Concrete Strategy (Estrategia Concreta)
 * - Implementa el algoritmo complejo para la condición de victoria en forma de 'L'.
 * - Es una de las múltiples estrategias que pueden ser utilizadas de forma intercambiable
 * para determinar la victoria en un cartón.
 */
public class LShapeWinStrategy implements WinStrategy {

    /**
     * Comprueba si el {@link Card cartón} proporcionado ha logrado una victoria
     * formando una "L" (columna izquierda + fila inferior) o una "L" invertida
     * (columna derecha + fila superior).
     *
     * @param card El cartón de Bingo a verificar.
     * @return true si el cartón ha logrado una victoria en forma de 'L', false en caso contrario.
     */
    @Override
    public boolean checkWin(Card card) {
        int rows = card.getRows(); // Obtiene el número de filas del cartón
        int cols = card.getCols(); // Obtiene el número de columnas del cartón
        boolean[][] marked = card.getMarked(); // Obtiene la matriz de estados de marcado del cartón

        // --- Patrón L invertida: Columna izquierda (j=0) y Fila inferior (i=rows-1) ---
        boolean leftColBottomRow = true;

        // Verificar la columna izquierda
        for (int i = 0; i < rows; i++) {
            if (!marked[i][0]) { // Si algún número en la primera columna no está marcado
                leftColBottomRow = false; // La "L" invertida no está completa
                break; // No es necesario revisar el resto de la columna
            }
        }

        // Si la columna izquierda está marcada, verificar la fila inferior
        // Solo verificamos si leftColBottomRow aún es true, para optimizar.
        if (leftColBottomRow) {
            for (int j = 0; j < cols; j++) {
                if (!marked[rows - 1][j]) { // Si algún número en la última fila no está marcado
                    leftColBottomRow = false; // La "L" invertida no está completa
                    break; // No es necesario revisar el resto de la fila
                }
            }
        }

        // --- Patrón L normal: Columna derecha (j=cols-1) y Fila superior (i=0) ---
        boolean rightColTopRow = true;

        // Verificar la columna derecha
        for (int i = 0; i < rows; i++) {
            if (!marked[i][cols - 1]) { // Si algún número en la última columna no está marcado
                rightColTopRow = false; // La "L" normal no está completa
                break; // No es necesario revisar el resto de la columna
            }
        }

        // Si la columna derecha está marcada, verificar la fila superior
        // Solo verificamos si rightColTopRow aún es true, para optimizar.
        if (rightColTopRow) {
            for (int j = 0; j < cols; j++) {
                if (!marked[0][j]) { // Si algún número en la primera fila no está marcado
                    rightColTopRow = false; // La "L" normal no está completa
                    break; // No es necesario revisar el resto de la fila
                }
            }
        }

        // El cartón gana si se cumple cualquiera de las dos condiciones de "L"
        return leftColBottomRow || rightColTopRow;
    }

    /**
     * Devuelve el nombre descriptivo de esta estrategia de victoria.
     *
     * @return La cadena "Letra L", representando el tipo de victoria.
     */
    @Override
    public String getName() {
        return "Letra L";
    }
}