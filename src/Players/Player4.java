package Players;

public class Player4 extends Player {

    public Player4(String name) {
        super(name);
    }

    // Si la lógica de checkBingoGUI() se movió a la superclase Player
    // y recibe la WinStrategy, entonces este método ya NO es necesario.
    // protected boolean checkBingoBasedOnStrategy() {
    //     return false;
    // }
}