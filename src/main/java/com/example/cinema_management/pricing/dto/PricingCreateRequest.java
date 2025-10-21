package com.example.cinema_management.pricing.dto;

import com.example.cinema_management.pricing.SeatType;
import com.example.cinema_management.pricing.Status;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Map;

public class PricingCreateRequest {
    @NotBlank
    public String name;

    @Valid
    @NotNull
    public Map<SeatType, @NotNull BigDecimal> prices;

    @NotNull(message = "Status must be provided.")
    private Status status = Status.ACTIVE;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<SeatType, BigDecimal> getPrices() {
        return prices;
    }

    public void setPrices(Map<SeatType, BigDecimal> prices) {
        this.prices = prices;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
