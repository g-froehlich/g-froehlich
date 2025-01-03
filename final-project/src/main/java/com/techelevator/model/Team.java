package com.techelevator.model;

/**
 * Model class representing a team.
 * <p>
 * Contains information about the team - id, manager id, name, total score, and win loss ratio.
 * <p>
 * Manager id will come from user.
 */
public class Team {
    private int id;
    private int managerId;
    private String name;
    private int totalScore;
    private double winLossRatio;

    public Team() {
    }

    public Team(int id, int managerId, String name, int totalScore, double winLossRatio) {
        this.id = id;
        this.managerId = managerId;
        this.name = name;
        this.totalScore = totalScore;
        this.winLossRatio = winLossRatio;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getManagerId() {
        return managerId;
    }

    public void setManagerId(int managerId) {
        this.managerId = managerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public double getWinLossRatio() {
        return winLossRatio;
    }

    public void setWinLossRatio(double winLossRatio) {
        this.winLossRatio = winLossRatio;
    }

    @Override
    public String toString() {
        return "Team{" +
                "id=" + id +
                ", managerId=" + managerId +
                ", name='" + name + '\'' +
                ", totalScore=" + totalScore +
                ", winLossRatio=" + winLossRatio +
                '}';
    }
}
