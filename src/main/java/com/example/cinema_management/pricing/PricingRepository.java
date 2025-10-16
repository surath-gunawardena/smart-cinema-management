package com.example.cinema_management.pricing;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PricingRepository extends JpaRepository<Pricing, Long> {

    Page<Pricing> findAll(Pageable pageable);

    Page<Pricing> findByScreenId(Integer screenId, Pageable pageable);

    Page<Pricing> findBySeatType(SeatType seatType, Pageable pageable);

    Optional<Pricing> findByScreenIdAndSeatType(Integer screenId, SeatType seatType);
}
