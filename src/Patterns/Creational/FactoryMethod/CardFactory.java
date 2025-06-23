package Patterns.Creational.FactoryMethod;

import Core.Card;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class CardFactory {
    private final Random random = new Random(); //Instancia de Random para generar números aleatorios
 
    /**
     * Método para crear un cartón con números aleatorios.
     * @param rows Número de filas del cartón.
     * @param cols Número de columnas del cartón.
     * @param minNum Número mínimo del rango de números a incluir en el cartón.
     * @param maxNum Número máximo del rango de números a incluir en el cartón.
     * @return Cartón con números aleatorios.
     */
    public Card createCard(int rows, int cols, int minNum, int maxNum) {
        List<Integer> allNumbers = new ArrayList<>(); //Lista de números aleatorios
        for (int i = minNum; i <= maxNum; i++) { //Recorrer el rango de números aleatorios
            allNumbers.add(i);  //Añadir todos los números a la lista
        }
        Collections.shuffle(allNumbers); //Aleatorizar la lista de números aleatorios
        List<Integer> cardNumbers = allNumbers.subList(0, rows * cols); //Extraer los números de la lista de números aleatorios
        return new Card(cardNumbers, rows, cols); //Crear el cartón con los números extraídos
    }
    
    /**
     * Método para crear un cartón predeterminado.
     * @return Cartón predeterminado.
     */
    public Card createDefaultCard() {
        return createCard(5, 5, 1, 75); //Crear un cartón con 5 filas y 5 columnas, con números de 1 a 75
    }
}