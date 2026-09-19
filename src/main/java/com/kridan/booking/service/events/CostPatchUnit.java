package com.kridan.booking.service.events;

public record CostPatchUnit(
        int cost,
        String sector,
        String type
){}