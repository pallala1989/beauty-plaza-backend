// dto/ReferralDTO.java (NEW)
package com.beautyplaza.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReferralDTO {
    private Long id;
    private Long referrerUserId;
    private String referralCode;
    private String status; // PENDING, COMPLETED, CANCELLED
    private LocalDateTime generatedDate;
    private LocalDateTime completedDate;
}