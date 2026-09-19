package com.kridan.booking.service.payment.exceptions;

public class PaymentException extends RuntimeException {
    public PaymentException(String message, Throwable cause) {
        super(message);
    }
}
