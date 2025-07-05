package Patterns.Creational.AbstractFactory;

import Players.Player;
import Players.Player3;

public class Player3Factory implements PlayerFactory {
    @Override
    public Player createPlayer(String name) {
        return new Player3(name);
    }
}
