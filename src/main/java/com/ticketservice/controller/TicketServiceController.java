package com.ticketservice.controller;

import com.ticketservice.domain.SeatHold;
import com.ticketservice.service.TicketService;
import org.h2.jdbc.JdbcSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Created by pvaradan on 10/21/15.
 */
@RestController
@RequestMapping("/ticket-service/")
public class TicketServiceController {

    @Autowired
    TicketService ticketService;

    @RequestMapping("/availableSeats")
    public @ResponseBody Integer availableSeats(@RequestParam(value="level", required=false) Integer level) {
        //model.addAttribute("seats", ticketService.numSeatsAvailable(Optional.of(level)));
        return new Integer(ticketService.numSeatsAvailable(Optional.ofNullable(level)));
    }

    @RequestMapping("/holdSeats")
    public @ResponseBody SeatHold holdSeats(
            @RequestParam(value="numSeats", required=true) Integer numSeats,
            @RequestParam(value="minLevel", required=false) Integer minLevel,
            @RequestParam(value="maxLevel", required=false) Integer maxLevel,
            @RequestParam(value="email", required=true) String email) {
        return new SeatHold();
    }


    @RequestMapping("/reserve")
    public @ResponseBody String reserve(
            @RequestParam(value="seatHoldId", required=true) Integer seatHoldId) {
        return new String("reserved");
    }
}
