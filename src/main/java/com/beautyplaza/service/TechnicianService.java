// service/TechnicianService.java (NEW - from SalonDataService)
package com.beautyplaza.service;


// Importing the TechnicianDto and Java utilities.
import com.beautyplaza.dto.TechnicianDto;
import java.util.List;

/**
 * Interface for Technician-related business logic.
 * Defines the contract for operations on Technician data.
 */
public interface TechnicianService {
    /**
     * Creates a new technician.
     * @param technicianDto The TechnicianDto containing technician details.
     * @return The created TechnicianDto.
     */
    TechnicianDto createTechnician(TechnicianDto technicianDto);

    /**
     * Retrieves a technician by their ID.
     * @param technicianId The ID of the technician to retrieve.
     * @return The TechnicianDto of the found technician.
     */
    TechnicianDto getTechnicianById(String technicianId);

    /**
     * Retrieves all technicians.
     * @return A list of all TechnicianDtos.
     */
    List<TechnicianDto> getAllTechnicians();

    /**
     * Retrieves all available technicians.
     * @return A list of available TechnicianDtos.
     */
    List<TechnicianDto> getAvailableTechnicians();

    /**
     * Updates an existing technician.
     * @param technicianId The ID of the technician to update.
     * @param technicianDto The TechnicianDto containing updated details.
     * @return The updated TechnicianDto.
     */
    TechnicianDto updateTechnician(String technicianId, TechnicianDto technicianDto);

    /**
     * Deletes a technician by their ID.
     * @param technicianId The ID of the technician to delete.
     */
    void deleteTechnician(String technicianId);
}
