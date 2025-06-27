package Players;

public class Player3 extends Player {

    public Player3(String name) {
        super(name);
    }

    // Si la lógica de checkBingoGUI() se movió a la superclase Player
    // y recibe la WinStrategy, entonces este método ya NO es necesario.
    // protected boolean checkBingoBasedOnStrategy() {
    //     return false;
    // }
}