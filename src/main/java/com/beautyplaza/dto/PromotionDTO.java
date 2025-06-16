// dto/PromotionDTO.java (NEW)
package com.beautyplaza.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class PromotionDTO {
    private Long id;
    private String name;
    private String description;
    private String promoCode;
    private String discountType; // e.g., PERCENTAGE, FIXED_AMOUNT
    private BigDecimal discountValue;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean isActive;
}