// model/AuthUser.java (NEW - Renamed from 'User' to avoid potential conflicts and provide context)
package com.beautyplaza.model;

// Importing JPA annotations, Lombok annotations, and Java utilities.
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents a User in the Beauty Plaza application.
 * This entity maps to the 'users' table in the database.
 */
@Entity
@Table(name = "users") // Maps this entity to the 'users' table.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id // Marks 'id' as the primary key.
    @GeneratedValue(generator = "uuid2") // Generates UUIDs for the ID.
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", columnDefinition = "VARCHAR(255)") // Defines column properties.
    private String id; // Unique identifier for the user, stored as UUID string.

    @Column(name = "email", unique = true, nullable = false) // 'email' column, must be unique and not null.
    private String email; // User's email address.

    @Column(name = "password", nullable = false) // 'password' column, cannot be null.
    private String password; // User's hashed password.

    @Column(name = "full_name") // 'full_name' column.
    private String fullName; // User's full name.

    @Column(name = "phone") // 'phone' column.
    private String phone; // User's phone number.

    @Enumerated(EnumType.STRING) // Stores enum as a String in the database.
    @Column(name = "role", nullable = false) // 'role' column, cannot be null.
    private Role role; // User's role (e.g., ADMIN, TECHNICIAN, USER).

    @Column(name = "is_active", nullable = false) // 'is_active' column, cannot be null.
    private Boolean isActive = true; // Indicates if the user account is active.

    @Column(name = "created_at", updatable = false) // 'created_at' column, not updatable after creation.
    private LocalDateTime createdAt; // Timestamp of user creation.

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
}
