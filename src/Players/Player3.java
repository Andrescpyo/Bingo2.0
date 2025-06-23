package Players;

import Core.Card;
import Patterns.Behavioral.Strategy.WinStrategy;

public class Player3 extends Player {

    public Player3(String name) {
        super(name);
    }

    @Override
    public boolean checkBingo() {
        for (Card card : cards) {
            if (winStrategy != null && winStrategy.checkWin(card)) {
                System.out.println("¡" + name + " hizo BINGO!");
                // Usa el nuevo método para mostrar el cartón ganador con decoración
                System.out.println(getCardDisplayString(card, null)); // No hay bola actual para resaltar
                return true;
            }
        }
        return false;
    }

    @Override
    public void showCards() {
        System.out.println("Cartones de " + name + ":");
        for (Card card : cards) {
            // Usa el nuevo método para mostrar los cartones
            System.out.println(getCardDisplayString(card, null));
        }
    }

    @Override
    public void update(int ball) {
        boolean markedAny = false;
        for (Card card : cards) {
            if (card.markNumber(ball)) {
                System.out.println(name + ": ¡El número " + ball + " está en uno de mis cartones!");
                // Usa el nuevo método para mostrar el cartón actualizado
                System.out.println(getCardDisplayString(card, ball)); // Pasa la bola actual
                markedAny = true;
            }
        }

        if (!markedAny) {
            System.out.println(name + ": El número " + ball + " no está en ninguno de mis cartones.");
        }
    }

    @Override
    public String updateGUI(int ball) {
        StringBuilder result = new StringBuilder();
        boolean markedAny = false;

        for (Card card : cards) {
            if (card.markNumber(ball)) {
                result.append(name)
                        .append(": ¡El número ")
                        .append(ball)
                        .append(" está en uno de mis cartones!\n");
                // Usa el nuevo método para obtener la representación HTML del cartón
                result.append(getCardDisplayString(card, ball)).append("\n");
                markedAny = true;
            }
        }

        if (!markedAny) {
            result.append(name)
                    .append(": El número ")
                    .append(ball)
                    .append(" no está en ninguno de mis cartones.\n");
        }

        return result.toString();
    }

    @Override
    public boolean checkBingoGUI() {
        for (Card card : cards) {
            if (winStrategy != null && winStrategy.checkWin(card)) {
                return true;
            }
        }
        return false;
    }
}