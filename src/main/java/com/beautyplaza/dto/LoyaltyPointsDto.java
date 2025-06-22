// dto/LoyaltyPointsDTO.java (NEW)
package com.beautyplaza.dto;

// Importing Lombok annotations and Jakarta validation annotations.
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;

// @Getter and @Setter automatically generate getters and setters.
@Getter
@Setter
public class LoyaltyPointsDto {
    private Long id; // Unique identifier for the loyalty points transaction.

    @NotBlank(message = "User ID is mandatory") // Ensures the user ID is not blank.
    private String userId; // ID of the user associated with the loyalty points.

    @NotBlank(message = "Transaction type is mandatory") // Ensures transaction type is not blank.
    @Pattern(regexp = "earned|redeemed", message = "Transaction type must be 'earned' or 'redeemed'") // Validates transaction type.
    private String transactionType; // Type of transaction (e.g., "earned", "redeemed").

    @NotNull(message = "Points value is mandatory") // Ensures points value is not null.
    @Min(value = 1, message = "Points must be at least 1") // Ensures points value is at least 1.
    private Integer points; // Number of loyalty points.

    private String description; // Description of the transaction.

    private Long appointmentId; // ID of the appointment, if points are related to an appointment.

    @Pattern(regexp = "gift_card|bank_credit", message = "Redemption method must be 'gift_card' or 'bank_credit'") // Validates redemption method.
    private String redemptionMethod; // Method of redemption (e.g., "gift_card", "bank_credit").

    private String bankAccount; // Bank account number for bank credit redemption.

    private String routingNumber; // Routing number for bank credit redemption.

    private BigDecimal redemptionValue; // Monetary value of the redeemed points.
}
