package Patterns.Behavioral.Command;

import UI.PlayerRegistrationGUI; // Asegúrate de que PlayerRegistrationGUI esté en el paquete UI
import javax.swing.*;

/**
 * Clase StartGameCommand
 *
 * Implementación concreta de la interfaz Command.
 * Este comando encapsula la acción de iniciar el proceso de registro de jugadores,
 * lo que lleva al inicio de un nuevo juego de Bingo.
 *
 * Rol en el patrón Command: Concrete Command (Comando Concreto)
 * - Encapsula la solicitud de iniciar el juego.
 * - Almacena la referencia a la ventana actual (`currentFrame`), que actúa como el receptor
 * sobre el cual se opera (ocultándola). La creación de `PlayerRegistrationGUI`
 * es la acción principal del receptor implícito.
 */
public class StartGameCommand implements Command {

    /**
     * El JFrame actual que se va a ocultar antes de iniciar la ventana de registro de jugadores.
     * Este es el "receptor" sobre el cual el comando opera directamente.
     */
    private JFrame currentFrame;

    /**
     * Constructor para StartGameCommand.
     *
     * @param currentFrame El JFrame que está visible actualmente y que debe ocultarse
     * al iniciar el proceso de registro de jugadores.
     */
    public StartGameCommand(JFrame currentFrame) {
        this.currentFrame = currentFrame;
    }

    /**
     * Ejecuta el comando para iniciar el juego.
     * Esto implica ocultar la ventana actual y abrir la ventana de registro de jugadores.
     * El método encapsula la lógica de navegación entre pantallas, desacoplando al invocador
     * (por ejemplo, un botón "Jugar") de los detalles de esta transición.
     */
    @Override
    public void execute() {
        // Oculta la ventana actual. Esta es la primera parte de la acción del receptor.
        currentFrame.setVisible(false);

        // Crea y muestra la nueva ventana para el registro de jugadores.
        // Esta es la acción principal del "receptor" del comando (la lógica de la GUI).
        new PlayerRegistrationGUI();
    }
}