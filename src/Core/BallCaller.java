package Core;

import java.util.ArrayList;
import java.util.Collections; 
import java.util.List;
import java.util.Random; 

import Patterns.Behavioral.Observer.Observer; 
import Patterns.Behavioral.Observer.Subject;  

/**
 * Clase BallCaller
 *
 * Representa el componente central que "canta" las bolas en un juego de Bingo.
 * Implementa el patrón de diseño Singleton para asegurar que solo exista una instancia
 * de BallCaller en todo el juego, lo que garantiza que las bolas no se repitan
 * y el estado sea consistente.
 *
 * También actúa como el "Sujeto" (Subject) en el patrón de diseño Observer,
 * notificando a los observadores (por ejemplo, los jugadores o la interfaz de usuario)
 * cada vez que se canta una nueva bola.
 *
 * Roles en los patrones de diseño:
 * - Singleton: Asegura una única instancia de BallCaller.
 * - Subject (Observer): Mantiene una lista de observadores y los notifica de cambios (nuevas bolas cantadas).
 */
public class BallCaller extends Subject {
    private static BallCaller instance;          // Instancia única de la clase (para Singleton).
    private List<Integer> balls;                 // Lista de números disponibles para ser llamados.
    private List<Integer> calledBalls;           // Lista de números que ya han sido llamados.
    private final Random random = new Random();  // Instancia de Random para generar números aleatorios.
    private int minNum;                          // Número mínimo del rango de números a llamar.
    private int maxNum;                          // Número máximo del rango de números a llamar.

    /**
     * Constructor privado de la clase BallCaller.
     * Este constructor es privado para aplicar el patrón Singleton,
     * forzando el uso de `getInstance()` para obtener la única instancia.
     * Inicializa la lista de números a llamar y la lista de números llamados.
     *
     * @param minNum Número mínimo del rango de números a llamar (e.g., 1 para Bingo de 75 bolas).
     * @param maxNum Número máximo del rango de números a llamar (e.g., 75 para Bingo de 75 bolas).
     */
    private BallCaller(int minNum, int maxNum) {
        this.minNum = minNum;
        this.maxNum = maxNum;
        reset(); // Inicializa o reinicia las listas de bolas.
    }

    /**
     * Método estático para obtener la instancia única de la clase BallCaller (patrón Singleton).
     * Si la instancia no existe, la crea; de lo contrario, devuelve la existente.
     * Utiliza un doble chequeo de bloqueo (Double-Checked Locking) para garantizar la
     * seguridad de los hilos al crear la instancia.
     *
     * @param minNum Número mínimo para el rango de bolas. Se usa solo en la primera creación.
     * @param maxNum Número máximo para el rango de bolas. Se usa solo en la primera creación.
     * @return Instancia única de la clase BallCaller.
     */
    public static BallCaller getInstance(int minNum, int maxNum) {
        if (instance == null) { // Primer chequeo: si la instancia no existe, entra al bloque sincronizado.
            synchronized (BallCaller.class) { // Bloquea el acceso a la clase para evitar múltiples creaciones.
                if (instance == null) { // Segundo chequeo: si la instancia sigue siendo null (otro hilo no la creó mientras esperábamos).
                    instance = new BallCaller(minNum, maxNum); // Crea la nueva instancia.
                }
            }
        }
        return instance; // Devuelve la instancia existente o recién creada.
    }

    /**
     * Restablece el estado del BallCaller para un nuevo juego.
     * Esto incluye rellenar la lista de bolas disponibles ({@code balls}),
     * mezclarlas aleatoriamente y limpiar la lista de bolas ya llamadas ({@code calledBalls}).
     */
    public void reset() {
        this.balls = new ArrayList<>();
        // Rellena la lista de bolas con números del minNum al maxNum.
        for (int i = minNum; i <= maxNum; i++) {
            this.balls.add(i);
        }
        Collections.shuffle(this.balls); // Mezcla aleatoriamente las bolas.
        this.calledBalls = new ArrayList<>(); // Vacía la lista de bolas ya llamadas.
    }

    /**
     * Llama a una nueva bola aleatoria del conjunto de bolas disponibles.
     * La bola llamada se elimina de la lista de disponibles y se añade a la lista de llamadas.
     * Notifica a todos los observadores registrados con el número de la bola cantada.
     *
     * @return El número de la bola que ha sido cantada, o {@code null} si no quedan bolas disponibles.
     */
    public Integer callBall() {
        if (!balls.isEmpty()) {
            Integer ball = balls.remove(0); // Elimina y obtiene la primera bola de la lista mezclada.
            calledBalls.add(ball);           // Añade la bola a la lista de llamadas.
            System.out.println("¡Bola cantada: " + ball + "!"); // Imprime en consola (para depuración/feedback).
            notifyObservers(ball);           // Notifica a todos los observadores sobre la nueva bola.
            return ball;
        }
        return null; // No quedan más bolas para llamar.
    }

    /**
     * Obtiene la lista de todos los números que ya han sido cantados en el juego actual.
     *
     * @return Una lista inmutable de números enteros que representan las bolas cantadas hasta el momento.
     */
    public List<Integer> getCalledBalls() {
        // Devuelve una copia defensiva para evitar modificaciones externas directas a la lista interna.
        return Collections.unmodifiableList(calledBalls);
    }
}