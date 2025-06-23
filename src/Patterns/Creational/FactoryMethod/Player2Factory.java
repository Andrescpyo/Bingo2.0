package Patterns.Creational.FactoryMethod;

import Players.Player;
import Players.Player2;

public class Player2Factory implements PlayerFactory {
    @Override
    public Player createPlayer(String name) {
        return new Player2(name);
    }
}
