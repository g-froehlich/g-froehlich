package com.techelevator.dao;

import com.techelevator.model.TeamPlayer;

import java.util.List;

public interface TeamPlayerDao {

    List<TeamPlayer> getTeamPlayers();

    TeamPlayer getTeamPlayerById(int id);

    List<TeamPlayer> getTeamPlayersByTeamId(int id);

    List<TeamPlayer> getTeamPlayersByPlayerId(int id);

    TeamPlayer createTeamPlayer(TeamPlayer teamPlayer);

    TeamPlayer updateTeamPlayer(TeamPlayer teamPlayer);

    int deleteTeamPlayerById(int id);
}
