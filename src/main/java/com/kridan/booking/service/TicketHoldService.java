package com.kridan.booking.service;

import com.kridan.booking.exceptions.SeatUnavailableException;
import com.kridan.booking.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class TicketHoldService {

    private final TicketRepository ticketRepository;

    @Transactional
    public void holdTicket(Long ticketId, Long eventId){
        Date dateNow = new Date();
        int result = ticketRepository.tryHold(
                ticketId,
                eventId,
                Date.from(Instant.now().plus(Duration.ofMinutes(15))),
                dateNow
        );

        if (result <= 0) throw new SeatUnavailableException(String.format("Seat %s is unavailable", ticketId));

    }
}
