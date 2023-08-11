package com.teachsync.services.payment;

import com.teachsync.dtos.payment.PaymentReadDTO;
import com.teachsync.entities.Payment;
import com.teachsync.repositories.PaymentRepository;
import com.teachsync.utils.enums.DtoOption;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private ModelMapper mapper;


    /* =================================================== CREATE =================================================== */


    /* =================================================== READ ===================================================== */


    /* =================================================== UPDATE =================================================== */


    /* =================================================== DELETE =================================================== */


    /* =================================================== WRAPPER ================================================== */
    @Override
    public PaymentReadDTO wrapDTO(Payment payment, Collection<DtoOption> options) throws Exception {
        PaymentReadDTO dto = mapper.map(payment, PaymentReadDTO.class);

        /* Add dependency */
        /* TODO:
        if (options != null && !options.isEmpty()) {
            if (options.contains(DtoOption.FK)) {
                FkReadDTO fkDTO = fkService.getDTOById(payment.getFkId());
                dto.setFk(fkDTO);
            }

            if (options.contains(DtoOption.FK_LIST)) {
                List<FkReadDTO> fkDTOList = fkService.getAllDTOById(payment.getFkId());
                dto.setFkList(fkDTOList);
            }
        }
        */

        return dto;
    }

    @Override
    public List<PaymentReadDTO> wrapListDTO(Collection<Payment> paymentCollection, Collection<DtoOption> options) throws Exception {
        List<PaymentReadDTO> dtoList = new ArrayList<>();
        PaymentReadDTO dto;

        /* TODO:
        Map<Long, FkReadDTO> fkIdFkDTOMap = new HashMap<>();
        Map<Long, List<FkReadDTO>> fkIdFkDTOListMap = new HashMap<>();

        if (options != null && !options.isEmpty()) {
            Set<Long> fkIdSet = new HashSet<>();

            for (Payment payment : paymentCollection) {
                fkIdSet.add(payment.getFkId());
            }

            if (options.contains(DtoOption.FK)) {
                fkIdFkDTOMap = fkService.mapIdDTOByIdIn(fkIdSet);
            }

            if (options.contains(DtoOption.FK_LIST)) {
                fkIdFkDTOListMap = fkService.mapIdListDTOByIdIn(fkIdSet);
            }
        }
        */

        for (Payment payment : paymentCollection) {
            dto = mapper.map(payment, PaymentReadDTO.class);

            /* Add dependency */
            /*
            dto.setFk(fkIdFkDTOMap.get(payment.getFkId()));

            dto.setFkList(fkIdFkDTOListMap.get(payment.getFkId()));
            */

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
