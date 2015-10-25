package com.ticketservice.domain;

import javax.persistence.*;

/**
 * Seat Transaction
 */
@Entity
@Table(name = "T_SEAT_TRANSACTION")
public class SeatTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long seatTransactionId;

    @Column(nullable = false)
    Integer levelId;

    @JoinColumn(name = "seatHoldId")
    @ManyToOne(optional = false)
    SeatHold seatHold;

    public SeatTransaction(Integer levelId, SeatHold seatHold){
        this.levelId = levelId;
        this.seatHold = seatHold;
    }

    public SeatTransaction(){

    }

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
