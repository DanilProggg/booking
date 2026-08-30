package com.kridan.booking.entity;


import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class VenueSeat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String sector;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private int number;

    @ManyToOne
    private VenueHall venueHall;
}
