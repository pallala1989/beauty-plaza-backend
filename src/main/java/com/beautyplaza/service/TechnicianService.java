// service/TechnicianService.java (NEW - from SalonDataService)
package com.beautyplaza.service;

import com.beautyplaza.dto.TechnicianDTO;
import com.beautyplaza.model.Technician;
import com.beautyplaza.repository.TechnicianRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service; // Changed from Component to Service

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TechnicianService {

    private final TechnicianRepository technicianRepository;

    public List<TechnicianDTO> getAllAvailableTechnicians() {
        return technicianRepository.findAll().stream()
                .filter(Technician::isAvailable)
                .map(this::toTechnicianDTO)
                .collect(Collectors.toList());
    }

    public Optional<TechnicianDTO> getTechnicianById(Long id) {
        return technicianRepository.findById(id)
                .map(this::toTechnicianDTO);
    }

    // Admin-like functionality to add/update technicians
    public TechnicianDTO createTechnician(TechnicianDTO technicianDTO) {
        Technician technician = toTechnicianEntity(technicianDTO);
        technician.setId(null); // Ensure new entity
        return toTechnicianDTO(technicianRepository.save(technician));
    }

    public TechnicianDTO updateTechnician(Long id, TechnicianDTO technicianDTO) {
        Technician existingTechnician = technicianRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Technician not found with ID: " + id));

        existingTechnician.setName(technicianDTO.getName());
        existingTechnician.setSpecialties(technicianDTO.getSpecialties());
        // isAvailable might be updated via a separate endpoint/method

        return toTechnicianDTO(technicianRepository.save(existingTechnician));
    }

    public void deleteTechnician(Long id) {
        technicianRepository.deleteById(id);
    }

    public void toggleTechnicianAvailability(Long id, boolean isAvailable) {
        Technician technician = technicianRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Technician not found with ID: " + id));
        technician.setAvailable(isAvailable);
        technicianRepository.save(technician);
    }

    private TechnicianDTO toTechnicianDTO(Technician technician) {
        TechnicianDTO dto = new TechnicianDTO();
        dto.setId(technician.getId());
        dto.setName(technician.getName());
        dto.setSpecialties(technician.getSpecialties());
        return dto;
    }

    private Technician toTechnicianEntity(TechnicianDTO dto) {
        Technician entity = new Technician();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setSpecialties(dto.getSpecialties());
        entity.setAvailable(true); // Default to available
        return entity;
    }
}