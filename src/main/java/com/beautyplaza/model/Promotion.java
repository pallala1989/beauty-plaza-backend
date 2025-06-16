// model/Promotion.java (NEW)
package com.beautyplaza.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "promotions")
public class Promotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(length = 500)
    private String description;
    @Column(unique = true)
    private String promoCode; // e.g., "SUMMER20"
    @Enumerated(EnumType.STRING)
    private DiscountType discountType; // e.g., PERCENTAGE, FIXED_AMOUNT
    private BigDecimal discountValue; // e.g., 0.15 for 15%, or 10.00 for $10 off
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean isActive;

    public enum DiscountType {
        PERCENTAGE, FIXED_AMOUNT
    }
}