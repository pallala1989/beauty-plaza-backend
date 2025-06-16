// model/GiftCard.java (NEW)
package com.beautyplaza.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "gift_cards")
public class GiftCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String code;
    private BigDecimal initialAmount;
    private BigDecimal currentBalance;
    private LocalDate expiryDate;
    private boolean isActive;
    private Long issuedByUserId; // Optional: track who issued it
    private Long purchasedByUserId; // Optional: track who bought it
}