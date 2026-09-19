package com.kridan.booking.service.events;

import com.kridan.booking.service.tickets.TicketDto;

import java.util.Date;
import java.util.List;

public record EventDto(
        Long id,
        String name,
        String publishStatus,
        String description,
        Long venueHallId,
        Date date,
        List<TicketDto> ticketDtos
) {
}
