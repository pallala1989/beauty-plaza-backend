// model/Referral.java (NEW)
package com.beautyplaza.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "referrals")
public class Referral {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long referrerUserId; // The user who referred
    @Column(unique = true, nullable = false)
    private String referralCode;
    private Long referredUserId; // The user who was referred (once they register)
    private String referredUserEmail; // Initial tracking by email
    @Enumerated(EnumType.STRING)
    private ReferralStatus status; // PENDING, COMPLETED, CANCELLED
    private LocalDateTime generatedDate;
    private LocalDateTime completedDate; // When the referred user completes an action

    public enum ReferralStatus {
        PENDING, COMPLETED, CANCELLED
    }
}