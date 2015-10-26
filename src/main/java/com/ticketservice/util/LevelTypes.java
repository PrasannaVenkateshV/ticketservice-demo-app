package com.ticketservice.util;

/**
 * level types enum
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
