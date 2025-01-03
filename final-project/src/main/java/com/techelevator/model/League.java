package com.techelevator.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * Model class representing a league.
 * <p>
 * Contains information about the league - id, manager id, court, name, start date, and end date.
 * <p>
 * Manager id will come from user.
 */
public class League {
    private int id;
    @NotNull(message = "The field `managerId` should not be blank.")
    private int managerId;
    @NotNull(message = "The field `courtId` should not be blank.")
    private int courtId;
    @NotBlank(message = "The field `name` should not be blank.")
    private String name;
    @NotNull(message = "The field `startDate` should not be blank.")
    private LocalDate startDate;
    private LocalDate endDate;

    public League() {
    }

    public League(int id, int managerId, int courtId, String name, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.managerId = managerId;
        this.courtId = courtId;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
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

    public int getCourtId() {
        return courtId;
    }

    public void setCourtId(int courtId) {
        this.courtId = courtId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "League{" +
                "id=" + id +
                ", managerId=" + managerId +
                ", courtId=" + courtId +
                ", name='" + name + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
