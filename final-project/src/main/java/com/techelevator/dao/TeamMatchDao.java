package com.techelevator.dao;

import com.techelevator.model.TeamMatch;

import java.util.List;

public interface TeamMatchDao {

    List<TeamMatch> getTeamMatches();

    TeamMatch getTeamMatchById(int id);

    List<TeamMatch> getTeamMatchesByLeagueId(int id);

    List<TeamMatch> getTeamMatchesByFirstTeamId(int id);

    List<TeamMatch> getTeamMatchedBySecondTeamId(int id);

    TeamMatch createTeamMatch(TeamMatch teamMatch);

    TeamMatch updateTeamMatch(TeamMatch teamMatch);

    int deleteTeamMatchById(int id);
}
