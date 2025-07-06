package Patterns.Creational.Builder;

import Core.Game; 
import Players.Player; 

import java.util.List;

/**
 * Interfaz GameBuilder
 *
 * Esta interfaz define la API para la construcción de objetos {@link Core.Game}.
 * Es el componente clave del patrón de diseño Builder.
 * Declara los pasos que se deben seguir para construir las diferentes partes
 * de un objeto complejo {@link Core.Game}.
 *
 * Rol en el patrón Builder: Builder (Interfaz de Constructor)
 * - Declara los métodos abstractos para crear las partes del Producto (el juego).
 * - No especifica cómo se crean las partes, solo qué partes se pueden crear.
 * - Es implementada por {@link Patterns.Creational.Builder.DefaultGameBuilder Constructores Concretos}.
 */
public interface GameBuilder {

    /**
     * Establece la lista de jugadores que participarán en el juego.
     * Este es un paso de construcción que configura una parte del juego.
     *
     * @param players Una lista de objetos {@link Player} que se añadirán al juego.
     */
    void setPlayers(List<Player> players);

    /**
     * Establece el número de cartones que se asignarán a cada jugador.
     * Este es otro paso de construcción que configura el juego.
     *
     * @param cards La cantidad de cartones por jugador.
     */
    void setCardsPerPlayer(int cards);

    /**
     * Construye las diferentes partes del objeto {@link Core.Game} y las ensambla.
     * Este método orquesta la creación del producto final.
     * Las implementaciones concretas de este método contendrán la lógica
     * para inicializar y configurar el juego.
     */
    void buildGame();

    /**
     * Recupera el objeto {@link Core.Game} completamente construido.
     * Este método proporciona el producto final al cliente después de que ha sido ensamblado.
     *
     * @return El objeto {@link Core.Game} que ha sido construido.
     */
    Game getGame();
}