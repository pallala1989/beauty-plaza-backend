// service/LoyaltyService.java (NEW)
package com.beautyplaza.service;

import com.beautyplaza.dto.LoyaltyPointsDTO;
import com.beautyplaza.model.LoyaltyPoint;
import com.beautyplaza.repository.LoyaltyPointRepository;
import com.beautyplaza.repository.AuthUserRepository; // Assuming user exists for loyalty
import com.beautyplaza.request.RedeemPointsRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LoyaltyService {

    private final LoyaltyPointRepository loyaltyPointRepository;
    private final AuthUserRepository authUserRepository; // To check if user exists

    public LoyaltyPointsDTO getUserLoyaltyPoints(Long userId) {
        authUserRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        List<LoyaltyPoint> pointsHistory = loyaltyPointRepository.findByUserIdOrderByTransactionDateDesc(userId);

        int totalPoints = pointsHistory.stream()
                .mapToInt(lp -> lp.getPointsEarned() - lp.getPointsRedeemed())
                .sum();

        LoyaltyPointsDTO dto = new LoyaltyPointsDTO();
        dto.setUserId(userId);
        dto.setTotalPoints(totalPoints);
        dto.setCurrentTier(determineLoyaltyTier(totalPoints)); // Implement tier logic
        return dto;
    }

    public LoyaltyPointsDTO addLoyaltyPoints(Long userId, Integer points, String description) {
        authUserRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        LoyaltyPoint newPoints = new LoyaltyPoint();
        newPoints.setUserId(userId);
        newPoints.setPointsEarned(points);
        newPoints.setPointsRedeemed(0);
        newPoints.setTransactionDate(LocalDateTime.now());
        newPoints.setDescription(description);
        loyaltyPointRepository.save(newPoints);

        return getUserLoyaltyPoints(userId);
    }

    public LoyaltyPointsDTO redeemLoyaltyPoints(RedeemPointsRequest request) {
        LoyaltyPointsDTO currentPoints = getUserLoyaltyPoints(request.getUserId());

        if (currentPoints.getTotalPoints() < request.getPointsToRedeem()) {
            throw new RuntimeException("Insufficient loyalty points.");
        }

        LoyaltyPoint redeemedPoints = new LoyaltyPoint();
        redeemedPoints.setUserId(request.getUserId());
        redeemedPoints.setPointsEarned(0);
        redeemedPoints.setPointsRedeemed(request.getPointsToRedeem());
        redeemedPoints.setTransactionDate(LocalDateTime.now());
        redeemedPoints.setDescription("Points redeemed for service/discount (Appointment ID: " + request.getAppointmentId() + ")");
        loyaltyPointRepository.save(redeemedPoints);

        return getUserLoyaltyPoints(request.getUserId());
    }

    private String determineLoyaltyTier(int points) {
        if (points >= 5000) return "Diamond";
        if (points >= 2000) return "Gold";
        if (points >= 500) return "Silver";
        return "Bronze";
    }

    // Admin functionality
    public List<LoyaltyPointsDTO> getAllLoyaltySummaries() {
        // This would be more complex, involving summing points per user.
        // For simplicity, just returning all individual point transactions for now.
        return loyaltyPointRepository.findAll().stream()
                .map(lp -> {
                    LoyaltyPointsDTO dto = new LoyaltyPointsDTO();
                    dto.setUserId(lp.getUserId());
                    dto.setTotalPoints(lp.getPointsEarned() - lp.getPointsRedeemed()); // This is just for that transaction
                    dto.setCurrentTier("N/A for individual transaction");
                    return dto;
                })
                .collect(Collectors.toList());
    }
}