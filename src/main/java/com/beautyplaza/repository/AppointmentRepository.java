// repository/AppointmentRepository.java
package com.beautyplaza.repository;

import com.beautyplaza.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByTechnicianIdAndAppointmentDate(Long technicianId, LocalDate appointmentDate);
}