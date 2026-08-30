package com.kridan.booking.repository;

import com.kridan.booking.entity.VenueSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VenueSeatRepository extends JpaRepository<VenueSeat, Long> {
}
