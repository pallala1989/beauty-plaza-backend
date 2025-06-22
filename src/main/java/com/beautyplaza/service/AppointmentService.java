
package com.beautyplaza.service;

// Importing the AppointmentDto and Java utilities.
import com.beautyplaza.dto.AppointmentDto;
import java.time.LocalDate;
import java.util.List;

/**
 * Interface for Appointment-related business logic.
 * Defines the contract for operations on Appointment data.
 */
public interface AppointmentService {
    /**
     * Creates a new appointment.
     * @param appointmentDto The AppointmentDto containing appointment details.
     * @return The created AppointmentDto.
     */
    AppointmentDto createAppointment(AppointmentDto appointmentDto);

    /**
     * Retrieves an appointment by its ID.
     * @param appointmentId The ID of the appointment to retrieve.
     * @return The AppointmentDto of the found appointment.
     */
    AppointmentDto getAppointmentById(Long appointmentId);

    /**
     * Retrieves all appointments.
     * @return A list of all AppointmentDtos.
     */
    List<AppointmentDto> getAllAppointments();

    /**
     * Retrieves appointments for a specific customer.
     * @param customerId The ID of the customer.
     * @return A list of AppointmentDtos for the given customer.
     */
    List<AppointmentDto> getAppointmentsByCustomerId(String customerId);

    /**
     * Retrieves appointments for a specific technician.
     * @param technicianId The ID of the technician.
     * @return A list of AppointmentDtos for the given technician.
     */
    List<AppointmentDto> getAppointmentsByTechnicianId(String technicianId);

    /**
     * Retrieves appointments for a specific date.
     * @param date The date to filter appointments by.
     * @return A list of AppointmentDtos for the given date.
     */
    List<AppointmentDto> getAppointmentsByDate(LocalDate date);

    /**
     * Updates an existing appointment.
     * @param appointmentId The ID of the appointment to update.
     * @param appointmentDto The AppointmentDto containing updated details.
     * @return The updated AppointmentDto.
     */
    AppointmentDto updateAppointment(Long appointmentId, AppointmentDto appointmentDto);

    /**
     * Updates the status of an appointment.
     * @param appointmentId The ID of the appointment to update.
     * @param status The new status (e.g., "confirmed", "completed").
     * @return The updated AppointmentDto.
     */
    AppointmentDto updateAppointmentStatus(Long appointmentId, String status);

    /**
     * Verifies an OTP for a given appointment.
     * @param appointmentId The ID of the appointment.
     * @param otp The OTP provided by the user.
     * @return The updated AppointmentDto after OTP verification.
     */
    AppointmentDto verifyOtp(Long appointmentId, String otp);

    /**
     * Deletes an appointment by its ID.
     * @param appointmentId The ID of the appointment to delete.
     */
    void deleteAppointment(Long appointmentId);
}
