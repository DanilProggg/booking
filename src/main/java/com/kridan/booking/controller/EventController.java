package com.kridan.booking.controller;

import com.kridan.booking.service.events.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/events")
public class EventController {
    private final EventService eventService;


    @PostMapping()
    public ResponseEntity<?> createEvent(@RequestBody EventCreationDto eventCreationDto){
        try {
            EventDto eventDto = eventService.addEvent(eventCreationDto);
            return ResponseEntity.ok(eventDto);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return ResponseEntity.internalServerError().body("Error has been occurred");
        }
    }

    @GetMapping()
    public ResponseEntity<?> getEvents(){
        try {
            List<EventDto> eventDtos = eventService.getEvents();
            return ResponseEntity.ok(eventDtos);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return ResponseEntity.internalServerError().body("Error has been occurred");
        }
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<?> getEventById(@PathVariable Long eventId){
        try {
            EventDto eventDto = eventService.getEventById(eventId);
            return ResponseEntity.ok(eventDto);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return ResponseEntity.internalServerError().body("Error has been occurred");
        }
    }


    @PatchMapping("/{eventId}")
    public ResponseEntity<?> setCost(@PathVariable Long eventId, @RequestBody List<CostPatchUnit> costPatchUnits){
        try {
            eventService.changeCostInEvent(eventId, costPatchUnits);
            return ResponseEntity.noContent().build();
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return ResponseEntity.internalServerError().body("Error has been occurred");
        }
    }
}
