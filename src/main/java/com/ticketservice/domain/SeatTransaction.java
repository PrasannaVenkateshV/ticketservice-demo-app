package com.ticketservice.domain;

import com.ticketservice.dao.SeatHoldRepository;
import com.ticketservice.util.InsufficientSeatsException;
import com.ticketservice.util.Venue;
import org.hibernate.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.util.Date;

/**
 * Seat Transaction entity
 */
@Entity
@Table(name = "T_SEAT_TRANSACTION")
public class SeatTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long seatTransactionId;

    @Column(nullable = false)
    // todo: this field need to be annotated with index
    Integer levelId;

    /**
     * join column with SeatHold.
     */
    @JoinColumn(name = "seatHoldId")
    @ManyToOne(optional = false)
    // todo: this field need to be annotated with index
    SeatHold seatHold;

    public SeatTransaction(Integer levelId, SeatHold seatHold){
        this.levelId = levelId;
        this.seatHold = seatHold;
    }

    public SeatTransaction(){}

    public Long getSeatTransactionId() {
        return seatTransactionId;
    }

    public void setSeatTransactionId(Long seatTransactionId) {
        this.seatTransactionId = seatTransactionId;
    }

    public Integer getLevelId() {
        return levelId;
    }

    public void setLevelId(Integer levelType) {
        this.levelId = levelType;
    }

    public SeatHold getSeatHold() {
        return seatHold;
    }

    public void setSeatHold(SeatHold seatHold) {
        this.seatHold = seatHold;
    }
}
