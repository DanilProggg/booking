package com.kridan.booking.service.dto;

import java.util.Date;

public record EventCreationDto(
        String name,
        String description,
        Long venueHallId,
        Date date
) {
}
