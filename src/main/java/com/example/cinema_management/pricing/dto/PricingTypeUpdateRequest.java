package com.example.cinema_management.pricing.dto;

import com.example.cinema_management.pricing.SeatType;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class PricingTypeUpdateRequest {
    @NotNull
    public SeatType type;
    @NotNull
    public BigDecimal price;

    public SeatType getType() {
        return type;
    }

    public void setType(SeatType type) {
        this.type = type;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
