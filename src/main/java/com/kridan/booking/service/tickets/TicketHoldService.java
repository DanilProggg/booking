package com.kridan.booking.service.tickets;

import com.kridan.booking.exceptions.SeatUnavailableException;
import com.kridan.booking.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketHoldService {

    private final TicketRepository ticketRepository;

    @Transactional
    public void holdTicket(List<Long> ticketIds, Long eventId){
        Date dateNow = new Date();
        int result = ticketRepository.tryHold(
                ticketIds,
                eventId,
                Date.from(Instant.now().plus(Duration.ofMinutes(15))),
                UUID.randomUUID().toString(),
                dateNow
        );

        if (result != ticketIds.size()) throw new SeatUnavailableException("Some tickets are unavailable");

    }
}
