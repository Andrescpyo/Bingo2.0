package Players;

/**
 * Clase Player3
 *
 * Representa otra implementación concreta de un jugador en el juego de Bingo,
 * siguiendo el mismo patrón que {@link Player1} y {@link Player2}.
 * Extiende la clase abstracta {@link Player}, heredando todas sus funcionalidades básicas.
 *
 * La existencia de múltiples clases de jugador (Player1, Player2, Player3)
 * es un buen ejemplo de cómo el patrón Factory Method (utilizado por
 * {@link PlayerFactory} y sus implementaciones
 * concretas como {@link Player1Factory})
 * puede ser empleado para crear diferentes tipos de objetos de forma flexible,
 * incluso si en la implementación actual no presentan comportamientos distintivos.
 * Esto permite una fácil extensibilidad futura para añadir características únicas
 * a cada tipo de jugador sin modificar el código cliente que los crea.
 */
public class Player3 extends Player {

    /**
     * Constructor para Player3.
     * Invoca al constructor de la clase padre ({@link Player}) para inicializar el nombre del jugador.
     *
     * @param name El nombre de este jugador.
     */
    public Player3(String name) {
        super(name); // Llama al constructor de la clase base Player
    }

    // Al igual que Player1 y Player2, en esta versión Player3 no tiene
    // implementaciones específicas de métodos o propiedades adicionales.
    // Su valor reside en su rol como un subtipo concreto de Player,
    // que puede ser instanciado a través de una fábrica.
    // En el futuro, se podrían añadir aquí comportamientos o atributos
    // que diferencien a "Player3" de otros tipos de jugadores.
}