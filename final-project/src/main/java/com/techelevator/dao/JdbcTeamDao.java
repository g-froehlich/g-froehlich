package com.techelevator.dao;

import com.techelevator.exception.DaoException;
import com.techelevator.model.Team;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTeamDao implements TeamDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcTeamDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static String WILDCARD = "%";

    @Override
    public List<Team> getTeams() {
        List<Team> teams = new ArrayList<>();
        String sql = "SELECT * FROM team ORDER BY team_name";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
            while (results.next()) {
                Team team = mapRowToTeam(results);
                teams.add(team);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return teams;
    }

    @Override
    public Team getTeamById(int id) {
        Team team = null;
        String sql = "SELECT * FROM team WHERE team_id=?";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
            if (results.next()) {
                team = mapRowToTeam(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return team;
    }

    @Override
    public Team getTeamByName(String name) {
        Team team = null;
        String sql = "SELECT * FROM team WHERE team_name ILIKE ?";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, WILDCARD + name + WILDCARD);
            if (results.next()) {
                team = mapRowToTeam(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return team;
    }

    @Override
    public Team createTeam(Team newTeam) {
        Team team = null;
        String createTeamSql = "INSERT INTO team (manager_id, team_name, total_score, win_loss_ratio) " +
                "VALUES (?,?,?,?) returning team_id";

        try {
            int teamId = jdbcTemplate.queryForObject(createTeamSql, int.class, newTeam.getManagerId(), newTeam.getName(),
                    newTeam.getTotalScore(), newTeam.getWinLossRatio());
            team = getTeamById(teamId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return team;
    }

    @Override
    public Team updateTeam(Team modifiedTeam) {
        Team updatedTeam = null;
        String updateTeamSql = "UPDATE team SET manager_id=?, team_name=?, total_score=?, win_loss_ratio=? " +
                "WHERE team_id=?";

        try {
            int rowsAffected = jdbcTemplate.update(updateTeamSql, modifiedTeam.getManagerId(), modifiedTeam.getName(),
                    modifiedTeam.getTotalScore(), modifiedTeam.getWinLossRatio(), modifiedTeam.getId());
            if (rowsAffected == 0) {
                throw new DaoException("Zero rows affected, expected at least one");
            }
            updatedTeam = getTeamById(modifiedTeam.getId());
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return updatedTeam;
    }

    @Override
    public int deleteTeamById(int id) {
        int count = 0;

        try {
            count += jdbcTemplate.update("DELETE FROM league_team WHERE team_id=?", id);
            count += jdbcTemplate.update("DELETE FROM team_match WHERE team_id=?", id);
            count += jdbcTemplate.update("DELETE FROM team_player WHERE team_id=?", id);
            count += jdbcTemplate.update("DELETE FROM team WHERE team_id=?", id);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return count;
    }

    private Team mapRowToTeam(SqlRowSet rowSet) {
        Team team = new Team();
        team.setId(rowSet.getInt("team_id"));
        team.setManagerId(rowSet.getInt("manager_id"));
        team.setName(rowSet.getString("team_name"));
        team.setTotalScore(rowSet.getInt("total_score"));
        team.setWinLossRatio(rowSet.getDouble("win_loss_ratio"));
        return team;
    }
}
