package com.beautyplaza.model;

// Importing JPA annotations, Lombok annotations, and Java utilities.
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Represents a Technician in the Beauty Plaza application.
 * This entity maps to the 'technicians' table in the database.
 */
@Entity
@Table(name = "technicians") // Maps this entity to the 'technicians' table.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Technician {

    @Id // Marks 'id' as the primary key.
    @GeneratedValue(generator = "uuid2") // Generates UUIDs for the ID.
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", columnDefinition = "VARCHAR(255)") // Defines column properties.
    private String id; // Unique identifier for the technician, stored as UUID string.

    @Column(name = "name", nullable = false) // 'name' column, cannot be null.
    private String name; // Name of the technician.

    // @JdbcTypeCode(SqlTypes.JSON) indicates that this field should be mapped to a JSON column type in the database.
    // The ObjectMapper is used to convert the List<String> to JSON string and vice versa.
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "specialties", nullable = true) // 'specialties' column, stores specialties as JSON array.
    private List<String> specialties; // List of specialties (e.g., "Hair Styling", "Manicure").

    @Column(name = "is_available", nullable = false) // 'is_available' column, cannot be null.
    private Boolean isAvailable = true; // Indicates if the technician is currently available.

    @Column(name = "image_url") // 'image_url' column.
    private String imageUrl; // URL to an image of the technician.

    @OneToOne(fetch = FetchType.LAZY) // One-to-one relationship with User entity.
    @JoinColumn(name = "user_id", referencedColumnName = "id") // Foreign key column.
    private User user; // Associated user account for this technician.

    @Column(name = "created_at", updatable = false) // 'created_at' column, not updatable.
    private LocalDateTime createdAt; // Timestamp of technician creation.

    @Column(name = "updated_at") // 'updated_at' column.
    private LocalDateTime updatedAt; // Timestamp of last update.

    /**
     * Pre-persists method to set creation timestamp and generate UUID if not set.
     * This method is called automatically before the entity is first persisted to the database.
     */
    @PrePersist
    protected void onCreate() {
        if (id == null) {
            id = UUID.randomUUID().toString(); // Generate UUID if ID is not set.
        }
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

    // Helper method to convert specialties List to JSON string for database storage
    public void setSpecialtiesJson(String jsonString) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            this.specialties = mapper.readValue(jsonString, new TypeReference<List<String>>() {});
        } catch (Exception e) {
            // Log error or throw a custom exception
            System.err.println("Error converting JSON string to List<String>: " + e.getMessage());
        }
    }

    // Helper method to convert JSON string from database to List of specialties
    public String getSpecialtiesJson() {
        if (this.specialties == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this.specialties);
        } catch (Exception e) {
            // Log error or throw a custom exception
            System.err.println("Error converting List<String> to JSON string: " + e.getMessage());
            return null;
        }
    }
}
