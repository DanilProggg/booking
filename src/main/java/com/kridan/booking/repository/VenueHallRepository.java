package com.kridan.booking.repository;

import com.kridan.booking.entity.VenueHall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VenueHallRepository extends JpaRepository<VenueHall, Long> {
}
