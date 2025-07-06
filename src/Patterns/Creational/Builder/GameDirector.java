package Patterns.Creational.Builder;

import Core.Game; 
import Players.Player; 

import java.util.List;

/**
 * Clase GameDirector
 *
 * Esta clase es el Director en el patrón de diseño Builder.
 * Se encarga de construir un objeto complejo {@link Core.Game} utilizando
 * una instancia de un {@link GameBuilder Constructor} específico.
 * El Director sabe en qué orden debe invocar los pasos de construcción
 * del Builder para obtener el producto final, desacoplando el proceso
 * de construcción de la representación del producto.
 *
 * Rol en el patrón Builder: Director
 * - Construye un objeto usando la interfaz {@link GameBuilder}.
 * - No conoce los detalles concretos del producto ni los pasos de construcción específicos.
 * - Su objetivo es aislar la lógica de construcción del cliente.
 */
public class GameDirector {
    /**
     * Una referencia a la interfaz {@link GameBuilder}.
     * El Director interactúa con el Builder a través de esta interfaz abstracta,
     * lo que le permite trabajar con cualquier {@link Patterns.Creational.Builder.ConcreteBuilder Constructor Concreto}.
     */
    private GameBuilder builder;

    /**
     * Constructor para GameDirector.
     * Inyecta la implementación específica del {@link GameBuilder} que utilizará.
     *
     * @param builder La instancia de {@link GameBuilder} que el Director usará para construir el juego.
     */
    public GameDirector(GameBuilder builder) {
        this.builder = builder;
    }

    /**
     * Construye un objeto {@link Core.Game} completo siguiendo una secuencia predefinida de pasos.
     * El Director invoca los métodos del Builder en el orden correcto para ensamblar el juego.
     * El cliente solo necesita llamar a este método para obtener un juego configurado.
     *
     * @param players La lista de jugadores que se añadirán al juego.
     * @param cardsPerPlayer El número de cartones que cada jugador recibirá.
     * @return El objeto {@link Core.Game} completamente configurado y construido.
     */
    public Game constructGame(List<Player> players, int cardsPerPlayer) {
        // Establece los jugadores en el Builder.
        builder.setPlayers(players);
        // Establece el número de cartones por jugador en el Builder.
        builder.setCardsPerPlayer(cardsPerPlayer);
        // Invoca el método para construir el juego, que orquesta la creación de las partes internas.
        builder.buildGame();
        // Obtiene y devuelve el producto final (el juego construido) del Builder.
        return builder.getGame();
    }
}