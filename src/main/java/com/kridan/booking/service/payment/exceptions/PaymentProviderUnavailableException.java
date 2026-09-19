package com.kridan.booking.service.payment.exceptions;

public class PaymentProviderUnavailableException extends PaymentException {
    public PaymentProviderUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
