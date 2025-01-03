package com.techelevator.model;

/**
 * Model class representing a player.
 * <p>
 * Contains information about the league - id, user id, first name, last name, total score, and win loss ratio.
 */
public class Player {
    private int id;
    private int userId;
    private String firstName;
    private String lastName;
    private int totalScore;
    private double winLossRatio;

    public Player() {
    }

    public Player(int id, int userId, String firstName, String lastName, int totalScore, double winLossRatio) {
        this.id = id;
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.totalScore = totalScore;
        this.winLossRatio = winLossRatio;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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
        return "Player{" +
                "id=" + id +
                ", userId=" + userId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", totalScore=" + totalScore +
                ", winLossRatio=" + winLossRatio +
                '}';
    }
}
