package Patterns.Creational.AbstractFactory;

import Players.Player; 
import Players.Player3; 

/**
 * Clase Player3Factory
 *
 * Implementación concreta de la interfaz {@link PlayerFactory}.
 * Esta clase es una fábrica específica para crear objetos de tipo {@link Player3}.
 * Es parte del patrón de diseño Factory Method, que permite a las clases crear objetos
 * sin especificar la clase exacta de los objetos que se crearán, delegando la instanciación
 * a las subclases o implementaciones de la fábrica.
 *
 * Rol en el patrón Factory Method: Concrete Creator (Creador Concreto)
 * - Implementa el método de fábrica {@code createPlayer()} para producir un {@link Player3 producto concreto}.
 * - Es responsable de la instanciación específica de {@link Player3}.
 */
public class Player3Factory implements PlayerFactory {

    /**
     * Crea y devuelve una nueva instancia de {@link Player3}.
     * Este método es el "factory method" que encapsula la lógica de creación
     * de un tipo específico de jugador.
     *
     * @param name El nombre que se asignará al nuevo jugador.
     * @return Una nueva instancia de {@link Player3}.
     */
    @Override
    public Player createPlayer(String name) {
        // Instancia y devuelve un Player3 concreto.
        return new Player3(name);
    }
}