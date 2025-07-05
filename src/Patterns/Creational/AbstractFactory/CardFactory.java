// Carpeta: Patterns/Creational/FactoryMethod
// Archivo: CardFactory.java
package Patterns.Creational.AbstractFactory;

import Core.Card;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class CardFactory {
    private final Random random = new Random();

    // Este método ya no necesitaría generar los números directamente,
    // ya que el constructor de Card se encargará de ello según las reglas de Bingo.
    // Sin embargo, lo mantendremos por si en el futuro quieres crear otros tipos de cartones.
    public Card createCard(int rows, int cols, int minNum, int maxNum) {
        // En un juego de Bingo estándar, las columnas tienen rangos específicos.
        // Si este método se usa para un cartón de Bingo real (5x5),
        // es mejor usar createDefaultCard()
        // Si se usa para otro tipo de "cartón" no-Bingo, la lógica actual es válida.
        List<Integer> allNumbers = new ArrayList<>();
        for (int i = minNum; i <= maxNum; i++) {
            allNumbers.add(i);
        }
        Collections.shuffle(allNumbers);
        List<Integer> cardNumbers = allNumbers.subList(0, rows * cols);
        return new Card(cardNumbers, rows, cols); // Usamos el constructor que acepta una lista
    }

    /**
     * Método para crear un cartón predeterminado de Bingo (5x5 con FREE y rangos BINGO).
     * @return Cartón predeterminado de Bingo.
     */
    public Card createDefaultCard() {
        return new Card(5, 5); // El constructor de Card (5,5) ya se encarga de llenarlo con reglas de Bingo
    }
}