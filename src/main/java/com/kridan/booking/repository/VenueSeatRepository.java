package com.kridan.booking.repository;

import com.kridan.booking.entity.VenueHall;
import com.kridan.booking.entity.VenueSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VenueSeatRepository extends JpaRepository<VenueSeat, Long> {

    List<VenueSeat> findByVenueHallId (Long venueHallId);
}
