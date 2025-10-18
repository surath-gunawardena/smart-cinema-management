package com.example.cinema_management.pricing.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class PricingShowtimeRequest {

    @NotNull private Long showTimeId;
    @NotNull @DecimalMin("0.00") private BigDecimal adultPrice;
    @NotNull @DecimalMin("0.00") private BigDecimal childPrice;

    public Long getShowTimeId() {
        return showTimeId;
    }

    public void setShowTimeId(Long showTimeId) {
        this.showTimeId = showTimeId;
    }

    public BigDecimal getAdultPrice() {
        return adultPrice;
    }

    public void setAdultPrice(BigDecimal adultPrice) {
        this.adultPrice = adultPrice;
    }

    public BigDecimal getChildPrice() {
        return childPrice;
    }

    public void setChildPrice(BigDecimal childPrice) {
        this.childPrice = childPrice;
    }
}
