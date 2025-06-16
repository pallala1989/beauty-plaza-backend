// service/ReferralService.java (NEW)
package com.beautyplaza.service;

import com.beautyplaza.dto.ReferralDTO;
import com.beautyplaza.model.Referral;
import com.beautyplaza.repository.ReferralRepository;
import com.beautyplaza.repository.AuthUserRepository; // Assuming users are involved
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReferralService {

    private final ReferralRepository referralRepository;
    private final AuthUserRepository authUserRepository; // To check if referrer/referred users exist

    public ReferralDTO generateReferralCode(Long referrerUserId) {
        authUserRepository.findById(referrerUserId)
                .orElseThrow(() -> new RuntimeException("Referrer user not found with ID: " + referrerUserId));

        // In a real app, you'd ensure this code is truly unique and perhaps more human-readable
        String uniqueCode = UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        Referral referral = new Referral();
        referral.setReferrerUserId(referrerUserId);
        referral.setReferralCode(uniqueCode);
        referral.setStatus(Referral.ReferralStatus.PENDING);
        referral.setGeneratedDate(LocalDateTime.now());

        return toReferralDTO(referralRepository.save(referral));
    }

    public Optional<ReferralDTO> getReferralByCode(String referralCode) {
        return referralRepository.findByReferralCode(referralCode)
                .map(this::toReferralDTO);
    }

    public ReferralDTO markReferralCompleted(String referralCode, Long referredUserId) {
        Referral referral = referralRepository.findByReferralCode(referralCode)
                .orElseThrow(() -> new RuntimeException("Referral code not found: " + referralCode));

        if (referral.getStatus() == Referral.ReferralStatus.COMPLETED) {
            throw new RuntimeException("Referral already completed.");
        }

        authUserRepository.findById(referredUserId)
                .orElseThrow(() -> new RuntimeException("Referred user not found with ID: " + referredUserId));

        referral.setReferredUserId(referredUserId);
        referral.setStatus(Referral.ReferralStatus.COMPLETED);
        referral.setCompletedDate(LocalDateTime.now());

        // In a real app, here you'd trigger rewards for both referrer and referred user
        // e.g., loyalty points, gift cards, discounts etc.

        return toReferralDTO(referralRepository.save(referral));
    }

    public List<ReferralDTO> getAllReferrals() { // For admin
        return referralRepository.findAll().stream()
                .map(this::toReferralDTO)
                .collect(Collectors.toList());
    }

    private ReferralDTO toReferralDTO(Referral referral) {
        ReferralDTO dto = new ReferralDTO();
        dto.setId(referral.getId());
        dto.setReferrerUserId(referral.getReferrerUserId());
        dto.setReferralCode(referral.getReferralCode());
        dto.setStatus(referral.getStatus().name());
        dto.setGeneratedDate(referral.getGeneratedDate());
        dto.setCompletedDate(referral.getCompletedDate());
        return dto;
    }
}