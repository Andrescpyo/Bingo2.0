package Core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import Patterns.Behavioral.Observer.Observer;
import Patterns.Behavioral.Observer.Subject;

public class BallCaller extends Subject {
    private static BallCaller instance;         //Instancia única de la clase
    private List<Integer> balls;                //Lista de números a llamar
    private List<Integer> calledBalls;          //Lista de números llamados
    private final Random random = new Random(); //Instancia de Random para generar números aleatorios
    private int minNum;                         //Número mínimo del rango de números a llamar
    private int maxNum;                         //Número máximo del rango de números a llamar

    /**
     * Constructor de la clase BallCaller.
     * Inicializa la lista de números a llamar y la lista de números llamados.
     * @param minNum Número mínimo del rango de números a llamar.
     * @param maxNum Número máximo del rango de números a llamar.        
     */
    private BallCaller(int minNum, int maxNum) { 
        this.minNum = minNum;
        this.maxNum = maxNum;
        reset(); //Reiniciar la lista de números a llamar y la lista de números llamados
    }

    /**
     * Método para obtener una instancia única de la clase BallCaller ( Para el patrón Singleton ).
     * @param minNum Número mínimo del rango de números a llamar.
     * @param maxNum Número máximo del rango de números a llamar.
     * @return Instancia única de la clase BallCaller.
     */
    public static BallCaller getInstance(int minNum, int maxNum) {
        if (instance == null) { //Si la instancia no existe
            synchronized (BallCaller.class) {  //Bloquear el acceso a la instancia
                if (instance == null) {  //Si la instancia no existe
                    instance = new BallCaller(minNum, maxNum); //Crear una nueva instancia
                }
            }
        }
        return instance; //Devolver la instancia
    }

    /**
     * Método para reiniciar la lista de números a llamar y la lista de números llamados para el siguiente juego.
     */
    public void reset() {
        this.balls = new ArrayList<>();
        for (int i = minNum; i <= maxNum; i++) {
            this.balls.add(i);
        }
        Collections.shuffle(this.balls);
        this.calledBalls = new ArrayList<>();
    }

    /**
     * Método para llamar un número aleatorio de la lista de números a llamar.
     * @return Número llamado.
     */
    public Integer callBall() {
        if (!balls.isEmpty()) {
            Integer ball = balls.remove(0); //Eliminar el primer número de la lista de números a llamar
            calledBalls.add(ball);                //Añadir el número llamado a la lista de números llamados
            System.out.println("¡Bola cantada: " + ball + "!");
            notifyObservers(ball); // Notificar a los observadores
            return ball;
        }
        return null;  
    }

    /**
     * Método para obtener la lista de números que ya han sido cantados.
     * @return Lista de números llamados en el último juego.
     */
    public List<Integer> getCalledBalls() {
        return calledBalls;
    }
}