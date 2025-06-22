package com.beautyplaza.controller;

// Importing necessary Spring Framework, DTO, and security classes.
import com.beautyplaza.dto.LoyaltyPointsDto;
import com.beautyplaza.service.LoyaltyPointsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * REST Controller for managing Loyalty Points entities.
 * Provides endpoints for recording and retrieving loyalty point transactions,
 * with role-based access control.
 */
@RestController // Marks this class as a REST controller.
@RequestMapping("/api/loyalty-points") // Base path for all endpoints in this controller.
public class LoyaltyController {

    @Autowired // Injects LoyaltyPointsService for business logic operations on loyalty points.
    private LoyaltyPointsService loyaltyPointsService;

    /**
     * Records a new loyalty points transaction (earn or redeem).
     * Accessible by ADMIN for general management, or by USER for their own transactions.
     * @param loyaltyPointsDto The LoyaltyPointsDto containing transaction details.
     * @return ResponseEntity with the created LoyaltyPointsDto.
     */
    @PreAuthorize("hasRole('ADMIN') or " +
            "(hasRole('USER') and #loyaltyPointsDto.userId == authentication.principal.username)") // Assuming userId is email
    @PostMapping
    public ResponseEntity<LoyaltyPointsDto> recordLoyaltyTransaction(@Valid @RequestBody LoyaltyPointsDto loyaltyPointsDto) {
        LoyaltyPointsDto createdTransaction = loyaltyPointsService.recordLoyaltyTransaction(loyaltyPointsDto);
        return new ResponseEntity<>(createdTransaction, HttpStatus.CREATED); // Return 201 Created.
    }

    /**
     * Retrieves a loyalty points transaction by ID. Accessible by ADMIN or the user associated with the transaction.
     * @param id The ID of the transaction to retrieve.
     * @return ResponseEntity with the LoyaltyPointsDto.
     */
    @PreAuthorize("hasRole('ADMIN') or " +
            "(@loyaltyPointsRepository.findById(#id).orElse(null)?.user?.email == authentication.principal.username)")
    @GetMapping("/{id}")
    public ResponseEntity<LoyaltyPointsDto> getLoyaltyTransactionById(@PathVariable Long id) {
        LoyaltyPointsDto transaction = loyaltyPointsService.getLoyaltyTransactionById(id);
        return ResponseEntity.ok(transaction); // Return 200 OK.
    }

    /**
     * Retrieves all loyalty points transactions for a specific user. Accessible by ADMIN or the user themselves.
     * @param userId The ID of the user.
     * @return ResponseEntity with a list of LoyaltyPointsDtos.
     */
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.username") // Assuming userId is email
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<LoyaltyPointsDto>> getLoyaltyTransactionsByUserId(@PathVariable String userId) {
        List<LoyaltyPointsDto> transactions = loyaltyPointsService.getLoyaltyTransactionsByUserId(userId);
        return ResponseEntity.ok(transactions); // Return 200 OK.
    }

    /**
     * Retrieves the total loyalty points for a specific user. Accessible by ADMIN or the user themselves.
     * @param userId The ID of the user.
     * @return ResponseEntity with the total loyalty points (Integer).
     */
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.username") // Assuming userId is email
    @GetMapping("/user/{userId}/total")
    public ResponseEntity<Integer> getTotalLoyaltyPointsForUser(@PathVariable String userId) {
        Integer totalPoints = loyaltyPointsService.getTotalLoyaltyPointsForUser(userId);
        return ResponseEntity.ok(totalPoints); // Return 200 OK.
    }

    /**
     * Updates an existing loyalty points transaction. Typically for administrative corrections.
     * Accessible by ADMIN only.
     * @param id The ID of the transaction to update.
     * @param loyaltyPointsDto The LoyaltyPointsDto containing updated details.
     * @return ResponseEntity with the updated LoyaltyPointsDto.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<LoyaltyPointsDto> updateLoyaltyTransaction(@PathVariable Long id, @Valid @RequestBody LoyaltyPointsDto loyaltyPointsDto) {
        LoyaltyPointsDto updatedTransaction = loyaltyPointsService.updateLoyaltyTransaction(id, loyaltyPointsDto);
        return ResponseEntity.ok(updatedTransaction); // Return 200 OK.
    }

    /**
     * Deletes a loyalty points transaction by ID. Accessible by ADMIN only.
     * This operation should be used with extreme caution.
     * @param id The ID of the transaction to delete.
     * @return ResponseEntity with no content.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLoyaltyTransaction(@PathVariable Long id) {
        loyaltyPointsService.deleteLoyaltyTransaction(id);
        return ResponseEntity.noContent().build(); // Return 204 No Content.
    }
}
