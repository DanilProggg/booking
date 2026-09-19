package com.kridan.booking.service.payment.exceptions;

public class PaymentRejectedException extends PaymentException {
    private final String providerCode;

    public PaymentRejectedException(String message, String providerCode) {
        super(message, null);
        this.providerCode = providerCode;
    }

    public String getProviderCode() {
        return providerCode;
    }
}