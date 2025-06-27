// Carpeta: Core
// Archivo: Card.java
package Core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Card {
    private int[][] numbers; // Almacenará los números del cartón 5x5
    private boolean[][] marked; // Almacenará el estado de marcado 5x5
    private final int rows;
    private final int cols;

    // Rangos de números para cada columna B-I-N-G-O
    private static final int[][] COLUMN_RANGES = {
            {1, 15},   // B
            {16, 30},  // I
            {31, 45},  // N
            {46, 60},  // G
            {61, 75}   // O
    };
    private static final String[] COLUMN_HEADERS = {"B", "I", "N", "G", "O"};

    public Card(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.numbers = new int[rows][cols];
        this.marked = new boolean[rows][cols];

        // Marcar la casilla central como FREE por defecto (si es 5x5)
        if (rows == 5 && cols == 5) {
            marked[2][2] = true; // La casilla central es 'FREE'
        }
        fillCardNumbers(); // Llenar los números del cartón según las reglas de Bingo
    }

    // Constructor para pruebas o casos específicos (no se usará mucho en el UI de Bingo)
    public Card(List<Integer> numbersList, int rows, int cols) {
        this(rows, cols); // Llama al constructor principal para inicializar la matriz
        // Sobreescribe los números generados por fillCardNumbers() si se proporcionan
        if (numbersList != null && numbersList.size() == rows * cols) {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    numbers[i][j] = numbersList.get(i * cols + j);
                }
            }
        }
        // Asegurar que la casilla FREE esté marcada
        if (rows == 5 && cols == 5) {
            marked[2][2] = true;
        }
    }


    private void fillCardNumbers() {
        Random random = new Random();
        for (int j = 0; j < cols; j++) { // Iterar por columnas
            List<Integer> availableNumbers = new ArrayList<>();
            int min = COLUMN_RANGES[j][0];
            int max = COLUMN_RANGES[j][1];
            for (int k = min; k <= max; k++) {
                availableNumbers.add(k);
            }
            Collections.shuffle(availableNumbers, random); // Barajar los números disponibles para la columna

            // Llenar 5 números para cada columna
            for (int i = 0; i < rows; i++) {
                if (i == 2 && j == 2 && rows == 5 && cols == 5) { // Si es la casilla central
                    numbers[i][j] = 0; // Representa FREE, usaremos 0 o un valor especial
                } else {
                    numbers[i][j] = availableNumbers.remove(0); // Tomar el primer número barajado
                }
            }
        }
    }

    public boolean markNumber(int ball) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                // No intentar marcar la casilla FREE (ya está marcada)
                if (i == 2 && j == 2 && rows == 5 && cols == 5) {
                    continue;
                }
                if (numbers[i][j] == ball) {
                    marked[i][j] = true;
                    return true; // Número encontrado y marcado
                }
            }
        }
        return false; // Número no encontrado
    }

    public int getNumber(int row, int col) {
        return numbers[row][col];
    }

    public boolean isMarked(int row, int col) {
        return marked[row][col];
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public boolean[][] getMarked() {
        return marked;
    }

    // Método para obtener los encabezados de columna
    public static String[] getColumnHeaders() {
        return COLUMN_HEADERS;
    }
}