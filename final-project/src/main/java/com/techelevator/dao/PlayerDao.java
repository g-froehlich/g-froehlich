package com.techelevator.dao;

import com.techelevator.model.Player;

import java.util.List;

public interface PlayerDao {

    List<Player> getPlayers();

    Player getPlayerById(int id);

    Player getPlayerByName(String firstName, String lastName);

    Player createPlayer(Player player);

    Player updatePlayer(Player player);

    int deletePlayerById(int id);
}
