package com.example.cinema_management.pricing;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PricingRepository extends JpaRepository<Pricing, Long> {

    List<Pricing> findByShowTimeId(Long showTimeId);
    Optional<Pricing> findByShowTimeIdAndSeatType(Long showTimeId, SeatType seatType);
    Page<Pricing> findByShowTimeId(Long showTimeId, Pageable pageable);

}
