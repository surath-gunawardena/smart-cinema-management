package com.example.cinema_management.pricing.dto;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public class PricingForm {
    private Long id;

    @NotBlank
    private String name;

    @NotNull(message = "Status must not be null")
    private String status = "DEACTIVE";

    @NotNull
    @DecimalMin("0.00")
    @Digits(integer = 8, fraction = 2)
    private BigDecimal adultPrice;

    @NotNull
    @DecimalMin("0.00")
    @Digits(integer = 8, fraction = 2)
    private BigDecimal childPrice;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {

        this.status = status;
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
