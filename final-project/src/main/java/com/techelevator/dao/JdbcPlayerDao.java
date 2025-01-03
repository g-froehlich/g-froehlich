package com.techelevator.dao;

import com.techelevator.exception.DaoException;
import com.techelevator.model.Player;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcPlayerDao implements PlayerDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcPlayerDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static String WILDCARD = "%";

    @Override
    public List<Player> getPlayers() {
        List<Player> players = new ArrayList<>();
        String sql = "SELECT * FROM player ORDER by player_last_name";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
            while (results.next()) {
                Player player = mapRowToPlayer(results);
                players.add(player);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return players;
    }

    @Override
    public Player getPlayerById(int id) {
        Player player = null;
        String sql = "SELECT * FROM player WHERE player_id=?";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
            if (results.next()) {
                player = mapRowToPlayer(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return player;
    }

    @Override
    public Player getPlayerByName(String firstName, String lastName) {
        Player player = null;
        String sql = "SELECT * FROM player WHERE player_first_name ILIKE ? AND player_last_name ILIKE ?";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, WILDCARD + firstName + WILDCARD, WILDCARD + lastName + WILDCARD);
            if (results.next()) {
                player = mapRowToPlayer(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return player;
    }

    @Override
    public Player createPlayer(Player newPlayer) {
        Player player = null;
        String createPlayerSql = "INSERT INTO player (user_id, player_first_name, " +
                "player_last_name, total_score, win_loss_ratio) " +
                "VALUES (?,?,?,?,?) returning player_id";

        try {
            int playerId = jdbcTemplate.queryForObject(createPlayerSql, int.class, newPlayer.getUserId(),
                    newPlayer.getFirstName(), newPlayer.getLastName(), newPlayer.getTotalScore(),
                    newPlayer.getWinLossRatio());
            player = getPlayerById(playerId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return player;
    }

    @Override
    public Player updatePlayer(Player modifiedPlayer) {
        Player updatedPlayer = null;
        String updatePlayerSql = "UPDATE player SET user_id=?, player_first_name=?, " +
                "player_last_name=?, total_score=?, win_loss_ratio=? " +
                "WHERE player_id=?";

        try {
            int rowsAffected = jdbcTemplate.update(updatePlayerSql, modifiedPlayer.getUserId(), modifiedPlayer.getFirstName(),
                    modifiedPlayer.getLastName(), modifiedPlayer.getTotalScore(), modifiedPlayer.getWinLossRatio(),
                    modifiedPlayer.getId());
            if (rowsAffected == 0) {
                throw new DaoException("Zero rows affected, expected at least one");
            }
            updatedPlayer = getPlayerById(modifiedPlayer.getId());
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return updatedPlayer;
    }

    @Override
    public int deletePlayerById(int id) {
        int count = 0;

        try {
            count += jdbcTemplate.update("DELETE FROM league_player WHERE player_id=?", id);
            count += jdbcTemplate.update("DELETE FROM player_match WHERE player_id=?", id);
            count += jdbcTemplate.update("DELETE FROM team_player WHERE player_id=?", id);
            count += jdbcTemplate.update("DELETE FROM player WHERE player_id=?", id);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return count;
    }

    private Player mapRowToPlayer(SqlRowSet rowSet) {
        Player player = new Player();
        player.setId(rowSet.getInt("player_id"));
        player.setUserId(rowSet.getInt("user_id"));
        player.setFirstName(rowSet.getString("player_first_name"));
        player.setLastName(rowSet.getString("player_last_name"));
        player.setTotalScore(rowSet.getInt("total_score"));
        player.setWinLossRatio(rowSet.getDouble("win_loss_ratio"));
        return player;
    }
}
