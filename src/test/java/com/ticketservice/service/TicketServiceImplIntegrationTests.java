package com.ticketservice.service;

import com.ticketservice.Application;
import com.ticketservice.dao.SeatHoldRepository;
import com.ticketservice.domain.SeatHold;
import com.ticketservice.domain.SeatTransaction;
import com.ticketservice.util.InsufficientSeatsException;
import com.ticketservice.util.Venue;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.SpringApplicationContextLoader;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

/**
 * TicketServiceImplIntegrationTests
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
// @IntegrationTest("server.port:0")
// @WebIntegrationTest
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class})
public class TicketServiceImplIntegrationTests {



    @Autowired
    SeatHoldRepository seatHoldRepository;

    @Autowired
    TicketService ticketService;

    SeatHold seatHold;
    SeatTransaction seatTransaction;

    @Before
    public void setup() {
        seatHoldRepository.deleteAll();

    }

    @Test
    public void numSeatsAvailableWhenSomeSeatsAreTaken() {
        int seatingLevel = 1;
        int availableSeats;
        int seatsToBeTaken =  10;
        //create max num of seats in a level and set it on hold.
        for(int i = 0; i < seatsToBeTaken; i ++) {
            seatHoldRepository.save(createSeatHoldForIntegrationTest(seatingLevel));
        }
        availableSeats = ticketService.numSeatsAvailable(Optional.of(1));
        assert(availableSeats == Venue.getVenue(1).getTotalSeats() - seatsToBeTaken);
    }

    @Test
    public void holdMoreThanAvailableSeats() {
        int seatingLevel = 1;
        int availableSeats;
        int seatsToBeTaken =  100;
        SeatHold seatHold = null;
        //create max num of seats in a level and set it on hold.
        for(int i = 0; i < seatsToBeTaken; i ++) {
            seatHoldRepository.save(createSeatHoldForIntegrationTest(seatingLevel));
        }
        try {
            seatHold = ticketService.findAndHoldSeats(1151, Optional.of(1), Optional.of(1),"abcd@123.com");
        } catch (Exception e) {
            assert(e instanceof InsufficientSeatsException);
            assert(e.getMessage().equals("1151 seats are not available"));
            assert (seatHold == null);
        }
    }

    @Test
    public void reserveASeatWithWrongSeatHoldId() {
        int seatingLevel = 1;
        int availableSeats;
        int seatsToBeTaken =  100;
        SeatHold seatHold = null;
        //create max num of seats in a level and set it on hold.
        for(int i = 0; i < seatsToBeTaken; i ++) {
            seatHoldRepository.save(createSeatHoldForIntegrationTest(seatingLevel));
        }
        try {
            seatHold = ticketService.findAndHoldSeats(1151, Optional.of(1), Optional.of(1),"abcd@123.com");
        } catch (Exception e) {
            assert(e instanceof InsufficientSeatsException);
            assert(e.getMessage().equals("1151 seats are not available"));
            assert (seatHold == null);
        }
    }
    //todo: repeat the above test for all possible scenarios of reserveSeats.

    private SeatHold createSeatHoldForIntegrationTest(int seatingLevel){
        SeatHold seatHold =  new SeatHold();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, 600);
        List<SeatTransaction> seatTransactionList =  new ArrayList<>();
        SeatTransaction seatTransaction1 = new SeatTransaction(seatingLevel, seatHold);
        seatTransactionList.add(seatTransaction1);
        seatHold.setSeatTransactions(seatTransactionList);
        seatHold.setSeatHoldExpirationTimestamp(calendar.getTime());
        seatHold.setCustomerEmail("abc@123.com");
        return seatHold;
    }

    @After
    public void tearDown(){
        seatHoldRepository.deleteAll();
    }


}
