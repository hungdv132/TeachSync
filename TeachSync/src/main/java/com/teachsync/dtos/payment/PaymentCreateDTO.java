package com.teachsync.dtos.payment;

import com.teachsync.dtos.BaseCreateDTO;
import com.teachsync.utils.enums.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for {@link com.teachsync.entities.Payment}
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentCreateDTO extends BaseCreateDTO {
    private Long payerId;
    private Long requestId;
    private PaymentType paymentType;
    private String paymentDesc;
    private Double paymentAmount;
    private LocalDateTime paymentAt;
    private byte[] paymentDoc;
    private String paymentDocLink;
    private Long verifierId;
}