package Players;

import Core.Card;

import java.util.ArrayList;
import java.util.List;

import Patterns.Behavioral.Observer.Observer;
import Patterns.Behavioral.Strategy.WinStrategy;
import Patterns.Structural.Decorator.ICardNumberFormatter;

public abstract class Player implements Observer {

    protected String name;
    protected List<Card> cards;
    protected WinStrategy winStrategy;
    private int tempCardCount = 1;
    protected ICardNumberFormatter cardNumberFormatter;

    public Player(String name) {
        this.name = name;
        this.cards = new ArrayList<>();
        this.cardNumberFormatter = new Patterns.Structural.Decorator.BaseNumberFormatter();
    }

    public String getName() {
        return name;
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public List<Card> getCards() {
        return cards;
    }

    public abstract boolean checkBingo();

    public abstract void showCards();

    @Override
    public abstract void update(int ball);

    public abstract String updateGUI(int ball);

    public void setWinStrategy(WinStrategy winStrategy) {
        this.winStrategy = winStrategy;
    }

    public void setCardNumberFormatter(ICardNumberFormatter formatter) {
        this.cardNumberFormatter = formatter;
    }

    public abstract boolean checkBingoGUI();

    public void setTempCardCount(int tempCardCount) {
        this.tempCardCount = tempCardCount;
    }

    public int getTempCardCount() {
        return tempCardCount;
    }

    /**
     * Genera la representación HTML de un cartón de Bingo, usando los decoradores.
     * ¡CRÍTICO!: Este método solo devuelve el fragmento HTML del cartón (ej. <pre>...</pre>),
     * sin las etiquetas <html> o <body>.
     */
    public String getCardDisplayString(Card card, Integer lastCalledBall) {
        StringBuilder sb = new StringBuilder();
        List<Integer> numbers = card.getNumbers();
        boolean[][] marked = card.getMarked();
        int rows = card.getRows();
        int cols = card.getCols();

        boolean isWinningCard = (winStrategy != null && winStrategy.checkWin(card));
        boolean[][] winPattern = null;
        if (isWinningCard) {
            if (winStrategy instanceof Patterns.Behavioral.Strategy.HorizontalWinStrategy) {
                for (int r = 0; r < rows; r++) {
                    boolean fullRow = true;
                    for (int c = 0; c < cols; c++) {
                        if (!marked[r][c]) {
                            fullRow = false;
                            break;
                        }
                    }
                    if (fullRow) {
                        winPattern = new boolean[rows][cols];
                        for(int c=0; c<cols; c++) winPattern[r][c] = true;
                        break;
                    }
                }
            } else if (winStrategy instanceof Patterns.Behavioral.Strategy.VerticalWinStrategy) {
                for (int c = 0; c < cols; c++) {
                    boolean fullCol = true;
                    for (int r = 0; r < rows; r++) {
                        if (!marked[r][c]) {
                            fullCol = false;
                            break;
                        }
                    }
                    if (fullCol) {
                        winPattern = new boolean[rows][cols];
                        for(int r=0; r<rows; r++) winPattern[r][c] = true;
                        break;
                    }
                }
            } else if (winStrategy instanceof Patterns.Behavioral.Strategy.DiagonalWinStrategy) {
                boolean primaryDiagonal = true;
                for (int i = 0; i < rows; i++) {
                    if (!marked[i][i]) {
                        primaryDiagonal = false;
                        break;
                    }
                }
                if (primaryDiagonal) {
                    winPattern = new boolean[rows][cols];
                    for (int i = 0; i < rows; i++) winPattern[i][i] = true;
                }

                boolean secondaryDiagonal = true;
                for (int i = 0; i < rows; i++) {
                    if (!marked[i][rows - 1 - i]) {
                        secondaryDiagonal = false;
                        break;
                    }
                }
                if (secondaryDiagonal) {
                    if (winPattern == null) winPattern = new boolean[rows][cols];
                    for (int i = 0; i < rows; i++) winPattern[i][rows - 1 - i] = true;
                }
            } else if (winStrategy instanceof Patterns.Behavioral.Strategy.LShapeWinStrategy ||
                       winStrategy instanceof Patterns.Behavioral.Strategy.XShapeWinStrategy) {
                // Para L y X, simplemente asume que si checkWin() es true,
                // no necesitas un patrón visual detallado aquí, ya que el formateador
                // solo marca los números que ya están marcados.
                // Si quisieras el patrón exacto, L y X requerirían más lógica aquí.
                // Por ahora, solo se marcarán los números que ya estén en 'marked'.
            }
        }


        sb.append("<pre>"); // Solo la etiqueta <pre>
        for (int i = 0; i < numbers.size(); i++) {
            int number = numbers.get(i);
            int row = i / cols;
            int col = i % cols;
            boolean cellIsMarked = marked[row][col];
            boolean cellIsLastCalled = (lastCalledBall != null && number == lastCalledBall);
            boolean cellInWinPattern = (winPattern != null && winPattern[row][col]);

            sb.append(cardNumberFormatter.format(number, row, col, cellIsMarked, cellIsLastCalled, cellInWinPattern)).append(" ");

            if ((i + 1) % cols == 0) {
                sb.append("\n");
            }
        }
        sb.append("</pre>"); // Solo la etiqueta </pre>
        return sb.toString();
    }
}