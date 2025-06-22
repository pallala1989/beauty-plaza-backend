
package com.beautyplaza.model;



// Importing JPA annotations, Lombok annotations, and Java utilities.
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;

/**
 * Represents application settings stored in the database.
 * This entity maps to the 'settings' table.
 */
@Entity
@Table(name = "settings") // Maps this entity to the 'settings' table.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Setting {

    @Id // Marks 'settingKey' as the primary key.
    @Column(name = "setting_key", nullable = false, unique = true) // 'setting_key' column, unique and not null.
    private String settingKey; // Unique key for the setting (e.g., "loyalty_conversion_rate").

    // @JdbcTypeCode(SqlTypes.JSON) indicates that this field should be mapped to a JSON column type.
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "setting_value", columnDefinition = "json", nullable = false) // 'setting_value' column, stores JSON.
    private JsonNode settingValue; // Value of the setting, stored as a JSON object.

    @Column(name = "created_at", updatable = false) // 'created_at' column, not updatable.
    private LocalDateTime createdAt; // Timestamp of setting creation.

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

    // Helper method to convert JsonNode to String (for potential logging/debugging)
    public String getSettingValueAsString() {
        if (settingValue == null) {
            return null;
        }
        return settingValue.toString();
    }

    // Helper method to set JsonNode from a String (for convenience, e.g., from a request)
    public void setSettingValueFromString(String jsonString) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            this.settingValue = mapper.readTree(jsonString);
        } catch (Exception e) {
            // Log error or throw a custom exception
            System.err.println("Error converting JSON string to JsonNode: " + e.getMessage());
        }
    }
}
