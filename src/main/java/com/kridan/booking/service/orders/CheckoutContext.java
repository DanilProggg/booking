package com.kridan.booking.service.orders;

import com.kridan.booking.service.payment.PaymentRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record CheckoutContext(
        UUID orderId,
        BigDecimal amount,
        String customerEmail,
        UUID idempotencyKey,
        String eventName,
        List<PaymentRequest.ReceiptItem> lines
) {}