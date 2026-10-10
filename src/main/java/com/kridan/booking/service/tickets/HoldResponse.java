package com.kridan.booking.service.tickets;

import java.util.Date;
import java.util.UUID;

public record HoldResponse(
        UUID orderId,
        Date expiresAt
) {
}
