// src/main/java/com/beautyplaza/controller/AppointmentController.java
package com.beautyplaza.controller;

// Importing necessary Spring Framework, DTO, and security classes.
import com.beautyplaza.dto.AppointmentDto;
import com.beautyplaza.service.AppointmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

/**
 * REST Controller for managing Appointment entities.
 * Provides endpoints for CRUD operations on appointments, with role-based access control.
 */
@RestController // Marks this class as a REST controller.
@RequestMapping("/api/appointments") // Base path for all endpoints in this controller.
public class AppointmentController {

    @Autowired // Injects AppointmentService for business logic operations on appointments.
    private AppointmentService appointmentService;

    /**
     * Creates a new appointment. Accessible by USER and ADMIN.
     * Users can only create appointments for themselves.
     * Admins can create appointments for any customer.
     * @param appointmentDto The AppointmentDto containing appointment details.
     * @param userDetails The authenticated user's details.
     * @return ResponseEntity with the created AppointmentDto.
     */
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<AppointmentDto> createAppointment(@Valid @RequestBody AppointmentDto appointmentDto,
                                                            @AuthenticationPrincipal UserDetails userDetails) {
        // If a regular user, ensure they are booking for themselves.
        if (userDetails.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            // Assuming customerId in DTO corresponds to the user's ID
            // For now, let's assume customerId is the user's email as per current UserDetails setup
            // This might need adjustment if userId is UUID and not email in UserDetails.
            // For now, matching by email:
            if (!appointmentDto.getCustomerId().equals(userDetails.getUsername())) { // Or get ID from custom UserDetails
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }
        AppointmentDto createdAppointment = appointmentService.createAppointment(appointmentDto);
        return new ResponseEntity<>(createdAppointment, HttpStatus.CREATED); // Return 201 Created.
    }

    /**
     * Retrieves an appointment by ID. Accessible by ADMIN, or if the authenticated user is the customer or technician.
     * @param id The ID of the appointment to retrieve.
     * @param userDetails The authenticated user's details.
     * @return ResponseEntity with the AppointmentDto.
     */
    @PreAuthorize("hasRole('ADMIN') or " +
            "(hasRole('USER') and @userRepository.findById(#id).orElse(null)?.customer?.email == authentication.principal.username) or " +
            "(hasRole('TECHNICIAN') and @userRepository.findById(#id).orElse(null)?.technician?.user?.email == authentication.principal.username)")
    @GetMapping("/{id}")
    public ResponseEntity<AppointmentDto> getAppointmentById(@PathVariable Long id,
                                                             @AuthenticationPrincipal UserDetails userDetails) {
        AppointmentDto appointment = appointmentService.getAppointmentById(id);
        return ResponseEntity.ok(appointment); // Return 200 OK.
    }

    /**
     * Retrieves all appointments. Accessible by ADMIN only.
     * @return ResponseEntity with a list of AppointmentDtos.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<AppointmentDto>> getAllAppointments() {
        List<AppointmentDto> appointments = appointmentService.getAllAppointments();
        return ResponseEntity.ok(appointments); // Return 200 OK.
    }

    /**
     * Retrieves appointments for a specific customer. Accessible by ADMIN or the customer themselves.
     * @param customerId The ID of the customer.
     * @param userDetails The authenticated user's details.
     * @return ResponseEntity with a list of AppointmentDtos.
     */
    @PreAuthorize("hasRole('ADMIN') or #customerId == authentication.principal.username") // Assuming customerId is email
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<AppointmentDto>> getAppointmentsByCustomerId(@PathVariable String customerId,
                                                                            @AuthenticationPrincipal UserDetails userDetails) {
        List<AppointmentDto> appointments = appointmentService.getAppointmentsByCustomerId(customerId);
        return ResponseEntity.ok(appointments); // Return 200 OK.
    }

    /**
     * Retrieves appointments for a specific technician. Accessible by ADMIN or the technician themselves.
     * @param technicianId The ID of the technician.
     * @param userDetails The authenticated user's details.
     * @return ResponseEntity with a list of AppointmentDtos.
     */
    @PreAuthorize("hasRole('ADMIN') or " +
            "(@technicianRepository.findById(#technicianId).orElse(null)?.user?.email == authentication.principal.username)")
    @GetMapping("/technician/{technicianId}")
    public ResponseEntity<List<AppointmentDto>> getAppointmentsByTechnicianId(@PathVariable String technicianId,
                                                                              @AuthenticationPrincipal UserDetails userDetails) {
        List<AppointmentDto> appointments = appointmentService.getAppointmentsByTechnicianId(technicianId);
        return ResponseEntity.ok(appointments); // Return 200 OK.
    }

    /**
     * Retrieves appointments for a specific date. Accessible by ADMIN only.
     * @param date The date to filter appointments by (format: YYYY-MM-DD).
     * @return ResponseEntity with a list of AppointmentDtos.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/date/{date}")
    public ResponseEntity<List<AppointmentDto>> getAppointmentsByDate(@PathVariable String date) {
        LocalDate appointmentDate = LocalDate.parse(date); // Parse date string to LocalDate.
        List<AppointmentDto> appointments = appointmentService.getAppointmentsByDate(appointmentDate);
        return ResponseEntity.ok(appointments); // Return 200 OK.
    }

    /**
     * Updates an existing appointment. Accessible by ADMIN only.
     * @param id The ID of the appointment to update.
     * @param appointmentDto The AppointmentDto containing updated details.
     * @return ResponseEntity with the updated AppointmentDto.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<AppointmentDto> updateAppointment(@PathVariable Long id, @Valid @RequestBody AppointmentDto appointmentDto) {
        AppointmentDto updatedAppointment = appointmentService.updateAppointment(id, appointmentDto);
        return ResponseEntity.ok(updatedAppointment); // Return 200 OK.
    }

    /**
     * Updates the status of an appointment. Accessible by ADMIN or TECHNICIAN.
     * @param id The ID of the appointment to update.
     * @param status The new status (e.g., "confirmed", "completed").
     * @return ResponseEntity with the updated AppointmentDto.
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'TECHNICIAN')")
    @PatchMapping("/{id}/status")
    public ResponseEntity<AppointmentDto> updateAppointmentStatus(@PathVariable Long id, @RequestParam String status) {
        AppointmentDto updatedAppointment = appointmentService.updateAppointmentStatus(id, status);
        return ResponseEntity.ok(updatedAppointment); // Return 200 OK.
    }

    /**
     * Verifies OTP for an appointment. Accessible by USER.
     * @param id The ID of the appointment.
     * @param otp The OTP provided by the user.
     * @return ResponseEntity with the updated AppointmentDto.
     */
    @PreAuthorize("hasRole('USER')") // Only users can verify OTP for their appointments
    @PostMapping("/{id}/verify-otp")
    public ResponseEntity<AppointmentDto> verifyOtp(@PathVariable Long id, @RequestParam String otp) {
        AppointmentDto verifiedAppointment = appointmentService.verifyOtp(id, otp);
        return ResponseEntity.ok(verifiedAppointment);
    }

    /**
     * Deletes an appointment by ID. Accessible by ADMIN only.
     * @param id The ID of the appointment to delete.
     * @return ResponseEntity with no content.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long id) {
        appointmentService.deleteAppointment(id);
        return ResponseEntity.noContent().build(); // Return 204 No Content.
    }
}
