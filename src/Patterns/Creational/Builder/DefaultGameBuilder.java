package Patterns.Creational.Builder;

import Core.Game;
import Patterns.Creational.AbstractFactory.CardFactory;
import Core.Card; 
import Players.Player;

import java.util.List;

/**
 * Clase DefaultGameBuilder
 *
 * Implementación concreta de la interfaz {@link GameBuilder}.
 * Este es un constructor concreto que define el proceso de construcción
 * de un objeto {@link Core.Game} paso a paso, utilizando una configuración predeterminada
 * para la adición de jugadores y la asignación de cartones.
 *
 * Rol en el patrón Builder: Concrete Builder (Constructor Concreto)
 * - Implementa los métodos de construcción definidos por la interfaz {@link GameBuilder}.
 * - Mantiene el estado del producto que está siendo construido (el objeto {@link Core.Game}).
 * - Define la secuencia de los pasos de construcción.
 * - Utiliza un {@link Patterns.Creational.AbstractFactory.CardFactory Factory Method} para la creación de cartones,
 * lo que demuestra la colaboración entre patrones.
 */
public class DefaultGameBuilder implements GameBuilder {
    /**
     * La instancia del juego que se está construyendo.
     * Este es el "producto" del patrón Builder.
     */
    private Game game;

    /**
     * La lista de jugadores que se añadirán al juego.
     * Estos son los "componentes" del producto Game.
     */
    private List<Player> players;

    /**
     * El número de cartones que se asignarán a cada jugador.
     */
    private int cardsPerPlayer;

    /**
     * Una instancia de {@link Patterns.Creational.AbstractFactory.CardFactory}
     * utilizada para crear los cartones para los jugadores.
     * Esto muestra la delegación de la creación de un tipo de objeto a un Factory Method.
     */
    private CardFactory factory = new CardFactory(); // Usando el Factory Method para crear cartones

    /**
     * Establece la lista de jugadores que participarán en el juego.
     * Este es uno de los pasos de construcción definidos por la interfaz {@link GameBuilder}.
     *
     * @param players La lista de objetos {@link Player}.
     */
    @Override
    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    /**
     * Establece el número de cartones que cada jugador recibirá.
     * Este es otro paso de construcción.
     *
     * @param cards El número de cartones por jugador.
     */
    @Override
    public void setCardsPerPlayer(int cards) {
        this.cardsPerPlayer = cards;
    }

    /**
     * Construye la instancia del juego, inicializándola y añadiendo jugadores
     * con la cantidad especificada de cartones.
     * Este método contiene la lógica principal para el ensamblaje del producto.
     *
     * Rol en el patrón Builder: Build Method (Método de Construcción)
     * - Orquesta los pasos necesarios para construir el objeto complejo {@link Core.Game}.
     * - Demuestra la interacción con {@link Patterns.Creational.AbstractFactory.CardFactory}
     * para obtener las {@link Core.Card cartas}.
     */
    @Override
    public void buildGame() {
        game = new Game(); // Inicializa el nuevo objeto Game
        // Itera sobre cada jugador para añadir cartones y luego añadir el jugador al juego.
        for (Player player : players) {
            for (int i = 0; i < cardsPerPlayer; i++) {
                // Utiliza la CardFactory para crear un cartón predeterminado y añadirlo al jugador.
                // Esta es la colaboración con el patrón Factory Method.
                player.addCard(factory.createDefaultCard());
            }
            game.addPlayer(player); // Añade el jugador configurado al juego
        }
    }

    /**
     * Devuelve la instancia del juego que ha sido construida.
     *
     * @return El objeto {@link Core.Game} completamente construido.
     *
     * Rol en el patrón Builder: Get Result Method (Método para Obtener el Resultado)
     * - Proporciona el producto final al cliente después de que ha sido construido.
     */
    @Override
    public Game getGame() {
        return game;
    }
}