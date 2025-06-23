package Patterns.Behavioral.Strategy;

import Core.Card;

public class HorizontalWinStrategy implements WinStrategy {

    @Override
    public boolean checkWin(Card card) {
        int rows = card.getRows();
        int cols = card.getCols();
        boolean[][] marked = card.getMarked();

        for (int i = 0; i < rows; i++) {
            boolean complete = true;
            for (int j = 0; j < cols; j++) {
                if (!marked[i][j]) {
                    complete = false;
                    break;
                }
            }
            if (complete) return true;
        }
        return false;
    }

    @Override
    public String getName() {
        return "Línea horizontal";
    }
}
