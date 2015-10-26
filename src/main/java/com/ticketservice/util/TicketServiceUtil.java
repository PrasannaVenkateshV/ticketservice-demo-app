package com.ticketservice.util;

/**
 * Utility class for ticket service
 */
public class TicketServiceUtil {

    /**
     * returns if a seating level is valid or not
     * @param levelId
     * @return
     */
    public static boolean isValidSeatingLevelId(int levelId) {
        if(levelId > 0 && levelId <= Venue.values().length ) {
            return true;
        }
        return false;
    }
}
