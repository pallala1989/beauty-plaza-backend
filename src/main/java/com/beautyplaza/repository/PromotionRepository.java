// repository/PromotionRepository.java (NEW)
package com.beautyplaza.repository;

import com.beautyplaza.model.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Long> {
    Optional<Promotion> findByPromoCodeAndIsActiveTrue(String promoCode);
    List<Promotion> findByIsActiveTrueAndEndDateAfter(LocalDate date);
}