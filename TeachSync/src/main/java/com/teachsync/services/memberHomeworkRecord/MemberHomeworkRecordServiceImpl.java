package com.teachsync.services.memberHomeworkRecord;

import com.teachsync.dtos.memberHomeworkRecord.MemberHomeworkRecordReadDTO;
import com.teachsync.dtos.user.UserReadDTO;
import com.teachsync.entities.Homework;
import com.teachsync.entities.MemberHomeworkRecord;
import com.teachsync.repositories.MemberHomeworkRecordRepository;
import com.teachsync.utils.MiscUtil;
import com.teachsync.utils.enums.DtoOption;
import com.teachsync.utils.enums.Status;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

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

    @Override
    @Transactional
    public void add(MemberHomeworkRecordReadDTO memberHomeworkRecordReadDTO, UserReadDTO userDTO) throws Exception {

        MemberHomeworkRecord memberHomeworkRecord = new MemberHomeworkRecord();
        memberHomeworkRecord.setName("Bài tập - " + MiscUtil.generateRandomName() + " - " + userDTO.getFullName());
        memberHomeworkRecord.setMemberId(memberHomeworkRecordReadDTO.getMemberId());
        memberHomeworkRecord.setHomeworkId(memberHomeworkRecordReadDTO.getHomeworkId());
        memberHomeworkRecord.setSubmission(memberHomeworkRecordReadDTO.getSubmission());//TODO upload file
        memberHomeworkRecord.setSubmissionLink(memberHomeworkRecordReadDTO.getSubmissionLink());

        memberHomeworkRecord.setCreatedBy(userDTO.getId());
        memberHomeworkRecord.setUpdatedBy(userDTO.getId());
        memberHomeworkRecord.setStatus(Status.CREATED);

        MemberHomeworkRecord memberHomeworkRecordDB = memberHomeworkRecordRepository.save(memberHomeworkRecord);
        if (ObjectUtils.isEmpty(memberHomeworkRecordDB)) {
            throw new Exception("Lỗi khi nộp bài tập về nhà");
        }
    }

    /* =================================================== READ ===================================================== */

    @Override
    public MemberHomeworkRecordReadDTO findById(Long id) throws Exception {
        MemberHomeworkRecord memberHomeworkRecord = memberHomeworkRecordRepository.findById(id).orElseThrow(() -> new Exception("không tìm thấy bài tập về nhà"));
        return mapper.map(memberHomeworkRecord, MemberHomeworkRecordReadDTO.class);
    }

    /* =================================================== UPDATE =================================================== */


    /* =================================================== DELETE =================================================== */

    @Override
    public void delete(Long id) throws Exception {
        MemberHomeworkRecord memberHomeworkRecord = memberHomeworkRecordRepository.findById(id).orElseThrow(() -> new Exception("không tìm thấy bài tập về nhà"));
        memberHomeworkRecord.setStatus(Status.DELETED);
        MemberHomeworkRecord memberHomeworkRecordDB = memberHomeworkRecordRepository.save(memberHomeworkRecord);
        if (ObjectUtils.isEmpty(memberHomeworkRecordDB)) {
            throw new Exception("Lỗi khi xóa record bài tập về nhà");
        }
    }

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
