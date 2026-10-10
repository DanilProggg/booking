package com.kridan.booking.service.tickets;

import com.kridan.booking.entity.Event;
import com.kridan.booking.entity.Order;
import com.kridan.booking.entity.User;
import com.kridan.booking.exceptions.SeatUnavailableException;
import com.kridan.booking.repository.EventRepository;
import com.kridan.booking.repository.OrderRepository;
import com.kridan.booking.repository.TicketRepository;
import com.kridan.booking.repository.UserRepository;
import com.kridan.booking.service.events.exceptions.EventNotFoundException;
import com.kridan.booking.service.users.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HoldTicketService {

    private final TicketRepository ticketRepository;
    private final OrderRepository orderRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    private static final Duration HOLD_TTL = Duration.ofMinutes(15);

    @Transactional
    public HoldResponse holdTicket(List<Long> ticketIds, Long eventId, Long userId){
        Date now = new Date();

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Order order = orderRepository.save(
                new Order(event, Date.from(now.toInstant().plus(HOLD_TTL)), user)
        );

        // Hold tickets while order (15 min)
        int result = ticketRepository.tryHold(
                ticketIds,
                eventId,
                order,
                now
        );

        if (result != ticketIds.size()) throw new SeatUnavailableException("Some seats are unavailable");

        return new HoldResponse(order.getId(), order.getExpiresAt());

    }
}
