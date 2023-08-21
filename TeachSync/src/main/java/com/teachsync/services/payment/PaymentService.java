package com.teachsync.services.payment;

import com.teachsync.dtos.payment.PaymentCreateDTO;
import com.teachsync.dtos.payment.PaymentReadDTO;
import com.teachsync.dtos.payment.PaymentUpdateDTO;
import com.teachsync.entities.Payment;
import com.teachsync.utils.enums.DtoOption;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;

public interface PaymentService {
    /* =================================================== CREATE =================================================== */
    Payment createPayment(Payment payment) throws Exception;
    PaymentReadDTO createPaymentByDTO(PaymentCreateDTO createDTO) throws Exception; 

    /* =================================================== READ ===================================================== */
    /* id */
    Payment getById(Long id) throws Exception;
    PaymentReadDTO getDTOById(Long id, Collection<DtoOption> options) throws Exception;

    /* requestId */
    Payment getByRequestId(Long requestId) throws Exception;
    PaymentReadDTO getDTOByRequestId(Long requestId, Collection<DtoOption> options) throws Exception;
    
    /* payerId (userId) */
    List<Payment> getAllByPayerId(Long payerId) throws Exception;
    List<PaymentReadDTO> getAllDTOByPayerIdIn(Long payerId, Collection<DtoOption> options) throws Exception;

    Page<Payment> getPageAllByPayerId(Pageable pageable, Long payerId) throws Exception;
    Page<PaymentReadDTO> getPageAllDTOByPayerIdIn(
            Pageable pageable, Long payerId, Collection<DtoOption> options) throws Exception;


    /* =================================================== UPDATE =================================================== */
    Payment updatePayment(Payment payment) throws Exception;
    PaymentReadDTO updatePaymentByDTO(PaymentUpdateDTO updateDTO) throws Exception;


    /* =================================================== DELETE =================================================== */


    /* =================================================== WRAPPER ================================================== */
    PaymentReadDTO wrapDTO(Payment payment, Collection<DtoOption> options) throws Exception;
    List<PaymentReadDTO> wrapListDTO(
            Collection<Payment> paymentCollection, Collection<DtoOption> options) throws Exception;
    Page<PaymentReadDTO> wrapPageDTO(Page<Payment> paymentPage, Collection<DtoOption> options) throws Exception;
}
