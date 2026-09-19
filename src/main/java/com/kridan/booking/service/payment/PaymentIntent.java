package com.kridan.booking.service.payment;

public record PaymentIntent(
        String externalPaymentId,
        ConfirmationType type,
        String value) {

    public enum ConfirmationType { REDIRECT_URL, EMBEDDED_TOKEN }
}
