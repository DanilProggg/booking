package com.kridan.booking.repository;

import com.kridan.booking.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
            update Ticket t
            set t.status = 'HELD',
                t.holdExpiresAt = :expiresAt
            where t.id = :ticketId
              and t.event.id = :eventId
              and (t.status = 'FREE'
                   or (t.status = 'HELD' and t.holdExpiresAt < :now))
            """)
    int tryHold(@Param("ticketId") Long ticketId,
                @Param("eventId") Long eventId,
                @Param("expiresAt") Date expiresAt,
                @Param("now") Date now);
}
