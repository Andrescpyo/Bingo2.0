package Patterns.Behavioral.Command;

/**
 * Interfaz Command
 *
 * Esta interfaz define el contrato para todos los comandos concretos.
 * Es parte del patrón de diseño Command, donde se encapsulan todas
 * la información necesaria para ejecutar una acción o evento en un objeto.
 *
 * Rol en el patrón Command: Command (Interfaz de Comando)
 * Define un método para ejecutar la operación.
 */
public interface Command {
    /**
     * Ejecuta la acción encapsulada por el comando.
     * Los comandos concretos implementarán este método para realizar la operación deseada.
     */
    void execute();
}