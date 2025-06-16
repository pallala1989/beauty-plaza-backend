// dto/LoyaltyPointsDTO.java (NEW)
package com.beautyplaza.dto;

import lombok.Data;

@Data
public class LoyaltyPointsDTO {
    private Long userId;
    private Integer totalPoints;
    private String currentTier; // e.g., Bronze, Silver, Gold
    // Could add details about next tier, benefits etc.
}