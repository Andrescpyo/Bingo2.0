package Patterns.Creational.AbstractFactory;

import Players.Player;

public interface PlayerFactory {
    Player createPlayer(String name);
}
