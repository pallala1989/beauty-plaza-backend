// model/Setting.java (NEW)
package com.beautyplaza.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "app_settings") // Using app_settings to distinguish from other types of settings
public class Setting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String settingKey; // e.g., "salon.opening.time", "tax.rate"
    @Column(length = 1000)
    private String settingValue;
    private String description; // Optional: explain what the setting does
    private String category; // Optional: group settings (e.g., "Booking", "General", "Financial")
}