package com.example.cinema_management.pricing;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PricingRepository extends JpaRepository<Pricing, Long> {
    Optional<Pricing> findByNameIgnoreCase(String name);
    boolean existsByNameIgnoreCase(String name);
}
