// repository/LoyaltyPointRepository.java (NEW)
package com.beautyplaza.repository;

import com.beautyplaza.model.LoyaltyPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Repository interface for LoyaltyPoints entities.
 * Extends JpaRepository to provide standard CRUD operations for LoyaltyPoints objects.
 * The generic parameters are: LoyaltyPoints (the entity type) and Long (the ID type of the entity).
 */
public interface LoyaltyPointsRepository extends JpaRepository<LoyaltyPoint, Long> {

    /**
     * Finds all loyalty point transactions for a specific user.
     * @param userId The ID of the user.
     * @return A list of LoyaltyPoints transactions for the given user.
     */
    List<LoyaltyPoint> findByUserId(String userId);

    /**
     * Calculates the total loyalty points for a user.
     * This custom query sums the 'points' based on transaction type.
     * Earned points are added, redeemed points are subtracted.
     * The return type is Long, as SUM typically returns a Long in JPA.
     * @param userId The ID of the user.
     * @return The total current loyalty points for the user as a Long.

    @Query("SELECT COALESCE(SUM(CASE WHEN lp.transactionType = 'EARNED' THEN lp.points ELSE -lp.points END), 0L) " +
            "FROM com.beautyplaza.beautyplaza.entity.LoyaltyPoints lp WHERE lp.user.id = :userId") // Corrected entity path
    Long calculateTotalPointsByUserId(String userId);    */
}
