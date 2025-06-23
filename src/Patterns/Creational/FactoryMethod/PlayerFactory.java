package Patterns.Creational.FactoryMethod;

import Players.Player;

public interface PlayerFactory {
    Player createPlayer(String name);
}
