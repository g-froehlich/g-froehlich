package com.techelevator.controller;

import com.techelevator.model.Court;
import com.techelevator.model.Player;
import com.techelevator.model.Team;
import com.techelevator.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(path = "/members")
@PreAuthorize("isAuthenticated()")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    // Player Endpoints

    @PreAuthorize("permitAll()")
    @RequestMapping(path = "/players", method = RequestMethod.GET)
    public List<Player> getAllPlayers() {
        List<Player> players = new ArrayList<>();
        try {
            players = memberService.getAllPlayers();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return players;
    }

    @PreAuthorize("permitAll()")
    @RequestMapping(path = "/players/{id}", method = RequestMethod.GET)
    public Player getPlayerById(@PathVariable int id) {
        try {
            return memberService.getPlayerById(id);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Player not found");
        }
    }

    @PreAuthorize("permitAll()")
    @RequestMapping(path = "/players/search", method = RequestMethod.GET)
    public Player getPlayerByFirstAndLast(@RequestParam String firstName, @RequestParam String lastName) {
        try {
            return memberService.getPlayerByName(firstName, lastName);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Player not found");
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN','LEAGUE_MANAGER')")
    @RequestMapping(path = "/players", method = RequestMethod.POST)
    public Player createPlayer(@Valid @RequestBody Player newPlayer) {
        return memberService.createPlayer(newPlayer);
    }

    @PreAuthorize("hasAnyRole('ADMIN','LEAGUE_MANAGER')")
    @RequestMapping(path = "/players/{id}", method = RequestMethod.PUT)
    public Player updatePlayer(@Valid @RequestBody Player modifiedPlayer, @PathVariable int id) {
        modifiedPlayer.setId(id);
        try {
            return memberService.updatePlayer(modifiedPlayer);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Player not found");
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN','LEAGUE_MANAGER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(path = "/players/{id}", method = RequestMethod.DELETE)
    public void deletePlayer(@PathVariable int id) {
        try {
            memberService.deletePlayerById(id);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Player not found");
        }
    }

    // Team Endpoints

    @PreAuthorize("permitAll()")
    @RequestMapping(path = "/teams", method = RequestMethod.GET)
    public List<Team> getAllTeams() {
        List<Team> teams = new ArrayList<>();
        try {
            teams = memberService.getAllTeams();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return teams;
    }

    @PreAuthorize("permitAll()")
    @RequestMapping(path = "/teams/{id}", method = RequestMethod.GET)
    public Team getTeamById(@PathVariable int id) {
        try {
            return memberService.getTeamById(id);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Team not found");
        }
    }

    @PreAuthorize("permitAll()")
    @RequestMapping(path = "/teams/search", method = RequestMethod.GET)
    public Team getTeamByName(@RequestParam String name) {
        try {
            return memberService.getTeamByName(name);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Team not found");
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN','LEAGUE_MANAGER')")
    @RequestMapping(path = "/teams", method = RequestMethod.POST)
    public Team createTeam(@Valid @RequestBody Team newTeam) {
        return memberService.createTeam(newTeam);
    }

    @PreAuthorize("hasAnyRole('ADMIN','LEAGUE_MANAGER')")
    @RequestMapping(path = "/teams/{id}", method = RequestMethod.PUT)
    public Team updateTeam(@Valid @RequestBody Team modifiedTeam, @PathVariable int id) {
        modifiedTeam.setId(id);
        try {
            return memberService.updateTeam(modifiedTeam);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Team not found");
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN','LEAGUE_MANAGER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(path = "/teams/{id}", method = RequestMethod.DELETE)
    public void deleteTeam(@PathVariable int id) {
        try {
            memberService.deleteTeamById(id);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Team not found");
        }
    }
}
