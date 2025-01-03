package com.techelevator.service;

import com.techelevator.dao.JdbcPlayerMatchDao;
import com.techelevator.dao.JdbcTeamMatchDao;
import com.techelevator.model.PlayerMatch;
import com.techelevator.model.TeamMatch;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MatchService {

    private final JdbcPlayerMatchDao playerMatchDao;
    private final JdbcTeamMatchDao teamMatchDao;

    public MatchService(JdbcPlayerMatchDao playerMatchDao, JdbcTeamMatchDao teamMatchDao) {
        this.playerMatchDao = playerMatchDao;
        this.teamMatchDao = teamMatchDao;
    }

    // Player Match Services
    public List<PlayerMatch> getAllPlayerMatches() {
        return playerMatchDao.getPlayerMatches();
    }

    public PlayerMatch getPlayerMatchById(int id) {
        return playerMatchDao.getPlayerMatchById(id);
    }

    public List<PlayerMatch> getPlayerMatchesByLeagueId(int leagueId) {
        return playerMatchDao.getPlayerMatchesByLeagueId(leagueId);
    }

    public List<PlayerMatch> getPlayerMatchesByFirstPlayerId(int playerId) {
        return playerMatchDao.getPlayerMatchesByFirstPlayerId(playerId);
    }

    public List<PlayerMatch> getPlayerMatchesBySecondPlayerId(int playerId) {
        return playerMatchDao.getPlayerMatchesBySecondPlayerId(playerId);
    }

    public PlayerMatch createPlayerMatch(PlayerMatch newMatch) {
        return playerMatchDao.createPlayerMatch(newMatch);
    }

    public PlayerMatch updatePlayerMatch(PlayerMatch modifiedMatch) {
        return playerMatchDao.updatePlayerMatch(modifiedMatch);
    }

    public int deletePlayerMatchById(int id) {
        return playerMatchDao.deletePlayerMatchById(id);
    }

    // Team Match Services
    public List<TeamMatch> getAllTeamMatches() {
        return teamMatchDao.getTeamMatches();
    }

    public TeamMatch getTeamMatchById(int id) {
        return teamMatchDao.getTeamMatchById(id);
    }

    public List<TeamMatch> getTeamMatchesByFirstTeamId(int teamId) {
        return teamMatchDao.getTeamMatchesByFirstTeamId(teamId);
    }

    public List<TeamMatch> getTeamMatchesBySecondTeamId(int teamId) {
        return teamMatchDao.getTeamMatchedBySecondTeamId(teamId);
    }

    public List<TeamMatch> getTeamMatchesByLeagueId(int leagueId) {
        return teamMatchDao.getTeamMatchesByLeagueId(leagueId);
    }

    public TeamMatch createTeamMatch(TeamMatch newMatch) {
        return teamMatchDao.createTeamMatch(newMatch);
    }

    public TeamMatch updateTeamMatch(TeamMatch modifiedMatch) {
        return teamMatchDao.updateTeamMatch(modifiedMatch);
    }

    public int deleteTeamMatchById(int id) {
        return teamMatchDao.deleteTeamMatchById(id);
    }
}
