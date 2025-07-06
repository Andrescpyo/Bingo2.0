package Players;

/**
 * Clase Player4
 *
 * Representa otra implementación concreta de un jugador en el juego de Bingo.
 * Al igual que {@link Player1}, {@link Player2} y {@link Player3}, extiende
 * la clase abstracta {@link Player} para heredar la funcionalidad básica de un jugador.
 *
 * La existencia de múltiples clases concretas de `Player` es fundamental para
 * ilustrar la flexibilidad del patrón Factory Method (utilizado por las fábricas
 * de jugadores en el paquete `Patterns.Creational.AbstractFactory`). Permite
 * que el sistema pueda crear y gestionar diferentes "tipos" de jugadores
 * de forma polimórfica, incluso si su implementación actual es idéntica.
 * Esta estructura facilita la introducción de comportamientos, reglas o
 * atributos específicos para cada tipo de jugador en el futuro, sin afectar
 * el código que los instancia.
 */
public class Player4 extends Player {

    /**
     * Constructor para Player4.
     * Llama al constructor de la clase padre ({@link Player}) para inicializar el nombre del jugador.
     *
     * @param name El nombre de este jugador.
     */
    public Player4(String name) {
        super(name); // Invoca al constructor de la clase base Player
    }

    // Por el momento, Player4 no introduce ninguna funcionalidad o propiedad
    // adicional que la distinga de Player1, Player2 o Player3.
    // Su principal propósito es servir como un tipo de jugador diferenciado
    // para los propósitos de demostración del patrón Factory Method.
    // Futuras adiciones podrían incluir:
    // - Lógicas de IA específicas para un tipo de oponente.
    // - Bonificaciones o penalizaciones aplicables solo a este tipo de jugador.
}