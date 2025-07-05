package Patterns.Creational.AbstractFactory;

import Players.Player;
import Players.Player1;

public class Player1Factory implements PlayerFactory {
    @Override
    public Player createPlayer(String name) {
        return new Player1(name);
    }
}
