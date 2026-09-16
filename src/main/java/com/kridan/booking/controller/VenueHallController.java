package com.kridan.booking.controller;

import com.kridan.booking.entity.VenueHall;
import com.kridan.booking.service.venue.VenueHallService;
import com.kridan.booking.service.venue.VenueHallCreationDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/venuehalls")
public class VenueHallController {
    private final VenueHallService venueHallService;

    @PostMapping()
    public ResponseEntity<?> addVenueHall(@RequestBody VenueHallCreationDto venueHallCreationDto){
        try {
            log.debug("Venuehalls POST request called");
            VenueHall venueHall = venueHallService.addHall(venueHallCreationDto);
            return ResponseEntity
                    .created(URI.create("/api/users/" + venueHall.getId()))
                    .body(venueHall);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return ResponseEntity.badRequest().body("Data are invalid");
        }
    }

    @GetMapping()
    public ResponseEntity<?> getVenueHalls(){
        try {
            log.debug("Venuehalls GET request called");
            List<VenueHall> venueHallList = venueHallService.getHalls();
            return ResponseEntity.ok(venueHallList);
        } catch (Exception ex){
            log.error(ex.getMessage());
            return ResponseEntity.internalServerError().body(ex.getMessage());
        }
    }
}
