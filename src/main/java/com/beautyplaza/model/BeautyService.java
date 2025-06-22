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
 * Represents a Service offered by Beauty Plaza.
 * This entity maps to the 'services' table in the database.
 */
@Entity
@Table(name = "services") // Maps this entity to the 'services' table.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BeautyService {

    @Id // Marks 'id' as the primary key.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increments the ID.
    private Long id; // Unique identifier for the service.

    @Column(name = "name", nullable = false) // 'name' column, cannot be null.
    private String name; // Name of the service.

    @Column(name = "description", columnDefinition = "TEXT") // 'description' column, stores longer text.
    private String description; // Detailed description of the service.

    @Column(name = "price", nullable = false, precision = 10, scale = 2) // 'price' column with precision and scale.
    private BigDecimal price; // Price of the service.

    @Column(name = "duration", nullable = false) // 'duration' column, cannot be null.
    private Integer duration; // Duration of the service in minutes.

    @Column(name = "image_url") // 'image_url' column.
    private String imageUrl; // URL to an image associated with the service.

    @Column(name = "is_active", nullable = false) // 'is_active' column, cannot be null.
    private Boolean isActive = true; // Indicates if the service is currently active and available.

    @Column(name = "created_at", updatable = false) // 'created_at' column, not updatable.
    private LocalDateTime createdAt; // Timestamp of service creation.

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
