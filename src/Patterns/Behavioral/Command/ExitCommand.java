package Patterns.Behavioral.Command;

/**
 * Clase ExitCommand
 *
 * Implementación concreta de la interfaz Command.
 * Este comando encapsula la acción de salir de la aplicación.
 *
 * Rol en el patrón Command: Concrete Command (Comando Concreto)
 * Define un enlace entre una acción y un receptor (implícito en este caso, System).
 */
public class ExitCommand implements Command {

    /**
     * Ejecuta el comando de salida de la aplicación.
     * Al ser invocado, termina la ejecución del programa.
     * Este método no tiene un receptor explícito, ya que interactúa directamente con el sistema.
     */
    @Override
    public void execute() {
        // Cierra la aplicación. Este es el "receptor" de esta acción.
        System.exit(0);
    }
}