package com.techelevator.dao;

import com.techelevator.exception.DaoException;
import com.techelevator.model.Court;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcCourtDao implements CourtDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcCourtDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static String WILDCARD = "%";

    @Override
    public List<Court> getCourts() {
        List<Court> courts = new ArrayList<>();
        String sql = "SELECT * FROM court ORDER BY court_name";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
            while (results.next()) {
                Court court = mapRowToCourt(results);
                courts.add(court);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return courts;
    }

    @Override
    public Court getCourtById(int id) {
        Court court = null;
        String sql = "SELECT * FROM court WHERE court_id=?";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
            if (results.next()) {
                court = mapRowToCourt(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return court;
    }

    @Override
    public Court getCourtByAddress(String name) {
        if (name == null) {
            name = "";
        }
        Court court = null;
        String sql = "SELECT * FROM court WHERE court_address ILIKE ?";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, WILDCARD + name + WILDCARD);
            if (results.next()) {
                court = mapRowToCourt(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return court;
    }

    @Override
    public Court getCourtByName(String name) {
        if (name == null) {
            name = "";
        }
        Court court = null;
        String sql = "SELECT * FROM court WHERE court_name ILIKE ?";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, WILDCARD + name + WILDCARD);
            if (results.next()) {
                court = mapRowToCourt(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return court;
    }

    @Override
    public Court createCourt(Court newCourt) {
        Court court = null;
        String createCourtSql = "INSERT INTO court (court_address, court_name) " +
                "VALUES (?,?) RETURNING court_id";

        if (newCourt.getAddress() == null) {
            throw new DaoException("Address field may not be empty");
        } else if (newCourt.getName() == null) {
            throw new DaoException("Name field may not be empty");
        }
        try {
            int courtId = jdbcTemplate.queryForObject(createCourtSql, int.class, newCourt.getAddress(),
                    newCourt.getName());
            court = getCourtById(courtId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return court;
    }

    @Override
    public Court updateCourt(Court modifiedCourt) {
        Court updatedCourt = null;
        String updateCourtSql = "UPDATE court SET court_address=?, court_name=? WHERE court_id=?";

        try {
            int rowsAffected = jdbcTemplate.update(updateCourtSql, modifiedCourt.getAddress(),
                    modifiedCourt.getName(), modifiedCourt.getId());
            if (rowsAffected == 0) {
                throw new DaoException("Zero rows affected, expected at least one");
            }
            updatedCourt = getCourtById(modifiedCourt.getId());
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return updatedCourt;
    }

    @Override
    public int deleteCourtById(int id) {
        int count = 0;

        try {
            count += jdbcTemplate.update("DELETE FROM league WHERE court_id=?", id); //delete from foreign relation first
            count += jdbcTemplate.update("DELETE FROM court WHERE court_id=?", id);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return count;
    }

    private Court mapRowToCourt(SqlRowSet rowSet) {
        Court court = new Court();
        court.setId(rowSet.getInt("court_id"));
        court.setAddress(rowSet.getString("court_address"));
        court.setName(rowSet.getString("court_name"));
        return court;
    }
}
