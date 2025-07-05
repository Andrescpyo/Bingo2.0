package Patterns.Creational.Builder;

import Core.Game;
import Patterns.Creational.AbstractFactory.CardFactory;
import Core.Card;
import Players.Player;

import java.util.List;

public class DefaultGameBuilder implements GameBuilder {
    private Game game;
    private List<Player> players;
    private int cardsPerPlayer;

    private CardFactory factory = new CardFactory();

    @Override
    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    @Override
    public void setCardsPerPlayer(int cards) {
        this.cardsPerPlayer = cards;
    }

    @Override
    public void buildGame() {
        game = new Game();
        for (Player player : players) {
            for (int i = 0; i < cardsPerPlayer; i++) {
                player.addCard(factory.createDefaultCard());
            }
            game.addPlayer(player);
        }
    }

    @Override
    public Game getGame() {
        return game;
    }
}
