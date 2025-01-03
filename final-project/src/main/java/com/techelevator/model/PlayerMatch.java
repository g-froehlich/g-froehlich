package com.techelevator.model;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Model class representing a match between individual players.
 * <p>
 * Contains information about the match - id, league id, match date,
 * first player id, second player id, first player score, and second player score.
 */
public class PlayerMatch {
    private int id;
    @NotNull(message = "The field `leagueId` should not be blank.")
    private int leagueId;
    @NotNull(message = "The field `matchDate` should not be blank.")
    private Date matchDate;
    @NotNull(message = "The field `firstPlayerId` should not be blank.")
    private int firstPlayerId;
    @NotNull(message = "The field `secondPlayerId` should not be blank.")
    private int secondPlayerId;
    @NotNull(message = "The field `firstPlayerScore` should not be blank.")
    private int firstPlayerScore;
    @NotNull(message = "The field `secondPlayerScore` should not be blank.")
    private int secondPlayerScore;

    public PlayerMatch() {
    }

    public PlayerMatch(int id, int leagueId, Date matchDate, int firstPlayerId, int secondPlayerId, int firstPlayerScore, int secondPlayerScore) {
        this.id = id;
        this.leagueId = leagueId;
        this.matchDate = matchDate;
        this.firstPlayerId = firstPlayerId;
        this.secondPlayerId = secondPlayerId;
        this.firstPlayerScore = firstPlayerScore;
        this.secondPlayerScore = secondPlayerScore;
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

    public int getFirstPlayerId() {
        return firstPlayerId;
    }

    public void setFirstPlayerId(int firstPlayerId) {
        this.firstPlayerId = firstPlayerId;
    }

    public int getSecondPlayerId() {
        return secondPlayerId;
    }

    public void setSecondPlayerId(int secondPlayerId) {
        this.secondPlayerId = secondPlayerId;
    }

    public int getFirstPlayerScore() {
        return firstPlayerScore;
    }

    public void setFirstPlayerScore(int firstPlayerScore) {
        this.firstPlayerScore = firstPlayerScore;
    }

    public int getSecondPlayerScore() {
        return secondPlayerScore;
    }

    public void setSecondPlayerScore(int secondPlayerScore) {
        this.secondPlayerScore = secondPlayerScore;
    }

    @Override
    public String toString() {
        return "PlayerMatch{" +
                "id=" + id +
                ", leagueId=" + leagueId +
                ", matchDate=" + matchDate +
                ", firstPlayerId=" + firstPlayerId +
                ", secondPlayerId=" + secondPlayerId +
                ", firstPlayerScore=" + firstPlayerScore +
                ", secondPlayerScore=" + secondPlayerScore +
                '}';
    }
}
