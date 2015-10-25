package com.ticketservice.util;

/**
 * Created by pvaradan on 10/24/15.
 */
public enum LevelTypes {
    ORCHESTRA("ORCHESTRA"), MAIN("MAIN"), BALCONY1("BALCONY1"), BALCONY2("BALCONY2");
     private String levelType;

    LevelTypes(String levelType){
        this.levelType = levelType;
    }

    public String getLevelType() {
        return levelType;
    }
}
