package com.ticketservice.domain;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * The SeatHold object - represents the SeatHold entity.
 */
@Entity
@Table(name = "T_SEAT_HOLD_TRANSACTION")
public class SeatHold {

    @Id
    @GeneratedValue
    int seatHoldId;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "seatHold", fetch = FetchType.LAZY)
    @OrderBy("seatTransactionId DESC")
    List<SeatTransaction> seatTransactions;

    String customerEmail;
    String ConfirmationCode;
    Date seatHold_Ts;
    Date seatHold_expiry_Ts;
    Date ins_ts;
    Date reserved_Ts;


}
