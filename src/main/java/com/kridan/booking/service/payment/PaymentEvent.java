package com.kridan.booking.service.payment;

import java.util.UUID;

public record PaymentEvent(
        String externalPaymentId,
        UUID orderId,
        PaymentState state
) {}
