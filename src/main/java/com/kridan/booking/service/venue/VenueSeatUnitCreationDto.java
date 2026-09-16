package com.kridan.booking.service.venue;

import jakarta.validation.constraints.NotBlank;

public record VenueSeatUnitCreationDto(
        int amount,
        @NotBlank String sector,
        @NotBlank String type
) {
}
