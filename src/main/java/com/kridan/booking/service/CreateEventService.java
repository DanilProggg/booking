package com.kridan.booking.service;

import com.kridan.booking.entity.Event;
import com.kridan.booking.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateEventService {
    private final EventRepository eventRepository;

    private void createEvent(){
        //seat`s creation


    }
}
