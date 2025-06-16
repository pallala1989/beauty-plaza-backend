// request/GiftCardCreateRequest.java (NEW)
package com.beautyplaza.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class GiftCardCreateRequest {
    @NotNull
    @DecimalMin(value = "0.01")
    private BigDecimal amount;
    private LocalDate expiryDate; // Optional
    private Long purchasedByUserId; // Optional
}