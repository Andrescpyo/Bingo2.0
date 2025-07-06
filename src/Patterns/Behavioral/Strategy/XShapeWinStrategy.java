package Patterns.Behavioral.Strategy;

import Core.Card;

/**
 * Clase XShapeWinStrategy
 *
 * Implementación concreta de la interfaz {@link WinStrategy}.
 * Define un algoritmo específico para determinar si un cartón de Bingo
 * ha ganado al tener todos los números marcados en ambas diagonales,
 * formando una "X".
 *
 * Rol en el patrón Strategy: Concrete Strategy (Estrategia Concreta)
 * - Implementa el algoritmo para la condición de victoria en forma de 'X'.
 * - Es una estrategia intercambiable que puede ser utilizada por el contexto
 * para determinar si un cartón ha ganado bajo este criterio.
 */
public class XShapeWinStrategy implements WinStrategy {

    /**
     * Comprueba si el {@link Card cartón} proporcionado ha logrado una victoria
     * marcando todos los números en ambas diagonales (principal y secundaria),
     * formando una "X".
     *
     * @param card El cartón de Bingo a verificar. Se asume que el cartón es cuadrado (ej., 5x5).
     * @return true si el cartón ha logrado una victoria en forma de 'X', false en caso contrario.
     */
    @Override
    public boolean checkWin(Card card) {
        int size = card.getRows(); // Obtiene el tamaño del cartón (asumiendo que filas == columnas para una 'X')
        boolean[][] marked = card.getMarked(); // Obtiene la matriz de estados de marcado del cartón

        boolean xShape = true; // Bandera para indicar si el patrón 'X' está completo

        // Itera a través del cartón para verificar ambas diagonales simultáneamente.
        // Un número debe estar marcado tanto en la diagonal principal como en la secundaria
        // para que la 'X' sea válida.
        for (int i = 0; i < size; i++) {
            // Verifica el elemento en la diagonal principal (i, i)
            // Y el elemento en la diagonal secundaria (i, size - 1 - i)
            // Si alguno de estos no está marcado, la 'X' no está completa.
            if (!marked[i][i] || !marked[i][size - 1 - i]) {
                xShape = false; // La forma de 'X' no está completa
                break; // No es necesario revisar más, ya falló el patrón
            }
        }

        // Retorna true si todos los números en ambas diagonales están marcados, formando una 'X'.
        return xShape;
    }

    /**
     * Devuelve el nombre descriptivo de esta estrategia de victoria.
     *
     * @return La cadena "Cruz (X)", representando el tipo de victoria.
     */
    @Override
    public String getName() {
        return "Cruz (X)";
    }
}