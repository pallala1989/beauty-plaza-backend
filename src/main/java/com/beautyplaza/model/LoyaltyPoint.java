// model/LoyaltyPoint.java (NEW)
package com.beautyplaza.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "loyalty_points")
public class LoyaltyPoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId; // The user who earned points
    private Integer pointsEarned;
    private Integer pointsRedeemed;
    private LocalDateTime transactionDate;
    private String description; // e.g., "Service purchase", "Birthday bonus"
    // You might link to a LoyaltyProgram entity if you have multiple tiers/programs
}