package com.ticketservice.service;

import com.ticketservice.dao.SeatHoldRepository;
import com.ticketservice.domain.SeatHold;
import com.ticketservice.domain.SeatTransaction;
import com.ticketservice.util.InsufficientSeatsException;
import com.ticketservice.util.TicketServiceUtil;
import com.ticketservice.util.Venue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Implementation of the TicketService
 */
@Service
public class TicketServiceImpl implements TicketService {


    //todo: implement logger in the application

    /**
     * the duration for which a seat Hold is active
     */
    //todo: move this to properties - this does not belong here
    private static final int seatOnHoldDuration = 60;

    @Autowired
    SeatHoldRepository seatHoldRepository;

    /**
     * Calculates the total available seats in a venue(optionally by level) by subtracting the seats which are onHold and reserved
     * @param venueLevel a numeric venue level identifier to limit the search
     * @return
     */
    public int numSeatsAvailable(Optional<Integer> venueLevel) {
        int venueLevelId = venueLevel.isPresent() ? venueLevel.get().intValue() : 0;
        if (TicketServiceUtil.isValidSeatingLevelId(venueLevelId)) {
            return Venue.getVenue(venueLevelId).getTotalSeats() - numOfSeatsOnHoldAndReservedByLevel(venueLevelId);
        }
        return Venue.getTotalNumberOfSeatsInVenue() - numOfSeatsOnHoldAndReservedByLevel(venueLevelId);
    }

    /**
     * finds and hold seats given the input parameters - holds seats with preference to the min level.
     * when minLevel and maxLevel is supplied - assumes the venue's min and Max
     * @param numSeats the number of seats to find and hold
     * @param minLevel the minimum venue level
     * @param maxLevel the maximum venue level
     * @param customerEmail unique identifier for the customer
     * @return
     * @throws InsufficientSeatsException
     */
    //todo: ugly use of synchronized here, this any way is not going to work in an clustered environment.
    // The solution is to create a table with Seat and level pre-populated(for every seat in venue) and add Seat expiry and seat reserved to the Seat Transaction table.
    // And use optimistic locking.
    public synchronized SeatHold findAndHoldSeats(int numSeats, Optional<Integer> minLevel, Optional<Integer> maxLevel, String customerEmail)
            throws InsufficientSeatsException {
        int minLevelId, maxLevelId = 0;
        minLevelId = minLevel.isPresent() ? minLevel.get().intValue() : 1;
        maxLevelId = maxLevel.isPresent() ? maxLevel.get().intValue() : 4;
        Map<Integer, Integer> seatsPerLevel  =  new HashMap<>();

        int numSeatsTaken = 0;
        for(int i = minLevelId; i <= maxLevelId; i++){
            int numSeatsAvailableInLevel = numSeatsAvailable(Optional.of(i));
            seatsPerLevel.put(i, numSeatsAvailableInLevel >= (numSeats - numSeatsTaken) ? (numSeats - numSeatsTaken) : numSeatsAvailableInLevel );
            numSeatsTaken += numSeatsAvailableInLevel;
            if(numSeatsTaken >= numSeats) {
                break;
            }
        }
        if(numSeatsTaken < numSeats) {
            throw new InsufficientSeatsException(numSeats + " seats are not available");
        }
        SeatHold seatHold =  new SeatHold();
        List<SeatTransaction> seatTransactions = new ArrayList<>();
        seatsPerLevel.forEach((k, v) -> {
            for(int i = 0 ; i < v; i ++) {
                seatTransactions.add(new SeatTransaction(k, seatHold));
            }
        });
        seatHold.setCustomerEmail(customerEmail);
        seatHold.setSeatHoldExpirationTimestamp(getSeatHoldExpiryTimestamp());
        seatHold.setSeatTransactions(seatTransactions);
        SeatHold persistedSeatHold = seatHoldRepository.save(seatHold);
        return persistedSeatHold;
    }

   public String reserveSeats(int seatHoldId, String customerEmail){
       SeatHold seatHold = seatHoldRepository.findOne(Long.valueOf(seatHoldId));
       if(seatHold == null) {
           return "not a valid seatHold ID";
       }
       if(seatHold.getSeatHoldExpirationTimestamp().before(new Date())){
           return "SeatHole expired please try again";
       }
       if(!seatHold.getCustomerEmail().equals(customerEmail)) {
          return "The seatHold ID and email address do not match";

       }
       seatHoldRepository.reserve(new Date(),seatHold.getSeatHoldId());
       return "reserved " + seatHoldId;
    }

   private int numOfSeatsOnHoldAndReservedByLevel(int levelId) {
       return seatHoldRepository.getCountOfSeatsOnHoldAndReserved(levelId);

   }

    private Date getSeatHoldExpiryTimestamp(){
        Calendar calendar = Calendar.getInstance(); // gets a calendar using the default time zone and locale.
        calendar.add(Calendar.SECOND, seatOnHoldDuration);
        return calendar.getTime();
    }

    public SeatHoldRepository getSeatHoldRepository() {
        return seatHoldRepository;
    }

    public void setSeatHoldRepository(SeatHoldRepository seatHoldRepository) {
        this.seatHoldRepository = seatHoldRepository;
    }

}


