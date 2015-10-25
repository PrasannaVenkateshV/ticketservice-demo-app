package com.ticketservice.util;

/**
 * Created by pvaradan on 10/24/15.
 */
public class TicketServiceUtil {

    public static boolean isValidSeatingLevelId(int levelId) {
        if(levelId > 0 && levelId <= Venue.values().length ) {
            return true;
        }
        return false;
    }
}
