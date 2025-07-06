package Patterns.Creational.AbstractFactory; 

import Core.Card;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Clase CardFactory
 *
 * Implementa el patrón de diseño Factory Method.
 * Es responsable de la creación de objetos {@link Core.Card},
 * encapsulando la lógica de instanciación de los cartones.
 * Esto permite que el cliente (quien necesita un cartón) no necesite saber
 * los detalles de cómo se crea un cartón (por ejemplo, cómo se generan sus números).
 *
 * Rol en el patrón Factory Method: Concrete Creator (Creador Concreto)
 * - Implementa el método de fábrica (`createCard` y `createDefaultCard`)
 * para producir instancias del {@link Core.Card producto}.
 * - En este caso, no hay una interfaz `Creator` explícita, sino que `CardFactory`
 * es la clase que directamente proporciona los métodos de fábrica.
 */
public class CardFactory {
    /**
     * Instancia de Random para la generación de números aleatorios,
     * utilizada en la creación de cartones con números en rangos específicos.
     */
    private final Random random = new Random();

    /**
     * Crea un cartón de Bingo con dimensiones y rangos de números personalizados.
     * Este método puede ser útil para crear cartones que no sigan estrictamente
     * las reglas de columna B-I-N-G-O, o para propósitos de prueba.
     *
     * @param rows El número de filas para el cartón.
     * @param cols El número de columnas para el cartón.
     * @param minNum El número mínimo posible para el cartón.
     * @param maxNum El número máximo posible para el cartón.
     * @return Una nueva instancia de {@link Core.Card} con números aleatorios dentro del rango especificado.
     *
     * Rol en el patrón Factory Method: Factory Method (Método de Fábrica)
     * - Este método es el "factory method" que produce un objeto {@link Core.Card Card}.
     * - Encapsula la lógica de creación (generación aleatoria de números y asignación al cartón).
     */
    public Card createCard(int rows, int cols, int minNum, int maxNum) {
        // Genera una lista de todos los números posibles dentro del rango.
        List<Integer> allNumbers = new ArrayList<>();
        for (int i = minNum; i <= maxNum; i++) {
            allNumbers.add(i);
        }
        // Mezcla los números para asegurar la aleatoriedad.
        Collections.shuffle(allNumbers);
        // Selecciona la cantidad necesaria de números para el cartón.
        List<Integer> cardNumbers = allNumbers.subList(0, rows * cols);
        // Instancia un nuevo Card usando el constructor que acepta una lista de números.
        return new Card(cardNumbers, rows, cols);
    }

    /**
     * Crea y devuelve un cartón predeterminado de Bingo (5x5) siguiendo las reglas estándar del juego.
     * Esto incluye la casilla "FREE" en el centro y la distribución de números por rangos
     * específicos de las columnas (B, I, N, G, O).
     *
     * @return Un {@link Core.Card cartón} predeterminado de Bingo listo para jugar.
     *
     * Rol en el patrón Factory Method: Factory Method (Método de Fábrica)
     * - Este es el método de fábrica principal para crear el tipo de cartón más común.
     * - Delega la lógica específica de llenado de números al constructor de {@link Core.Card},
     * lo que simplifica la fábrica al no tener que replicar esa lógica.
     */
    public Card createDefaultCard() {
        // La creación del Card (5x5) delega la lógica de llenado con reglas de Bingo
        // directamente al constructor de la clase Card.
        return new Card(5, 5);
    }
}