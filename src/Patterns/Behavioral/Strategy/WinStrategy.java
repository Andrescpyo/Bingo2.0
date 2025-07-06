package Patterns.Behavioral.Strategy;

import Core.Card;

/**
 * Interfaz WinStrategy
 *
 * Esta interfaz define el contrato para todas las estrategias de verificación de victoria
 * en el juego de Bingo. Es el componente clave del patrón de diseño Strategy.
 * Permite que el algoritmo de verificación de victoria sea seleccionado en tiempo de ejecución.
 *
 * Rol en el patrón Strategy: Strategy (Interfaz de Estrategia)
 * - Declara una interfaz común para todos los algoritmos soportados.
 * - El {@link Game Contexto} usa esta interfaz para llamar al algoritmo
 * definido por un {@link ConcreteStrategy Concrete Strategy}.
 */
public interface WinStrategy {

    /**
     * Comprueba si el {@link Card cartón} proporcionado cumple con las condiciones
     * de victoria definidas por esta estrategia específica.
     *
     * @param card El cartón de Bingo a verificar.
     * @return true si el cartón ha logrado la victoria según esta estrategia, false en caso contrario.
     */
    boolean checkWin(Card card);

    /**
     * Obtiene el nombre descriptivo de esta estrategia de victoria.
     * Esto es útil para mostrar al usuario qué tipo de victoria se ha conseguido (ej. "Línea horizontal").
     *
     * @return Una cadena que representa el nombre de la estrategia de victoria.
     */
    String getName();
}