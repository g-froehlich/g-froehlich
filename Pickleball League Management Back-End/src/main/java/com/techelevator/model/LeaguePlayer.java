package com.techelevator.model;

/**
 * Model class representing a relationship between a league and a player.
 * <p>
 * Contains information about the league relating it to the player.
 */
public class LeaguePlayer {
    private int id;
    private int leagueId;
    private int playerId;

    public LeaguePlayer() {
    }

    public LeaguePlayer(int id, int leagueId, int playerId) {
        this.id = id;
        this.leagueId = leagueId;
        this.playerId = playerId;
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

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    @Override
    public String toString() {
        return "LeaguePlayer{" +
                "id=" + id +
                ", leagueId=" + leagueId +
                ", playerId=" + playerId +
                '}';
    }
}
