package com.techelevator.dao;

import com.techelevator.model.League;

import java.util.List;

public interface LeagueDao {

    List<League> getLeagues();

    List<League> getLeaguesByManagerId(int id);

    League getLeagueById(int id);

    League getLeagueByName(String name);

    League createLeague(League league);

    League updateLeague(League league);

    int deleteLeagueById(int id);
}
