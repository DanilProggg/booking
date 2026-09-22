package com.kridan.booking.service.venue;

import java.util.List;

public record VenueHallCreationDto(
        String name,
        String description,
        List<VenueSeatUnitCreationDto> venueSeatList
) {
}


