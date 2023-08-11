package com.teachsync.services.payment;

import com.teachsync.dtos.payment.PaymentReadDTO;
import com.teachsync.entities.Payment;
import com.teachsync.utils.enums.DtoOption;
import org.springframework.data.domain.Page;

import java.util.Collection;
import java.util.List;

public interface PaymentService {
    /* =================================================== CREATE =================================================== */


    /* =================================================== READ ===================================================== */


    /* =================================================== UPDATE =================================================== */


    /* =================================================== DELETE =================================================== */


    /* =================================================== WRAPPER ================================================== */
    PaymentReadDTO wrapDTO(Payment payment, Collection<DtoOption> options) throws Exception;
    List<PaymentReadDTO> wrapListDTO(Collection<Payment> paymentCollection, Collection<DtoOption> options) throws Exception;
    Page<PaymentReadDTO> wrapPageDTO(Page<Payment> paymentPage, Collection<DtoOption> options) throws Exception;
}
