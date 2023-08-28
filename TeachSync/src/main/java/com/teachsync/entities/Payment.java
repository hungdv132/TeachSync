package com.teachsync.entities;

import com.teachsync.utils.enums.PaymentType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "payment")
public class Payment extends BaseEntity {
    @Column(name = "payerId", nullable = false)
    private Long payerId;

    @Column(name = "requestId", nullable = false)
    private Long requestId;

    @Column(name = "paymentType", nullable = false, length = 45)
    private PaymentType paymentType;

    @Lob
    @Column(name = "paymentDesc", nullable = true, length = -1)
    private String paymentDesc;

    @Column(name = "paymentAmount", nullable = false, precision = 0)
    private Double paymentAmount;

    @Column(name = "paymentAt", nullable = false)
    private LocalDateTime paymentAt;

    @Column(name = "paymentDoc", nullable = true)
    private byte[] paymentDoc;

    @Lob
    @Column(name = "paymentDocLink", nullable = true, length = -1)
    private String paymentDocLink;

    @Column(name = "verifierId", nullable = false)
    private Long verifierId;
}