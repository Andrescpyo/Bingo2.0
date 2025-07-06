package Players;

/**
 * Clase Player2
 *
 * Representa otra implementación concreta de un jugador en el juego de Bingo.
 * Extiende la clase abstracta {@link Player}, heredando todas sus funcionalidades
 * básicas. Al igual que {@link Player1}, esta clase sirve para diferenciar
 * posibles tipos de jugadores en el sistema.
 *
 * Esta distinción entre {@link Player1} y {@link Player2} es útil en el contexto
 * del patrón Factory Method (ej. {@link Player1Factory},
 * {@link Player2Factory}) donde se crean instancias
 * específicas de jugadores. Aunque actualmente no tienen comportamientos distintos,
 * su estructura permite futuras extensiones para introducir diferencias de lógica
 * o características entre ellos.
 *
 * No participa directamente en un patrón de diseño GoF como un rol primario más allá
 * de ser un Concrete Product (si se considera {@link Player} como un Abstract Product)
 * para el patrón Factory Method, y un Concrete Observer.
 */
public class Player2 extends Player {

    /**
     * Constructor para Player2.
     * Invoca al constructor de la clase padre ({@link Player}) para inicializar el nombre del jugador.
     *
     * @param name El nombre de este jugador.
     */
    public Player2(String name) {
        super(name); // Llama al constructor de la clase base Player
    }

    // Similar a Player1, Player2 no añade ninguna funcionalidad o comportamiento específico
    // en esta versión. Su propósito principal es demostrar la flexibilidad del sistema
    // para manejar diferentes subtipos de jugadores, lo que es relevante para
    // patrones como Factory Method.
    // Futuras adiciones podrían incluir:
    // - Comportamientos diferenciados para IA si el juego tuviera IA.
    // - Características estéticas o de personalización únicas para este tipo de jugador.
}