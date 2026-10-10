package com.kridan.booking.repository;

import com.kridan.booking.entity.Order;
import com.kridan.booking.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
        update Ticket t
        set t.status = 'HELD',
            t.order = :order
        where t.id in :ticketIds
          and t.event.id = :eventId
          and (t.status = 'FREE'
               or (t.status = 'HELD'
                   and t.order.id in (select o.id from Order o where o.expiresAt < :now)))
        """)
    int tryHold(@Param("ticketIds") List<Long> ticketIds,
                @Param("eventId") Long eventId,
                @Param("order") Order order,
                @Param("now") Date now);

    @Query("""
        SELECT COALESCE(SUM(t.cost), 0)
        FROM Ticket t
        WHERE t.order.id = :orderId
        """)
    int totalCostByOrderId(
            @Param("orderId") UUID orderId
    );

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
        update Ticket t
        set t.status = 'PENDING'
        where t.order.id = :orderId
          and t.status = 'HELD'
        """)
    int moveToPending(@Param("orderId") UUID orderId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
        update Ticket t
        set t.status = 'SOLD'
        where t.order.id = :orderId
          and t.status = 'PENDING'
        """)
    int markSold(@Param("orderId") UUID orderId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
        update Ticket t
        set t.status = 'FREE',
            t.order = null
        where t.order.id = :orderId
          and t.status = 'PENDING'
        """)
    int releasePending(@Param("orderId") UUID orderId);
}
