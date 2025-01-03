package com.techelevator.dao;

import com.techelevator.exception.DaoException;
import com.techelevator.model.PlayerMatch;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcPlayerMatchDao implements PlayerMatchDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcPlayerMatchDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<PlayerMatch> getPlayerMatches() {
        List<PlayerMatch> playerMatches = new ArrayList<>();
        String sql = "SELECT * FROM player_match ORDER BY league_id, match_date";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
            while (results.next()) {
                PlayerMatch playerMatch = mapRowToPlayerMatch(results);
                playerMatches.add(playerMatch);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return playerMatches;
    }

    @Override
    public PlayerMatch getPlayerMatchById(int id) {
        PlayerMatch playerMatch = null;
        String sql = "SELECT * FROM player_match WHERE match_id=?";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
            if (results.next()) {
                playerMatch = mapRowToPlayerMatch(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return playerMatch;
    }

    @Override
    public List<PlayerMatch> getPlayerMatchesByLeagueId(int id) {
        List<PlayerMatch> playerMatches = new ArrayList<>();
        String sql = "SELECT * FROM player_match WHERE league_id=? ORDER BY match_id";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
            while (results.next()) {
                PlayerMatch playerMatch = mapRowToPlayerMatch(results);
                playerMatches.add(playerMatch);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return playerMatches;
    }

    @Override
    public List<PlayerMatch> getPlayerMatchesByFirstPlayerId(int id) {
        List<PlayerMatch> playerMatches = new ArrayList<>();
        String sql = "SELECT * FROM player_match WHERE player1_id=? ORDER BY league_id";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
            while (results.next()) {
                PlayerMatch playerMatch = mapRowToPlayerMatch(results);
                playerMatches.add(playerMatch);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return playerMatches;
    }

    @Override
    public List<PlayerMatch> getPlayerMatchesBySecondPlayerId(int id) {
        List<PlayerMatch> playerMatches = new ArrayList<>();
        String sql = "SELECT * FROM player_match WHERE player2_id=? ORDER BY league_id";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
            while (results.next()) {
                PlayerMatch playerMatch = mapRowToPlayerMatch(results);
                playerMatches.add(playerMatch);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return playerMatches;
    }

    @Override
    public PlayerMatch createPlayerMatch(PlayerMatch newPlayerMatch) {
        PlayerMatch playerMatch = null;
        String createPlayerMatchSql = "INSERT INTO player_match (league_id, match_date, " +
                "player1_id, player2_id, player1_score, player2_score) " +
                "VALUES (?,?,?,?,?,?) returning match_id";

        try {
            int playerMatchId = jdbcTemplate.queryForObject(createPlayerMatchSql, int.class,
                    newPlayerMatch.getLeagueId(), newPlayerMatch.getMatchDate(), newPlayerMatch.getFirstPlayerId(),
                    newPlayerMatch.getSecondPlayerId(), newPlayerMatch.getFirstPlayerScore(),
                    newPlayerMatch.getSecondPlayerScore());
            playerMatch = getPlayerMatchById(playerMatchId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return playerMatch;
    }

    @Override
    public PlayerMatch updatePlayerMatch(PlayerMatch modifiedPlayerMatch) {
        PlayerMatch updatedPlayerMatch = null;
        String updatePlayerMatchSql = "UPDATE player_match SET league_id=?, match_date=?, " +
                "player1_id=?, player2_id=?, player1_score=?, player2_score=? WHERE match_id=?";

        try {
            int rowsAffected = jdbcTemplate.update(updatePlayerMatchSql, modifiedPlayerMatch.getLeagueId(),
                    modifiedPlayerMatch.getMatchDate(), modifiedPlayerMatch.getFirstPlayerId(),
                    modifiedPlayerMatch.getSecondPlayerId(), modifiedPlayerMatch.getFirstPlayerScore(),
                    modifiedPlayerMatch.getSecondPlayerScore(), modifiedPlayerMatch.getId());
            if (rowsAffected == 0) {
                throw new DaoException("Zero rows affected, expected at least one");
            }
            updatedPlayerMatch = getPlayerMatchById(modifiedPlayerMatch.getId());
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return updatedPlayerMatch;
    }

    @Override
    public int deletePlayerMatchById(int id) {
        int count = 0;

        try {
            count += jdbcTemplate.update("DELETE FROM player_match WHERE match_id=?", id);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return count;
    }

    private PlayerMatch mapRowToPlayerMatch(SqlRowSet rowSet) {
        PlayerMatch playerMatch = new PlayerMatch();
        playerMatch.setId(rowSet.getInt("match_id"));
        playerMatch.setLeagueId(rowSet.getInt("league_id"));
        playerMatch.setMatchDate(rowSet.getDate("match_date"));
        playerMatch.setFirstPlayerId(rowSet.getInt("player1_id"));
        playerMatch.setSecondPlayerId(rowSet.getInt("player2_id"));
        playerMatch.setFirstPlayerScore(rowSet.getInt("player1_score"));
        playerMatch.setSecondPlayerScore(rowSet.getInt("player2_score"));
        return playerMatch;
    }
}
