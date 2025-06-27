package Core;

import Players.Player;

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
import java.util.Collections; // ¡Asegúrate de importar esto!

public class Game extends Patterns.Behavioral.Observer.Subject {

    private List<Player> players;
    private List<Integer> availableBalls; // Lista de bolas que AÚN NO HAN SIDO CANTADAS
    private List<Integer> calledBalls;    // Bolas ya cantadas
    private BallCaller ballCaller; // Instancia del bolillero

    private WinStrategy winStrategy;

    public Game() {
        this.players = new ArrayList<>();
        this.availableBalls = new ArrayList<>();
        // Inicializar availableBalls con todas las bolas y mezclarlas
        for (int i = 1; i <= 75; i++) { // Asumiendo rango de 1 a 75
            availableBalls.add(i);
        }
        Collections.shuffle(availableBalls); // ¡Importante para que salgan en orden aleatorio!
        
        this.calledBalls = new ArrayList<>();
        // El BallCaller ahora solo extrae de la lista que le damos, o genera un número si es un generador puro.
        // Si BallCaller ya tiene su propia lógica de bolillero, podríamos simplificar esto.
        // Pero para que 'availableBalls' sea el maestro de la verdad, BallCaller necesita ajustarse o ser solo un "lector".
        // Para el propósito de arreglar getBallsRemaining(), vamos a hacer que 'Game' controle las 'availableBalls'.
        this.ballCaller = BallCaller.getInstance(1, 75); // Sigue usando tu BallCaller si tiene lógica específica.
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
        if (availableBalls.isEmpty()) { // Si ya no quedan bolas en nuestra lista maestra
            return -1; 
        }

        // Obtén la siguiente bola de la lista de disponibles
        int calledBall = availableBalls.remove(0); // REMOVE la bola de la lista
        
        calledBalls.add(calledBall); // Añádela a las bolas ya cantadas
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
    }

    // --- ¡NUEVOS MÉTODOS AÑADIDOS! ---

    /**
     * Retorna el número de bolas que aún no han sido cantadas.
     * @return El número de bolas disponibles.
     */
    public int getBallsRemaining() {
        return availableBalls.size();
    }

    /**
     * Retorna verdadero si aún quedan bolas por cantar, falso en caso contrario.
     * @return true si hay bolas restantes, false si no.
     */
    public boolean hasMoreBalls() {
        return !availableBalls.isEmpty();
    }
}