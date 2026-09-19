package com.kridan.booking.service.payment;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record PaymentRequest(
        UUID orderId,
        String orderNumber,
        BigDecimal amount,
        String customerEmail,
        List<ReceiptItem> items,
        String returnUrl,
        UUID idempotencyKey) {

    public record ReceiptItem(String description, int quantity, BigDecimal price) {}
}