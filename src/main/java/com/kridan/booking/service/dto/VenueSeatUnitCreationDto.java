package com.kridan.booking.service.dto;

import jakarta.validation.constraints.NotBlank;

public record VenueSeatUnitCreationDto(
        int amount,
        @NotBlank String sector,
        @NotBlank String type
) {
}
