// repository/LoyaltyPointRepository.java (NEW)
package com.beautyplaza.repository;

import com.beautyplaza.model.LoyaltyPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface LoyaltyPointRepository extends JpaRepository<LoyaltyPoint, Long> {
    List<LoyaltyPoint> findByUserIdOrderByTransactionDateDesc(Long userId);
    // You might add more complex queries to sum points etc.
}