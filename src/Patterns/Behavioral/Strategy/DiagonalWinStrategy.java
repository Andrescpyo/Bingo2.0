package Patterns.Behavioral.Strategy;

import Core.Card;

public class DiagonalWinStrategy implements WinStrategy {

    @Override
    public boolean checkWin(Card card) {
        int size = card.getRows(); // Suponiendo 5x5
        boolean[][] marked = card.getMarked();

        boolean primaryDiagonal = true;
        boolean secondaryDiagonal = true;

        for (int i = 0; i < size; i++) {
            if (!marked[i][i]) {
                primaryDiagonal = false;
            }
            if (!marked[i][size - 1 - i]) {
                secondaryDiagonal = false;
            }
        }

        return primaryDiagonal || secondaryDiagonal;
    }

    @Override
    public String getName() {
        return "Diagonal";
    }
}
