package com.teachsync.dtos.priceLog;

import com.teachsync.dtos.BaseCreateDTO;
import com.teachsync.dtos.BaseUpdateDTO;
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
public class PriceLogUpdateDTO extends BaseUpdateDTO {
    private Long courseId;

    private Double price;

    private Boolean isPromotion;

    private PromotionType promotionType;

    private Double promotionAmount;

    private String promotionDesc;

    private LocalDateTime validFrom;

    private LocalDateTime validTo;
}
