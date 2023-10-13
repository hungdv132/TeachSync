package com.teachsync.services.payment;

import com.teachsync.dtos.payment.PaymentCreateDTO;
import com.teachsync.dtos.payment.PaymentReadDTO;
import com.teachsync.dtos.payment.PaymentUpdateDTO;
import com.teachsync.dtos.priceLog.PriceLogReadDTO;
import com.teachsync.dtos.request.RequestReadDTO;
import com.teachsync.dtos.user.UserReadDTO;
import com.teachsync.entities.Payment;
import com.teachsync.repositories.PaymentRepository;
import com.teachsync.services.course.CourseService;
import com.teachsync.services.priceLog.PriceLogService;
import com.teachsync.services.request.RequestService;
import com.teachsync.services.user.UserService;
import com.teachsync.utils.MiscUtil;
import com.teachsync.utils.enums.DtoOption;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.teachsync.utils.enums.DtoOption.*;
import static com.teachsync.utils.enums.Status.*;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;

    @Lazy
    @Autowired
    private RequestService requestService;
    @Lazy
    @Autowired
    private UserService userService;
    @Lazy
    @Autowired
    private CourseService courseService;
    @Lazy
    @Autowired
    private PriceLogService priceLogService;

    @Autowired
    private ModelMapper mapper;
    @Autowired
    private MiscUtil miscUtil;


    /* =================================================== CREATE =================================================== */
    @Override
    public Payment createPayment(Payment payment) throws Exception {
        RequestReadDTO requestDTO = requestService.getDTOById(
                payment.getRequestId(),
                List.of(CLAZZ, COURSE_SEMESTER));

        /* Check FK */
        if (requestDTO == null) {
            throw new IllegalArgumentException(
                    "Create error. No FK Request found with id:" + payment.getRequestId());
        }
        if (userService.getAllByIdIn(
                List.of(payment.getPayerId(), payment.getVerifierId(), payment.getCreatedBy())) == null) {
            throw new IllegalArgumentException(
                    "Create error. 1 or more User not found within the id given");
        }

        /* Validate input */
        if (!payment.getPayerId().equals(requestDTO.getRequesterId())) {
            /* TODO: Phụ huynh đóng hộ no throw exception */
            throw new IllegalArgumentException(
                    "Create error. Mismatch id of payer and requester.");
        }
        PriceLogReadDTO priceDTO =
                priceLogService.getDTOByCourseIdAt(
                        requestDTO.getClazz().getCourseId(),
                        requestDTO.getCreatedAt());
        if (!payment.getPaymentAmount().equals(priceDTO.getFinalPrice())){
            /* Giá tiền nộp != giá tiền yêu cầu lúc gửi đơn */
            throw new IllegalArgumentException(
                    "Create error. Mismatch amount payed vs amount declare at Request time: " + requestDTO.getCreatedAt());
        }

        /* Insert DB */
        return paymentRepository.saveAndFlush(payment);
    }

    @Override
    public PaymentReadDTO createPaymentByDTO(PaymentCreateDTO createDTO) throws Exception {
        Payment payment = mapper.map(createDTO, Payment.class);

        payment = createPayment(payment);

        return wrapDTO(payment, null);
    }


    /* =================================================== READ ===================================================== */
    /* id */
    @Override
    public Payment getById(Long id) throws Exception {
        return paymentRepository
                .findByIdAndStatusNot(id, DELETED)
                .orElse(null);
    }
    @Override
    public PaymentReadDTO getDTOById(Long id, Collection<DtoOption> options) throws Exception {
        Payment payment = getById(id);
        if (payment == null) {
            return null;
        }
        return wrapDTO(payment, options);
    }

    /* requestId */
    @Override
    public Payment getByRequestId(Long requestId) throws Exception {
        return paymentRepository
                .findByIdAndStatusNot(requestId, DELETED)
                .orElse(null);
    }
    @Override
    public PaymentReadDTO getDTOByRequestId(Long requestId, Collection<DtoOption> options) throws Exception {
        Payment payment = getByRequestId(requestId);
        if (payment == null) { return null; }
        return wrapDTO(payment, options);
    }

    /* payerId (userId) */
    @Override
    public List<Payment> getAllByPayerId(Long payerId) throws Exception {
        List<Payment> paymentList = paymentRepository.findAllByPayerIdAndStatusNot(payerId, DELETED);
        if (paymentList.isEmpty()) { return null; }
        return paymentList;
    }
    @Override
    public List<PaymentReadDTO> getAllDTOByPayerIdIn(Long payerId, Collection<DtoOption> options) throws Exception {
        List<Payment> paymentList = getAllByPayerId(payerId);
        if (paymentList == null) { return null; }
        return wrapListDTO(paymentList, options);
    }

    @Override
    public Page<Payment> getPageAllByPayerId(Pageable pageable, Long payerId) throws Exception {
        if (pageable == null) { pageable = miscUtil.defaultPaging(); }
        Page<Payment> paymentPage = paymentRepository.findAllByPayerIdAndStatusNot(payerId, DELETED, pageable);
        if (paymentPage.isEmpty()) { return null; }
        return paymentPage;
    }
    @Override
    public Page<PaymentReadDTO> getPageAllDTOByPayerIdIn(
            Pageable pageable, Long payerId, Collection<DtoOption> options) throws Exception {
        Page<Payment> paymentPage = getPageAllByPayerId(pageable, payerId);
        if (paymentPage == null) { return null; }
        return wrapPageDTO(paymentPage, options);
    }


    /* =================================================== UPDATE =================================================== */
    @Override
    public Payment updatePayment(Payment payment) throws Exception {
        return null;
    }

    @Override
    public PaymentReadDTO updatePaymentByDTO(PaymentUpdateDTO updateDTO) throws Exception {
        return null;
    }


    /* =================================================== DELETE =================================================== */


    /* =================================================== WRAPPER ================================================== */
    @Override
    public PaymentReadDTO wrapDTO(Payment payment, Collection<DtoOption> options) throws Exception {
        PaymentReadDTO dto = mapper.map(payment, PaymentReadDTO.class);

        /* Add dependency */
        if (options != null && !options.isEmpty()) {
            if (options.contains(PAYER)) {
                UserReadDTO payer = userService.getDTOById(payment.getPayerId(), options);
                dto.setPayer(payer);
            }

            if (options.contains(REQUEST)) {
                RequestReadDTO request = requestService.getDTOById(payment.getRequestId(), options);
                dto.setRequest(request);
            }
        }

        return dto;
    }

    @Override
    public List<PaymentReadDTO> wrapListDTO(Collection<Payment> paymentCollection, Collection<DtoOption> options) throws Exception {
        List<PaymentReadDTO> dtoList = new ArrayList<>();
        PaymentReadDTO dto;

        Map<Long, UserReadDTO> payerIdUserDTOMap = new HashMap<>();
        Map<Long, RequestReadDTO> requestIdRequestDTOMap = new HashMap<>();

        if (options != null && !options.isEmpty()) {
            Set<Long> payerIdSet = new HashSet<>();
            Set<Long> requestIdSet = new HashSet<>();

            for (Payment payment : paymentCollection) {
                payerIdSet.add(payment.getPayerId());
                requestIdSet.add(payment.getRequestId());
            }

            if (options.contains(PAYER)) {
                payerIdUserDTOMap = userService.mapIdDTOByIdIn(payerIdSet, options);
            }

            if (options.contains(REQUEST)) {
                requestIdRequestDTOMap = requestService.mapIdDTOByIdIn(requestIdSet, options);
            }
        }

        for (Payment payment : paymentCollection) {
            dto = mapper.map(payment, PaymentReadDTO.class);

            /* Add dependency */
            dto.setPayer(payerIdUserDTOMap.get(payment.getPayerId()));

            dto.setRequest(requestIdRequestDTOMap.get(payment.getRequestId()));

            dtoList.add(dto);
        }

        return dtoList;
    }

    @Override
    public Page<PaymentReadDTO> wrapPageDTO(Page<Payment> paymentPage, Collection<DtoOption> options) throws Exception {
        return new PageImpl<>(
                wrapListDTO(paymentPage.getContent(), options),
                paymentPage.getPageable(),
                paymentPage.getTotalPages());
    }
}
