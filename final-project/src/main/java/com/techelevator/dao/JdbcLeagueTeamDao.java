package com.techelevator.dao;

import com.techelevator.exception.DaoException;
import com.techelevator.model.LeagueTeam;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcLeagueTeamDao implements LeagueTeamDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcLeagueTeamDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<LeagueTeam> getLeagueTeams() {
        List<LeagueTeam> leagueTeams = new ArrayList<>();
        String sql = "SELECT * FROM league_team ORDER BY league_id";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
            while (results.next()) {
                LeagueTeam leagueTeam = mapRowToLeagueTeam(results);
                leagueTeams.add(leagueTeam);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return leagueTeams;
    }

    @Override
    public LeagueTeam getLeagueTeamById(int id) {
        LeagueTeam leagueTeam = null;
        String sql = "SELECT * FROM league_team WHERE league_team_id=?";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
            if (results.next()) {
                leagueTeam = mapRowToLeagueTeam(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return leagueTeam;
    }

    @Override
    public List<LeagueTeam> getLeagueTeamsByLeagueId(int leagueId) {
        List<LeagueTeam> leagueTeams = new ArrayList<>();
        String sql = "SELECT * FROM league_team WHERE league_id=? ORDER BY league_team_id";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, leagueId);
            while (results.next()) {
                LeagueTeam leagueTeam = mapRowToLeagueTeam(results);
                leagueTeams.add(leagueTeam);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return leagueTeams;
    }

    @Override
    public List<LeagueTeam> getLeaguesByTeamId(int teamId) {
        List<LeagueTeam> leagueTeams = new ArrayList<>();
        String sql = "SELECT * FROM league_team WHERE team_id=? ORDER BY league_id";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, teamId);
            while (results.next()) {
                LeagueTeam leagueTeam = mapRowToLeagueTeam(results);
                leagueTeams.add(leagueTeam);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return leagueTeams;
    }

    @Override
    public LeagueTeam createLeagueTeam(LeagueTeam newLeagueTeam) {
        LeagueTeam leagueTeam = null;
        String createLeagueTeamSql = "INSERT INTO league_team (league_id, team_id) " +
                "VALUES (?,?) returning league_team_id";

        try {
            int leagueTeamId = jdbcTemplate.queryForObject(createLeagueTeamSql, int.class,
                    newLeagueTeam.getLeagueId(), newLeagueTeam.getTeamId());
            leagueTeam = getLeagueTeamById(leagueTeamId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return leagueTeam;
    }

    @Override
    public LeagueTeam updateLeagueTeam(LeagueTeam modifiedLeagueTeam) {
        LeagueTeam updatedLeagueTeam = null;
        String updatedLeagueTeamSql = "UPDATE league_team SET league_id=?, team_id=? " +
                "WHERE league_team_id=?";

        try {
            int rowsAffected = jdbcTemplate.update(updatedLeagueTeamSql, modifiedLeagueTeam.getLeagueId(),
                    modifiedLeagueTeam.getTeamId(), modifiedLeagueTeam.getId());
            if (rowsAffected == 0) {
                throw new DaoException("Zero rows affected, expected at least one");
            }
            updatedLeagueTeam = getLeagueTeamById(modifiedLeagueTeam.getId());
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return updatedLeagueTeam;
    }

    @Override
    public int deleteLeagueTeamById(int id) {
        int count = 0;

        try {
            count += jdbcTemplate.update("DELETE FROM league_team WHERE league_team_id=?", id);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return count;
    }

    private LeagueTeam mapRowToLeagueTeam(SqlRowSet rowSet) {
        LeagueTeam leagueTeam = new LeagueTeam();
        leagueTeam.setId(rowSet.getInt("league_team_id"));
        leagueTeam.setLeagueId(rowSet.getInt("league_id"));
        leagueTeam.setTeamId(rowSet.getInt("player_id"));
        return leagueTeam;
    }
}
