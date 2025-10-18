package com.example.cinema_management.pricing.dto;

import java.math.BigDecimal;

public class PricingForm {
    private Long id;

    @jakarta.validation.constraints.NotBlank
    private String name;

    @jakarta.validation.constraints.NotNull
    @jakarta.validation.constraints.DecimalMin("0.00")
    @jakarta.validation.constraints.Digits(integer = 8, fraction = 2)
    private BigDecimal adultPrice;

    @jakarta.validation.constraints.NotNull
    @jakarta.validation.constraints.DecimalMin("0.00")
    @jakarta.validation.constraints.Digits(integer = 8, fraction = 2)
    private BigDecimal childPrice;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public BigDecimal getAdultPrice() { return adultPrice; }
    public void setAdultPrice(BigDecimal adultPrice) { this.adultPrice = adultPrice; }

    public BigDecimal getChildPrice() { return childPrice; }
    public void setChildPrice(BigDecimal childPrice) { this.childPrice = childPrice; }
}
