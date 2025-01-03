package com.techelevator.model;

import javax.validation.constraints.NotBlank;

/**
 * Model class representing a court.
 * <p>
 * Contains information about the court - id, address, and name.
 */
public class Court {
    private int id;
    @NotBlank(message = "The field `address` should not be blank.")
    private String address;
    @NotBlank(message = "The field `name` should not be blank.")
    private String name;

    public Court() {
    }

    public Court(int id, String address, String name) {
        this.id = id;
        this.address = address;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Court{" +
                "id=" + id +
                ", address='" + address + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
