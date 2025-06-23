package Main;

import UI.MainMenuGUI;

public class Main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            new MainMenuGUI().setVisible(true);
        });
    }
}
