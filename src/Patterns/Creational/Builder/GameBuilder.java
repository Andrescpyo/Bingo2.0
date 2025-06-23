package Patterns.Creational.Builder;

import Core.Game;
import Players.Player;
import java.util.List;

public interface GameBuilder {
    void setPlayers(List<Player> players);
    void setCardsPerPlayer(int cards);
    void buildGame();
    Game getGame();
}
