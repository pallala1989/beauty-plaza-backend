// repository/ReferralRepository.java (NEW)
package com.beautyplaza.repository;

import com.beautyplaza.model.Referral;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ReferralRepository extends JpaRepository<Referral, Long> {
    Optional<Referral> findByReferralCode(String referralCode);
    Optional<Referral> findByReferrerUserId(Long referrerUserId);
    Optional<Referral> findByReferredUserId(Long referredUserId);
}