package com.techelevator.model;

/**
 * Model class representing a relationship between a league and a team.
 * <p>
 * Contains information about the league relating it to the team.
 */
public class LeagueTeam {
    private int id;
    private int leagueId;
    private int teamId;

    public LeagueTeam() {
    }

    public LeagueTeam(int id, int leagueId, int teamId) {
        this.id = id;
        this.leagueId = leagueId;
        this.teamId = teamId;
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

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    @Override
    public String toString() {
        return "LeagueTeam{" +
                "id=" + id +
                ", leagueId=" + leagueId +
                ", teamId=" + teamId +
                '}';
    }
}
