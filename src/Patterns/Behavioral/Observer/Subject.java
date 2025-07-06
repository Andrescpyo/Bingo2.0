package Patterns.Behavioral.Observer;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase Abstracta Subject
 *
 * Esta clase abstracta implementa la parte del 'Sujeto' (Subject) en el patrón de diseño Observer.
 * Un Sujeto es un objeto que mantiene una lista de sus dependientes, llamados "observadores",
 * y los notifica automáticamente de cualquier cambio de estado, usualmente llamando a uno de sus métodos.
 *
 * Rol en el patrón Observer: Subject (Sujeto / Publicador)
 * - Mantiene una lista de objetos Observer.
 * - Proporciona una interfaz para adjuntar y desvincular objetos Observer.
 * - Notifica a los observadores sobre cambios de estado.
 */
public abstract class Subject {

    /**
     * Lista privada que almacena todos los objetos Observer actualmente registrados
     * para recibir notificaciones de este Sujeto.
     * Esta lista es crucial para la gestión de las dependencias.
     */
    private List<Observer> observers = new ArrayList<>();

    /**
     * Registra un nuevo observador en la lista de observadores del Sujeto.
     * Si el observador ya está registrado, no se añade de nuevo para evitar duplicados.
     *
     * Rol en el patrón Observer: Método de registro de Observer.
     *
     * @param observer El objeto Observer a registrar.
     */
    public void attach(Observer observer) {
        if (!observers.contains(observer)) { // Evita añadir el mismo observador múltiples veces
            observers.add(observer);
        }
    }

    /**
     * Elimina un observador de la lista de observadores del Sujeto.
     * Después de llamar a este método, el observador ya no recibirá notificaciones de este Sujeto.
     *
     * Rol en el patrón Observer: Método de desvinculación de Observer.
     *
     * @param observer El objeto Observer a eliminar.
     */
    public void detach(Observer observer) {
        observers.remove(observer);
    }

    /**
     * Notifica a todos los observadores registrados sobre un cambio de estado en el Sujeto.
     * Este método itera sobre la lista de observadores y llama a su método `update()`,
     * pasándoles el estado relevante (en este caso, el número de bola llamado).
     *
     * Rol en el patrón Observer: Método de notificación.
     *
     * @param ball El número de bola que ha sido llamado, que representa el cambio de estado.
     */
    public void notifyObservers(int ball) {
        // Itera sobre una copia para evitar ConcurrentModificationException si los observadores
        // se modifican a sí mismos durante la notificación (aunque en este caso no lo hacen).
        for (Observer observer : new ArrayList<>(observers)) {
            observer.update(ball);
        }
    }
}