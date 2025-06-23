package Patterns.Behavioral.Strategy;

import Core.Card;

public class XShapeWinStrategy implements WinStrategy {

    @Override
    public boolean checkWin(Card card) {
        int size = card.getRows();
        boolean[][] marked = card.getMarked();

        boolean xShape = true;

        for (int i = 0; i < size; i++) {
            if (!marked[i][i] || !marked[i][size - 1 - i]) {
                xShape = false;
                break;
            }
        }

        return xShape;
    }

    @Override
    public String getName() {
        return "Cruz (X)";
    }
}
