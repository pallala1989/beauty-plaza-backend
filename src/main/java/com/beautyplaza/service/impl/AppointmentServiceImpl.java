
package com.beautyplaza.service.impl;

// Importing necessary classes for service logic.
import com.beautyplaza.dto.AppointmentDto;
import com.beautyplaza.model.*; // Import all entities
import com.beautyplaza.exception.ApiException;
import com.beautyplaza.exception.ResourceNotFoundException;
import com.beautyplaza.repository.AppointmentRepository;
import com.beautyplaza.repository.BeautyServiceRepository;
import com.beautyplaza.repository.TechnicianRepository;
import com.beautyplaza.repository.UserRepository;
import com.beautyplaza.service.AppointmentService;
import com.beautyplaza.util.OtpUtil; // Import OtpUtil
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of the AppointmentService interface.
 * Handles business logic related to Appointment entities, including booking, updating, and OTP verification.
 */
@Service // Marks this class as a Spring Service component.
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired // Injects AppointmentRepository for database interaction.
    private AppointmentRepository appointmentRepository;

    @Autowired // Injects UserRepository to fetch customer details.
    private UserRepository userRepository;

    @Autowired // Injects ServiceRepository to fetch service details.
    private BeautyServiceRepository serviceRepository;

    @Autowired // Injects TechnicianRepository to fetch technician details.
    private TechnicianRepository technicianRepository;

    @Autowired // Injects ModelMapper for object mapping (Entity <-> DTO).
    private ModelMapper modelMapper;

    @Autowired // Injects OtpUtil for OTP generation and verification.
    private OtpUtil otpUtil;

    /**
     * Creates a new appointment.
     * Performs validation for customer, service, technician existence and availability.
     * Generates OTP for confirmation.
     * @param appointmentDto The AppointmentDto containing appointment details.
     * @return The created AppointmentDto.
     * @throws ResourceNotFoundException if customer, service, or technician not found.
     * @throws ApiException if technician is unavailable or already has an appointment at the requested time.
     */
    @Override
    @Transactional // Ensures the entire method runs as a single transaction.
    public AppointmentDto createAppointment(AppointmentDto appointmentDto) {
        // Retrieve associated entities or throw ResourceNotFoundException.
        User customer = userRepository.findById(appointmentDto.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", appointmentDto.getCustomerId()));
        BeautyService service = serviceRepository.findById(appointmentDto.getServiceId())
                .orElseThrow(() -> new ResourceNotFoundException("Service", "id", appointmentDto.getServiceId()));
        Technician technician = technicianRepository.findById(appointmentDto.getTechnicianId())
                .orElseThrow(() -> new ResourceNotFoundException("Technician", "id", appointmentDto.getTechnicianId()));

        // Validate technician availability.
        if (!technician.getIsAvailable()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Technician is not available for bookings.");
        }

        // Check for existing appointments for the technician at the exact time.
        if (appointmentRepository.existsByTechnicianIdAndAppointmentDateAndAppointmentTime(
                technician.getId(), appointmentDto.getAppointmentDate(), appointmentDto.getAppointmentTime())) {
            throw new ApiException(HttpStatus.CONFLICT, "Technician already has an appointment at this date and time.");
        }

        // Map DTO to Entity.
        Appointment appointment = modelMapper.map(appointmentDto, Appointment.class);
        appointment.setCustomer(customer);
        appointment.setService(service);
        appointment.setTechnician(technician);
        appointment.setStatus(AppointmentStatus.SCHEDULED); // Default status.
        appointment.setOtpVerified(false); // Initially, OTP is not verified.

        // Calculate total amount if not provided (or recalculate based on service price)
        if (appointment.getTotalAmount() == null) {
            appointment.setTotalAmount(service.getPrice());
        }

        // Generate and potentially send OTP (simulated here)
        String generatedOtp = otpUtil.generateOtp(appointment.getCustomerEmail());
        // In a real application, you would send this OTP via email/SMS.
        System.out.println("Generated OTP for appointment " + appointment.getId() + " (" + appointment.getCustomerEmail() + "): " + generatedOtp);

        // Save the new appointment to the database.
        Appointment savedAppointment = appointmentRepository.save(appointment);
        // Map the saved Entity back to DTO and return.
        return modelMapper.map(savedAppointment, AppointmentDto.class);
    }

    /**
     * Retrieves an appointment by its unique ID.
     * @param appointmentId The ID of the appointment to retrieve.
     * @return The AppointmentDto of the found appointment.
     * @throws ResourceNotFoundException if no appointment is found with the given ID.
     */
    @Override
    public AppointmentDto getAppointmentById(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment", "id", appointmentId));
        return modelMapper.map(appointment, AppointmentDto.class);
    }

    /**
     * Retrieves a list of all appointments.
     * @return A list of AppointmentDto objects.
     */
    @Override
    public List<AppointmentDto> getAllAppointments() {
        List<Appointment> appointments = appointmentRepository.findAll();
        // Stream through the list of Appointment entities, map each to an AppointmentDto, and collect into a new list.
        return appointments.stream().map(appointment -> modelMapper.map(appointment, AppointmentDto.class)).collect(Collectors.toList());
    }

    /**
     * Retrieves appointments for a specific customer.
     * @param customerId The ID of the customer.
     * @return A list of AppointmentDtos for the given customer.
     * @throws ResourceNotFoundException if the customer does not exist.
     */
    @Override
    public List<AppointmentDto> getAppointmentsByCustomerId(String customerId) {
        userRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", customerId));
        List<Appointment> appointments = appointmentRepository.findByCustomerId(customerId);
        return appointments.stream().map(appointment -> modelMapper.map(appointment, AppointmentDto.class)).collect(Collectors.toList());
    }

    /**
     * Retrieves appointments for a specific technician.
     * @param technicianId The ID of the technician.
     * @return A list of AppointmentDtos for the given technician.
     * @throws ResourceNotFoundException if the technician does not exist.
     */
    @Override
    public List<AppointmentDto> getAppointmentsByTechnicianId(String technicianId) {
        technicianRepository.findById(technicianId)
                .orElseThrow(() -> new ResourceNotFoundException("Technician", "id", technicianId));
        List<Appointment> appointments = appointmentRepository.findByTechnicianId(technicianId);
        return appointments.stream().map(appointment -> modelMapper.map(appointment, AppointmentDto.class)).collect(Collectors.toList());
    }

    /**
     * Retrieves appointments for a specific date.
     * @param date The date to filter appointments by.
     * @return A list of AppointmentDtos for the given date.
     */
    @Override
    public List<AppointmentDto> getAppointmentsByDate(LocalDate date) {
        List<Appointment> appointments = appointmentRepository.findByAppointmentDate(date);
        return appointments.stream().map(appointment -> modelMapper.map(appointment, AppointmentDto.class)).collect(Collectors.toList());
    }

    /**
     * Updates an existing appointment's information.
     * Revalidates technician availability if technician, date, or time is changed.
     * @param appointmentId The ID of the appointment to update.
     * @param appointmentDto The AppointmentDto containing the updated appointment details.
     * @return The updated AppointmentDto.
     * @throws ResourceNotFoundException if the appointment or associated entities are not found.
     * @throws ApiException if technician is unavailable or already has an appointment at the requested time.
     */
    @Override
    @Transactional // Ensures the entire method runs as a single transaction.
    public AppointmentDto updateAppointment(Long appointmentId, AppointmentDto appointmentDto) {
        Appointment existingAppointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment", "id", appointmentId));

        // Update customer if provided and different.
        if (appointmentDto.getCustomerId() != null && !appointmentDto.getCustomerId().equals(existingAppointment.getCustomer().getId())) {
            User newCustomer = userRepository.findById(appointmentDto.getCustomerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", appointmentDto.getCustomerId()));
            existingAppointment.setCustomer(newCustomer);
        }

        // Update service if provided and different.
        if (appointmentDto.getServiceId() != null && !appointmentDto.getServiceId().equals(existingAppointment.getService().getId())) {
            BeautyService newService = serviceRepository.findById(appointmentDto.getServiceId())
                    .orElseThrow(() -> new ResourceNotFoundException("Service", "id", appointmentDto.getServiceId()));
            existingAppointment.setService(newService);
            existingAppointment.setTotalAmount(newService.getPrice()); // Recalculate total amount if service changes.
        }

        // Update technician, date, or time if provided and different.
        boolean technicianChanged = appointmentDto.getTechnicianId() != null && !appointmentDto.getTechnicianId().equals(existingAppointment.getTechnician().getId());
        boolean dateChanged = appointmentDto.getAppointmentDate() != null && !appointmentDto.getAppointmentDate().equals(existingAppointment.getAppointmentDate());
        boolean timeChanged = appointmentDto.getAppointmentTime() != null && !appointmentDto.getAppointmentTime().equals(existingAppointment.getAppointmentTime());

        if (technicianChanged || dateChanged || timeChanged) {
            Technician newTechnician = technicianChanged ?
                    technicianRepository.findById(appointmentDto.getTechnicianId())
                            .orElseThrow(() -> new ResourceNotFoundException("Technician", "id", appointmentDto.getTechnicianId())) :
                    existingAppointment.getTechnician();

            LocalDate newDate = dateChanged ? appointmentDto.getAppointmentDate() : existingAppointment.getAppointmentDate();
            LocalTime newTime = timeChanged ? appointmentDto.getAppointmentTime() : existingAppointment.getAppointmentTime();

            if (!newTechnician.getIsAvailable()) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "Technician is not available for bookings.");
            }

            // Check for conflicts with other appointments, excluding the current appointment being updated
            if (appointmentRepository.existsByTechnicianIdAndAppointmentDateAndAppointmentTime(
                    newTechnician.getId(), newDate, newTime) &&
                    // Ensure we're not checking against the appointment we are currently updating
                    !(existingAppointment.getTechnician().getId().equals(newTechnician.getId()) &&
                            existingAppointment.getAppointmentDate().equals(newDate) &&
                            existingAppointment.getAppointmentTime().equals(newTime))) {
                throw new ApiException(HttpStatus.CONFLICT, "Technician already has an appointment at this updated date and time.");
            }
            existingAppointment.setTechnician(newTechnician);
            existingAppointment.setAppointmentDate(newDate);
            existingAppointment.setAppointmentTime(newTime);
        }

        // Update other fields if provided.
        Optional.ofNullable(appointmentDto.getServiceType())
                .ifPresent(type -> existingAppointment.setServiceType(ServiceType.valueOf(type.toUpperCase())));
        Optional.ofNullable(appointmentDto.getStatus())
                .ifPresent(status -> existingAppointment.setStatus(AppointmentStatus.valueOf(status.toUpperCase())));
        Optional.ofNullable(appointmentDto.getNotes()).ifPresent(existingAppointment::setNotes);
        Optional.ofNullable(appointmentDto.getCustomerPhone()).ifPresent(existingAppointment::setCustomerPhone);
        Optional.ofNullable(appointmentDto.getCustomerEmail()).ifPresent(existingAppointment::setCustomerEmail);
        Optional.ofNullable(appointmentDto.getTotalAmount()).ifPresent(existingAppointment::setTotalAmount);
        Optional.ofNullable(appointmentDto.getLoyaltyPointsUsed()).ifPresent(existingAppointment::setLoyaltyPointsUsed);
        Optional.ofNullable(appointmentDto.getLoyaltyDiscount()).ifPresent(existingAppointment::setLoyaltyDiscount);
        Optional.ofNullable(appointmentDto.getOtpVerified()).ifPresent(existingAppointment::setOtpVerified);


        Appointment updatedAppointment = appointmentRepository.save(existingAppointment);
        return modelMapper.map(updatedAppointment, AppointmentDto.class);
    }

    /**
     * Updates the status of an appointment.
     * @param appointmentId The ID of the appointment to update.
     * @param status The new status (e.g., "CONFIRMED", "COMPLETED").
     * @return The updated AppointmentDto.
     * @throws ResourceNotFoundException if the appointment is not found.
     * @throws ApiException if the provided status is invalid.
     */
    @Override
    public AppointmentDto updateAppointmentStatus(Long appointmentId, String status) {
        Appointment existingAppointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment", "id", appointmentId));

        try {
            existingAppointment.setStatus(AppointmentStatus.valueOf(status.toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Invalid appointment status: " + status);
        }

        Appointment updatedAppointment = appointmentRepository.save(existingAppointment);
        return modelMapper.map(updatedAppointment, AppointmentDto.class);
    }

    /**
     * Verifies the OTP for an appointment.
     * If OTP is valid, sets otpVerified to true and changes status to CONFIRMED.
     * @param appointmentId The ID of the appointment.
     * @param otp The OTP provided for verification.
     * @return The updated AppointmentDto.
     * @throws ResourceNotFoundException if the appointment is not found.
     * @throws ApiException if the OTP is invalid or expired.
     */
    @Override
    public AppointmentDto verifyOtp(Long appointmentId, String otp) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment", "id", appointmentId));

        if (otpUtil.validateOtp(appointment.getCustomerEmail(), otp)) {
            appointment.setOtpVerified(true);
            appointment.setStatus(AppointmentStatus.CONFIRMED); // Automatically confirm after OTP verification.
            Appointment updatedAppointment = appointmentRepository.save(appointment);
            return modelMapper.map(updatedAppointment, AppointmentDto.class);
        } else {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Invalid or expired OTP.");
        }
    }

    /**
     * Deletes an appointment from the system by its ID.
     * @param appointmentId The ID of the appointment to delete.
     * @throws ResourceNotFoundException if no appointment is found with the given ID.
     */
    @Override
    public void deleteAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment", "id", appointmentId));
        appointmentRepository.delete(appointment);
    }
}
