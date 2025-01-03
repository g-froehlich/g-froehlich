package com.techelevator.dao;

import com.techelevator.model.Court;

import java.util.List;

public interface CourtDao {

    List<Court> getCourts();

    Court getCourtById(int id);

    Court getCourtByAddress(String name);

    Court getCourtByName(String name);

    Court createCourt(Court court);

    Court updateCourt(Court court);

    int deleteCourtById(int id);
}
