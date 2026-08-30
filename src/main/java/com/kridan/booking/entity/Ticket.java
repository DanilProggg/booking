package com.kridan.booking.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String type; //usual- vip

    @Column(nullable = false)
    private String sector; //sitting - table - table

    @Column(nullable = false)
    private int number;

    @Column(nullable = false)
    private int cost;  //Default Euro

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "usr_id", nullable = true)
    private User user;

    @ManyToOne
    private VenueSeat venueSeat;
}
