package com.beautyplaza.service.impl;


// Importing necessary classes for service logic.
import com.beautyplaza.dto.TechnicianDto;
import com.beautyplaza.model.Technician;
import com.beautyplaza.exception.ResourceNotFoundException;
import com.beautyplaza.repository.TechnicianRepository;
import com.beautyplaza.repository.UserRepository; // Import UserRepository to link Technician to User
import com.beautyplaza.service.TechnicianService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of the TechnicianService interface.
 * Handles business logic related to Technician entities.
 */
@Service // Marks this class as a Spring Service component.
public class TechnicianServiceImpl implements TechnicianService {

    @Autowired // Injects TechnicianRepository for database interaction.
    private TechnicianRepository technicianRepository;

    @Autowired // Injects UserRepository for associating technicians with users.
    private UserRepository userRepository;

    @Autowired // Injects ModelMapper for object mapping (Entity <-> DTO).
    private ModelMapper modelMapper;

    /**
     * Creates a new technician.
     * If a userId is provided, attempts to link the technician to an existing user.
     * @param technicianDto The TechnicianDto containing technician details.
     * @return The created TechnicianDto.
     * @throws ResourceNotFoundException if the provided userId does not correspond to an existing user.
     */
    @Override
    public TechnicianDto createTechnician(TechnicianDto technicianDto) {
        // Map DTO to Entity.
        Technician technician = modelMapper.map(technicianDto, Technician.class);
        technician.setIsAvailable(true); // Set new technicians as available by default.

        // If a userId is provided in the DTO, attempt to link the technician to an existing user.
        if (technicianDto.getUserId() != null) {
            technician.setUser(userRepository.findById(technicianDto.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User", "id", technicianDto.getUserId())));
        }

        // Save the new technician to the database.
        Technician savedTechnician = technicianRepository.save(technician);
        // Map the saved Entity back to DTO and return.
        return modelMapper.map(savedTechnician, TechnicianDto.class);
    }

    /**
     * Retrieves a technician by their unique ID.
     * @param technicianId The ID of the technician to retrieve.
     * @return The TechnicianDto of the found technician.
     * @throws ResourceNotFoundException if no technician is found with the given ID.
     */
    @Override
    public TechnicianDto getTechnicianById(String technicianId) {
        Technician technician = technicianRepository.findById(technicianId)
                .orElseThrow(() -> new ResourceNotFoundException("Technician", "id", technicianId));
        return modelMapper.map(technician, TechnicianDto.class);
    }

    /**
     * Retrieves a list of all technicians.
     * @return A list of TechnicianDto objects.
     */
    @Override
    public List<TechnicianDto> getAllTechnicians() {
        List<Technician> technicians = technicianRepository.findAll();
        // Stream through the list of Technician entities, map each to a TechnicianDto, and collect into a new list.
        return technicians.stream().map(technician -> modelMapper.map(technician, TechnicianDto.class)).collect(Collectors.toList());
    }

    /**
     * Retrieves a list of all available technicians.
     * @return A list of available TechnicianDto objects.
     */
    @Override
    public List<TechnicianDto> getAvailableTechnicians() {
        List<Technician> availableTechnicians = technicianRepository.findByIsAvailable(true);
        return availableTechnicians.stream().map(technician -> modelMapper.map(technician, TechnicianDto.class)).collect(Collectors.toList());
    }

    /**
     * Updates an existing technician's information.
     * @param technicianId The ID of the technician to update.
     * @param technicianDto The TechnicianDto containing the updated technician details.
     * @return The updated TechnicianDto.
     * @throws ResourceNotFoundException if no technician is found with the given ID.
     */
    @Override
    public TechnicianDto updateTechnician(String technicianId, TechnicianDto technicianDto) {
        Technician existingTechnician = technicianRepository.findById(technicianId)
                .orElseThrow(() -> new ResourceNotFoundException("Technician", "id", technicianId));

        // Update fields if provided in DTO.
        Optional.ofNullable(technicianDto.getName()).ifPresent(existingTechnician::setName);
        Optional.ofNullable(technicianDto.getSpecialties()).ifPresent(existingTechnician::setSpecialties);
        Optional.ofNullable(technicianDto.getIsAvailable()).ifPresent(existingTechnician::setIsAvailable);
        Optional.ofNullable(technicianDto.getImageUrl()).ifPresent(existingTechnician::setImageUrl);

        // Update associated user if userId is provided and different.
        if (technicianDto.getUserId() != null && !technicianDto.getUserId().equals(existingTechnician.getUser() != null ? existingTechnician.getUser().getId() : null)) {
            existingTechnician.setUser(userRepository.findById(technicianDto.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User", "id", technicianDto.getUserId())));
        } else if (technicianDto.getUserId() == null) {
            existingTechnician.setUser(null); // Disassociate user if userId is explicitly set to null.
        }

        Technician updatedTechnician = technicianRepository.save(existingTechnician);
        return modelMapper.map(updatedTechnician, TechnicianDto.class);
    }

    /**
     * Deletes a technician from the system by their ID.
     * @param technicianId The ID of the technician to delete.
     * @throws ResourceNotFoundException if no technician is found with the given ID.
     */
    @Override
    public void deleteTechnician(String technicianId) {
        Technician technician = technicianRepository.findById(technicianId)
                .orElseThrow(() -> new ResourceNotFoundException("Technician", "id", technicianId));
        technicianRepository.delete(technician);
    }
}
