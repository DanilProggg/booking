package com.kridan.booking.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Setter
@Getter
public class VenueSeat {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private String sector;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private int number;

    @ManyToOne
    private VenueHall venueHall;

    public VenueSeat(String sector, String type, int number, VenueHall venueHall) {
        this.sector = sector;
        this.type = type;
        this.number = number;
        this.venueHall = venueHall;
    }
}
