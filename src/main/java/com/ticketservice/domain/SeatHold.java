package com.ticketservice.domain;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * The SeatHold object - represents the SeatHold entity.
 */
@Entity
@Table(name = "T_SEAT_HOLD")
public class SeatHold {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seatHoldId;

    /**
     * SeatTransaction
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "seatHold", fetch = FetchType.EAGER)
    @OrderBy("seatTransactionId DESC")
    private List<SeatTransaction> seatTransactions;

    @Column(nullable = false)
    private String customerEmail;

    @Column(nullable = false)
    private Date seatHoldExpirationTimestamp;

    @Column
    private Date insertTimeStamp;

    @Column
    private Date reservedTimestamp;

    @PrePersist
    private void onCreate() {
        if (insertTimeStamp == null) {
            insertTimeStamp = new Date();
        }
    }

    public Long getSeatHoldId() {
        return seatHoldId;
    }

    public void setSeatHoldId(Long seatHoldId) {
        this.seatHoldId = seatHoldId;
    }

    public List<SeatTransaction> getSeatTransactions() {
        return seatTransactions;
    }

    public void setSeatTransactions(List<SeatTransaction> seatTransactions) {
        this.seatTransactions = seatTransactions;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public Date getSeatHoldExpirationTimestamp() {
        return seatHoldExpirationTimestamp;
    }

    public void setSeatHoldExpirationTimestamp(Date seatHoldExpirationTimestamp) {
        this.seatHoldExpirationTimestamp = seatHoldExpirationTimestamp;
    }

    public Date getInsertTimeStamp() {
        return insertTimeStamp;
    }

    public void setInsertTimeStamp(Date insertTimeStamp) {
        this.insertTimeStamp = insertTimeStamp;
    }

    public Date getReservedTimestamp() {
        return reservedTimestamp;
    }

    public void setReservedTimestamp(Date reservedTimestamp) {
        this.reservedTimestamp = reservedTimestamp;
    }



}
