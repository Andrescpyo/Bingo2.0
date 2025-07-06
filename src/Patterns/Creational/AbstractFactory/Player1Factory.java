package Patterns.Creational.AbstractFactory; 

import Players.Player; 
import Players.Player1; 

/**
 * Clase Player1Factory
 *
 * Implementación concreta de la interfaz {@link PlayerFactory}.
 * Esta clase es una fábrica específica para crear objetos de tipo {@link Player1}.
 * Es parte del patrón de diseño Factory Method, que permite a las clases crear objetos
 * sin especificar la clase exacta de los objetos que se crearán, delegando la instanciación
 * a las subclases o implementaciones de la fábrica.
 *
 * Rol en el patrón Factory Method: Concrete Creator (Creador Concreto)
 * - Implementa el método de fábrica {@code createPlayer()} para producir un {@link Player1 producto concreto}.
 * - Es responsable de la instanciación específica de {@link Player1}.
 */
public class Player1Factory implements PlayerFactory {

    /**
     * Crea y devuelve una nueva instancia de {@link Player1}.
     * Este método es el "factory method" que encapsula la lógica de creación
     * de un tipo específico de jugador.
     *
     * @param name El nombre que se asignará al nuevo jugador.
     * @return Una nueva instancia de {@link Player1}.
     */
    @Override
    public Player createPlayer(String name) {
        // Instancia y devuelve un Player1 concreto.
        return new Player1(name);
    }
}