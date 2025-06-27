package Players;

// No es necesario importar WinStrategy aquí si checkBingoGUI está en la superclase Player
// import Patterns.Behavioral.Strategy.WinStrategy; // <--- Ya no es necesario aquí

public class Player1 extends Player {

    public Player1(String name) {
        super(name); // Llama al constructor de la clase base Player
    }

    // Si la lógica de checkBingoGUI() se movió a la superclase Player
    // y recibe la WinStrategy, entonces este método ya NO es necesario.
    // protected boolean checkBingoBasedOnStrategy() {
    //     // Esta implementación específica de la estrategia ya no es necesaria aquí.
    //     // La lógica se maneja en el método checkBingoGUI(WinStrategy) de la clase base Player.
    //     return false; // O cualquier valor por defecto si fuera requerido
    // }
}