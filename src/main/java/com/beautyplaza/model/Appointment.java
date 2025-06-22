// model/Appointment.java
package com.beautyplaza.model;

// Importing JPA annotations, Lombok annotations, and Java utilities.
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

/**
 * Represents an Appointment in the Beauty Plaza application.
 * This entity maps to the 'appointments' table in the database.
 */
@Entity
@Table(name = "appointments") // Maps this entity to the 'appointments' table.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {

    @Id // Marks 'id' as the primary key.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increments the ID.
    private Long id; // Unique identifier for the appointment.

    @ManyToOne(fetch = FetchType.LAZY) // Many-to-one relationship with User (customer).
    @JoinColumn(name = "customer_id", nullable = false) // Foreign key column to users table.
    private User customer; // The customer who booked the appointment.

    @ManyToOne(fetch = FetchType.LAZY) // Many-to-one relationship with Service.
    @JoinColumn(name = "service_id", nullable = false) // Foreign key column to services table.
    private BeautyService service; // The service booked for the appointment.

    @ManyToOne(fetch = FetchType.LAZY) // Many-to-one relationship with Technician.
    @JoinColumn(name = "technician_id", nullable = false) // Foreign key column to technicians table.
    private Technician technician; // The technician assigned to the appointment.

    @Column(name = "appointment_date", nullable = false) // 'appointment_date' column.
    private LocalDate appointmentDate; // Date of the appointment.

    @Column(name = "appointment_time", nullable = false) // 'appointment_time' column.
    private LocalTime appointmentTime; // Time of the appointment.

    @Enumerated(EnumType.STRING) // Stores enum as a String.
    @Column(name = "service_type", nullable = false) // 'service_type' column.
    private ServiceType serviceType; // Type of service (e.g., IN_STORE, IN_HOME).

    @Enumerated(EnumType.STRING) // Stores enum as a String.
    @Column(name = "status", nullable = false) // 'status' column.
    private AppointmentStatus status = AppointmentStatus.SCHEDULED; // Current status of the appointment.

    @Column(name = "notes", columnDefinition = "TEXT") // 'notes' column for additional details.
    private String notes; // Any additional notes for the appointment.

    @Column(name = "customer_phone") // 'customer_phone' column.
    private String customerPhone; // Customer's phone number.

    @Column(name = "customer_email") // 'customer_email' column.
    private String customerEmail; // Customer's email.

    @Column(name = "total_amount", precision = 10, scale = 2) // 'total_amount' column with precision and scale.
    private BigDecimal totalAmount; // Total cost of the appointment.

    @Column(name = "loyalty_points_used") // 'loyalty_points_used' column.
    private Integer loyaltyPointsUsed = 0; // Number of loyalty points redeemed for this appointment.

    @Column(name = "loyalty_discount", precision = 10, scale = 2) // 'loyalty_discount' column.
    private BigDecimal loyaltyDiscount = BigDecimal.ZERO; // Discount applied from loyalty points.

    @Column(name = "otp_verified", nullable = false) // 'otp_verified' column.
    private Boolean otpVerified = false; // Indicates if OTP verification was successful for the appointment.

    @Column(name = "created_at", updatable = false) // 'created_at' column, not updatable.
    private LocalDateTime createdAt; // Timestamp of appointment creation.

    @Column(name = "updated_at") // 'updated_at' column.
    private LocalDateTime updatedAt; // Timestamp of last update.

    /**
     * Pre-persists method to set creation timestamp.
     * This method is called automatically before the entity is first persisted to the database.
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now(); // Set creation timestamp.
        updatedAt = LocalDateTime.now(); // Set update timestamp initially.
    }

    /**
     * Pre-update method to set update timestamp.
     * This method is called automatically before the entity is updated in the database.
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now(); // Update timestamp on every update.
    }
}
