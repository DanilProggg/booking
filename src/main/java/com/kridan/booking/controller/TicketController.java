package com.kridan.booking.controller;

import com.kridan.booking.exceptions.SeatUnavailableException;
import com.kridan.booking.service.TicketHoldService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class TicketController {

    private final TicketHoldService ticketHoldService;

    @PostMapping("/events/{eventId}/tickets/{ticketId}/hold")
    public ResponseEntity<?> holdSeat(@PathVariable Long eventId, @PathVariable Long ticketId){
        try {
            ticketHoldService.holdTicket(ticketId, eventId);
            return ResponseEntity.noContent().build();
        } catch (SeatUnavailableException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Ticket unavailable ");
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return ResponseEntity.internalServerError().body("Error has been occurred");
        }
    }
}
