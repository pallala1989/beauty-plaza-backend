// service/LoyaltyService.java (NEW)
package com.beautyplaza.service;


// Importing the LoyaltyPointsDto and Java utilities.
import com.beautyplaza.dto.LoyaltyPointsDto;
import java.util.List;

/**
 * Interface for Loyalty Points-related business logic.
 * Defines the contract for operations on LoyaltyPoints data.
 */
public interface LoyaltyPointsService {
    /**
     * Records a new loyalty points transaction (earning or redeeming).
     * @param loyaltyPointsDto The LoyaltyPointsDto containing transaction details.
     * @return The created LoyaltyPointsDto.
     */
    LoyaltyPointsDto recordLoyaltyTransaction(LoyaltyPointsDto loyaltyPointsDto);

    /**
     * Retrieves a loyalty points transaction by its ID.
     * @param transactionId The ID of the transaction to retrieve.
     * @return The LoyaltyPointsDto of the found transaction.
     */
    LoyaltyPointsDto getLoyaltyTransactionById(Long transactionId);

    /**
     * Retrieves all loyalty points transactions for a specific user.
     * @param userId The ID of the user.
     * @return A list of LoyaltyPointsDtos for the given user.
     */
    List<LoyaltyPointsDto> getLoyaltyTransactionsByUserId(String userId);

    /**
     * Calculates the total loyalty points for a specific user.
     * @param userId The ID of the user.
     * @return The total loyalty points for the user.
     */
    Integer getTotalLoyaltyPointsForUser(String userId);

    /**
     * Updates an existing loyalty points transaction.
     * Note: Typically, loyalty transactions are immutable after creation.
     * This method is provided for administrative corrections if needed.
     * @param transactionId The ID of the transaction to update.
     * @param loyaltyPointsDto The LoyaltyPointsDto containing updated details.
     * @return The updated LoyaltyPointsDto.
     */
    LoyaltyPointsDto updateLoyaltyTransaction(Long transactionId, LoyaltyPointsDto loyaltyPointsDto);

    /**
     * Deletes a loyalty points transaction by its ID.
     * Note: Deleting loyalty transactions should be handled with care as it affects user balances.
     * @param transactionId The ID of the transaction to delete.
     */
    void deleteLoyaltyTransaction(Long transactionId);
}
