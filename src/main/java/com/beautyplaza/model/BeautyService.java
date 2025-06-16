package com.beautyplaza.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "beauty_services")
public class BeautyService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "duration_minutes") // Explicit column name for clarity
    private Integer durationMinutes; // Duration in minutes

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    // Use Boolean wrapper for isActive for nullability and consistency
    // Lombok will generate getIsActive() and setIsActive(Boolean)
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true; // Default to true

}