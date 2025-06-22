// src/main/java/com/beautyplaza/dto/TechnicianDTO.java
package com.beautyplaza.dto;
// Importing Lombok annotations and Jakarta validation annotations.
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

// @Getter and @Setter automatically generate getters and setters.
@Getter
@Setter
public class TechnicianDto {
    private String id; // Unique identifier for the technician.

    @NotBlank(message = "Technician name is mandatory") // Ensures the name field is not null and contains at least one non-whitespace character.
    private String name; // Name of the technician.

    @NotNull(message = "Specialties are mandatory") // Ensures the specialties list is not null.
    private List<String> specialties; // List of specialties (e.g., "Facial", "Hair Coloring").

    private Boolean isAvailable; // Flag indicating if the technician is currently available.

    private String imageUrl; // URL to an image of the technician.

    private String userId; // ID of the associated user account (if any).
}
