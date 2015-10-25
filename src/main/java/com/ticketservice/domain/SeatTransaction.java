package com.ticketservice.domain;

import com.ticketservice.util.LevelTypes;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by pvaradan on 10/19/15.
 */
@Entity
@Table(name = "T_SEAT_TRANSACTION")
public class SeatTransaction {

    Long seatTransactionId;
    String levelType;
    SeatHold seatHold;
    Boolean onHold;
    Boolean Reserved;
}
