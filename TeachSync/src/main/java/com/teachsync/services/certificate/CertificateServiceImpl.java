package com.teachsync.services.certificate;

import com.teachsync.dtos.certificate.CertificateReadDTO;
import com.teachsync.entities.Certificate;
import com.teachsync.repositories.CertificateRepository;
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
public class CertificateServiceImpl implements CertificateService {
    @Autowired
    private CertificateRepository certificateRepository;

    @Autowired
    private ModelMapper mapper;


    /* =================================================== CREATE =================================================== */


    /* =================================================== READ ===================================================== */


    /* =================================================== UPDATE =================================================== */


    /* =================================================== DELETE =================================================== */


    /* =================================================== WRAPPER ================================================== */
    @Override
    public CertificateReadDTO wrapDTO(Certificate certificate, Collection<DtoOption> options) throws Exception {
        CertificateReadDTO dto = mapper.map(certificate, CertificateReadDTO.class);

        /* Add dependency */
        /* TODO:
        if (options != null && !options.isEmpty()) {
            if (options.contains(DtoOption.FK)) {
                FkReadDTO fkDTO = fkService.getDTOById(certificate.getFkId());
                dto.setFk(fkDTO);
            }

            if (options.contains(DtoOption.FK_LIST)) {
                List<FkReadDTO> fkDTOList = fkService.getAllDTOById(certificate.getFkId());
                dto.setFkList(fkDTOList);
            }
        }
        */

        return dto;
    }

    @Override
    public List<CertificateReadDTO> wrapListDTO(Collection<Certificate> certificateCollection, Collection<DtoOption> options) throws Exception {
        List<CertificateReadDTO> dtoList = new ArrayList<>();
        CertificateReadDTO dto;

        /* TODO:
        Map<Long, FkReadDTO> fkIdFkDTOMap = new HashMap<>();
        Map<Long, List<FkReadDTO>> fkIdFkDTOListMap = new HashMap<>();

        if (options != null && !options.isEmpty()) {
            Set<Long> fkIdSet = new HashSet<>();

            for (Certificate certificate : certificateCollection) {
                fkIdSet.add(certificate.getFkId());
            }

            if (options.contains(DtoOption.FK)) {
                fkIdFkDTOMap = fkService.mapIdDTOByIdIn(fkIdSet);
            }

            if (options.contains(DtoOption.FK_LIST)) {
                fkIdFkDTOListMap = fkService.mapIdListDTOByIdIn(fkIdSet);
            }
        }
        */

        for (Certificate certificate : certificateCollection) {
            dto = mapper.map(certificate, CertificateReadDTO.class);

            /* Add dependency */
            /*
            dto.setFk(fkIdFkDTOMap.get(certificate.getFkId()));

            dto.setFkList(fkIdFkDTOListMap.get(certificate.getFkId()));
            */

            dtoList.add(dto);
        }

        return dtoList;
    }

    @Override
    public Page<CertificateReadDTO> wrapPageDTO(Page<Certificate> certificatePage, Collection<DtoOption> options) throws Exception {
        return new PageImpl<>(
                wrapListDTO(certificatePage.getContent(), options),
                certificatePage.getPageable(),
                certificatePage.getTotalPages());
    }
}
