// model/LoyaltyPoint.java (NEW)
package com.beautyplaza.model;


// Importing JPA annotations, Lombok annotations, and Java utilities.
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Represents a Loyalty Points transaction for a user.
 * This entity maps to the 'loyalty_points' table in the database.
 */
@Entity
@Table(name = "loyalty_points") // Maps this entity to the 'loyalty_points' table.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoyaltyPoint {

    @Id // Marks 'id' as the primary key.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increments the ID.
    private Long id; // Unique identifier for the loyalty points transaction.

    @ManyToOne(fetch = FetchType.LAZY) // Many-to-one relationship with User.
    @JoinColumn(name = "user_id", nullable = false) // Foreign key column to users table.
    private User user; // The user associated with this loyalty points transaction.

    @Enumerated(EnumType.STRING) // Stores enum as a String.
    @Column(name = "transaction_type", nullable = false) // 'transaction_type' column.
    private TransactionType transactionType; // Type of transaction (e.g., EARNED, REDEEMED).

    @Column(name = "points", nullable = false) // 'points' column, cannot be null.
    private Integer points; // Number of loyalty points involved in the transaction.

    @Column(name = "description", columnDefinition = "VARCHAR(500)") // 'description' column.
    private String description; // Description of the loyalty points transaction.

    @OneToOne(fetch = FetchType.LAZY) // One-to-one relationship with Appointment.
    @JoinColumn(name = "appointment_id") // Foreign key column to appointments table.
    private Appointment appointment; // The appointment related to this loyalty points transaction (can be null).

    @Enumerated(EnumType.STRING) // Stores enum as a String.
    @Column(name = "redemption_method") // 'redemption_method' column.
    private RedemptionMethod redemptionMethod; // Method of redemption if points are redeemed.

    @Column(name = "bank_account") // 'bank_account' column.
    private String bankAccount; // Bank account number for bank credit redemption.

    @Column(name = "routing_number") // 'routing_number' column.
    private String routingNumber; // Routing number for bank credit redemption.

    @Column(name = "redemption_value", precision = 10, scale = 2) // 'redemption_value' column with precision and scale.
    private BigDecimal redemptionValue; // Monetary value of redeemed points.

    @Column(name = "created_at", updatable = false) // 'created_at' column, not updatable.
    private LocalDateTime createdAt; // Timestamp of loyalty points transaction.

    /**
     * Pre-persists method to set creation timestamp.
     * This method is called automatically before the entity is first persisted to the database.
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now(); // Set creation timestamp.
    }
}
