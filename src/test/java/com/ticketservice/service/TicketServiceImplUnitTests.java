package com.ticketservice.service;

import com.ticketservice.dao.SeatHoldRepository;
import com.ticketservice.domain.Seat;
import com.ticketservice.domain.SeatHold;
import com.ticketservice.domain.SeatTransaction;
import com.ticketservice.util.InsufficientSeatsException;
import com.ticketservice.util.Venue;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by pvaradan on 10/25/15.
 */
public class TicketServiceImplUnitTests {

//    @Mock
//    private SeatHold seatHold;
//
//    @Mock
//    private SeatTransaction seatTransaction;

    @Mock
    private SeatHoldRepository seatHoldRepository;

    @InjectMocks
    TicketService ticketService = new TicketServiceImpl();;

    @Before
    public void setupMock() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void testNumSeatsAvailable(){
        int level = 1;
        int seatsOnHoldAndReserved = 2;
        when(seatHoldRepository.getCountOfSeatsOnHoldAndReserved(level)).thenReturn(seatsOnHoldAndReserved);
        int availableSeats =  ticketService.numSeatsAvailable(Optional.of(level));
        assert(availableSeats == Venue.getVenue(level).getTotalSeats() - seatsOnHoldAndReserved);
    }

    @Test
    public void testFindAndHoldSeats() {
        int numOfSeats = 2;
        String customerEmail = "abc@test.com";
        SeatHold seatHold = null;
        when(seatHoldRepository.save(any(SeatHold.class))).thenReturn(createSeatHoldForTest());
        try {
            seatHold = ticketService.findAndHoldSeats(numOfSeats, Optional.empty(), Optional.empty(), customerEmail);
        } catch (InsufficientSeatsException e) {
            e.printStackTrace();
        }
        assertNotNull(seatHold);
    }

    //todo: mock findOne and run the test
    public void testReserveSeats() {
        SeatHold seatHold = createSeatHoldForTest();
        when(seatHoldRepository.reserve(any(Date.class), any(Long.class))).thenReturn(1);
        String response = ticketService.reserveSeats(seatHold.getSeatHoldId().intValue(), seatHold.getCustomerEmail());
        assertEquals(response, "reserved "+ seatHold.getSeatHoldId());
    }

    private SeatHold createSeatHoldForTest(){
        SeatHold seatHold =  new SeatHold();
        List<SeatTransaction>  seatTransactionList =  new ArrayList<>();
        SeatTransaction seatTransaction1 = new SeatTransaction(1, seatHold);
        seatTransaction1.setSeatTransactionId(1L);
        SeatTransaction seatTransaction2 = new SeatTransaction(1, seatHold);
        seatTransaction2.setSeatTransactionId(2L);
        seatTransactionList.add(seatTransaction1);
        seatTransactionList.add(seatTransaction2);
        seatHold.setSeatTransactions(seatTransactionList);
        seatHold.setSeatHoldExpirationTimestamp(new Date());
        seatHold.setSeatHoldId(1L);
        return seatHold;
    }

}
