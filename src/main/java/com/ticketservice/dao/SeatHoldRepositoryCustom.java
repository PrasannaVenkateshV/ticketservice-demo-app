package com.ticketservice.dao;

import com.ticketservice.domain.SeatHold;

import java.util.Date;

/**
 * Created by pvaradan on 10/24/15.
 */
public interface SeatHoldRepositoryCustom {

    public int getCountOfSeatsOnHoldAndReserved(int level);
    public int reserve(Date reservedTimestamp, Long seatHoldId);
    //public SeatHold save(SeatHold seatHold);

}
