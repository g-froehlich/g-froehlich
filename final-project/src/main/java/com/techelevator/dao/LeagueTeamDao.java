package com.techelevator.dao;

import com.techelevator.model.LeagueTeam;

import java.util.List;

public interface LeagueTeamDao {

    List<LeagueTeam> getLeagueTeams();

    LeagueTeam getLeagueTeamById(int id);

    List<LeagueTeam> getLeagueTeamsByLeagueId(int leagueId);

    List<LeagueTeam> getLeaguesByTeamId(int teamId);

    LeagueTeam createLeagueTeam(LeagueTeam leagueTeam);

    LeagueTeam updateLeagueTeam(LeagueTeam leagueTeam);

    int deleteLeagueTeamById(int id);
}
