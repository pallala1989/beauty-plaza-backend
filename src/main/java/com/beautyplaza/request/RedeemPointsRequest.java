// request/RedeemPointsRequest.java (NEW)
package com.beautyplaza.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RedeemPointsRequest {
    @NotNull
    private Long userId;
    @Min(1)
    private Integer pointsToRedeem;
    private Long appointmentId; // Or order ID
}