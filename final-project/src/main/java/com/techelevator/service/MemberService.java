package com.techelevator.service;

import com.techelevator.dao.JdbcPlayerDao;
import com.techelevator.dao.JdbcTeamDao;
import com.techelevator.model.Player;
import com.techelevator.model.Team;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {

    private final JdbcPlayerDao playerDao;
    private final JdbcTeamDao teamDao;

    public MemberService(JdbcPlayerDao playerDao, JdbcTeamDao teamDao) {
        this.playerDao = playerDao;
        this.teamDao = teamDao;
    }

    // Player methods

    public List<Player> getAllPlayers() {
        return playerDao.getPlayers();
    }

    public Player getPlayerById(int id) {
        return playerDao.getPlayerById(id);
    }

    public Player getPlayerByName(String firstName, String lastName) {
        return playerDao.getPlayerByName(firstName, lastName);
    }

    public Player createPlayer(Player newPlayer) {
        return playerDao.createPlayer(newPlayer);
    }

    public Player updatePlayer(Player modifiedPlayer) {
        return playerDao.updatePlayer(modifiedPlayer);
    }

    public int deletePlayerById(int id) {
        return playerDao.deletePlayerById(id);
    }

    // Team methods

    public List<Team> getAllTeams() {
        return teamDao.getTeams();
    }

    public Team getTeamById(int id) {
        return teamDao.getTeamById(id);
    }

    public Team getTeamByName(String name) {
        return teamDao.getTeamByName(name);
    }

    public Team createTeam(Team newTeam) {
        return teamDao.createTeam(newTeam);
    }

    public Team updateTeam(Team modifiedTeam) {
        return teamDao.updateTeam(modifiedTeam);
    }

    public int deleteTeamById(int id) {
        return teamDao.deleteTeamById(id);
    }
}
