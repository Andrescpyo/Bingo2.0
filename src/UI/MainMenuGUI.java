package UI;


import Patterns.Behavioral.Command.Command;
import Patterns.Behavioral.Command.ExitCommand;
import Patterns.Behavioral.Command.ShowInstructionsCommand;
import Patterns.Behavioral.Command.StartGameCommand;

import javax.swing.*;
import java.awt.*;

public class MainMenuGUI extends JFrame {

    public MainMenuGUI() {
        setTitle("Menú Principal - Bingo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        initUI();
    }

    private void initUI() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // Crear comandos
        Command startCommand = new StartGameCommand(this);
        Command instructionsCommand = new ShowInstructionsCommand(this);
        Command exitCommand = new ExitCommand();

        // Crear botones
        JButton startButton = new JButton("Comenzar juego");
        JButton instructionsButton = new JButton("Instrucciones");
        JButton exitButton = new JButton("Salir");

        // Asociar comandos a botones
        startButton.addActionListener(e -> startCommand.execute());
        instructionsButton.addActionListener(e -> instructionsCommand.execute());
        exitButton.addActionListener(e -> exitCommand.execute());

        panel.add(startButton);
        panel.add(instructionsButton);
        panel.add(exitButton);

        add(panel);
    }

}
