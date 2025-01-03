package com.techelevator.controller;

import com.techelevator.dao.CourtDao;
import com.techelevator.exception.DaoException;
import com.techelevator.model.Court;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(path = "/courts")
@PreAuthorize("isAuthenticated()")
public class CourtController {

    private CourtDao courtDao;

    public CourtController(CourtDao courtDao) {
        this.courtDao = courtDao;
    }

    @PreAuthorize("permitAll()")
    @RequestMapping(method = RequestMethod.GET)
    public List<Court> getAllCourts() {
        List<Court> courts = new ArrayList<>();

        try {
            courts = courtDao.getCourts();
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return courts;
    }

    @PreAuthorize("permitAll()")
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public Court get(@PathVariable int id) {
        Court court = courtDao.getCourtById(id);
        if (court == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Court Not Found");
        } else {
            return court;
        }
    }

    @PreAuthorize("permitAll()")
    @RequestMapping(path = "/search", method = RequestMethod.GET)
    public Court getByString(@RequestParam String method, @RequestParam String value) {
        Court court = null;

        if (method.equalsIgnoreCase("name")) {
            court = courtDao.getCourtByName(value);
        } else if (method.equalsIgnoreCase("address")) {
            court = courtDao.getCourtByAddress(value);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid method specified");
        }

        if (court == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Court Not Found");
        }

        return court;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST)
    public Court create(@Valid @RequestBody Court court) {
        return courtDao.createCourt(court);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    public Court update(@Valid @RequestBody Court court, @PathVariable int id) {
        court.setId(id); //id in endpoint takes concern
        try {
            return courtDao.updateCourt(court);
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Court Not Found");
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable int id) {
        courtDao.deleteCourtById(id);
    }
}
