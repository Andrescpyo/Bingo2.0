package Patterns.Structural.Facade;

import Core.BallCaller; 
import Core.Game; 
import Patterns.Creational.AbstractFactory.CardFactory; 
import Core.Card; 
import Players.Player; 

import java.util.List;

/**
 * Clase GameFacade
 *
 * Esta clase implementa el patrón de diseño Facade.
 * Proporciona una interfaz unificada y simplificada a un conjunto complejo de subsistemas
 * relacionados con la lógica del juego de Bingo (Game, Player, CardFactory, BallCaller, etc.).
 * El objetivo es hacer que el subsistema sea más fácil de usar para los clientes,
 * ocultando la complejidad subyacente y las interacciones entre los objetos del subsistema.
 *
 * Rol en el patrón Facade: Facade
 * - Proporciona una interfaz de alto nivel que simplifica el uso del subsistema.
 * - Conoce qué clases del subsistema son responsables de una solicitud y delega la solicitud a los objetos apropiados.
 * - Desacopla al cliente de los componentes internos del subsistema.
 */
public class GameFacade {

    /**
     * Instancia del subsistema principal del juego.
     * La Facade interactúa con esta clase y otras clases para cumplir con las solicitudes del cliente.
     */
    private Game game;

    /**
     * Instancia de la fábrica de cartones, utilizada para crear los cartones de los jugadores.
     * La Facade integra la funcionalidad de otros patrones (Factory Method en este caso).
     */
    private CardFactory cardFactory;

    /**
     * Constructor predeterminado para GameFacade.
     * Inicializa una nueva instancia de {@link Core.Game} y {@link CardFactory}.
     */
    public GameFacade() {
        this.game = new Game(); // Instancia el componente Game del subsistema.
        this.cardFactory = new CardFactory(); // Instancia el CardFactory (Factory Method) del subsistema.
    }

    /**
     * Constructor para GameFacade que permite inyectar una instancia de {@link Core.Game} existente.
     * Esto puede ser útil para pruebas o para continuar un juego preexistente.
     *
     * @param game La instancia de {@link Core.Game} a usar.
     */
    public GameFacade(Game game) {
        this.game = game; // Utiliza la instancia de Game proporcionada.
        this.cardFactory = new CardFactory(); // Aunque el juego ya existe, la fábrica de cartones sigue siendo necesaria.
    }

    /**
     * Registra una lista de jugadores en el juego y les asigna la cantidad de cartones deseada.
     * Este método simplifica la compleja interacción de añadir jugadores y generar cartones para cada uno.
     *
     * @param players La lista de objetos {@link Player} a registrar.
     */
    public void registerPlayers(List<Player> players) {
        // La Facade orquesta la interacción entre Player y CardFactory (Factory Method).
        for (Player player : players) {
            int cardsPerPlayer = player.getTempCardCount(); // Obtiene el número de cartones deseado por el jugador.
            for (int i = 0; i < cardsPerPlayer; i++) {
                // Utiliza el Factory Method para crear un cartón por defecto.
                Card card = cardFactory.createDefaultCard();
                player.addCard(card); // Añade el cartón al jugador.
            }
            game.addPlayer(player); // Añade el jugador configurado al objeto Game.
        }
    }

    /**
     * Inicializa las configuraciones cruciales del juego, como la selección aleatoria de la estrategia de victoria.
     * Esto oculta los detalles de la configuración interna del juego.
     */
    public void initializeGameSettings() {
        // La Facade delega la lógica de negocio al subsistema Game.
        this.game.randomlyChooseWinStrategy(); // Método que utiliza el patrón Strategy internamente.
    }

    /**
     * Inicia la interfaz gráfica de usuario del juego.
     * Este método delega la responsabilidad al objeto {@link Core.Game}.
     *
     * @return Una cadena que representa el estado inicial del juego en la GUI (podría ser un mensaje o un ID de GUI).
     */
    public String startGameGUI() {
        return game.startGameGUI();
    }

    /**
     * Juega una ronda del juego, que típicamente implica llamar a una nueva bola,
     * marcarla en los cartones de los jugadores y verificar si hay un ganador.
     * Este método encapsula toda la complejidad de una ronda de juego.
     *
     * @return La bola cantada en esta ronda, o -1 si el juego ha terminado (no quedan más bolas).
     */
    public int playRound() {
        // La Facade orquesta la lógica de una ronda de juego, interactuando con BallCaller (implícito) y Game.
        return game.playRoundGUI();
    }

    /**
     * Obtiene el historial de bolas cantadas hasta el momento.
     * Delega la solicitud al subsistema {@link Core.Game}.
     *
     * @return Una lista de enteros que representa las bolas que ya han sido cantadas.
     */
    public List<Integer> getCalledBallsHistory() {
        return game.getCalledBalls();
    }

    /**
     * Obtiene la instancia del objeto {@link Core.Game} que está siendo gestionado por esta Facade.
     * Esto puede ser útil si un cliente necesita acceder directamente al objeto Game para operaciones avanzadas.
     *
     * @return La instancia de {@link Core.Game}.
     */
    public Game getGame() {
        return game;
    }

    /**
     * Obtiene la lista de jugadores registrados en el juego.
     * Delega la solicitud al subsistema {@link Core.Game}.
     *
     * @return Una lista de objetos {@link Player} que participan en el juego.
     */
    public List<Player> getPlayers() {
        return game.getPlayers();
    }

    /**
     * Obtiene el número de bolas que aún quedan por cantar en el juego.
     * Delega la solicitud al subsistema {@link Core.Game}.
     *
     * @return El número de bolas restantes en el bombo.
     */
    public int getBallsRemaining() {
        return game.getBallsRemaining(); // Asume que Game tiene este método
    }

    /**
     * Verifica si todavía quedan bolas en el bombo para ser cantadas.
     * Una versión simplificada para la GUI.
     *
     * @return true si quedan bolas, false si el bombo está vacío.
     */
    public boolean areBallsLeft() {
        return game.getBallsRemaining() > 0;
    }
}