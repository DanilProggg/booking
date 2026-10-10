package com.kridan.booking.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "booking.hold")
public record HoldProperties(Duration selectionTtl, Duration paymentTtl) {

    public HoldProperties {
        if (selectionTtl == null) {
            selectionTtl = Duration.ofMinutes(15);
        }
        if (paymentTtl == null) {
            paymentTtl = Duration.ofMinutes(20);
        }
    }
}