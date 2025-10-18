package com.example.cinema_management.pricing;

import com.example.cinema_management.pricing.dto.PricingBulkRequest;
import com.example.cinema_management.pricing.dto.PricingShowtimeRequest;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class PricingService {
    private final PricingRepository repo;

    public PricingService(PricingRepository repo) { this.repo = repo; }

    public List<Pricing> listForShowtime(Long showTimeId) {
        return repo.findByShowTimeId(showTimeId);
    }

    public Page<Pricing> pageForShowtime(Long showTimeId, Pageable pageable) {
        return repo.findByShowTimeId(showTimeId, pageable);
    }

    @Transactional
    public void upsertTwoTypes(Long showTimeId, BigDecimal adult, BigDecimal child) {
        upsert(showTimeId, SeatType.ADULT, adult);
        upsert(showTimeId, SeatType.CHILD, child);
    }

    private void upsert(Long showTimeId, SeatType type, BigDecimal price) {
        var pricing = repo.findByShowTimeIdAndSeatType(showTimeId, type)
                .orElseGet(() -> {
                    var p = new Pricing();
                    p.setShowTimeId(showTimeId);
                    p.setSeatType(type);
                    return p;
                });
        pricing.setPrice(price);
        repo.save(pricing);
    }
}
