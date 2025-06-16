// service/GiftCardService.java (NEW)
package com.beautyplaza.service;

import com.beautyplaza.dto.GiftCardDTO;
import com.beautyplaza.model.GiftCard;
import com.beautyplaza.repository.GiftCardRepository;
import com.beautyplaza.request.GiftCardCreateRequest;
import com.beautyplaza.request.RedeemGiftCardRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GiftCardService {

    private final GiftCardRepository giftCardRepository;

    public GiftCardDTO createGiftCard(GiftCardCreateRequest request) {
        GiftCard giftCard = new GiftCard();
        giftCard.setCode(generateUniqueGiftCardCode()); // Generate unique code
        giftCard.setInitialAmount(request.getAmount());
        giftCard.setCurrentBalance(request.getAmount());
        giftCard.setExpiryDate(request.getExpiryDate() != null ? request.getExpiryDate() : LocalDate.now().plusYears(1)); // Default 1 year
        giftCard.setActive(true);
        giftCard.setPurchasedByUserId(request.getPurchasedByUserId());

        return toGiftCardDTO(giftCardRepository.save(giftCard));
    }

    public Optional<GiftCardDTO> getGiftCardDetails(String cardCode) {
        return giftCardRepository.findByCode(cardCode)
                .map(this::toGiftCardDTO);
    }

    public GiftCardDTO redeemGiftCard(RedeemGiftCardRequest request) {
        GiftCard giftCard = giftCardRepository.findByCode(request.getCardCode())
                .orElseThrow(() -> new RuntimeException("Gift card not found: " + request.getCardCode()));

        if (!giftCard.isActive() || (giftCard.getExpiryDate() != null && giftCard.getExpiryDate().isBefore(LocalDate.now()))) {
            throw new RuntimeException("Gift card is inactive or expired.");
        }

        if (giftCard.getCurrentBalance().compareTo(request.getRedeemAmount()) < 0) {
            throw new RuntimeException("Insufficient balance on gift card.");
        }

        giftCard.setCurrentBalance(giftCard.getCurrentBalance().subtract(request.getRedeemAmount()));
        if (giftCard.getCurrentBalance().compareTo(BigDecimal.ZERO) == 0) {
            giftCard.setActive(false); // Deactivate if balance is zero
        }

        return toGiftCardDTO(giftCardRepository.save(giftCard));
    }

    public List<GiftCardDTO> getAllGiftCards() { // For admin
        return giftCardRepository.findAll().stream()
                .map(this::toGiftCardDTO)
                .collect(Collectors.toList());
    }

    private String generateUniqueGiftCardCode() {
        // Simple UUID based code. In production, ensure uniqueness with database check.
        return UUID.randomUUID().toString().substring(0, 12).toUpperCase();
    }

    private GiftCardDTO toGiftCardDTO(GiftCard giftCard) {
        GiftCardDTO dto = new GiftCardDTO();
        dto.setId(giftCard.getId());
        dto.setCode(giftCard.getCode());
        dto.setCurrentBalance(giftCard.getCurrentBalance());
        dto.setExpiryDate(giftCard.getExpiryDate());
        dto.setActive(giftCard.isActive());
        return dto;
    }
}