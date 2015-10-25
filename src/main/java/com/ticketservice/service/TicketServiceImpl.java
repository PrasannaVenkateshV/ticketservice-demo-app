package com.ticketservice.service;

import com.ticketservice.domain.Seat;
import com.ticketservice.domain.SeatHold;
import com.ticketservice.util.TicketServiceUtil;
import com.ticketservice.util.Venue;
import org.h2.jdbc.JdbcSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Implementation of the TicketService
 */
@Service
public class TicketServiceImpl implements TicketService {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public int numSeatsAvailable(Optional<Integer> venueLevel) {
        int venueLevelId = 0 ;
        if(venueLevel.isPresent()){
            venueLevelId = venueLevel.get().intValue();
        }
        if (TicketServiceUtil.isValidSeatingLevelId(venueLevelId)) {
            return Venue.get(venueLevelId).getTotalSeats() - numOfSeatsOnHoldAndReservedByLevel(venueLevelId);
        }
        return Venue.getTotalNumberOfSeatsInVenue() - numOfSeatsOnHoldAndReservedByLevel(venueLevelId);
    }

    public SeatHold findAndHoldSeats(int numSeats, Optional<Integer> minLevel, Optional<Integer> maxLevel, String customerEmail){
        //return jdbcTemplate.queryForList("SELECT * FROM seat").size();
        return  new SeatHold();
    }

   public String reserveSeats(int seatHoldId, String customerEmail){
        return "reserved";
    }

   private int numOfSeatsOnHoldAndReservedByLevel(int levelId) {
       if(TicketServiceUtil.isValidSeatingLevelId(levelId)) {
           return 100;
       }
       return 200;
   }

}
