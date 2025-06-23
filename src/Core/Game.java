package Core;

import Players.Player;
import Patterns.Behavioral.*;
import Patterns.Behavioral.Strategy.DiagonalWinStrategy;
import Patterns.Behavioral.Strategy.HorizontalWinStrategy;
import Patterns.Behavioral.Strategy.LShapeWinStrategy;
import Patterns.Behavioral.Strategy.VerticalWinStrategy;
import Patterns.Behavioral.Strategy.WinStrategy;
import Patterns.Behavioral.Strategy.XShapeWinStrategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Game extends Patterns.Behavioral.Observer.Subject {

    private List<Player> players;
    private List<Integer> availableBalls;
    private List<Integer> calledBalls;
    private BallCaller ballCaller;
    private WinStrategy winStrategy;

    public Game() {
        this.players = new ArrayList<>();
        this.availableBalls = new ArrayList<>();
        for (int i = 1; i <= 75; i++) {
            availableBalls.add(i);
        }
        this.calledBalls = new ArrayList<>();
        this.ballCaller = BallCaller.getInstance(1, 75);
    }

    public void addPlayer(Player player) {
        players.add(player);
        attach(player);
    }

    public List<Player> getPlayers() {
        return players;
    }

    public WinStrategy getWinStrategy() {
        return winStrategy;
    }

    public List<Integer> getCalledBalls() {
        return calledBalls;
    }

    /**
     * Juega una ronda del juego, canta una bola y notifica a los observadores.
     * @return La bola cantada en esta ronda, o -1 si no quedan más bolas.
     */
    public int playRoundGUI() {
        if (availableBalls.isEmpty()) {
            return -1; // Indicar que no se cantó una bola
        }

        int calledBall = ballCaller.callBall();
        calledBalls.add(calledBall);
        notifyObservers(calledBall); // Notificar a todos los jugadores

        return calledBall;
    }

    /**
     * Método de inicio de juego simplificado para la GUI. La GUI manejará el bucle de rondas.
     * @return Un mensaje de inicio.
     */
    public String startGameGUI() {
        return "Juego listo para iniciar rondas.";
    }

    /**
     * Elige aleatoriamente una estrategia de victoria y la establece para el juego y todos los jugadores.
     */
    public void randomlyChooseWinStrategy() {
        List<WinStrategy> strategies = Arrays.asList(
                new HorizontalWinStrategy(),
                new VerticalWinStrategy(),
                new DiagonalWinStrategy(),
                new LShapeWinStrategy(),
                new XShapeWinStrategy()
        );
        Random random = new Random();
        winStrategy = strategies.get(random.nextInt(strategies.size()));

        for (Player player : players) {
            player.setWinStrategy(winStrategy);
        }
    }
}