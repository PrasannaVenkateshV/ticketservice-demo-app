package com.ticketservice.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Enum representing the Venue
 * It is better to create a lookup table with all the seats in a venue for easy management in real time.
 * keeping it as enum for faster development
 */


public enum Venue {

    ORCHESTRA (1, LevelTypes.ORCHESTRA, 100.00d, 25, 50),
    MAIN      (2, LevelTypes.MAIN,      75.00d,  20, 100),
    BALCONY1  (3, LevelTypes.BALCONY1,  50.00d,  15, 100),
    BALCONY2  (4, LevelTypes.BALCONY2,  40.00d,  15, 100);

    private final int levelId;
    private final LevelTypes levelName;
    private final double pricePerSeatInUSD;
    private final int rowsPerLevel;
    private final int seatsPerRow;
    private static int totalNumberOfSeatsInVenue;
    // Reverse-lookup map for getting a Venue by id
    private static final Map<Integer, Venue> lookup = new HashMap<>();

    static {
        for(Venue v: Venue.values()){
            lookup.put(v.getLevelId(), v);
            totalNumberOfSeatsInVenue +=  v.getTotalSeats();
        }
    }

    Venue(int levelId, LevelTypes levelName, double pricePerSeatInUSD, int rowsPerLevel, int seatsPerRow) {
        this.levelId = levelId;
        this.levelName = levelName;
        this.rowsPerLevel = rowsPerLevel;
        this.seatsPerRow = seatsPerRow;
        this.pricePerSeatInUSD = pricePerSeatInUSD;
    }

    public int getTotalSeats() {
        return rowsPerLevel*seatsPerRow;
    }



    public double getPricePerSeatInUSDByLevelName() {
        return pricePerSeatInUSD;
    }

    public double getTotalSeatsByLevelName(int levelId) {
        return pricePerSeatInUSD;
    }

    public int getLevelId() {
        return levelId;
    }

    public static Venue getVenue(int levelId) {
        return lookup.get(levelId);
    }

    public static int getTotalNumberOfSeatsInVenue() {
        return totalNumberOfSeatsInVenue;
    }

    public String getLevelName(){
        return levelName.getLevelType();
    }

}