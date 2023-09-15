package com.teachsync.dtos.payment;
import com.teachsync.dtos.BaseReadDTO;
import com.teachsync.dtos.request.RequestReadDTO;
import com.teachsync.dtos.user.UserReadDTO;
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
public class PaymentReadDTO extends BaseReadDTO {
    private Long payerId;
    private UserReadDTO payer;

    private Long requestId;
    private RequestReadDTO request;

    private PaymentType paymentType;

    private String paymentDesc;

    private Double paymentAmount;

    private LocalDateTime paymentAt;

    private byte[] paymentDoc;
    private String paymentDocLink;

    private Long verifierId;
}