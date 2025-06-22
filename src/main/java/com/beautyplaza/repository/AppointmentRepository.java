// repository/AppointmentRepository.java
package com.beautyplaza.repository;

import com.beautyplaza.model.Appointment;
import com.beautyplaza.model.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

/**
 * Repository interface for Appointment entities.
 * Extends JpaRepository to provide standard CRUD operations for Appointment objects.
 * The generic parameters are: Appointment (the entity type) and Long (the ID type of the entity).
 */
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    /**
     * Finds a list of Appointments for a specific customer.
     * @param customerId The ID of the customer.
     * @return A list of Appointments associated with the given customer.
     */
    List<Appointment> findByCustomerId(String customerId);

    /**
     * Finds a list of Appointments for a specific technician.
     * @param technicianId The ID of the technician.
     * @return A list of Appointments associated with the given technician.
     */
    List<Appointment> findByTechnicianId(String technicianId);

    /**
     * Finds a list of Appointments by date.
     * @param appointmentDate The date of the appointment.
     * @return A list of Appointments scheduled for the given date.
     */
    List<Appointment> findByAppointmentDate(LocalDate appointmentDate);

    /**
     * Finds a list of Appointments by status.
     * @param status The status of the appointment (e.g., SCHEDULED, COMPLETED).
     * @return A list of Appointments matching the given status.
     */
    List<Appointment> findByStatus(AppointmentStatus status);

    /**
     * Finds appointments for a specific technician on a given date.
     * This is useful for checking technician availability.
     * @param technicianId The ID of the technician.
     * @param appointmentDate The date of the appointment.
     * @return A list of appointments for that technician on that date.
     */
    List<Appointment> findByTechnicianIdAndAppointmentDate(String technicianId, LocalDate appointmentDate);

    /**
     * Checks if there's an existing appointment for a technician at a specific date and time.
     * This is crucial for preventing double bookings.
     * @param technicianId The ID of the technician.
     * @param appointmentDate The date of the appointment.
     * @param appointmentTime The time of the appointment.
     * @return True if an appointment exists, false otherwise.
     */
    boolean existsByTechnicianIdAndAppointmentDateAndAppointmentTime(String technicianId, LocalDate appointmentDate, java.time.LocalTime appointmentTime);
}
