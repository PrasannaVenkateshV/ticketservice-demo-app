package com.ticketservice.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ticketservice.domain.SeatHold;
import com.ticketservice.service.TicketService;
import com.ticketservice.util.TicketServiceUtil;
import org.springframework.beans.factory.annotation.Autowired;
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
    public @ResponseBody String availableSeats(@RequestParam(value="level", required=false) Integer level) {
        //model.addAttribute("seats", ticketService.numSeatsAvailable(Optional.of(level)));
        if(level == null || TicketServiceUtil.isValidSeatingLevelId(level)) {
            return "Available Seats: " + String.valueOf(ticketService.numSeatsAvailable(Optional.ofNullable(level)));
        }
        return level + " is not a valid seating level. Seating level should be between 1 and 4";
    }

    @RequestMapping("/holdSeats")
    public @ResponseBody String holdSeats(
            @RequestParam(value="numSeats", required=true) Integer numSeats,
            @RequestParam(value="minLevel", required=false) Integer minLevel,
            @RequestParam(value="maxLevel", required=false) Integer maxLevel,
            @RequestParam(value="email", required=true) String email) {
        if((minLevel == null || TicketServiceUtil.isValidSeatingLevelId(minLevel)) &&
                (maxLevel == null || TicketServiceUtil.isValidSeatingLevelId(maxLevel))) {

            try {
                SeatHold seatHold = ticketService.findAndHoldSeats(numSeats, Optional.ofNullable(minLevel), Optional.ofNullable(maxLevel), email);
                //ObjectMapper mapper = new ObjectMapper();
                //String seatHoldJson = mapper.writeValueAsString(seatHold);
                return "SeatHoldId: " + seatHold.getSeatHoldId().toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "not a valid seating level. seating level should be between 1 and 4";
    }


    @RequestMapping("/reserve")
    public @ResponseBody String reserve(
            @RequestParam(value="seatHoldId", required=true) Integer seatHoldId,
            @RequestParam(value="email", required=true) String email) {
        return ticketService.reserveSeats(seatHoldId, email);
    }
}
