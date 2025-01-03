package com.techelevator.model;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Model class representing a match between teams of players.
 * <p>
 * Contains information about the match - id, league id, match date,
 * first team id, second team id, first team score, and second team score.
 */
public class TeamMatch {
    private int id;
    @NotNull(message = "The field `leagueId` should not be blank.")
    private int leagueId;
    @NotNull(message = "The field `matchDate` should not be blank.")
    private Date matchDate;
    @NotNull(message = "The field `firstTeamId` should not be blank.")
    private int firstTeamId;
    @NotNull(message = "The field `secondTeamId` should not be blank.")
    private int secondTeamId;
    @NotNull(message = "The field `firstTeamScore` should not be blank.")
    private int firstTeamScore;
    @NotNull(message = "The field `secondTeamScore` should not be blank.")
    private int secondTeamScore;

    public TeamMatch() {
    }

    public TeamMatch(int id, int leagueId, Date matchDate, int firstTeamId, int secondTeamId, int firstTeamScore, int secondTeamScore) {
        this.id = id;
        this.leagueId = leagueId;
        this.matchDate = matchDate;
        this.firstTeamId = firstTeamId;
        this.secondTeamId = secondTeamId;
        this.firstTeamScore = firstTeamScore;
        this.secondTeamScore = secondTeamScore;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLeagueId() {
        return leagueId;
    }

    public void setLeagueId(int leagueId) {
        this.leagueId = leagueId;
    }

    public Date getMatchDate() {
        return matchDate;
    }

    public void setMatchDate(Date matchDate) {
        this.matchDate = matchDate;
    }

    public int getFirstTeamId() {
        return firstTeamId;
    }

    public void setFirstTeamId(int firstTeamId) {
        this.firstTeamId = firstTeamId;
    }

    public int getSecondTeamId() {
        return secondTeamId;
    }

    public void setSecondTeamId(int secondTeamId) {
        this.secondTeamId = secondTeamId;
    }

    public int getFirstTeamScore() {
        return firstTeamScore;
    }

    public void setFirstTeamScore(int firstTeamScore) {
        this.firstTeamScore = firstTeamScore;
    }

    public int getSecondTeamScore() {
        return secondTeamScore;
    }

    public void setSecondTeamScore(int secondTeamScore) {
        this.secondTeamScore = secondTeamScore;
    }

    @Override
    public String toString() {
        return "TeamMatch{" +
                "id=" + id +
                ", leagueId=" + leagueId +
                ", matchDate=" + matchDate +
                ", firstTeamId=" + firstTeamId +
                ", secondTeamId=" + secondTeamId +
                ", firstTeamScore=" + firstTeamScore +
                ", secondTeamScore=" + secondTeamScore +
                '}';
    }
}
