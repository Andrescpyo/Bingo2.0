package Patterns.Structural.Facade;

import Core.BallCaller;
import Core.Game;
import Patterns.Creational.FactoryMethod.CardFactory;
import Core.Card;
import Players.Player;

import java.util.List;

public class GameFacade {

    private Game game;
    private CardFactory cardFactory;

    public GameFacade() {
        this.game = new Game();
        this.cardFactory = new CardFactory();
    }

    public GameFacade(Game game) {
        this.game = game;
        this.cardFactory = new CardFactory();
    }

    public void registerPlayers(List<Player> players) {
        for (Player player : players) {
            int cardsPerPlayer = player.getTempCardCount();
            for (int i = 0; i < cardsPerPlayer; i++) {
                Card card = cardFactory.createDefaultCard();
                player.addCard(card);
            }
            game.addPlayer(player);
        }
    }

    /**
     * Inicializa las configuraciones del juego, incluyendo la elección de la
     * estrategia de victoria.
     */
    public void initializeGameSettings() {
        this.game.randomlyChooseWinStrategy();
    }

    public String startGameGUI() {
        return game.startGameGUI();
    }

    /**
     * Juega una ronda del juego y devuelve la bola cantada.
     *
     * @return La bola cantada, o -1 si el juego ha terminado.
     */
    public int playRound() {
        return game.playRoundGUI();
    }

    public List<Integer> getCalledBallsHistory() {
        return game.getCalledBalls();
    }

    public Game getGame() {
        return game;
    }

    public List<Player> getPlayers() {
        return game.getPlayers();
    }

    // Dentro de la clase GameFacade
    public int getBallsRemaining() {
        return game.getBallsRemaining(); // Asume que Game tiene este método
    }

// O, si prefieres un booleano para el GUI:
    public boolean areBallsLeft() {
        return game.getBallsRemaining() > 0;
    }
}
