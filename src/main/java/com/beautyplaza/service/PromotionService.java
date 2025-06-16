// service/PromotionService.java (NEW)
package com.beautyplaza.service;

import com.beautyplaza.dto.PromotionDTO;
import com.beautyplaza.model.Promotion;
import com.beautyplaza.repository.PromotionRepository;
import com.beautyplaza.request.ApplyPromotionRequest;
import com.beautyplaza.request.PromotionCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PromotionService {

    private final PromotionRepository promotionRepository;

    public List<PromotionDTO> getAllActivePromotions() {
        return promotionRepository.findByIsActiveTrueAndEndDateAfter(LocalDate.now()).stream()
                .map(this::toPromotionDTO)
                .collect(Collectors.toList());
    }

    public Optional<PromotionDTO> getPromotionById(Long id) {
        return promotionRepository.findById(id)
                .map(this::toPromotionDTO);
    }

    public Optional<PromotionDTO> getPromotionByCode(String promoCode) {
        return promotionRepository.findByPromoCodeAndIsActiveTrue(promoCode)
                .map(this::toPromotionDTO);
    }

    public String applyPromotion(ApplyPromotionRequest request) {
        Promotion promotion = promotionRepository.findByPromoCodeAndIsActiveTrue(request.getPromoCode())
                .orElseThrow(() -> new RuntimeException("Invalid or inactive promotion code."));

        if (promotion.getEndDate() != null && promotion.getEndDate().isBefore(LocalDate.now())) {
            throw new RuntimeException("Promotion has expired.");
        }

        // Here, you would apply the promotion logic to the booking/order specified by bookingId/userId.
        // This is complex business logic that would involve updating the total amount of the booking,
        // and possibly logging the promotion usage.
        // For now, it's a placeholder.

        return "Promotion '" + promotion.getName() + "' applied successfully to booking " + request.getBookingId() + " (Placeholder)";
    }

    // Admin functionality
    public PromotionDTO createPromotion(PromotionCreateRequest request) {
        Promotion promotion = new Promotion();
        promotion.setName(request.getName());
        promotion.setDescription(request.getDescription());
        promotion.setPromoCode(request.getPromoCode());
        promotion.setDiscountType(Promotion.DiscountType.valueOf(request.getDiscountType()));
        promotion.setDiscountValue(request.getDiscountValue());
        promotion.setStartDate(request.getStartDate());
        promotion.setEndDate(request.getEndDate());
        promotion.setActive(true); // Default to active

        return toPromotionDTO(promotionRepository.save(promotion));
    }

    public PromotionDTO updatePromotion(Long id, PromotionCreateRequest request) {
        Promotion existingPromotion = promotionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Promotion not found with ID: " + id));

        existingPromotion.setName(request.getName());
        existingPromotion.setDescription(request.getDescription());
        existingPromotion.setPromoCode(request.getPromoCode());
        existingPromotion.setDiscountType(Promotion.DiscountType.valueOf(request.getDiscountType()));
        existingPromotion.setDiscountValue(request.getDiscountValue());
        existingPromotion.setStartDate(request.getStartDate());
        existingPromotion.setEndDate(request.getEndDate());
        // existingPromotion.setActive(request.isActive()); // Could be updated via request

        return toPromotionDTO(promotionRepository.save(existingPromotion));
    }

    public void deletePromotion(Long id) {
        promotionRepository.deleteById(id);
    }

    private PromotionDTO toPromotionDTO(Promotion promotion) {
        PromotionDTO dto = new PromotionDTO();
        dto.setId(promotion.getId());
        dto.setName(promotion.getName());
        dto.setDescription(promotion.getDescription());
        dto.setPromoCode(promotion.getPromoCode());
        dto.setDiscountType(promotion.getDiscountType().name());
        dto.setDiscountValue(promotion.getDiscountValue());
        dto.setStartDate(promotion.getStartDate());
        dto.setEndDate(promotion.getEndDate());
        dto.setActive(promotion.isActive());
        return dto;
    }
}