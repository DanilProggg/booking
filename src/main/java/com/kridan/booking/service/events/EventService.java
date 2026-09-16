package com.kridan.booking.service.events;

import com.kridan.booking.entity.Event;
import com.kridan.booking.entity.VenueHall;
import com.kridan.booking.service.venue.HallNotFoundException;
import com.kridan.booking.repository.EventRepository;
import com.kridan.booking.repository.VenueHallRepository;
import com.kridan.booking.repository.VenueSeatRepository;
import com.kridan.booking.service.tickets.TicketDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final VenueHallRepository venueHallRepository;
    private final VenueSeatRepository venueSeatRepository;

    public EventDto addEvent(EventCreationDto eventCreationDto){
        VenueHall venueHall = venueHallRepository.findById(eventCreationDto.venueHallId()).orElseThrow(
                () -> new HallNotFoundException(String.format("Hall with ID %s not found", eventCreationDto.venueHallId())));

        Event event = new Event(
                eventCreationDto.name(),
                eventCreationDto.description(),
                venueHall,
                venueSeatRepository.findByVenueHallId(venueHall.getId()),
                eventCreationDto.date()
        );

        eventRepository.save(event);

        EventDto eventDto = new EventDto(
                event.getName(),
                event.getDescription(),
                event.getVenueHall().getId(),
                event.getDate(),

                event.getTickets().stream().map(
                        ticket -> new TicketDto(
                                ticket.getCost(),
                                ticket.getEvent().getId(),
                                ticket.getUser() == null ? null : ticket.getUser().getId(), //ticker without user at start
                                ticket.getVenueSeat().getId(),
                                ticket.getVenueSeat().getSector(),
                                ticket.getVenueSeat().getType(),
                                ticket.getVenueSeat().getNumber()
                        )
                ).toList()
        );

        return eventDto;

    }

    public List<EventDto> getEvents(){
        List<Event> events = eventRepository.findAll();

        List<EventDto> eventDtos = events.stream()
                .map(
                        event -> new EventDto(
                                event.getName(),
                                event.getDescription(),
                                event.getVenueHall().getId(),
                                event.getDate(),

                                event.getTickets().stream().map(
                                        ticket -> new TicketDto(
                                                ticket.getCost(),
                                                ticket.getEvent().getId(),
                                                ticket.getUser() == null ? null : ticket.getUser().getId(), //ticker without user at start
                                                ticket.getVenueSeat().getId(),
                                                ticket.getVenueSeat().getSector(),
                                                ticket.getVenueSeat().getType(),
                                                ticket.getVenueSeat().getNumber()
                                        )
                                ).toList()
                        )
                ).toList();

        return eventDtos;

    }
}
