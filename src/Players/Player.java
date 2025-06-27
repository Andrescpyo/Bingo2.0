package Players;

import Core.Card;
import Patterns.Structural.Decorator.ICardNumberFormatter;
import Patterns.Behavioral.Observer.Observer;
import Patterns.Behavioral.Strategy.WinStrategy;

import java.util.ArrayList;
import java.util.List;

public abstract class Player implements Observer {
    private String name;
    private List<Card> cards;
    private int tempCardCount;
    protected ICardNumberFormatter cardNumberFormatter;

    public Player(String name) {
        this.name = name;
        this.cards = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void addCard(Card card) {
        this.cards.add(card);
    }

    public void setTempCardCount(int count) {
        this.tempCardCount = count;
    }

    public int getTempCardCount() {
        return tempCardCount;
    }

    public void setCardNumberFormatter(ICardNumberFormatter formatter) {
        this.cardNumberFormatter = formatter;
    }

    public String getCardDisplayString(Card card, Integer lastCalledBall) {
        if (cardNumberFormatter == null) {
            return "<html><body>Formato no configurado</body></html>";
        }

        StringBuilder html = new StringBuilder();
        html.append("<html><head><style>");
        html.append("table { width:100%; border-collapse: collapse; table-layout: fixed; }"); // fixed layout crucial para ancho de columna
        html.append("th, td { border: 1px solid black; padding: 8px; text-align: center; vertical-align: middle; }"); // Centrado y padding
        html.append("th { background-color:#f0f0f0; font-size:16pt; font-weight:bold; }"); // Estilo para encabezados
        html.append("td { font-size:14pt; }"); // Ajusta el tamaño de la fuente si es necesario
        html.append("</style></head><body>");

        html.append("<table>");

        // Encabezado BINGO
        html.append("<tr>");
        for (String header : Card.getColumnHeaders()) {
            html.append("<th>").append(header).append("</th>");
        }
        html.append("</tr>");

        // Números del cartón
        for (int i = 0; i < card.getRows(); i++) {
            html.append("<tr>");
            for (int j = 0; j < card.getCols(); j++) {
                int number = card.getNumber(i, j);
                boolean isMarked = card.isMarked(i, j);
                boolean isLastCalled = (lastCalledBall != null && number == lastCalledBall);
                
                String displayValue;
                if (i == 2 && j == 2 && card.getRows() == 5 && card.getCols() == 5 && number == 0) {
                    displayValue = "<font color='purple'><b>FREE</b></font>";
                } else {
                    displayValue = cardNumberFormatter.format(number, i, j, isMarked, isLastCalled, false);
                }
                
                html.append("<td>").append(displayValue).append("</td>");
            }
            html.append("</tr>");
        }
        html.append("</table>");
        html.append("</body></html>"); // Cierra tags HTML
        return html.toString();
    }

    public boolean checkBingoGUI(WinStrategy currentWinStrategy) {
        if (currentWinStrategy == null) {
            return false;
        }
        for (Card card : cards) {
            if (currentWinStrategy.checkWin(card)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void update(int ball) {
        for (Card card : cards) {
            card.markNumber(ball);
        }
    }
}