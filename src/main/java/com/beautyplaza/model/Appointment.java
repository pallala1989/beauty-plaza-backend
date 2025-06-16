// model/Appointment.java
package com.beautyplaza.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Entity
@Table(name = "appointments")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long beautyServiceId;
    private String beautyServiceName;
    private Long technicianId;
    private String technicianName;
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private String status; // e.g., "SCHEDULED", "CONFIRMED", "CANCELLED"
    private String serviceType; // e.g., "in-store" or "in-home"
    @Column(length = 1000)
    private String notes;
    private BigDecimal totalAmount;
}