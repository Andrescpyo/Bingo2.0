package Patterns.Behavioral.Command;

import UI.PlayerRegistrationGUI;
import javax.swing.*;

public class StartGameCommand implements Command {

    private JFrame currentFrame;

    public StartGameCommand(JFrame currentFrame) {
        this.currentFrame = currentFrame;
    }

    @Override
    public void execute() {
        currentFrame.setVisible(false);
        new PlayerRegistrationGUI();
    }
}
