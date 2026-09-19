package com.kridan.booking.repository;

import com.kridan.booking.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
            update Ticket t
            set t.cost = :cost
            where t.event.id = :eventId
            and t.status = 'FREE'
            and t.venueSeat.sector = :sector
            and t.venueSeat.type = :type
            """)
    int updateCost(@Param("cost") int newCost,
                   @Param("sector") String sector,
                   @Param("type") String type,
                   @Param("eventId") Long eventId
                   );
}
