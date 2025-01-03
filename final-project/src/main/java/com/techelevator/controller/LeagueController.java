package com.techelevator.controller;

import com.techelevator.dao.LeagueDao;
import com.techelevator.exception.DaoException;
import com.techelevator.model.League;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(path = "/leagues")
@PreAuthorize("isAuthenticated()")
public class LeagueController {

    private LeagueDao leagueDao;

    public LeagueController(LeagueDao leagueDao) {
        this.leagueDao = leagueDao;
    }

    @PreAuthorize("permitAll()")
    @RequestMapping(method = RequestMethod.GET)
    public List<League> getAllLeagues() {
        List<League> leagues = new ArrayList<>();

        try {
            leagues = leagueDao.getLeagues();
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return leagues;
    }

    @PreAuthorize("permitAll()")
    @RequestMapping(path = "/searchByIdType", method = RequestMethod.GET)
    public List<League> findByIdType(@RequestParam String idType, @RequestParam int value) {
        List<League> leagues = new ArrayList<>();

        if (idType.equalsIgnoreCase("league")) {
            League league = leagueDao.getLeagueById(value);
            leagues.add(league);
        } else if (idType.equalsIgnoreCase("manager")) {
            leagues = leagueDao.getLeaguesByManagerId(value);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid id type specified");
        }

        if (leagues == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "League Not Found");
        } else {
            return leagues;
        }
    }

    @PreAuthorize("permitAll()")
    @RequestMapping(path = "/searchByName", method = RequestMethod.GET)
    public League findByName(@RequestParam String search) {
        League league = null;

        league = leagueDao.getLeagueByName(search);

        if (league == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "League Not Found");
        }

        return league;
    }

    @PreAuthorize("hasAnyRole('ADMIN','LEAGUE_MANAGER')")
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST)
    public League create(@Valid @RequestBody League league) {
        return leagueDao.createLeague(league);
    }

    @PreAuthorize("hasAnyRole('ADMIN','LEAGUE_MANAGER')")
    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    public League update(@Valid @RequestBody League league, @PathVariable int id) {
        league.setId(id); //id in endpoint takes concern
        try {
            return leagueDao.updateLeague(league);
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "League Not Found");
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN','LEAGUE_MANAGER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable int id) {
        leagueDao.deleteLeagueById(id);
    }
}
