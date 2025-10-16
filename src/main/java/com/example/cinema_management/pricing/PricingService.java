package com.example.cinema_management.pricing;

import com.example.cinema_management.pricing.dto.PricingBulkRequest;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PricingService {
    private final PricingRepository repo;

    public PricingService(PricingRepository repo) {
        this.repo = repo;
    }

    public List<Pricing> listForScreen(Integer screenId) {
        return repo.findByScreenId(screenId, Pageable.unpaged()).getContent();
    }

    public Page<Pricing> pageAll(Integer screenId, SeatType seatType, Pageable pageable) {
        // both filters -> at most one row; wrap it into a Page
        if (screenId != null && seatType != null) {
            Optional<Pricing> opt = repo.findByScreenIdAndSeatType(screenId, seatType);
            if (opt.isPresent()) {
                return new PageImpl<>(List.of(opt.get()), pageable, 1);
            } else {
                return Page.empty(pageable);
            }
        }

        if (screenId != null) {
            return repo.findByScreenId(screenId, pageable);
        }

        if (seatType != null) {
            return repo.findBySeatType(seatType, pageable);
        }

        return repo.findAll(pageable);
    }

    public void upsertForScreen(PricingBulkRequest req) {
        // your existing upsert code here
    }

    public void deleteForScreenAndType(Integer screenId, SeatType seatType) {
        repo.findByScreenIdAndSeatType(screenId, seatType).ifPresent(repo::delete);
    }
}
