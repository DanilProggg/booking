package com.kridan.booking.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = true)
    private String description;

    @OneToMany(
            mappedBy = "event",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<Ticket> tickets = new ArrayList<>();

    @ManyToOne
    private VenueHall venueHall;

    @Column(nullable = false)
    private Date date;

    public Event(String name, String description, VenueHall venueHall, List<VenueSeat> venueSeats, Date date) {
        this.name = name;
        this.description = description;
        this.venueHall = venueHall;
        this.date = date;
        this.tickets = venueSeats.stream()
                .map(venueSeat -> new Ticket(this, venueSeat))
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
