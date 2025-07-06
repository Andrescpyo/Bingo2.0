package Players;

import Core.Card; 
import Patterns.Structural.Decorator.ICardNumberFormatter; 
import Patterns.Behavioral.Observer.Observer; 
import Patterns.Behavioral.Strategy.WinStrategy; 

import java.util.ArrayList;
import java.util.List;

/**
 * Clase abstracta Player
 *
 * Representa un jugador genérico en el juego de Bingo.
 * Esta clase sirve como el {@link Observer Sujeto Concreto} en el patrón Observer,
 * ya que observa las bolas cantadas (es notificado por el {@link Core.BallCaller Sujeto Concreto}).
 * También contiene la lógica para la gestión de sus cartones y la verificación de victoria.
 *
 * Rol en el patrón Observer: Concrete Observer (Observador Concreto)
 * - Implementa la interfaz {@link Observer}.
 * - Mantiene una referencia al {@link Core.BallCaller Sujeto} (implícito a través de la actualización).
 * - Implementa el método {@code update()} para reaccionar a los cambios de estado del sujeto (nuevas bolas cantadas).
 *
 * Rol en el patrón Strategy: Context (Contexto)
 * - Mantiene una referencia a un objeto {@link WinStrategy Strategy}.
 * - Configura la estrategia de verificación de victoria que se utilizará para sus cartones.
 *
 * Rol en el patrón Decorator: Client (Cliente)
 * - Utiliza la interfaz {@link Patterns.Structural.Decorator.ICardNumberFormatter Componente}
 * para formatear la visualización de los números en sus cartones, combinando decoradores.
 */
public abstract class Player implements Observer {
    /**
     * El nombre del jugador.
     */
    private String name;

    /**
     * Lista de cartones de Bingo que posee este jugador.
     */
    private List<Card> cards;

    /**
     * Campo temporal para almacenar la cantidad de cartones que el jugador desea tener.
     * Utilizado durante el proceso de registro antes de que se creen los cartones.
     */
    private int tempCardCount;

    /**
     * El formateador de números de cartón, que puede ser decorado.
     * Se utiliza para generar la representación visual de los números en la GUI.
     */
    protected ICardNumberFormatter cardNumberFormatter;

    /**
     * Constructor para la clase Player.
     * Inicializa el nombre del jugador y su lista de cartones.
     *
     * @param name El nombre del jugador.
     */
    public Player(String name) {
        this.name = name;
        this.cards = new ArrayList<>();
    }

    /**
     * Obtiene el nombre del jugador.
     *
     * @return El nombre del jugador.
     */
    public String getName() {
        return name;
    }

    /**
     * Obtiene la lista de cartones que posee el jugador.
     *
     * @return Una lista de objetos {@link Core.Card}.
     */
    public List<Card> getCards() {
        return cards;
    }

    /**
     * Añade un cartón a la colección del jugador.
     *
     * @param card El objeto {@link Core.Card} a añadir.
     */
    public void addCard(Card card) {
        this.cards.add(card);
    }

    /**
     * Establece la cantidad temporal de cartones que el jugador desea.
     * Este valor se usa en la fase de configuración del juego.
     *
     * @param count El número temporal de cartones.
     */
    public void setTempCardCount(int count) {
        this.tempCardCount = count;
    }

    /**
     * Obtiene la cantidad temporal de cartones que el jugador desea.
     *
     * @return La cantidad temporal de cartones.
     */
    public int getTempCardCount() {
        return tempCardCount;
    }

    /**
     * Establece el formateador de números de cartón para este jugador.
     * Este método permite inyectar el componente {@link ICardNumberFormatter}
     * o una cadena de {@link NumberFormatterDecorator decoradores}.
     *
     * @param formatter La instancia de {@link ICardNumberFormatter} a utilizar.
     */
    public void setCardNumberFormatter(ICardNumberFormatter formatter) {
        this.cardNumberFormatter = formatter;
    }

    /**
     * Genera una cadena HTML que representa visualmente un cartón de Bingo específico del jugador.
     * Utiliza el {@link ICardNumberFormatter formateador} configurado
     * para estilizar cada número del cartón.
     *
     * @param card El cartón de {@link Core.Card} a mostrar.
     * @param lastCalledBall El último número de bola cantado (para resaltarlo). Puede ser null.
     * @return Una cadena HTML que representa el cartón, lista para ser mostrada en un componente como JEditorPane.
     */
    public String getCardDisplayString(Card card, Integer lastCalledBall) {
        if (cardNumberFormatter == null) {
            return "<html><body>Formato no configurado</body></html>";
        }

        StringBuilder html = new StringBuilder();
        html.append("<html><head><style>");
        // Estilos CSS integrados para la visualización del cartón
        html.append("body { margin: 0; padding: 0; font-family: 'Comic Sans MS', cursive, sans-serif; font-size: 8pt; line-height: 1.0; }");
        html.append("table { width:100%; height:100%; border-collapse: collapse; border: 3px solid #4CAF50; }");
        html.append("th, td { border: 1px solid #8BC34A; padding: 1px; text-align: center; vertical-align: middle; }");
        html.append("th { background-color:#FFEB3B; font-size:10pt; font-weight:bold; color: #D32F2F; text-shadow: 1px 1px 1px #000000; }");
        html.append("td { font-size:12pt; font-weight: bold; color: #3F51B5; background-color: #E8F5E9; }");
        html.append("td font { color: purple; font-weight: bold; }"); // Este estilo podría ser sobrescrito por los decoradores.
        html.append(".free-cell { background-color: #FFC107; color: #FFFFFF; font-size: 12pt; font-weight: bold; text-shadow: 1px 1px 1px #000000; }");
        html.append("</style></head><body>");

        html.append("<table>");

        // Encabezados B-I-N-G-O
        html.append("<tr>");
        for (String header : Card.getColumnHeaders()) {
            html.append("<th>").append(header).append("</th>");
        }
        html.append("</tr>");

        // Números del cartón
        for (int i = 0; i < card.getRows(); i++) {
            html.append("<tr>");
            for (int j = 0; j < card.getCols(); j++) {
                int number = card.getNumber(i, j);
                boolean isMarked = card.isMarked(i, j);
                boolean isLastCalled = (lastCalledBall != null && number == lastCalledBall);
                // NOTA: 'inWinPattern' se pasa como false aquí. Debería ser determinado por la lógica de victoria
                // y pasarse correctamente si se quiere resaltar las líneas ganadoras en tiempo real.
                // Para una implementación completa del patrón Decorator con 'inWinPattern',
                // el Player o Game necesitarían saber si un número específico es parte de una línea ganadora activa.
                boolean inWinPattern = false; // Placeholder, necesita lógica para ser true

                String displayValue;
                // Manejo especial para la casilla "FREE" (si es un cartón 5x5 y el número es 0)
                if (i == 2 && j == 2 && card.getRows() == 5 && card.getCols() == 5 && number == 0) {
                    displayValue = "<span class='free-cell'>FREE</span>";
                } else {
                    // Utiliza el formateador (con decoradores aplicados) para obtener la representación del número.
                    displayValue = cardNumberFormatter.format(number, i, j, isMarked, isLastCalled, inWinPattern);
                }

                html.append("<td>").append(displayValue).append("</td>");
            }
            html.append("</tr>");
        }
        html.append("</table>");
        html.append("</body></html>");
        return html.toString();
    }

    /**
     * Comprueba si alguno de los cartones del jugador ha logrado una victoria
     * utilizando la estrategia de victoria proporcionada.
     * Este método actúa como el "Contexto" para el patrón Strategy.
     *
     * @param currentWinStrategy La estrategia de victoria actual a utilizar (ej. Línea Horizontal, Diagonal).
     * @return true si el jugador ha logrado un Bingo en al menos uno de sus cartones, false en caso contrario.
     */
    public boolean checkBingoGUI(WinStrategy currentWinStrategy) {
        if (currentWinStrategy == null) {
            return false;
        }
        for (Card card : cards) {
            // Delega la verificación de la victoria a la estrategia de victoria actual.
            if (currentWinStrategy.checkWin(card)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Método de actualización para el patrón Observer.
     * Es invocado por el {@link Core.BallCaller Sujeto} cuando se canta una nueva bola.
     * El jugador reacciona marcando la bola en todos sus cartones.
     *
     * @param ball La nueva bola que ha sido cantada.
     */
    @Override
    public void update(int ball) {
        // Itera sobre todos los cartones del jugador y marca el número si lo encuentra.
        for (Card card : cards) {
            card.markNumber(ball);
        }
    }
}