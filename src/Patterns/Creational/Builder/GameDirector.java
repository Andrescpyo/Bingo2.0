package Patterns.Creational.Builder;

import Core.Game;
import Players.Player;

import java.util.List;

public class GameDirector {
    private GameBuilder builder;

    public GameDirector(GameBuilder builder) {
        this.builder = builder;
    }

    public Game constructGame(List<Player> players, int cardsPerPlayer) {
        builder.setPlayers(players);
        builder.setCardsPerPlayer(cardsPerPlayer);
        builder.buildGame();
        return builder.getGame();
    }
}
