
package com.beautyplaza.service.impl;

// Importing necessary classes for service logic.
import com.beautyplaza.dto.BeautyServiceDTO;
import com.beautyplaza.model.BeautyService;
import com.beautyplaza.exception.ResourceNotFoundException;
import com.beautyplaza.repository.BeautyServiceRepository;
import com.beautyplaza.service.BeautyServiceService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of the ServiceService interface.
 * Handles business logic related to Service entities.
 */
@Service // Marks this class as a Spring Service component.
public class BeautyServiceServiceImpl implements BeautyServiceService {

    @Autowired // Injects ServiceRepository for database interaction.
    private BeautyServiceRepository serviceRepository;

    @Autowired // Injects ModelMapper for object mapping (Entity <-> DTO).
    private ModelMapper modelMapper;

    /**
     * Creates a new service.
     * @param serviceDto The ServiceDto containing service details.
     * @return The created ServiceDto.
     */
    @Override
    public BeautyServiceDTO createService(BeautyServiceDTO serviceDto) {
        // Map DTO to Entity.
        BeautyService service = modelMapper.map(serviceDto, BeautyService.class);
        service.setIsActive(true); // Set new services as active by default.

        // Save the new service to the database.
        BeautyService savedService = serviceRepository.save(service);
        // Map the saved Entity back to DTO and return.
        return modelMapper.map(savedService, BeautyServiceDTO.class);
    }

    /**
     * Retrieves a service by its unique ID.
     * @param serviceId The ID of the service to retrieve.
     * @return The ServiceDto of the found service.
     * @throws ResourceNotFoundException if no service is found with the given ID.
     */
    @Override
    public BeautyServiceDTO getServiceById(Long serviceId) {
        BeautyService service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Service", "id", serviceId));
        return modelMapper.map(service, BeautyServiceDTO.class);
    }

    /**
     * Retrieves a list of all available services.
     * @return A list of ServiceDto objects.
     */
    @Override
    public List<BeautyServiceDTO> getAllServices() {
        List<BeautyService> services = serviceRepository.findAll();
        // Stream through the list of Service entities, map each to a ServiceDto, and collect into a new list.
        return services.stream().map(service -> modelMapper.map(service, BeautyServiceDTO.class)).collect(Collectors.toList());
    }

    /**
     * Updates an existing service's information.
     * @param serviceId The ID of the service to update.
     * @param serviceDto The ServiceDto containing the updated service details.
     * @return The updated ServiceDto.
     * @throws ResourceNotFoundException if no service is found with the given ID.
     */
    @Override
    public BeautyServiceDTO updateService(Long serviceId, BeautyServiceDTO serviceDto) {
        BeautyService existingService = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Service", "id", serviceId));

        // Update fields if provided in DTO.
        Optional.ofNullable(serviceDto.getName()).ifPresent(existingService::setName);
        Optional.ofNullable(serviceDto.getDescription()).ifPresent(existingService::setDescription);
        Optional.ofNullable(serviceDto.getPrice()).ifPresent(existingService::setPrice);
        Optional.ofNullable(serviceDto.getDuration()).ifPresent(existingService::setDuration);
        Optional.ofNullable(serviceDto.getImageUrl()).ifPresent(existingService::setImageUrl);
        Optional.ofNullable(serviceDto.getIsActive()).ifPresent(existingService::setIsActive);

        BeautyService updatedService = serviceRepository.save(existingService);
        return modelMapper.map(updatedService, BeautyServiceDTO.class);
    }

    /**
     * Deletes a service from the system by its ID.
     * @param serviceId The ID of the service to delete.
     * @throws ResourceNotFoundException if no service is found with the given ID.
     */
    @Override
    public void deleteService(Long serviceId) {
        BeautyService service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Service", "id", serviceId));
        serviceRepository.delete(service);
    }
}
