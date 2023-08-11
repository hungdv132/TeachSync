package com.teachsync.services.certificateIssue;

import com.teachsync.dtos.certificateIssue.CertificateIssueReadDTO;
import com.teachsync.entities.CertificateIssue;
import com.teachsync.repositories.CertificateIssueRepository;
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
public class CertificateIssueServiceImpl implements CertificateIssueService {
    @Autowired
    private CertificateIssueRepository certificateIssueRepository;

    @Autowired
    private ModelMapper mapper;


    /* =================================================== CREATE =================================================== */


    /* =================================================== READ ===================================================== */


    /* =================================================== UPDATE =================================================== */


    /* =================================================== DELETE =================================================== */


    /* =================================================== WRAPPER ================================================== */
    @Override
    public CertificateIssueReadDTO wrapDTO(CertificateIssue certificateIssue, Collection<DtoOption> options) throws Exception {
        CertificateIssueReadDTO dto = mapper.map(certificateIssue, CertificateIssueReadDTO.class);

        /* Add dependency */
        /* TODO:
        if (options != null && !options.isEmpty()) {
            if (options.contains(DtoOption.FK)) {
                FkReadDTO fkDTO = fkService.getDTOById(certificateIssue.getFkId());
                dto.setFk(fkDTO);
            }

            if (options.contains(DtoOption.FK_LIST)) {
                List<FkReadDTO> fkDTOList = fkService.getAllDTOById(certificateIssue.getFkId());
                dto.setFkList(fkDTOList);
            }
        }
        */

        return dto;
    }

    @Override
    public List<CertificateIssueReadDTO> wrapListDTO(Collection<CertificateIssue> certificateIssueCollection, Collection<DtoOption> options) throws Exception {
        List<CertificateIssueReadDTO> dtoList = new ArrayList<>();
        CertificateIssueReadDTO dto;

        /* TODO:
        Map<Long, FkReadDTO> fkIdFkDTOMap = new HashMap<>();
        Map<Long, List<FkReadDTO>> fkIdFkDTOListMap = new HashMap<>();

        if (options != null && !options.isEmpty()) {
            Set<Long> fkIdSet = new HashSet<>();

            for (CertificateIssue certificateIssue : certificateIssueCollection) {
                fkIdSet.add(certificateIssue.getFkId());
            }

            if (options.contains(DtoOption.FK)) {
                fkIdFkDTOMap = fkService.mapIdDTOByIdIn(fkIdSet);
            }

            if (options.contains(DtoOption.FK_LIST)) {
                fkIdFkDTOListMap = fkService.mapIdListDTOByIdIn(fkIdSet);
            }
        }
        */

        for (CertificateIssue certificateIssue : certificateIssueCollection) {
            dto = mapper.map(certificateIssue, CertificateIssueReadDTO.class);

            /* Add dependency */
            /*
            dto.setFk(fkIdFkDTOMap.get(certificateIssue.getFkId()));

            dto.setFkList(fkIdFkDTOListMap.get(certificateIssue.getFkId()));
            */

            dtoList.add(dto);
        }

        return dtoList;
    }

    @Override
    public Page<CertificateIssueReadDTO> wrapPageDTO(Page<CertificateIssue> certificateIssuePage, Collection<DtoOption> options) throws Exception {
        return new PageImpl<>(
                wrapListDTO(certificateIssuePage.getContent(), options),
                certificateIssuePage.getPageable(),
                certificateIssuePage.getTotalPages());
    }
}
