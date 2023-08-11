package com.teachsync.dtos.priceLog;

import com.teachsync.dtos.BaseReadDTO;
import com.teachsync.utils.enums.PromotionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for {@link com.teachsync.entities.PriceLog}
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PriceLogReadDTO extends BaseReadDTO {
    private Long courseId;

    private String price;

    private Boolean isPromotion;

    private PromotionType promotionType;

    private String promotionAmount;

    private String promotionDesc;

    private LocalDateTime validFrom;

    private LocalDateTime validTo;

    private String finalPrice;
}
