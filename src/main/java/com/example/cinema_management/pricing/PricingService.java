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

    public PricingService(PricingRepository repo) {
        this.repo = repo;
    }

    public List<Pricing> listForShowtime(Long showTimeId) {
        return repo.findByShowTimeId(showTimeId);
    }

    @Transactional
    public void upsertForShowtime(PricingShowtimeRequest r) {
        upsert(r.getShowTimeId(), SeatType.ADULT,   r.getAdultPrice());
        upsert(r.getShowTimeId(), SeatType.CHILD,   r.getChildPrice());
    }

    private void upsert(Long stId, SeatType type, BigDecimal price) {
        var row = repo.findByShowTimeIdAndSeatType(stId, type).orElseGet(() -> {
            var p = new Pricing();
            p.setShowTimeId(stId);
            p.setSeatType(type);
            return p;
        });
        row.setPrice(price);
        repo.save(row);
    }

    public void deleteForShowtimeAndType(Long stId, SeatType type) {
        repo.findByShowTimeIdAndSeatType(stId, type).ifPresent(repo::delete);
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

        if (seatType != null) {
            return repo.findBySeatType(seatType, pageable);
        }

        return repo.findAll(pageable);
    }
}
