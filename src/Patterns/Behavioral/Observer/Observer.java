package Patterns.Behavioral.Observer;

/**
 * Interfaz Observer
 *
 * Esta interfaz define el contrato para todos los objetos "Observadores"
 * que desean ser notificados de cambios en el estado de un "Sujeto".
 * Es una parte fundamental del patrón de diseño Observer.
 *
 * Rol en el patrón Observer: Observer (Interfaz de Observador)
 * Define la interfaz de actualización para los objetos que deben ser notificados
 * de los cambios en un sujeto.
 */
public interface Observer {
    /**
     * Método para actualizar el estado del objeto Observador.
     * Este método es invocado por el Sujeto cuando su estado cambia,
     * permitiendo al Observador reaccionar y actualizarse.
     *
     * @param ball El número de bola que ha sido llamado, que representa el dato relevante
     * del cambio de estado del Sujeto.
     */
    void update(int ball);
}