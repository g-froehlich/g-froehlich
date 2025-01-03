package com.techelevator.dao;

import com.techelevator.exception.DaoException;
import com.techelevator.model.League;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcLeagueDao implements LeagueDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcLeagueDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static String WILDCARD = "%";

    @Override
    public List<League> getLeagues() {
        List<League> leagues = new ArrayList<>();
        String sql = "SELECT * FROM league ORDER BY league_start_date";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
            while (results.next()) {
                League league = mapRowToLeague(results);
                leagues.add(league);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return leagues;
    }

    @Override
    public List<League> getLeaguesByManagerId(int id) {
        List<League> leagues = new ArrayList<>();
        String sql = "SELECT * FROM league WHERE manager_id=? ORDER BY league_start_date";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
            while (results.next()) {
                League league = mapRowToLeague(results);
                leagues.add(league);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return leagues;
    }

    @Override
    public League getLeagueById(int id) {
        League league = null;
        String sql = "SELECT * FROM league WHERE league_id=?";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
            if (results.next()) {
                league = mapRowToLeague(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return league;
    }

    @Override
    public League getLeagueByName(String name) {
        League league = null;
        String sql = "SELECT * FROM league WHERE league_name ILIKE ?";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, WILDCARD + name + WILDCARD);
            if (results.next()) {
                league = mapRowToLeague(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return league;
    }

    @Override
    public League createLeague(League newLeague) {
        League league = null;
        String createLeagueSql = "INSERT INTO league (manager_id, court_id, " +
                "league_name, league_start_date, league_end_date) " +
                "VALUES (?,?,?,?,?) returning league_id";

        try {
            int leagueId = jdbcTemplate.queryForObject(createLeagueSql, int.class,
                    newLeague.getManagerId(), newLeague.getCourtId(), newLeague.getName(),
                    newLeague.getStartDate(), newLeague.getEndDate());
            league = getLeagueById(leagueId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return league;
    }

    @Override
    public League updateLeague(League modifiedLeague) {
        League updatedLeague = null;
        String updateLeagueSql = "UPDATE league SET manager_id=?, court_id=?, league_name=?, " +
                "league_start_date=?, league_end_date=? WHERE league_id=?";

        try {
            int rowsAffected = jdbcTemplate.update(updateLeagueSql, modifiedLeague.getManagerId(),
                    modifiedLeague.getCourtId(), modifiedLeague.getName(), modifiedLeague.getStartDate(),
                    modifiedLeague.getEndDate(), modifiedLeague.getId());
            if (rowsAffected == 0) {
                throw new DaoException("Zero rows affected, expected at least one");
            }
            updatedLeague = getLeagueById(modifiedLeague.getId());
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return updatedLeague;
    }

    @Override
    public int deleteLeagueById(int id) {
        int count = 0;

        try {
            count += jdbcTemplate.update("DELETE FROM team_match WHERE league_id=?", id);
            count += jdbcTemplate.update("DELETE FROM player_match WHERE league_id=?", id);
            count += jdbcTemplate.update("DELETE FROM league_team WHERE league_id=?", id);
            count += jdbcTemplate.update("DELETE FROM league_player WHERE league_id=?", id);
            count += jdbcTemplate.update("DELETE FROM league WHERE league_id=?", id);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return count;
    }

    private League mapRowToLeague(SqlRowSet rowSet) {
        League league = new League();
        league.setId(rowSet.getInt("league_id"));
        league.setManagerId(rowSet.getInt("manager_id"));
        league.setCourtId(rowSet.getInt("court_id"));
        league.setName(rowSet.getString("league_name"));
        league.setStartDate(rowSet.getDate("league_start_date").toLocalDate());

        // Check if league_end_date is null before calling toLocalDate()
        if (rowSet.getDate("league_end_date") != null) {
            league.setEndDate(rowSet.getDate("league_end_date").toLocalDate());
        } else {
            league.setEndDate(null);  // Handle null end date
        }

        return league;
    }
}
