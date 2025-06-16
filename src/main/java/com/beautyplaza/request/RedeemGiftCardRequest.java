// request/RedeemGiftCardRequest.java (NEW)
package com.beautyplaza.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class RedeemGiftCardRequest {
    @NotBlank
    private String cardCode;
    @NotNull
    @DecimalMin(value = "0.01")
    private BigDecimal redeemAmount;
    private Long userId; // User who is redeeming
    private Long appointmentId; // Or order ID
}