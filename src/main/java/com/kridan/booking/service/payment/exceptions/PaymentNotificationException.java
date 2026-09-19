package com.kridan.booking.service.payment.exceptions;

public class PaymentNotificationException extends PaymentException {
    public PaymentNotificationException(String message, Throwable cause) {
        super(message, cause);
    }
}