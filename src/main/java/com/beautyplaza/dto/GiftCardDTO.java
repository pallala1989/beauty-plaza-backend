// dto/GiftCardDTO.java (NEW)
package com.beautyplaza.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class GiftCardDTO {
    private Long id;
    private String code;
    private BigDecimal currentBalance;
    private LocalDate expiryDate;
    private boolean isActive;
}