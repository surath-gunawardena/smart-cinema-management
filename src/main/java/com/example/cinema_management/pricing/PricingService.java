package com.example.cinema_management.pricing;

import com.example.cinema_management.pricing.dto.PricingCreateRequest;
import com.example.cinema_management.pricing.dto.PricingTypeUpdateRequest;
import com.example.cinema_management.pricing.dto.PricingUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.EnumSet;
import java.util.List;

@Service
public class PricingService {

    private final PricingRepository pricingRepo;
    private final PricingTypeRepository typeRepo;

    public PricingService(PricingRepository pricingRepo, PricingTypeRepository typeRepo) {
        this.pricingRepo = pricingRepo;
        this.typeRepo = typeRepo;
    }

    public Page<Pricing> page(Pageable pageable) {
        return pricingRepo.findAll(pageable);
    }

    public Pricing getOrThrow(Long id) {
        return pricingRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Pricing not found: " + id));
    }

    @Transactional
    public Long create(PricingCreateRequest req) {
        if (pricingRepo.existsByNameIgnoreCase(req.name)) {
            throw new IllegalArgumentException("Pricing name already exists");
        }

        Pricing p = new Pricing();
        p.setName(req.name.trim());
        Pricing saved = pricingRepo.save(p);

        // Ensure only ADULT/CHILD exist; upsert two rows
        upsertType(saved.getId(), SeatType.ADULT, req.prices.getOrDefault(SeatType.ADULT, BigDecimal.ZERO));
        upsertType(saved.getId(), SeatType.CHILD, req.prices.getOrDefault(SeatType.CHILD, BigDecimal.ZERO));

        return saved.getId();
    }

    @Transactional
    public void update(Long id, PricingUpdateRequest req) {
        Pricing p = getOrThrow(id);
        // keep unique
        pricingRepo.findByNameIgnoreCase(req.name)
                .filter(other -> !other.getId().equals(id))
                .ifPresent(other -> { throw new IllegalArgumentException("Pricing name already exists"); });

        p.setName(req.name.trim());
        pricingRepo.save(p);

        upsertType(id, SeatType.ADULT, req.prices.getOrDefault(SeatType.ADULT, BigDecimal.ZERO));
        upsertType(id, SeatType.CHILD, req.prices.getOrDefault(SeatType.CHILD, BigDecimal.ZERO));
    }

    @Transactional
    public void updateSingleType(Long pricingId, PricingTypeUpdateRequest req) {
        // verifies pricing exists
        getOrThrow(pricingId);
        upsertType(pricingId, req.type, req.price);
    }

    public List<PricingType> listTypes(Long pricingId) {
        return typeRepo.findByPricingId(pricingId);
    }

    @Transactional
    public void delete(Long pricingId) {
        // types will cascade because of FK ON DELETE CASCADE
        pricingRepo.deleteById(pricingId);
    }

    private void upsertType(Long pricingId, SeatType type, BigDecimal price) {
        PricingType row = typeRepo.findByPricingIdAndType(pricingId, type)
                .orElseGet(() -> {
                    PricingType t = new PricingType();
                    t.setPricingId(pricingId);
                    t.setType(type);
                    return t;
                });

        row.setPrice(price);
        typeRepo.save(row);
    }
}
