package Patterns.Behavioral.Command;

import javax.swing.*;

public class ShowInstructionsCommand implements Command {

    private JFrame parentFrame;

    public ShowInstructionsCommand(JFrame parentFrame) {
        this.parentFrame = parentFrame;
    }

    @Override
    public void execute() {
        JOptionPane.showMessageDialog(parentFrame,
                """
                - El juego se basa en el clásico Bingo con 75 bolas.
                - Cada jugador tendrá uno o más cartones con números del 1 al 75.
                - Se selecciona una forma de victoria al azar: línea, diagonal, L, etc.
                - Se van cantando bolas aleatorias.
                - El primer jugador que cumpla la condición de victoria gana.
                """,
                "Instrucciones", JOptionPane.INFORMATION_MESSAGE);
    }
}
