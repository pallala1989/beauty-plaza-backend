// request/ApplyPromotionRequest.java (NEW)
package com.beautyplaza.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ApplyPromotionRequest {
    @NotBlank
    private String promoCode;
    private Long bookingId; // Or order ID, depending on where it's applied
    private Long userId; // User applying the promo
}