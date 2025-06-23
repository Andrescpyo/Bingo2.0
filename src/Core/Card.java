package Core;

import java.util.ArrayList;
import java.util.List;

public class Card {

    private List<Integer> numbers;       // Lista de números en el cartón
    private int rows;                    // Número de filas
    private int cols;                    // Número de columnas
    private boolean[][] marked;          // Matriz de casillas marcadas

    /**
     * Constructor de la clase Card.
     *
     * @param numbers Lista de números en el cartón.
     * @param rows    Número de filas.
     * @param cols    Número de columnas.
     */
    public Card(List<Integer> numbers, int rows, int cols) {
        this.numbers = numbers;
        this.rows = rows;
        this.cols = cols;
        this.marked = new boolean[rows][cols];
    }

    /**
     * Marca un número en el cartón si está presente.
     *
     * @param number Número a marcar.
     * @return true si el número fue marcado; false si no estaba en el cartón.
     */
    public boolean markNumber(int number) {
        int index = numbers.indexOf(number);
        if (index != -1) {
            int row = index / cols;
            int col = index % cols;
            marked[row][col] = true;
            return true;
        }
        return false;
    }

    /**
     * Devuelve la lista de números del cartón.
     */
    public List<Integer> getNumbers() {
        return numbers;
    }

    /**
     * Devuelve la matriz de celdas marcadas.
     */
    public boolean[][] getMarked() {
        return marked;
    }

    /**
     * Devuelve el número de filas del cartón.
     */
    public int getRows() {
        return rows;
    }

    /**
     * Devuelve el número de columnas del cartón.
     */
    public int getCols() {
        return cols;
    }

    /**
     * Muestra el cartón en consola 
     */
    public void display() {
        for (int i = 0; i < numbers.size(); i++) {
            int number = numbers.get(i);
            boolean isMarked = marked[i / cols][i % cols];
            System.out.printf("%2d%s ", number, isMarked ? "*" : " ");
            if ((i + 1) % cols == 0) {
                System.out.println();
            }
        }
        System.out.println();
    }
}
