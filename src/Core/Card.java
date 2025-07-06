package Core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Clase Card
 *
 * Representa un cartón de Bingo con una cuadrícula de números y su estado de marcado.
 * La clase maneja la generación de números siguiendo las reglas estándar del Bingo (B-I-N-G-O)
 * y permite marcar números a medida que son llamados.
 *
 * Esta clase es un componente fundamental del sistema, y es utilizada por otras clases
 * como {@link Player} y las {@link WinStrategy} para
 * verificar las condiciones de victoria.
 *
 * No participa directamente en un patrón de diseño GoF en sí misma, pero es un "producto"
 * para el patrón {@link CardFactory} y un "elemento"
 * sobre el cual el patrón {@link WinStrategy} opera.
 */
public class Card {
    /**
     * Matriz que almacena los números del cartón (por ejemplo, 5x5).
     * El valor 0 se usa para representar la casilla "FREE" si aplica.
     */
    private int[][] numbers;

    /**
     * Matriz booleana que almacena el estado de marcado de cada número en el cartón.
     * `true` si el número ha sido marcado, `false` en caso contrario.
     */
    private boolean[][] marked;

    /**
     * Número de filas del cartón.
     */
    private final int rows;

    /**
     * Número de columnas del cartón.
     */
    private final int cols;

    /**
     * Rangos de números para cada columna del cartón de Bingo, siguiendo la convención B-I-N-G-O.
     * Cada sub-array define [min, max] para una columna específica.
     */
    private static final int[][] COLUMN_RANGES = {
            {1, 15},   // B
            {16, 30},  // I
            {31, 45},  // N
            {46, 60},  // G
            {61, 75}   // O
    };

    /**
     * Encabezados de las columnas del cartón de Bingo.
     */
    private static final String[] COLUMN_HEADERS = {"B", "I", "N", "G", "O"};

    /**
     * Constructor principal para crear un cartón de Bingo.
     * Inicializa las dimensiones del cartón y rellena los números según las reglas del Bingo.
     * Si el cartón es 5x5, la casilla central se marca como "FREE" por defecto.
     *
     * @param rows El número de filas del cartón.
     * @param cols El número de columnas del cartón.
     */
    public Card(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.numbers = new int[rows][cols];
        this.marked = new boolean[rows][cols];

        // Marcar la casilla central como FREE por defecto si es un cartón de 5x5
        if (rows == 5 && cols == 5) {
            marked[2][2] = true; // La casilla central (fila 2, columna 2) es 'FREE'
        }
        fillCardNumbers(); // Llenar los números del cartón según las reglas de Bingo
    }

    /**
     * Constructor alternativo para crear un cartón con números predefinidos.
     * Útil para pruebas o escenarios donde los números no se generan aleatoriamente.
     * Llama al constructor principal para la inicialización y luego sobrescribe los números.
     *
     * @param numbersList Una lista de enteros que contiene los números para el cartón,
     * en orden de fila principal. Debe tener `rows * cols` elementos.
     * @param rows El número de filas del cartón.
     * @param cols El número de columnas del cartón.
     */
    public Card(List<Integer> numbersList, int rows, int cols) {
        this(rows, cols); // Llama al constructor principal para inicializar las matrices y la casilla FREE
        // Sobreescribe los números generados por fillCardNumbers() si se proporcionan
        if (numbersList != null && numbersList.size() == rows * cols) {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    numbers[i][j] = numbersList.get(i * cols + j);
                }
            }
        }
        // Asegurar que la casilla FREE esté marcada (esto ya lo hace el constructor principal, pero se mantiene explícito)
        if (rows == 5 && cols == 5) {
            marked[2][2] = true;
        }
    }

    /**
     * Rellena la matriz de números del cartón siguiendo las reglas estándar del Bingo.
     * Cada columna (B, I, N, G, O) tiene un rango específico de números (por ejemplo, B: 1-15, I: 16-30).
     * Los números dentro de cada columna se seleccionan aleatoriamente y sin repetición.
     * La casilla central (si el cartón es 5x5) se establece como 0 para representar "FREE".
     */
    private void fillCardNumbers() {
        Random random = new Random();
        for (int j = 0; j < cols; j++) { // Iterar por columnas (0 a 4 para B-I-N-G-O)
            List<Integer> availableNumbers = new ArrayList<>();
            int min = COLUMN_RANGES[j][0];
            int max = COLUMN_RANGES[j][1];
            for (int k = min; k <= max; k++) {
                availableNumbers.add(k); // Llenar la lista con todos los números posibles para esta columna
            }
            // Barajar los números disponibles para la columna para asegurar la aleatoriedad
            Collections.shuffle(availableNumbers, random);

            // Llenar 5 números para cada columna del cartón
            for (int i = 0; i < rows; i++) {
                // Si es un cartón 5x5, la casilla central (2,2) es "FREE"
                if (i == 2 && j == 2 && rows == 5 && cols == 5) {
                    numbers[i][j] = 0; // Se usa 0 para representar la casilla FREE
                } else {
                    // Tomar el primer número barajado y eliminarlo de la lista para evitar duplicados en la columna
                    numbers[i][j] = availableNumbers.remove(0);
                }
            }
        }
    }

    /**
     * Intenta marcar un número en el cartón si este coincide con el número llamado (ball).
     * No marca la casilla "FREE" ya que se asume que siempre está marcada.
     *
     * @param ball El número que ha sido llamado.
     * @return `true` si el número fue encontrado y marcado en el cartón, `false` en caso contrario.
     */
    public boolean markNumber(int ball) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                // No intentar marcar la casilla FREE si aplica, ya está marcada por defecto.
                if (i == 2 && j == 2 && rows == 5 && cols == 5) {
                    continue;
                }
                if (numbers[i][j] == ball) {
                    marked[i][j] = true; // Marcar el número si coincide
                    return true; // Número encontrado y marcado con éxito
                }
            }
        }
        return false; // El número no se encontró en el cartón
    }

    /**
     * Obtiene el número en una posición específica del cartón.
     *
     * @param row La fila del número.
     * @param col La columna del número.
     * @return El número en la posición `(row, col)`.
     */
    public int getNumber(int row, int col) {
        return numbers[row][col];
    }

    /**
     * Verifica si un número en una posición específica del cartón está marcado.
     *
     * @param row La fila del número.
     * @param col La columna del número.
     * @return `true` si el número en `(row, col)` está marcado, `false` en caso contrario.
     */
    public boolean isMarked(int row, int col) {
        return marked[row][col];
    }

    /**
     * Obtiene el número de filas del cartón.
     *
     * @return El número de filas.
     */
    public int getRows() {
        return rows;
    }

    /**
     * Obtiene el número de columnas del cartón.
     *
     * @return El número de columnas.
     */
    public int getCols() {
        return cols;
    }

    /**
     * Obtiene la matriz completa que representa el estado de marcado del cartón.
     *
     * @return Una matriz booleana donde `true` indica una casilla marcada.
     */
    public boolean[][] getMarked() {
        return marked;
    }

    /**
     * Obtiene los encabezados de columna estándar para un cartón de Bingo (B, I, N, G, O).
     * Este es un método estático ya que los encabezados son constantes para todos los cartones.
     *
     * @return Un array de Strings con los encabezados de columna.
     */
    public static String[] getColumnHeaders() {
        return COLUMN_HEADERS;
    }
}