package com.kridan.booking.controller;

import com.kridan.booking.controller.dto.UserResponse;
import com.kridan.booking.entity.VenueHall;
import com.kridan.booking.service.VenueHallService;
import com.kridan.booking.service.dto.VenueHallCreationDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/venuehalls")
public class VenueHallController {
    private final VenueHallService venueHallService;

    @PostMapping()
    private ResponseEntity<?> addVenueHall(@RequestBody VenueHallCreationDto venueHallCreationDto){
        try {
            log.debug("Venuehall POST request called");
            VenueHall venueHall = venueHallService.addHall(venueHallCreationDto);
            return ResponseEntity
                    .created(URI.create("/api/users/" + venueHall.getId()))
                    .body(venueHall);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return ResponseEntity.badRequest().body("Data are invalid");
        }
    }
}
