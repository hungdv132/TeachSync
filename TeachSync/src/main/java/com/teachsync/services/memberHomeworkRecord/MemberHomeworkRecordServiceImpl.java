package com.teachsync.services.memberHomeworkRecord;

import com.teachsync.dtos.clazzMember.ClazzMemberReadDTO;
import com.teachsync.dtos.homework.HomeworkReadDTO;
import com.teachsync.dtos.memberHomeworkRecord.MemberHomeworkRecordCreateDTO;
import com.teachsync.dtos.memberHomeworkRecord.MemberHomeworkRecordReadDTO;
import com.teachsync.dtos.user.UserReadDTO;
import com.teachsync.entities.Homework;
import com.teachsync.entities.MemberHomeworkRecord;
import com.teachsync.repositories.HomeworkRepository;
import com.teachsync.repositories.MemberHomeworkRecordRepository;
import com.teachsync.services.clazzMember.ClazzMemberService;
import com.teachsync.services.homework.HomeworkService;
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

import java.util.*;

@Service
public class MemberHomeworkRecordServiceImpl implements MemberHomeworkRecordService {
    @Autowired
    private MemberHomeworkRecordRepository memberHomeworkRecordRepository;

    @Autowired
    private ClazzMemberService clazzMemberService;
    @Autowired
    private HomeworkService homeworkService;

    @Autowired
    private ModelMapper mapper;


    /* =================================================== CREATE =================================================== */

    @Override
    @Transactional
    public void add(MemberHomeworkRecordCreateDTO createDTO) throws Exception {
        // TODO: Check có học lớp này hay không, đã đến giờ/quá hạn nộp bài, trùng lặp...

        MemberHomeworkRecord record = mapper.map(createDTO, MemberHomeworkRecord.class);
        /* Dùng mapper tương ứng a.setX( b.getX() ) cho mọi thuộc tính giống tên giống kiểu */

        MemberHomeworkRecord memberHomeworkRecordDB = memberHomeworkRecordRepository.save(record);
        if (ObjectUtils.isEmpty(memberHomeworkRecordDB)) {
            throw new Exception("Lỗi khi nộp bài tập về nhà");
        }
    }

    /* =================================================== READ ===================================================== */

    @Override
    public MemberHomeworkRecordReadDTO findById(Long id) throws Exception {
        MemberHomeworkRecord memberHomeworkRecord =
                memberHomeworkRecordRepository
                        .findById(id)
                        .orElseThrow(() -> new Exception("không tìm thấy bài tập về nhà"));
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
        if (options != null && !options.isEmpty()) {
            if (options.contains(DtoOption.MEMBER)) {
                ClazzMemberReadDTO memberDTO = clazzMemberService.getDTOById(memberHomeworkRecord.getMemberId(), options);
                dto.setMember(memberDTO);
            }

            /* TODO:
            if (options.contains(DtoOption.HOMEWORK)) {
                HomeworkReadDTO homeworkDTO = homeworkService.getDTOById(memberHomeworkRecord.getHomeworkId(), options);
                dto.setHomework(homeworkDTO);
            }*/
        }

        return dto;
    }

    @Override
    public List<MemberHomeworkRecordReadDTO> wrapListDTO(Collection<MemberHomeworkRecord> memberHomeworkRecordCollection, Collection<DtoOption> options) throws Exception {
        List<MemberHomeworkRecordReadDTO> dtoList = new ArrayList<>();
        MemberHomeworkRecordReadDTO dto;

        Map<Long, ClazzMemberReadDTO> memberIdMemberDTOMap = new HashMap<>();
        Map<Long, HomeworkReadDTO> homeworkIdHomeworkDTOMap = new HashMap<>();

        if (options != null && !options.isEmpty()) {
            Set<Long> memberIdSet = new HashSet<>();
            Set<Long> homeworkIdSet = new HashSet<>();

            for (MemberHomeworkRecord record : memberHomeworkRecordCollection) {
                memberIdSet.add(record.getMemberId());
                homeworkIdSet.add(record.getHomeworkId());
            }

            if (options.contains(DtoOption.MEMBER)) {
                memberIdMemberDTOMap = clazzMemberService.mapIdDTOByIdIn(memberIdSet, options);
            }

            /* TODO:
            if (options.contains(DtoOption.HOMEWORK)) {
                homeworkIdHomeworkDTOMap = homeworkService.mapIdDTOByIdIn(homeworkIdSet, options);
            }
            */
        }

        for (MemberHomeworkRecord record : memberHomeworkRecordCollection) {
            dto = mapper.map(record, MemberHomeworkRecordReadDTO.class);

            /* Add dependency */
            dto.setMember(memberIdMemberDTOMap.get(record.getMemberId()));

            dto.setHomework(homeworkIdHomeworkDTOMap.get(record.getHomeworkId()));

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
