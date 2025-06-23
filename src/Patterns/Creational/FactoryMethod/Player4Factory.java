package Patterns.Creational.FactoryMethod;

import Players.Player;
import Players.Player4;

public class Player4Factory implements PlayerFactory {
    @Override
    public Player createPlayer(String name) {
        return new Player4(name);
    }
}
