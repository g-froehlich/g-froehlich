package com.techelevator.dao;

import com.techelevator.exception.DaoException;
import com.techelevator.model.LeaguePlayer;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcLeaguePlayerDao implements LeaguePlayerDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcLeaguePlayerDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<LeaguePlayer> getLeaguePlayers() {
        List<LeaguePlayer> leaguePlayers = new ArrayList<>();
        String sql = "SELECT * FROM league_player ORDER BY league_id";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
            while (results.next()) {
                LeaguePlayer leaguePlayer = mapRowToLeaguePlayer(results);
                leaguePlayers.add(leaguePlayer);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return leaguePlayers;
    }

    @Override
    public LeaguePlayer getLeaguePlayerById(int id) {
        LeaguePlayer leaguePlayer = null;
        String sql = "SELECT * FROM league_player WHERE league_player_id=?";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
            if (results.next()) {
                leaguePlayer = mapRowToLeaguePlayer(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return leaguePlayer;
    }

    @Override
    public List<LeaguePlayer> getLeaguePlayersByLeagueId(int leagueId) {
        List<LeaguePlayer> leaguePlayers = new ArrayList<>();
        String sql = "SELECT * FROM league_player WHERE league_id=? ORDER BY league_player_id";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, leagueId);
            while (results.next()) {
                LeaguePlayer leaguePlayer = mapRowToLeaguePlayer(results);
                leaguePlayers.add(leaguePlayer);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return leaguePlayers;
    }

    @Override
    public List<LeaguePlayer> getLeaguesByPlayerId(int playerId) {
        List<LeaguePlayer> leaguePlayers = new ArrayList<>();
        String sql = "SELECT * FROM league_player WHERE player_id=? ORDER BY league_id";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, playerId);
            while (results.next()) {
                LeaguePlayer leaguePlayer = mapRowToLeaguePlayer(results);
                leaguePlayers.add(leaguePlayer);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return leaguePlayers;
    }

    @Override
    public LeaguePlayer createLeaguePlayer(LeaguePlayer newLeaguePlayer) {
        LeaguePlayer leaguePlayer = null;
        String createLeaguePlayerSql = "INSERT INTO league_player (league_id, player_id) " +
                "VALUES (?,?) returning league_player_id";

        try {
            int leaguePlayerId = jdbcTemplate.queryForObject(createLeaguePlayerSql, int.class,
                    newLeaguePlayer.getLeagueId(), newLeaguePlayer.getPlayerId());
            leaguePlayer = getLeaguePlayerById(leaguePlayerId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return leaguePlayer;
    }

    @Override
    public LeaguePlayer updateLeaguePlayer(LeaguePlayer modifiedLeaguePlayer) {
        LeaguePlayer updatedLeaguePlayer = null;
        String updateLeaguePlayerSql = "UPDATE league_player SET league_id=?, player_id=? " +
                "WHERE league_player_id=?";

        try {
            int rowsAffected = jdbcTemplate.update(updateLeaguePlayerSql, modifiedLeaguePlayer.getLeagueId(),
                    modifiedLeaguePlayer.getPlayerId(), modifiedLeaguePlayer.getId());
            if (rowsAffected == 0) {
                throw new DaoException("Zero rows affected, expected at least one");
            }
            updatedLeaguePlayer = getLeaguePlayerById(modifiedLeaguePlayer.getId());
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return updatedLeaguePlayer;
    }

    @Override
    public int deleteLeaguePlayerById(int id) {
        int count = 0;

        try {
            count += jdbcTemplate.update("DELETE FROM league_player WHERE league_player_id=?", id);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return count;
    }

    private LeaguePlayer mapRowToLeaguePlayer(SqlRowSet rowSet) {
        LeaguePlayer leaguePlayer = new LeaguePlayer();
        leaguePlayer.setId(rowSet.getInt("league_player_id"));
        leaguePlayer.setLeagueId(rowSet.getInt("league_id"));
        leaguePlayer.setPlayerId(rowSet.getInt("player_id"));
        return leaguePlayer;
    }
}
