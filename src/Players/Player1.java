package Players;

/**
 * Clase Player1
 *
 * Representa una implementación concreta de un jugador en el juego de Bingo.
 * Extiende la clase abstracta {@link Player}, heredando todas sus funcionalidades
 * básicas como la gestión de cartones, la capacidad de ser un observador (para marcar bolas),
 * y la lógica para la visualización de sus cartones.
 *
 * Esta clase podría ser utilizada para representar un tipo de jugador estándar,
 * o podría extenderse en el futuro con comportamientos o propiedades específicas
 * que la diferencien de otros tipos de jugadores (ej., habilidades especiales, estadísticas).
 *
 * No participa directamente en un patrón de diseño GoF como un rol primario más allá
 * de ser un Concrete Product (si se considera {@link Player} como un Abstract Product)
 * para el patrón Factory Method, y un Concrete Observer.
 * Su existencia es necesaria para demostrar la capacidad de crear diferentes "tipos" de jugadores.
 */
public class Player1 extends Player {

    /**
     * Constructor para Player1.
     * Invoca al constructor de la clase padre ({@link Player}) para inicializar el nombre del jugador.
     *
     * @param name El nombre de este jugador.
     */
    public Player1(String name) {
        super(name); // Llama al constructor de la clase base Player
    }

    // Por ahora, Player1 no añade ninguna funcionalidad o comportamiento específico
    // más allá de lo que hereda de la clase Player.
    // Futuras extensiones podrían incluir:
    // - Lógica de toma de decisiones para jugadores controlados por IA.
    // - Métodos adicionales que definan acciones únicas de este tipo de jugador.
}