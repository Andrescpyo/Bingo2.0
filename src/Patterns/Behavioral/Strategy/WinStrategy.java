package Patterns.Behavioral.Strategy;

import Core.Card;

public interface WinStrategy {
    boolean checkWin(Card card);
    String getName(); // Para mostrar "Ganó con estrategia Horizontal", etc.
}
