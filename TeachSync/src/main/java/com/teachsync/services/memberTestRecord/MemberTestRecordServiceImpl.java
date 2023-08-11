package com.teachsync.services.memberTestRecord;

import com.teachsync.dtos.memberTestRecord.MemberTestRecordReadDTO;
import com.teachsync.entities.MemberTestRecord;
import com.teachsync.repositories.MemberTestRecordRepository;
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
public class MemberTestRecordServiceImpl implements MemberTestRecordService {
    @Autowired
    private MemberTestRecordRepository memberTestRecordRepository;

    @Autowired
    private ModelMapper mapper;


    /* =================================================== CREATE =================================================== */


    /* =================================================== READ ===================================================== */


    /* =================================================== UPDATE =================================================== */


    /* =================================================== DELETE =================================================== */


    /* =================================================== WRAPPER ================================================== */
    @Override
    public MemberTestRecordReadDTO wrapDTO(MemberTestRecord memberTestRecord, Collection<DtoOption> options) throws Exception {
        MemberTestRecordReadDTO dto = mapper.map(memberTestRecord, MemberTestRecordReadDTO.class);

        /* Add dependency */
        /* TODO:
        if (options != null && !options.isEmpty()) {
            if (options.contains(DtoOption.FK)) {
                FkReadDTO fkDTO = fkService.getDTOById(memberTestRecord.getFkId());
                dto.setFk(fkDTO);
            }

            if (options.contains(DtoOption.FK_LIST)) {
                List<FkReadDTO> fkDTOList = fkService.getAllDTOById(memberTestRecord.getFkId());
                dto.setFkList(fkDTOList);
            }
        }
        */

        return dto;
    }

    @Override
    public List<MemberTestRecordReadDTO> wrapListDTO(Collection<MemberTestRecord> memberTestRecordCollection, Collection<DtoOption> options) throws Exception {
        List<MemberTestRecordReadDTO> dtoList = new ArrayList<>();
        MemberTestRecordReadDTO dto;

        /* TODO:
        Map<Long, FkReadDTO> fkIdFkDTOMap = new HashMap<>();
        Map<Long, List<FkReadDTO>> fkIdFkDTOListMap = new HashMap<>();

        if (options != null && !options.isEmpty()) {
            Set<Long> fkIdSet = new HashSet<>();

            for (MemberTestRecord memberTestRecord : memberTestRecordCollection) {
                fkIdSet.add(memberTestRecord.getFkId());
            }

            if (options.contains(DtoOption.FK)) {
                fkIdFkDTOMap = fkService.mapIdDTOByIdIn(fkIdSet);
            }

            if (options.contains(DtoOption.FK_LIST)) {
                fkIdFkDTOListMap = fkService.mapIdListDTOByIdIn(fkIdSet);
            }
        }
        */

        for (MemberTestRecord memberTestRecord : memberTestRecordCollection) {
            dto = mapper.map(memberTestRecord, MemberTestRecordReadDTO.class);

            /* Add dependency */
            /*
            dto.setFk(fkIdFkDTOMap.get(memberTestRecord.getFkId()));

            dto.setFkList(fkIdFkDTOListMap.get(memberTestRecord.getFkId()));
            */

            dtoList.add(dto);
        }

        return dtoList;
    }

    @Override
    public Page<MemberTestRecordReadDTO> wrapPageDTO(Page<MemberTestRecord> memberTestRecordPage, Collection<DtoOption> options) throws Exception {
        return new PageImpl<>(
                wrapListDTO(memberTestRecordPage.getContent(), options),
                memberTestRecordPage.getPageable(),
                memberTestRecordPage.getTotalPages());
    }
}
