package com.kridan.booking.service.orders;

import java.util.Date;
import java.util.UUID;

public record CheckoutResponse(
        UUID orderId,
        String confirmationUrl,
        Date expiresAt
) {
}