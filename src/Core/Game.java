package Core;

import Players.Player; 

// Importaciones de patrones de estrategia
import Patterns.Behavioral.Strategy.DiagonalWinStrategy;
import Patterns.Behavioral.Strategy.HorizontalWinStrategy;
import Patterns.Behavioral.Strategy.LShapeWinStrategy;
import Patterns.Behavioral.Strategy.VerticalWinStrategy;
import Patterns.Behavioral.Strategy.WinStrategy; // Interfaz Strategy
import Patterns.Behavioral.Strategy.XShapeWinStrategy;

import java.util.ArrayList;
import java.util.Arrays;     
import java.util.List;
import java.util.Random;     
import java.util.Collections; 

/**
 * Clase Game
 *
 * Esta clase representa la lógica principal del juego de Bingo.
 * Gestiona a los jugadores, el flujo de las bolas (cantadas y disponibles),
 * y la estrategia de victoria actual.
 *
 * Implementa el patrón Observer como 'Sujeto' (Subject), notificando a los jugadores
 * (que actúan como 'Observadores') cada vez que se canta una nueva bola.
 * También utiliza el patrón Strategy para definir dinámicamente la condición de victoria.
 *
 * Roles en los patrones de diseño:
 * - Subject (Observer): Mantiene una lista de jugadores (observadores) y los notifica de
 * las bolas cantadas.
 * - Context (Strategy): La clase Game es el contexto que mantiene una referencia a una
 * `WinStrategy` y delega la lógica de comprobación de victoria a esta estrategia.
 */
public class Game extends Patterns.Behavioral.Observer.Subject {

    private List<Player> players;             // Lista de jugadores en el juego.
    private List<Integer> availableBalls;     // Bolas que aún no han sido cantadas.
    private List<Integer> calledBalls;        // Bolas que ya han sido cantadas.
    private BallCaller ballCaller;            // Instancia del bolillero (Singleton).

    private WinStrategy winStrategy;          // La estrategia de victoria actual (patrón Strategy).

    /**
     * Constructor de la clase Game.
     * Inicializa las listas de jugadores, bolas disponibles y bolas cantadas.
     * Prepara el conjunto inicial de bolas para el juego y las mezcla.
     */
    public Game() {
        this.players = new ArrayList<>();
        this.availableBalls = new ArrayList<>();
        // Inicializa 'availableBalls' con todas las bolas del rango de Bingo (1 a 75).
        for (int i = 1; i <= 75; i++) {
            availableBalls.add(i);
        }
        Collections.shuffle(availableBalls); // Mezcla las bolas para que el orden sea aleatorio.

        this.calledBalls = new ArrayList<>(); // Inicializa la lista de bolas cantadas vacía.

        // Obtiene la instancia única de BallCaller (Singleton).
        // Si BallCaller ya tiene su propia lista de bolas y lógica de extracción,
        // se podría sincronizar aquí o dejar que BallCaller sea el único maestro de las bolas.
        // En este diseño, 'Game' mantiene 'availableBalls' como su propia fuente de verdad
        // para la lógica de juego y 'BallCaller' notifica lo que 'Game' le indica.
        this.ballCaller = BallCaller.getInstance(1, 75); // Se inicializa con el rango de 1 a 75.
    }

    /**
     * Añade un jugador al juego y lo registra como observador.
     *
     * @param player El objeto {@link Player} a añadir.
     */
    public void addPlayer(Player player) {
        players.add(player); // Añade el jugador a la lista del juego.
        attach(player);      // Registra al jugador como observador para recibir notificaciones de bolas cantadas.
    }

    /**
     * Obtiene la lista de todos los jugadores que participan en el juego.
     *
     * @return Una lista de objetos {@link Player}.
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * Obtiene la estrategia de victoria actualmente seleccionada para el juego.
     *
     * @return La {@link WinStrategy} activa.
     */
    public WinStrategy getWinStrategy() {
        return winStrategy;
    }

    /**
     * Obtiene la lista de todas las bolas que ya han sido cantadas en el juego actual.
     *
     * @return Una lista de números enteros que representan las bolas cantadas.
     */
    public List<Integer> getCalledBalls() {
        return Collections.unmodifiableList(calledBalls); // Devuelve una copia inmutable.
    }

    /**
     * Juega una ronda del juego en el contexto de una GUI.
     * Canta una nueva bola, la añade a las bolas cantadas y notifica a los observadores.
     *
     * @return La bola cantada en esta ronda, o -1 si no quedan más bolas para cantar.
     */
    public int playRoundGUI() {
        if (availableBalls.isEmpty()) { // Comprueba si aún quedan bolas disponibles.
            return -1; // Retorna -1 si no hay más bolas.
        }

        // Obtiene la siguiente bola de la lista de disponibles y la elimina.
        int calledBall = availableBalls.remove(0);

        calledBalls.add(calledBall);     // Añade la bola a la lista de bolas ya cantadas.
        notifyObservers(calledBall);     // Notifica a todos los observadores (jugadores) la nueva bola cantada.

        return calledBall; // Retorna la bola que acaba de ser cantada.
    }

    /**
     * Método de inicio de juego simplificado para la GUI.
     * La lógica del bucle de rondas y la interfaz de usuario se gestionan en la GUI.
     *
     * @return Un mensaje de confirmación de inicio.
     */
    public String startGameGUI() {
        // En una aplicación GUI, este método podría simplemente preparar el estado inicial
        // y la GUI se encargaría de la interacción paso a paso (e.g., clic para la siguiente bola).
        return "Juego listo para iniciar rondas.";
    }

    /**
     * Elige aleatoriamente una de las estrategias de victoria disponibles
     * y la establece como la estrategia actual para el juego.
     * Esto demuestra la flexibilidad del patrón Strategy.
     */
    public void randomlyChooseWinStrategy() {
        // Lista de todas las implementaciones de WinStrategy.
        List<WinStrategy> strategies = Arrays.asList(
                new HorizontalWinStrategy(),
                new VerticalWinStrategy(),
                new DiagonalWinStrategy(),
                new LShapeWinStrategy(),
                new XShapeWinStrategy()
        );
        Random random = new Random();
        // Selecciona una estrategia aleatoria de la lista.
        winStrategy = strategies.get(random.nextInt(strategies.size()));
    }

    /**
     * Retorna el número de bolas que aún no han sido cantadas.
     *
     * @return La cantidad de bolas que quedan en el bombo.
     */
    public int getBallsRemaining() {
        return availableBalls.size();
    }

    /**
     * Retorna verdadero si aún quedan bolas por cantar, falso en caso contrario.
     *
     * @return {@code true} si hay bolas restantes, {@code false} si el bombo está vacío.
     */
    public boolean hasMoreBalls() {
        return !availableBalls.isEmpty();
    }
}