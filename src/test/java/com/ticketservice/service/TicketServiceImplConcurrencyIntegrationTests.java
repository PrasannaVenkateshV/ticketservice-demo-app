package com.ticketservice.service;

import com.ticketservice.Application;
import com.ticketservice.dao.SeatHoldRepository;
import com.ticketservice.domain.SeatHold;
import com.ticketservice.domain.SeatTransaction;
import com.ticketservice.util.InsufficientSeatsException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
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
import java.util.concurrent.*;

/**
 * Creating a concurrency Tests just as a sanity
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
// @IntegrationTest("server.port:0")
// @WebIntegrationTest
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class})
public class TicketServiceImplConcurrencyIntegrationTests {


    @Autowired
    SeatHoldRepository seatHoldRepository;

    @Autowired
    TicketService ticketService;

    @Before
    public void setup() {
        seatHoldRepository.deleteAll();

    }


    // todo: move this to an util method, and not repeat in every test class
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



    /**
     * Number of concurrent users/threads to simulate in the test.
     */
    private int CONCURRENT_USERS = 10;

    @Test
    public void whenMultipleUsersHoldSeatsRaceCondition() {
        int seatingLevel = 1;
        int availableSeats;
        int seatsToBeTaken =  1200;
        //create max num of seats in a level and set it on hold.
        for(int i = 0; i < seatsToBeTaken; i ++) {
            seatHoldRepository.save(createSeatHoldForIntegrationTest(seatingLevel));
        }
        CountDownLatch beginSeatHoldSignal =  new CountDownLatch(CONCURRENT_USERS);
        CountDownLatch doneSeatHoldSignal =  new CountDownLatch(CONCURRENT_USERS);
        ExecutorService executor = new ThreadPoolExecutor(CONCURRENT_USERS, CONCURRENT_USERS, 0L, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(CONCURRENT_USERS));
        SeatHoldWorker[] tasks = new SeatHoldWorker[CONCURRENT_USERS];
        for(int i = 0; i < CONCURRENT_USERS; i++) {
            tasks[i] = new SeatHoldWorker(i, beginSeatHoldSignal, doneSeatHoldSignal, ticketService);
            executor.execute(tasks[i]);
        }
        try {
            doneSeatHoldSignal.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int numOfSeatAvailable =  ticketService.numSeatsAvailable(Optional.of(1));
        assert(numOfSeatAvailable ==  0);
    }

    /**
     * Seat Hold  worker
     * Seat Hold worker
     */

    class SeatHoldWorker  implements Runnable {

        private int id;
        private final CountDownLatch beginSeatHoldSignal;
        private final CountDownLatch doneSeatHoldSignal;
        private TicketService ticketService;
        public SeatHoldWorker(int id, CountDownLatch beginSeatHoldSignal, CountDownLatch doneSeatHoldSignal, TicketService ticketService) {
            this.id = id;
            this.beginSeatHoldSignal = beginSeatHoldSignal;
            this.doneSeatHoldSignal = doneSeatHoldSignal;
            this.ticketService = ticketService;
        }

        @Override
        public void run() {
            doSeatHold();
        }

        private void doSeatHold() {
            Thread curThread = Thread.currentThread();
            System.out.println(curThread.getName() + " currently executing the task " + id);

            beginSeatHoldSignal.countDown();
            try {
                beginSeatHoldSignal.await();
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
            try {

                SeatHold seatHold = ticketService.findAndHoldSeats(50, Optional.of(1), Optional.of(1), "abc" + id + "@test.com");
            } catch (InsufficientSeatsException e) {
                e.printStackTrace();
            }
            doneSeatHoldSignal.countDown();
        }

    }

    @After
    public void tearDown(){
        seatHoldRepository.deleteAll();
    }

}
