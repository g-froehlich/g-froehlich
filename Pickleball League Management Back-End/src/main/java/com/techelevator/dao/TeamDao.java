package com.techelevator.dao;

import com.techelevator.model.Team;

import java.util.List;

public interface TeamDao {

    List<Team> getTeams();

    Team getTeamById(int id);

    Team getTeamByName(String name);

    Team createTeam(Team team);

    Team updateTeam(Team team);

    int deleteTeamById(int id);
}
