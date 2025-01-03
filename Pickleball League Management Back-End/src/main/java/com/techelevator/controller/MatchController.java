package com.techelevator.controller;

import com.techelevator.exception.DaoException;
import com.techelevator.model.PlayerMatch;
import com.techelevator.model.TeamMatch;
import com.techelevator.service.MatchService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(path = "/matches")
@PreAuthorize("isAuthenticated()")
public class MatchController {

    private final MatchService matchService;

    public MatchController(MatchService matchService){
        this.matchService = matchService;
    }

    //Player Match Endpoints
    @PreAuthorize("permitAll()")
    @RequestMapping(path = "/players", method = RequestMethod.GET)
    public List<PlayerMatch> getAllPlayerMatches(){
        List<PlayerMatch> playerMatches = new ArrayList<>();

        try{
            playerMatches = matchService.getAllPlayerMatches();
        } catch (DaoException e) {
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return playerMatches;
    }

    @PreAuthorize("permitAll()")
    @RequestMapping(path = "/players/searchByIdType", method = RequestMethod.GET)
    public List<PlayerMatch> getPlayerMatchByIdType(@RequestParam String idType, @RequestParam int value){
        List<PlayerMatch> playerMatches = new ArrayList<>();

        if (idType.equalsIgnoreCase("playerMatch")) {
            PlayerMatch playerMatch = matchService.getPlayerMatchById(value);
            playerMatches.add(playerMatch);
        } else if (idType.equalsIgnoreCase("league")) {
            playerMatches = matchService.getPlayerMatchesByLeagueId(value);
        } else if (idType.equalsIgnoreCase("firstPlayer")) {
            playerMatches = matchService.getPlayerMatchesByFirstPlayerId(value);
        } else if (idType.equalsIgnoreCase("secondPlayer")) {
            playerMatches = matchService.getPlayerMatchesBySecondPlayerId(value);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid id type specified");
        }

        if (playerMatches == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Player Match Not Found");
        } else {
            return playerMatches;
        }
    }

    @PreAuthorize("permitAll()")
    @RequestMapping(path = "/players", method = RequestMethod.POST)
    public PlayerMatch createPlayerMatch(@Valid @RequestBody PlayerMatch playerMatch) {
        return matchService.createPlayerMatch(playerMatch);
    }

    @PreAuthorize("permitAll()")
    @RequestMapping(path = "/players/{id}", method = RequestMethod.PUT)
    public PlayerMatch updatePlayerMatch(@Valid @RequestBody PlayerMatch playerMatch, @PathVariable int id) {
        playerMatch.setId(id); //id in endpoint takes concern
        try {
            return matchService.updatePlayerMatch(playerMatch);
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Player Match Not Found");
        }
    }

    @PreAuthorize("permitAll()")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(path = "players/{id}", method = RequestMethod.DELETE)
    public void deletePlayerMatch(@PathVariable int id){
        matchService.deletePlayerMatchById(id);
    }

    //Team Match Endpoints
    @PreAuthorize("permitAll()")
    @RequestMapping(path = "/teams", method = RequestMethod.GET)
    public List<TeamMatch> getAllTeamMatches(){
        List<TeamMatch> teamMatches = new ArrayList<>();

        try{
            teamMatches = matchService.getAllTeamMatches();
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return teamMatches;
    }

    @PreAuthorize("permitAll()")
    @RequestMapping(path = "/teams/searchByIdType", method = RequestMethod.GET)
    public List<TeamMatch> getTeamMatchByIdType(@RequestParam String idType, @RequestParam int value){
        List<TeamMatch> teamMatches = new ArrayList<>();

        if (idType.equalsIgnoreCase("teamMatch")) {
            TeamMatch teamMatch = matchService.getTeamMatchById(value);
            teamMatches.add(teamMatch);
        } else if (idType.equalsIgnoreCase("league")) {
            teamMatches = matchService.getTeamMatchesByLeagueId(value);
        } else if (idType.equalsIgnoreCase("firstTeam")) {
            teamMatches = matchService.getTeamMatchesByFirstTeamId(value);
        } else if (idType.equalsIgnoreCase("secondTeam")) {
            teamMatches = matchService.getTeamMatchesBySecondTeamId(value);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid id type specified");
        }

        if (teamMatches == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Team Match Not Found");
        } else {
            return teamMatches;
        }
    }

    @PreAuthorize("permitAll()")
    @RequestMapping(path = "/teams", method = RequestMethod.POST)
    public TeamMatch createTeamMatch(@Valid @RequestBody TeamMatch teamMatch) {
        return matchService.createTeamMatch(teamMatch);
    }

    @PreAuthorize("permitAll()")
    @RequestMapping(path = "/teams/{id}", method = RequestMethod.PUT)
    public TeamMatch updateTeamMatch(@Valid @RequestBody TeamMatch teamMatch, @PathVariable int id) {
        teamMatch.setId(id); //id in endpoint takes concern
        try {
            return matchService.updateTeamMatch(teamMatch);
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Team Match Not Found");
        }
    }

    @PreAuthorize("permitAll()")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(path = "teams/{id}", method = RequestMethod.DELETE)
    public void deleteTeamMatch(@PathVariable int id){
        matchService.deleteTeamMatchById(id);
    }
}
