package com.kridan.booking.service.events.exceptions;

public class EventNotFindException extends RuntimeException {
    public EventNotFindException(String message) {
        super(message);
    }
}
