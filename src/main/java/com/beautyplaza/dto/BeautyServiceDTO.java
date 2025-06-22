package com.beautyplaza.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BeautyServiceDTO {
    private Long id; // Unique identifier for the service.

    @NotBlank(message = "Service name is mandatory") // Ensures the name field is not null and contains at least one non-whitespace character.
    private String name; // Name of the service.

    private String description; // Description of the service.

    @NotNull(message = "Price is mandatory") // Ensures the price field is not null.
    @Min(value = 0, message = "Price cannot be negative") // Ensures the price is non-negative.
    private BigDecimal price; // Price of the service.

    @NotNull(message = "Duration is mandatory") // Ensures the duration field is not null.
    @Min(value = 1, message = "Duration must be at least 1 minute") // Ensures duration is at least 1 minute.
    private Integer duration; // Duration of the service in minutes.

    private String imageUrl; // URL to an image representing the service.

    private Boolean isActive; // Flag indicating if the service is active/available.
}
