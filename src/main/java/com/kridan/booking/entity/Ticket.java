package com.kridan.booking.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String status; //FREE, HELD, PENDING, SOLD

    private String holdKey;  //Key to mark group of ticket in pending

    private Date holdExpiresAt; //Holding expiration time

    @Column(nullable = true)
    private int cost;  //Default Euro

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "usr_id", nullable = true)
    private User user;

    @ManyToOne
    private VenueSeat venueSeat;

    public Ticket(Event event, VenueSeat venueSeat) {
        this.event = event;
        this.venueSeat = venueSeat;
        this.status = "FREE";
    }
}
