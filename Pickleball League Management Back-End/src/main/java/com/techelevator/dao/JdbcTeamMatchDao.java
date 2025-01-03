package com.techelevator.dao;

import com.techelevator.exception.DaoException;
import com.techelevator.model.TeamMatch;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTeamMatchDao implements TeamMatchDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcTeamMatchDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<TeamMatch> getTeamMatches() {
        List<TeamMatch> teamMatches = new ArrayList<>();
        String sql = "SELECT * FROM team_match ORDER BY league_id, team_match";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
            while (results.next()) {
                TeamMatch teamMatch = mapRowToTeamMatch(results);
                teamMatches.add(teamMatch);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return teamMatches;
    }

    @Override
    public TeamMatch getTeamMatchById(int id) {
        TeamMatch teamMatch = null;
        String sql = "SELECT * FROM team_match WHERE match_id=?";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
            if (results.next()) {
                teamMatch = mapRowToTeamMatch(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return teamMatch;
    }

    @Override
    public List<TeamMatch> getTeamMatchesByLeagueId(int id) {
        List<TeamMatch> teamMatches = new ArrayList<>();
        String sql = "SELECT * FROM team_match WHERE league_id=? ORDER BY match_id";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
            while (results.next()) {
                TeamMatch teamMatch = mapRowToTeamMatch(results);
                teamMatches.add(teamMatch);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return teamMatches;
    }

    @Override
    public List<TeamMatch> getTeamMatchesByFirstTeamId(int id) {
        List<TeamMatch> teamMatches = new ArrayList<>();
        String sql = "SELECT * FROM team_match WHERE team1_id=? ORDER BY league_id";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
            while (results.next()) {
                TeamMatch teamMatch = mapRowToTeamMatch(results);
                teamMatches.add(teamMatch);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return teamMatches;
    }

    @Override
    public List<TeamMatch> getTeamMatchedBySecondTeamId(int id) {
        List<TeamMatch> teamMatches = new ArrayList<>();
        String sql = "SELECT * FROM team_match WHERE team2_id=? ORDER BY league_id";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
            while (results.next()) {
                TeamMatch teamMatch = mapRowToTeamMatch(results);
                teamMatches.add(teamMatch);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return teamMatches;
    }

    @Override
    public TeamMatch createTeamMatch(TeamMatch newTeamMatch) {
        TeamMatch teamMatch = null;
        String createTeamMatchSql = "INSERT INTO team_match (league_id, match_date, " +
                "team1_id, team2_id, team1_score, team2_score) " +
                "VALUES (?,?,?,?,?,?) returning match_id";

        try {
            int teamMatchId = jdbcTemplate.queryForObject(createTeamMatchSql, int.class,
                    newTeamMatch.getLeagueId(), newTeamMatch.getMatchDate(), newTeamMatch.getFirstTeamId(),
                    newTeamMatch.getSecondTeamId(), newTeamMatch.getFirstTeamScore(),
                    newTeamMatch.getSecondTeamScore());
            teamMatch = getTeamMatchById(teamMatchId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return teamMatch;
    }

    @Override
    public TeamMatch updateTeamMatch(TeamMatch modifiedTeamMatch) {
        TeamMatch updatedTeamMatch = null;
        String updateTeamMatchSql = "UPDATE team_match SET league_id=?, match_date=?, " +
                "team1_id=?, team2_id=?, team1_score=?, team2_score=? WHERE match_id=?";

        try {
            int rowsAffected = jdbcTemplate.update(updateTeamMatchSql, modifiedTeamMatch.getLeagueId(),
                    modifiedTeamMatch.getMatchDate(), modifiedTeamMatch.getFirstTeamId(),
                    modifiedTeamMatch.getSecondTeamId(), modifiedTeamMatch.getFirstTeamScore(),
                    modifiedTeamMatch.getSecondTeamScore(), modifiedTeamMatch.getId());
            if (rowsAffected == 0) {
                throw new DaoException("Zero rows affected, expected at least one");
            }
            updatedTeamMatch = getTeamMatchById(modifiedTeamMatch.getId());
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return updatedTeamMatch;
    }

    @Override
    public int deleteTeamMatchById(int id) {
        int count = 0;

        try {
            count += jdbcTemplate.update("DELETE FROM team_match WHERE match_id=?", id);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return count;
    }

    private TeamMatch mapRowToTeamMatch(SqlRowSet rowSet) {
        TeamMatch teamMatch = new TeamMatch();
        teamMatch.setId(rowSet.getInt("match_id"));
        teamMatch.setLeagueId(rowSet.getInt("league_id"));
        teamMatch.setMatchDate(rowSet.getDate("match_date"));
        teamMatch.setFirstTeamId(rowSet.getInt("team1_id"));
        teamMatch.setSecondTeamId(rowSet.getInt("team2_id"));
        teamMatch.setFirstTeamScore(rowSet.getInt("team1_score"));
        teamMatch.setSecondTeamScore(rowSet.getInt("team2_score"));
        return teamMatch;
    }
}
