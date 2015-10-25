package com.ticketservice.domain;

import java.util.Date;

/**
 * Created by pvaradan on 10/7/15.
 */
public class Seat {


    private int seatID;
    private int levelId;
    private String levelName;
    private int price;

    public Seat(int seatId, int levelId, String levelName){
        this.seatID = seatId;
        this.levelId = levelId;
        this.levelName = levelName;
        this.price = 100;
    }

    public int getSeatID() {
        return seatID;
    }

    public void setSeatID(int seatID) {
        this.seatID = seatID;
    }

    public int getLevelId() {
        return levelId;
    }

    public void setLevelId(int levelId) {
        this.levelId = levelId;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return String.format(
                "Seat[seatId=%d, levelId='%d', levelName='%s']",
                seatID, levelId, levelName);
    }

}
