package UI;

import Core.Game;
import Patterns.Creational.*;
import Patterns.Creational.Builder.DefaultGameBuilder;
import Patterns.Creational.Builder.GameBuilder;
import Patterns.Creational.Builder.GameDirector;
import Patterns.Creational.FactoryMethod.Player1Factory;
import Patterns.Creational.FactoryMethod.Player2Factory;
import Patterns.Creational.FactoryMethod.Player3Factory;
import Patterns.Creational.FactoryMethod.Player4Factory;
import Patterns.Creational.FactoryMethod.PlayerFactory;
import Patterns.Structural.Facade.GameFacade;
import Players.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class PlayerRegistrationGUI {

    private int totalPlayers;
    private int currentPlayerIndex = 0;
    private List<Player> players = new ArrayList<>();

    private PlayerFactory[] factories = {
        new Player1Factory(), new Player2Factory(),
        new Player3Factory(), new Player4Factory()
    };

    public PlayerRegistrationGUI() {
        preguntarCantidadJugadores();
    }

    private void preguntarCantidadJugadores() {
        JTextField input = new JTextField();
        int option = JOptionPane.showConfirmDialog(null, input,
                "¿Cuántos jugadores jugarán? (2 a 4)", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                int cantidad = Integer.parseInt(input.getText().trim());
                if (cantidad < 2 || cantidad > 4) {
                    JOptionPane.showMessageDialog(null, "Debe haber entre 2 y 4 jugadores.");
                    preguntarCantidadJugadores();
                    return;
                }
                this.totalPlayers = cantidad;
                preguntarNombreJugador();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Número inválido.");
                preguntarCantidadJugadores();
            }
        }
    }

    private void preguntarNombreJugador() {
        JTextField input = new JTextField();
        int option = JOptionPane.showConfirmDialog(null, input,
                "Nombre del jugador " + (currentPlayerIndex + 1), JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String nombre = input.getText().trim();
            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(null, "El nombre no puede estar vacío.");
                preguntarNombreJugador();
                return;
            }
            preguntarCantidadCartones(nombre);
        }
    }

    private void preguntarCantidadCartones(String nombre) {
        JTextField input = new JTextField();
        int option = JOptionPane.showConfirmDialog(null, input,
                "¿Cuántos cartones tendrá " + nombre + "? (1 a 3)", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                int cantidad = Integer.parseInt(input.getText().trim());
                if (cantidad < 1 || cantidad > 3) {
                    JOptionPane.showMessageDialog(null, "Debe tener entre 1 y 3 cartones.");
                    preguntarCantidadCartones(nombre);
                    return;
                }

                PlayerFactory factory = factories[currentPlayerIndex];
                Player player = factory.createPlayer(nombre);
                player.setTempCardCount(cantidad);

                players.add(player);
                currentPlayerIndex++;

                if (currentPlayerIndex < totalPlayers) {
                    preguntarNombreJugador();
                } else {
                    iniciarJuego();
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Número inválido.");
                preguntarCantidadCartones(nombre);
            }
        }
    }

    private void iniciarJuego() {
        GameBuilder builder = new DefaultGameBuilder();
        GameDirector director = new GameDirector(builder);
        // The Game object created here does not yet have players or cards.
        // It's a base Game object that the Facade will populate.
        Game game = director.constructGame(new ArrayList<>(), 0); // Players and cards are added by facade.registerPlayers

        GameFacade facade = new GameFacade(game);
        facade.registerPlayers(players); // This assigns the correct players and their desired card counts to the 'game' object within facade.
        facade.initializeGameSettings(); // <--- NEW LINE: Initialize the win strategy here!

        GameWindowGUI window = new GameWindowGUI(facade);
        window.setVisible(true);
    }
}