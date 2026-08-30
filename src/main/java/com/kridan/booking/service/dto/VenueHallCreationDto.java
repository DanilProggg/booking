package com.kridan.booking.service.dto;

import java.util.List;

public record VenueHallCreationDto(
        String name,
        String description,
        List<VenueSeatUnitCreationDto> venueSeatUnitCreationDtoList
) {
}


