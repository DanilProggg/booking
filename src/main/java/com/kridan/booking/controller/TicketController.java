package com.kridan.booking.controller;

import com.kridan.booking.entity.AppUserDetails;
import com.kridan.booking.exceptions.SeatUnavailableException;
import com.kridan.booking.service.tickets.HoldResponse;
import com.kridan.booking.service.tickets.HoldTicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class TicketController {

    private final HoldTicketService holdTicketService;

    @PostMapping("/events/{eventId}/tickets/hold")
    public ResponseEntity<?> holdSeat(@PathVariable Long eventId,
                                      @RequestBody List<Long> ticketId,
                                      @AuthenticationPrincipal AppUserDetails principal){
        try {
            HoldResponse holdResponse = holdTicketService.holdTicket(ticketId, eventId, principal.getId());
            return ResponseEntity.ok(holdResponse);
        } catch (SeatUnavailableException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Ticket unavailable");
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return ResponseEntity.internalServerError().body("Error has been occurred");
        }
    }
}
