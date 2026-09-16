package com.kridan.booking.service.events;

import com.kridan.booking.service.tickets.TicketDto;

import java.util.Date;
import java.util.List;

public record EventDto(
        String name,
        String description,
        Long venueHallId,
        Date date,
        List<TicketDto> ticketDtos
) {
}
