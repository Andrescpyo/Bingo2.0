package Patterns.Creational.AbstractFactory; 

import Players.Player;

/**
 * Interfaz PlayerFactory
 *
 * Esta interfaz define el contrato para todas las fábricas que crean objetos de tipo {@link Player}.
 * Es un componente clave del patrón de diseño Factory Method.
 * Permite que las clases que necesitan un objeto {@link Player} soliciten su creación
 * a una fábrica, sin necesidad de conocer la clase concreta del jugador que se está instanciando.
 *
 * Rol en el patrón Factory Method: Creator (Interfaz de Creador)
 * - Declara el método de fábrica {@code createPlayer()}, que debe ser implementado
 * por los creadores concretos para producir un {@link Player producto}.
 * - Permite que el sistema sea más flexible al añadir nuevos tipos de jugadores
 * sin modificar el código cliente que usa la fábrica.
 */
public interface PlayerFactory {

    /**
     * Método de fábrica para crear una instancia de un jugador.
     * Las implementaciones concretas de esta interfaz (ej., {@link Player1Factory},
     * {@link Player2Factory}) decidirán qué tipo específico de {@link Player} instanciar.
     *
     * @param name El nombre que se asignará al nuevo jugador.
     * @return Una nueva instancia de un objeto que implementa {@link Player}.
     */
    Player createPlayer(String name);
}