package com.techelevator.dao;

import com.techelevator.model.PlayerMatch;

import java.util.List;

public interface PlayerMatchDao {

    List<PlayerMatch> getPlayerMatches();

    PlayerMatch getPlayerMatchById(int id);

    List<PlayerMatch> getPlayerMatchesByLeagueId(int id);

    List<PlayerMatch> getPlayerMatchesByFirstPlayerId(int id);

    List<PlayerMatch> getPlayerMatchesBySecondPlayerId(int id);

    PlayerMatch createPlayerMatch(PlayerMatch playerMatch);

    PlayerMatch updatePlayerMatch(PlayerMatch playerMatch);

    int deletePlayerMatchById(int id);
}
