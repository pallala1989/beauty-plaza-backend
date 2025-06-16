// dto/AppointmentDTO.java
package com.beautyplaza.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class AppointmentDTO {
    private Long id;
    private Long beautyServiceId;
    private String beautyServiceName;
    private Long technicianId;
    private String technicianName;
    private String customerName;
    private String customerEmail;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private String status;
    private BigDecimal totalAmount;
}