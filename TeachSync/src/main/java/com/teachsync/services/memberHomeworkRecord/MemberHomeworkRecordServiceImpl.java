package com.teachsync.services.memberHomeworkRecord;

import com.teachsync.dtos.memberHomeworkRecord.MemberHomeworkRecordReadDTO;
import com.teachsync.entities.MemberHomeworkRecord;
import com.teachsync.repositories.MemberHomeworkRecordRepository;
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
public class MemberHomeworkRecordServiceImpl implements MemberHomeworkRecordService {
    @Autowired
    private MemberHomeworkRecordRepository memberHomeworkRecordRepository;

    @Autowired
    private ModelMapper mapper;


    /* =================================================== CREATE =================================================== */


    /* =================================================== READ ===================================================== */


    /* =================================================== UPDATE =================================================== */


    /* =================================================== DELETE =================================================== */


    /* =================================================== WRAPPER ================================================== */
    @Override
    public MemberHomeworkRecordReadDTO wrapDTO(MemberHomeworkRecord memberHomeworkRecord, Collection<DtoOption> options) throws Exception {
        MemberHomeworkRecordReadDTO dto = mapper.map(memberHomeworkRecord, MemberHomeworkRecordReadDTO.class);

        /* Add dependency */
        /* TODO:
        if (options != null && !options.isEmpty()) {
            if (options.contains(DtoOption.FK)) {
                FkReadDTO fkDTO = fkService.getDTOById(memberHomeworkRecord.getFkId());
                dto.setFk(fkDTO);
            }

            if (options.contains(DtoOption.FK_LIST)) {
                List<FkReadDTO> fkDTOList = fkService.getAllDTOById(memberHomeworkRecord.getFkId());
                dto.setFkList(fkDTOList);
            }
        }
        */

        return dto;
    }

    @Override
    public List<MemberHomeworkRecordReadDTO> wrapListDTO(Collection<MemberHomeworkRecord> memberHomeworkRecordCollection, Collection<DtoOption> options) throws Exception {
        List<MemberHomeworkRecordReadDTO> dtoList = new ArrayList<>();
        MemberHomeworkRecordReadDTO dto;

        /* TODO:
        Map<Long, FkReadDTO> fkIdFkDTOMap = new HashMap<>();
        Map<Long, List<FkReadDTO>> fkIdFkDTOListMap = new HashMap<>();

        if (options != null && !options.isEmpty()) {
            Set<Long> fkIdSet = new HashSet<>();

            for (MemberHomeworkRecord memberHomeworkRecord : memberHomeworkRecordCollection) {
                fkIdSet.add(memberHomeworkRecord.getFkId());
            }

            if (options.contains(DtoOption.FK)) {
                fkIdFkDTOMap = fkService.mapIdDTOByIdIn(fkIdSet);
            }

            if (options.contains(DtoOption.FK_LIST)) {
                fkIdFkDTOListMap = fkService.mapIdListDTOByIdIn(fkIdSet);
            }
        }
        */

        for (MemberHomeworkRecord memberHomeworkRecord : memberHomeworkRecordCollection) {
            dto = mapper.map(memberHomeworkRecord, MemberHomeworkRecordReadDTO.class);

            /* Add dependency */
            /*
            dto.setFk(fkIdFkDTOMap.get(memberHomeworkRecord.getFkId()));

            dto.setFkList(fkIdFkDTOListMap.get(memberHomeworkRecord.getFkId()));
            */

            dtoList.add(dto);
        }

        return dtoList;
    }

    @Override
    public Page<MemberHomeworkRecordReadDTO> wrapPageDTO(Page<MemberHomeworkRecord> memberHomeworkRecordPage, Collection<DtoOption> options) throws Exception {
        return new PageImpl<>(
                wrapListDTO(memberHomeworkRecordPage.getContent(), options),
                memberHomeworkRecordPage.getPageable(),
                memberHomeworkRecordPage.getTotalPages());
    }
}
