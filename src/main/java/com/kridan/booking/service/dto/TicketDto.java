package com.kridan.booking.service.dto;

public record TicketDto(
        Integer cost,
        Long eventId,
        Long userId,
        Long venueSeatId,
        String sector,
        String type,
        int number
        ) {
}
