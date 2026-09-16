package com.kridan.booking.service.venue;

public class HallNotFoundException extends RuntimeException {
    public HallNotFoundException(String message) {
        super(message);
    }
}
