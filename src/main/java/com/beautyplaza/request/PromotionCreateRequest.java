// request/PromotionCreateRequest.java
package com.beautyplaza.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class PromotionCreateRequest {
    @NotBlank(message = "Promotion name is required")
    private String name;

    private String description;

    @NotBlank(message = "Promotion code is required")
    private String promoCode;

    @NotBlank(message = "Discount type is required (e.g., PERCENTAGE, FIXED_AMOUNT)")
    private String discountType; // Will be mapped to Promotion.DiscountType enum in service

    @NotNull(message = "Discount value is required")
    @DecimalMin(value = "0.01", message = "Discount value must be positive")
    private BigDecimal discountValue;

    @NotNull(message = "Start date is required")
    @FutureOrPresent(message = "Start date cannot be in the past")
    private LocalDate startDate;

    private LocalDate endDate; // Optional, can be null for indefinite promotions

    // Note: 'isActive' status can be managed by a separate admin endpoint
    // or defaulted to true on creation and then toggled.
}