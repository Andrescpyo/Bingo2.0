package Patterns.Behavioral.Strategy;

import Core.Card;

public class LShapeWinStrategy implements WinStrategy {

    @Override
    public boolean checkWin(Card card) {
        int rows = card.getRows();
        int cols = card.getCols();
        boolean[][] marked = card.getMarked();

        // L invertida (columna izquierda y fila inferior)
        boolean leftColBottomRow = true;
        for (int i = 0; i < rows; i++) {
            if (!marked[i][0]) {
                leftColBottomRow = false;
                break;
            }
        }
        for (int j = 0; j < cols; j++) {
            if (!marked[rows - 1][j]) {
                leftColBottomRow = false;
                break;
            }
        }

        // L normal (columna derecha y fila superior)
        boolean rightColTopRow = true;
        for (int i = 0; i < rows; i++) {
            if (!marked[i][cols - 1]) {
                rightColTopRow = false;
                break;
            }
        }
        for (int j = 0; j < cols; j++) {
            if (!marked[0][j]) {
                rightColTopRow = false;
                break;
            }
        }

        return leftColBottomRow || rightColTopRow;
    }

    @Override
    public String getName() {
        return "Letra L";
    }
}
