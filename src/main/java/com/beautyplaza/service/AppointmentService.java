// service/AppointmentService.java (Renamed from BookingService for clarity)
package com.beautyplaza.service;

import com.beautyplaza.dto.AppointmentDTO;
import com.beautyplaza.model.Appointment;
import com.beautyplaza.model.BeautyService;
import com.beautyplaza.model.Technician;
import com.beautyplaza.repository.AppointmentRepository;
import com.beautyplaza.repository.BeautyServiceRepository;
import com.beautyplaza.repository.TechnicianRepository;
import com.beautyplaza.request.BookingRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service; // Changed from Component to Service

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentService { // Renamed from BookingService

    private final AppointmentRepository appointmentRepository;
    private final BeautyServiceRepository beautyServiceRepository;
    private final TechnicianRepository technicianRepository;

    public List<LocalTime> getAvailableSlots(Long technicianId, LocalDate date, int serviceDurationInMinutes) {
        List<LocalTime> allSlots = new ArrayList<>();
        // Example: Salon open from 9 AM to 7 PM
        LocalTime openingTime = LocalTime.of(9, 0);
        LocalTime closingTime = LocalTime.of(19, 0);

        List<Appointment> existingAppointments = appointmentRepository.findByTechnicianIdAndAppointmentDate(technicianId, date);

        LocalTime currentTimeSlot = openingTime;
        while (currentTimeSlot.isBefore(closingTime)) {
            boolean isBooked = false;
            for (Appointment appt : existingAppointments) {
                // Simplified check: assuming a slot is 30 mins and any overlap blocks the slot.
                // A more robust system would check if the service duration fits into available gaps.
                if (currentTimeSlot.equals(appt.getAppointmentTime())) {
                    isBooked = true;
                    break;
                }
            }
            if (!isBooked) {
                allSlots.add(currentTimeSlot);
            }
            currentTimeSlot = currentTimeSlot.plusMinutes(30); // Assuming 30-min slot intervals
        }
        return allSlots;
    }

    public AppointmentDTO createBooking(BookingRequest bookingRequest) {
        BeautyService beautyService = beautyServiceRepository.findById(bookingRequest.getBeautyServiceId())
                .orElseThrow(() -> new RuntimeException("Beauty Service not found"));
        Technician technician = technicianRepository.findById(bookingRequest.getTechnicianId())
                .orElseThrow(() -> new RuntimeException("Technician not found"));

        Appointment appointment = new Appointment();
        appointment.setBeautyServiceId(beautyService.getId());
        appointment.setBeautyServiceName(beautyService.getName());
        appointment.setTechnicianId(technician.getId());
        appointment.setTechnicianName(technician.getName());
        appointment.setAppointmentDate(bookingRequest.getAppointmentDate());
        appointment.setAppointmentTime(bookingRequest.getAppointmentTime());
        appointment.setCustomerName(bookingRequest.getCustomerName());
        appointment.setCustomerEmail(bookingRequest.getCustomerEmail());
        appointment.setCustomerPhone(bookingRequest.getCustomerPhone());
        appointment.setNotes(bookingRequest.getNotes());
        appointment.setServiceType(bookingRequest.getServiceType());
        appointment.setStatus("SCHEDULED");
        appointment.setTotalAmount(beautyService.getPrice());

        Appointment savedAppointment = appointmentRepository.save(appointment);
        return toAppointmentDTO(savedAppointment);
    }

    private AppointmentDTO toAppointmentDTO(Appointment appointment) {
        AppointmentDTO dto = new AppointmentDTO();
        dto.setId(appointment.getId());
        dto.setBeautyServiceId(appointment.getBeautyServiceId());
        dto.setBeautyServiceName(appointment.getBeautyServiceName());
        dto.setTechnicianId(appointment.getTechnicianId());
        dto.setTechnicianName(appointment.getTechnicianName());
        dto.setCustomerName(appointment.getCustomerName());
        dto.setCustomerEmail(appointment.getCustomerEmail());
        dto.setAppointmentDate(appointment.getAppointmentDate());
        dto.setAppointmentTime(appointment.getAppointmentTime());
        dto.setStatus(appointment.getStatus());
        dto.setTotalAmount(appointment.getTotalAmount());
        return dto;
    }
}