package com.techelevator.dao;

import com.techelevator.exception.DaoException;
import com.techelevator.model.TeamPlayer;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTeamPlayerDao implements TeamPlayerDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcTeamPlayerDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<TeamPlayer> getTeamPlayers() {
        List<TeamPlayer> teamPlayers = new ArrayList<>();
        String sql = "SELECT * FROM team_player ORDER BY player_id";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
            while (results.next()) {
                TeamPlayer teamPlayer = mapRowToTeamPlayer(results);
                teamPlayers.add(teamPlayer);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return teamPlayers;
    }

    @Override
    public TeamPlayer getTeamPlayerById(int id) {
        TeamPlayer teamPlayer = null;
        String sql = "SELECT * FROM team_player WHERE team_player_id=?";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
            if (results.next()) {
                teamPlayer = mapRowToTeamPlayer(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return teamPlayer;
    }

    @Override
    public List<TeamPlayer> getTeamPlayersByTeamId(int id) {
        List<TeamPlayer> teamPlayers = new ArrayList<>();
        String sql = "SELECT * FROM team_player WHERE team_id=? ORDER BY team_player_id";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
            while (results.next()) {
                TeamPlayer teamPlayer = mapRowToTeamPlayer(results);
                teamPlayers.add(teamPlayer);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return teamPlayers;
    }

    @Override
    public List<TeamPlayer> getTeamPlayersByPlayerId(int id) {
        List<TeamPlayer> teamPlayers = new ArrayList<>();
        String sql = "SELECT * FROM team_player WHERE player_id=? ORDER BY team_player_id";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
            while (results.next()) {
                TeamPlayer teamPlayer = mapRowToTeamPlayer(results);
                teamPlayers.add(teamPlayer);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return teamPlayers;
    }

    @Override
    public TeamPlayer createTeamPlayer(TeamPlayer newTeamPlayer) {
        TeamPlayer teamPlayer = null;
        String createTeamPlayerSql = "INSERT INTO team_player (player_id, team_id) " +
                "VALUES (?,?) returning team_player_id";

        try {
            int teamPlayerId = jdbcTemplate.queryForObject(createTeamPlayerSql, int.class,
                    newTeamPlayer.getPlayerId(), newTeamPlayer.getTeamId());
            teamPlayer = getTeamPlayerById(teamPlayerId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return teamPlayer;
    }

    @Override
    public TeamPlayer updateTeamPlayer(TeamPlayer modifiedTeamPlayer) {
        TeamPlayer updatedTeamPlayer = null;
        String updateTeamPlayerSql = "UPDATE team_player SET player_id=?, team_id=? " +
                "WHERE team_player_id=?";

        try {
            int rowsAffected = jdbcTemplate.update(updateTeamPlayerSql, modifiedTeamPlayer.getPlayerId(),
                    modifiedTeamPlayer.getTeamId(), modifiedTeamPlayer.getId());
            if (rowsAffected == 0) {
                throw new DaoException("Zero rows affected, expected at least one");
            }
            updatedTeamPlayer = getTeamPlayerById(modifiedTeamPlayer.getId());
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return updatedTeamPlayer;
    }

    @Override
    public int deleteTeamPlayerById(int id) {
        int count = 0;

        try {
            count += jdbcTemplate.update("DELETE FROM team_player WHERE team_player_id=?", id);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return count;
    }

    private TeamPlayer mapRowToTeamPlayer(SqlRowSet rowSet) {
        TeamPlayer teamPlayer = new TeamPlayer();
        teamPlayer.setId(rowSet.getInt("team_player_id"));
        teamPlayer.setPlayerId(rowSet.getInt("player_id"));
        teamPlayer.setTeamId(rowSet.getInt("team_id"));
        return teamPlayer;
    }
}
