// dto/AppointmentDTO.java
package com.beautyplaza.dto;


// Importing Lombok annotations and Jakarta validation annotations.
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

// @Getter and @Setter automatically generate getters and setters.
@Getter
@Setter
public class AppointmentDto {
    private Long id; // Unique identifier for the appointment.

    @NotBlank(message = "Customer ID is mandatory") // Ensures the customer ID is not blank.
    private String customerId; // ID of the customer who booked the appointment.

    @NotNull(message = "Service ID is mandatory") // Ensures the service ID is not null.
    private Long serviceId; // ID of the service booked.

    @NotBlank(message = "Technician ID is mandatory") // Ensures the technician ID is not blank.
    private String technicianId; // ID of the technician assigned to the appointment.

    @NotNull(message = "Appointment date is mandatory") // Ensures the date is not null.
    private LocalDate appointmentDate; // Date of the appointment.

    @NotNull(message = "Appointment time is mandatory") // Ensures the time is not null.
    private LocalTime appointmentTime; // Time of the appointment.

    @NotBlank(message = "Service type is mandatory") // Ensures service type is not blank.
    @Pattern(regexp = "in-store|in-home", message = "Service type must be 'in-store' or 'in-home'") // Validates service type.
    private String serviceType; // Type of service (e.g., "in-store", "in-home").

    private String status; // Current status of the appointment (e.g., "scheduled", "confirmed").

    private String notes; // Any additional notes for the appointment.

    @Pattern(regexp = "^[0-9]{10}$", message = "Customer phone number must be 10 digits") // Validates phone number.
    private String customerPhone; // Customer's phone number.

    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", message = "Customer email should be valid") // Validates email format.
    private String customerEmail; // Customer's email.

    private BigDecimal totalAmount; // Total amount for the appointment.

    private Integer loyaltyPointsUsed; // Number of loyalty points used for this appointment.

    private BigDecimal loyaltyDiscount; // Discount applied from loyalty points.

    private Boolean otpVerified; // Flag indicating if OTP verification was successful.
}
