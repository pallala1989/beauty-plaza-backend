package com.beautyplaza.service;

import com.beautyplaza.dto.BeautyServiceDTO;
import com.beautyplaza.exception.ResourceNotFoundException;
import com.beautyplaza.model.BeautyService;
import com.beautyplaza.repository.BeautyServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BeautyServiceService {

    private final BeautyServiceRepository beautyServiceRepository;

    @Autowired
    public BeautyServiceService(BeautyServiceRepository beautyServiceRepository) {
        this.beautyServiceRepository = beautyServiceRepository;
    }

    public List<BeautyServiceDTO> getAllActiveBeautyServices() {
        List<BeautyService> services = beautyServiceRepository.findByIsActiveTrue();
        return services.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public BeautyServiceDTO getBeautyServiceById(Long id) {
        BeautyService beautyService = beautyServiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("BeautyService not found with id: " + id));
        return convertToDto(beautyService);
    }

    public List<BeautyServiceDTO> getAllBeautyServices() {
        List<BeautyService> services = beautyServiceRepository.findAll();
        return services.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public BeautyServiceDTO createBeautyService(BeautyServiceDTO beautyServiceDTO) {
        BeautyService beautyService = convertToEntity(beautyServiceDTO);
        beautyService.setIsActive(true); // New services are active by default
        BeautyService savedService = beautyServiceRepository.save(beautyService);
        return convertToDto(savedService);
    }

    public BeautyServiceDTO updateBeautyService(Long id, BeautyServiceDTO beautyServiceDTO) {
        BeautyService existingService = beautyServiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("BeautyService not found with id: " + id));

        existingService.setName(beautyServiceDTO.getName());
        existingService.setDescription(beautyServiceDTO.getDescription());
        existingService.setPrice(beautyServiceDTO.getPrice());
        existingService.setDurationMinutes(beautyServiceDTO.getDurationMinutes());
        existingService.setImageUrl(beautyServiceDTO.getImageUrl());

        if (beautyServiceDTO.getIsActive() != null) {
            existingService.setIsActive(beautyServiceDTO.getIsActive());
        }

        BeautyService updatedService = beautyServiceRepository.save(existingService);
        return convertToDto(updatedService);
    }

    public void deleteBeautyService(Long id) {
        BeautyService existingService = beautyServiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("BeautyService not found with id: " + id));
        existingService.setIsActive(false); // Soft delete by setting to inactive
        beautyServiceRepository.save(existingService);
    }

    private BeautyServiceDTO convertToDto(BeautyService beautyService) {
        BeautyServiceDTO dto = new BeautyServiceDTO();
        dto.setId(beautyService.getId());
        dto.setName(beautyService.getName());
        dto.setDescription(beautyService.getDescription());
        dto.setPrice(beautyService.getPrice());
        dto.setDurationMinutes(beautyService.getDurationMinutes());
        dto.setImageUrl(beautyService.getImageUrl());
        dto.setIsActive(beautyService.getIsActive());
        return dto;
    }

    private BeautyService convertToEntity(BeautyServiceDTO beautyServiceDTO) {
        BeautyService entity = new BeautyService();
        entity.setName(beautyServiceDTO.getName());
        entity.setDescription(beautyServiceDTO.getDescription());
        entity.setPrice(beautyServiceDTO.getPrice());
        entity.setDurationMinutes(beautyServiceDTO.getDurationMinutes());
        entity.setImageUrl(beautyServiceDTO.getImageUrl());
        entity.setIsActive(beautyServiceDTO.getIsActive() != null ? beautyServiceDTO.getIsActive() : true);
        return entity;
    }
}