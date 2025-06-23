package Patterns.Behavioral.Observer;

import java.util.ArrayList;
import java.util.List;

public abstract class Subject {

    private List<Observer> observers = new ArrayList<>(); //Lista de observadores

    /**
     * Método para registrar un observador.
     * @param observer Observador a registrar.
     */
    public void attach(Observer observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    /**
     * Método para eliminar un observador.
     * @param observer Observador a eliminar.
     */
    public void detach(Observer observer) {
        observers.remove(observer);
    }

    /**
     * Método para notificar a los observadores.
     * @param ball Número llamado.
     */
    public void notifyObservers(int ball) {
        for (Observer observer : observers) {
            observer.update(ball);
        }
    }
}
