package com.beautyplaza.service;

import com.beautyplaza.dto.BeautyServiceDTO;

import java.util.List;

/**
 * Interface for Service-related business logic.
 * Defines the contract for operations on Service data.
 */
public interface BeautyServiceService {
    /**
     * Creates a new service.
     * @param serviceDto The ServiceDto containing service details.
     * @return The created ServiceDto.
     */
    BeautyServiceDTO createService(BeautyServiceDTO serviceDto);

    /**
     * Retrieves a service by its ID.
     * @param serviceId The ID of the service to retrieve.
     * @return The ServiceDto of the found service.
     */
    BeautyServiceDTO getServiceById(Long serviceId);

    /**
     * Retrieves all services.
     * @return A list of all ServiceDtos.
     */
    List<BeautyServiceDTO> getAllServices();

    /**
     * Updates an existing service.
     * @param serviceId The ID of the service to update.
     * @param serviceDto The ServiceDto containing updated details.
     * @return The updated ServiceDto.
     */
    BeautyServiceDTO updateService(Long serviceId, BeautyServiceDTO serviceDto);

    /**
     * Deletes a service by its ID.
     * @param serviceId The ID of the service to delete.
     */
    void deleteService(Long serviceId);
}
