package com.beautyplaza.controller;

// Importing necessary Spring Framework, DTO classes.
import com.beautyplaza.dto.BeautyServiceDTO;
import com.beautyplaza.service.BeautyServiceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * REST Controller for managing Service entities.
 * Provides endpoints for CRUD operations on services, with role-based access control.
 */
@RestController // Marks this class as a REST controller.
@RequestMapping("/api/services") // Base path for all endpoints in this controller.
public class BeautyServiceController {

    @Autowired // Injects ServiceService for business logic operations on services.
    private BeautyServiceService serviceService;

    /**
     * Creates a new service. Accessible by ADMIN only.
     * @param serviceDto The ServiceDto containing service details.
     * @return ResponseEntity with the created ServiceDto.
     */
    @PreAuthorize("hasRole('ADMIN')") // Only users with ADMIN role can access this.
    @PostMapping
    public ResponseEntity<BeautyServiceDTO> createService(@Valid @RequestBody BeautyServiceDTO serviceDto) {
        BeautyServiceDTO createdService = serviceService.createService(serviceDto);
        return new ResponseEntity<>(createdService, HttpStatus.CREATED); // Return 201 Created.
    }

    /**
     * Retrieves a service by ID. Accessible by all authenticated users (ADMIN, TECHNICIAN, USER).
     * @param id The ID of the service to retrieve.
     * @return ResponseEntity with the ServiceDto.
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'TECHNICIAN', 'USER')")
    @GetMapping("/{id}")
    public ResponseEntity<BeautyServiceDTO> getServiceById(@PathVariable Long id) {
        BeautyServiceDTO service = serviceService.getServiceById(id);
        return ResponseEntity.ok(service); // Return 200 OK.
    }

    /**
     * Retrieves all services. Accessible by all authenticated users.
     * @return ResponseEntity with a list of ServiceDtos.
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'TECHNICIAN', 'USER')")
    @GetMapping
    public ResponseEntity<List<BeautyServiceDTO>> getAllServices() {
        List<BeautyServiceDTO> services = serviceService.getAllServices();
        return ResponseEntity.ok(services); // Return 200 OK.
    }

    /**
     * Updates an existing service. Accessible by ADMIN only.
     * @param id The ID of the service to update.
     * @param serviceDto The ServiceDto containing updated details.
     * @return ResponseEntity with the updated ServiceDto.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<BeautyServiceDTO> updateService(@PathVariable Long id, @Valid @RequestBody BeautyServiceDTO serviceDto) {
        BeautyServiceDTO updatedService = serviceService.updateService(id, serviceDto);
        return ResponseEntity.ok(updatedService); // Return 200 OK.
    }

    /**
     * Deletes a service by ID. Accessible by ADMIN only.
     * @param id The ID of the service to delete.
     * @return ResponseEntity with no content.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteService(@PathVariable Long id) {
        serviceService.deleteService(id);
        return ResponseEntity.noContent().build(); // Return 204 No Content.
    }
}
