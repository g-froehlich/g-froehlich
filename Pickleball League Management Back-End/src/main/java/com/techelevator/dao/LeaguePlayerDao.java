package com.techelevator.dao;

import com.techelevator.model.LeaguePlayer;

import java.util.List;

public interface LeaguePlayerDao {

    List<LeaguePlayer> getLeaguePlayers();

    LeaguePlayer getLeaguePlayerById(int id);

    List<LeaguePlayer> getLeaguePlayersByLeagueId(int leagueId);

    List<LeaguePlayer> getLeaguesByPlayerId(int playerId);

    LeaguePlayer createLeaguePlayer(LeaguePlayer leaguePlayer);

    LeaguePlayer updateLeaguePlayer(LeaguePlayer leaguePlayer);

    int deleteLeaguePlayerById(int id);
}
