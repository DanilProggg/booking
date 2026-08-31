package com.kridan.booking.service;

import com.kridan.booking.entity.Event;
import com.kridan.booking.exceptions.HallNotFoundException;
import com.kridan.booking.repository.EventRepository;
import com.kridan.booking.repository.VenueHallRepository;
import com.kridan.booking.service.dto.EventCreationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final VenueHallRepository venueHallRepository;

    private Event addEvent(EventCreationDto eventCreationDto){
        Event event = new Event(
                eventCreationDto.name(),
                eventCreationDto.description(),
                venueHallRepository.findById(eventCreationDto.venueHallId()).orElseThrow(
                        () -> new HallNotFoundException(String.format("Hall with ID %s not found", eventCreationDto.venueHallId()))
                ),
                eventCreationDto.date()
        );

        return eventRepository.save(event);
    }

    private List<Event> getEvents(){
        List<Event> events = eventRepository.findAll();
        return events;
    }
}
