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
        // Reducir la fuente base aún más para que la tabla sea más pequeña.
        html.append("body { margin: 0; padding: 0; font-family: 'Comic Sans MS', cursive, sans-serif; font-size: 8pt; line-height: 1.0; }"); 

        // Tabla al 100% de ancho y alto, pero como el JEditorPane es grande, la tabla será "pequeña" en proporción a él.
        // Quitamos los border-radius que pueden causar problemas con el JEditorPane.
        html.append("table { width:100%; height:100%; border-collapse: collapse; border: 3px solid #4CAF50; }"); 
        
        // Celdas: padding mínimo, texto centrado, alineación vertical media.
        html.append("th, td { border: 1px solid #8BC34A; padding: 1px; text-align: center; vertical-align: middle; }"); // Padding muy pequeño
        html.append("th { background-color:#FFEB3B; font-size:10pt; font-weight:bold; color: #D32F2F; text-shadow: 1px 1px 1px #000000; }"); // Fuente de encabezado más pequeña
        html.append("td { font-size:12pt; font-weight: bold; color: #3F51B5; background-color: #E8F5E9; }"); // Fuente de números más pequeña
        html.append("td font { color: purple; font-weight: bold; }"); 

        // Estilos para "FREE"
        html.append(".free-cell { background-color: #FFC107; color: #FFFFFF; font-size: 12pt; font-weight: bold; text-shadow: 1px 1px 1px #000000; }"); 

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
                    displayValue = "<span class='free-cell'>FREE</span>"; 
                } else {
                    displayValue = cardNumberFormatter.format(number, i, j, isMarked, isLastCalled, false);
                }
                
                html.append("<td>").append(displayValue).append("</td>");
            }
            html.append("</tr>");
        }
        html.append("</table>");
        html.append("</body></html>");
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