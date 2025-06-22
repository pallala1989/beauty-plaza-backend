package com.beautyplaza.controller;

// Importing necessary Spring Framework, DTO classes.
import com.beautyplaza.dto.TechnicianDto;
import com.beautyplaza.service.TechnicianService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * REST Controller for managing Technician entities.
 * Provides endpoints for CRUD operations on technicians, with role-based access control.
 */
@RestController // Marks this class as a REST controller.
@RequestMapping("/api/technicians") // Base path for all endpoints in this controller.
public class TechnicianController {

    @Autowired // Injects TechnicianService for business logic operations on technicians.
    private TechnicianService technicianService;

    /**
     * Creates a new technician. Accessible by ADMIN only.
     * @param technicianDto The TechnicianDto containing technician details.
     * @return ResponseEntity with the created TechnicianDto.
     */
    @PreAuthorize("hasRole('ADMIN')") // Only users with ADMIN role can access this.
    @PostMapping
    public ResponseEntity<TechnicianDto> createTechnician(@Valid @RequestBody TechnicianDto technicianDto) {
        TechnicianDto createdTechnician = technicianService.createTechnician(technicianDto);
        return new ResponseEntity<>(createdTechnician, HttpStatus.CREATED); // Return 201 Created.
    }

    /**
     * Retrieves a technician by ID. Accessible by all authenticated users.
     * @param id The ID of the technician to retrieve.
     * @return ResponseEntity with the TechnicianDto.
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'TECHNICIAN', 'USER')")
    @GetMapping("/{id}")
    public ResponseEntity<TechnicianDto> getTechnicianById(@PathVariable String id) {
        TechnicianDto technician = technicianService.getTechnicianById(id);
        return ResponseEntity.ok(technician); // Return 200 OK.
    }

    /**
     * Retrieves all technicians. Accessible by all authenticated users.
     * @return ResponseEntity with a list of TechnicianDtos.
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'TECHNICIAN', 'USER')")
    @GetMapping
    public ResponseEntity<List<TechnicianDto>> getAllTechnicians() {
        List<TechnicianDto> technicians = technicianService.getAllTechnicians();
        return ResponseEntity.ok(technicians); // Return 200 OK.
    }

    /**
     * Retrieves all available technicians. Accessible by all authenticated users.
     * This is typically used by regular users to see who they can book.
     * @return ResponseEntity with a list of available TechnicianDtos.
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'TECHNICIAN', 'USER')")
    @GetMapping("/available")
    public ResponseEntity<List<TechnicianDto>> getAvailableTechnicians() {
        List<TechnicianDto> availableTechnicians = technicianService.getAvailableTechnicians();
        return ResponseEntity.ok(availableTechnicians); // Return 200 OK.
    }

    /**
     * Updates an existing technician. Accessible by ADMIN or the technician themselves.
     * @param id The ID of the technician to update.
     * @param technicianDto The TechnicianDto containing updated details.
     * @return ResponseEntity with the updated TechnicianDto.
     */
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.username") // Assuming technician ID matches their user ID
    @PutMapping("/{id}")
    public ResponseEntity<TechnicianDto> updateTechnician(@PathVariable String id, @Valid @RequestBody TechnicianDto technicianDto) {
        TechnicianDto updatedTechnician = technicianService.updateTechnician(id, technicianDto);
        return ResponseEntity.ok(updatedTechnician); // Return 200 OK.
    }

    /**
     * Deletes a technician by ID. Accessible by ADMIN only.
     * @param id The ID of the technician to delete.
     * @return ResponseEntity with no content.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTechnician(@PathVariable String id) {
        technicianService.deleteTechnician(id);
        return ResponseEntity.noContent().build(); // Return 204 No Content.
    }
}
