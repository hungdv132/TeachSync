package com.teachsync.services.memberTestRecord;

import com.teachsync.dtos.clazzMember.ClazzMemberReadDTO;
import com.teachsync.dtos.clazzTest.ClazzTestReadDTO;
import com.teachsync.dtos.memberTestRecord.MemberTestRecordReadDTO;
import com.teachsync.entities.ClazzMember;
import com.teachsync.entities.MemberTestRecord;
import com.teachsync.repositories.MemberTestRecordRepository;
import com.teachsync.services.clazzMember.ClazzMemberService;
import com.teachsync.services.clazzTest.ClazzTestService;
import com.teachsync.utils.MiscUtil;
import com.teachsync.utils.enums.DtoOption;
import com.teachsync.utils.enums.Status;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MemberTestRecordServiceImpl implements MemberTestRecordService {
    @Autowired
    private MemberTestRecordRepository memberTestRecordRepository;

    @Autowired
    private ClazzMemberService clazzMemberService;
    @Autowired
    private ClazzTestService clazzTestService;

    @Autowired
    private ModelMapper mapper;
    @Autowired
    private MiscUtil miscUtil;


    /* =================================================== CREATE =================================================== */


    /* =================================================== READ ===================================================== */
    @Override
    public Page<MemberTestRecord> getPageAll(Pageable pageable) throws Exception {
        if (pageable == null) {
            pageable = miscUtil.defaultPaging();
        }

        Page<MemberTestRecord> memberTestRecordPage =
                memberTestRecordRepository.findAllByStatusNot(Status.DELETED, pageable);

        if (memberTestRecordPage.isEmpty()) {
            return null;
        }

        return memberTestRecordPage;
    }
    @Override
    public Page<MemberTestRecordReadDTO> getPageAllDTO(Pageable pageable, Collection<DtoOption> options) throws Exception {
        Page<MemberTestRecord> memberTestRecordPage = getPageAll(pageable);

        if (memberTestRecordPage == null) {
            return null;
        }

        return wrapPageDTO(memberTestRecordPage, options);
    }

    /* clazzTestId */
    @Override
    public List<MemberTestRecord> getAllByClazzTestId(Long clazzTestId) throws Exception {
        List<MemberTestRecord> memberTestRecordList =
                memberTestRecordRepository.findAllByClazzTestIdAndStatusNot(clazzTestId, Status.DELETED);

        if (memberTestRecordList.isEmpty()) {
            return null;
        }

        return memberTestRecordList;
    }
    @Override
    public List<MemberTestRecordReadDTO> getAllDTOByClazzTestId(Long clazzTestId, Collection<DtoOption> options) throws Exception {
        List<MemberTestRecord> memberTestRecordList = getAllByClazzTestId(clazzTestId);

        if (memberTestRecordList == null) {
            return null;
        }

        return wrapListDTO(memberTestRecordList, options);
    }

    /* memberId & clazzTestId */
    @Override
    public MemberTestRecord getByMemberIdAndClazzTestId(Long memberId, Long clazzTestId) throws Exception {
        return memberTestRecordRepository
                .findByMemberIdAndClazzTestIdAndStatusNot(memberId, clazzTestId, Status.DELETED)
                .orElse(null);
    }
    @Override
    public MemberTestRecordReadDTO getDTOByMemberIdAndClazzTestId(
            Long memberId, Long clazzTestId, Collection<DtoOption> options) throws Exception {
        MemberTestRecord memberTestRecord = getByMemberIdAndClazzTestId(memberId, clazzTestId);
        if (memberTestRecord == null) {
            return null;
        }

        return wrapDTO(memberTestRecord, options);
    }


    /* =================================================== UPDATE =================================================== */


    /* =================================================== DELETE =================================================== */


    /* =================================================== WRAPPER ================================================== */
    @Override
    public MemberTestRecordReadDTO wrapDTO(
            MemberTestRecord memberTestRecord, Collection<DtoOption> options) throws Exception {
        MemberTestRecordReadDTO dto = mapper.map(memberTestRecord, MemberTestRecordReadDTO.class);

        /* Add dependency */
        if (options != null && !options.isEmpty()) {
            if (options.contains(DtoOption.MEMBER)) {
                ClazzMemberReadDTO memberDTO = clazzMemberService.getDTOById(memberTestRecord.getMemberId(), options);
                dto.setMember(memberDTO);
            }

            if (options.contains(DtoOption.CLAZZ_TEST)) {
                ClazzTestReadDTO clazzTestDTO = clazzTestService.getDTOById(memberTestRecord.getClazzTestId(), options);
                dto.setClazzTest(clazzTestDTO);
            }
        }

        return dto;
    }

    @Override
    public List<MemberTestRecordReadDTO> wrapListDTO(
            Collection<MemberTestRecord> memberTestRecordCollection, Collection<DtoOption> options) throws Exception {
        List<MemberTestRecordReadDTO> dtoList = new ArrayList<>();
        MemberTestRecordReadDTO dto;

        Map<Long, ClazzMemberReadDTO> memberIdMemberDTOMap = new HashMap<>();
        Map<Long, ClazzTestReadDTO> clazzTestIdClazzTestDTOMap = new HashMap<>();

        if (options != null && !options.isEmpty()) {
            Set<Long> memberIdSet = new HashSet<>();
            Set<Long> clazzTestIdSet = new HashSet<>();

            for (MemberTestRecord memberTestRecord : memberTestRecordCollection) {
                memberIdSet.add(memberTestRecord.getMemberId());
                clazzTestIdSet.add(memberTestRecord.getClazzTestId());
            }

            if (options.contains(DtoOption.MEMBER)) {
                memberIdMemberDTOMap = clazzMemberService.mapIdDTOByIdIn(memberIdSet, options);
            }

            if (options.contains(DtoOption.CLAZZ_TEST)) {
                clazzTestIdClazzTestDTOMap = clazzTestService.mapIdDTOByIdIn(clazzTestIdSet, options);
            }
        }

        for (MemberTestRecord memberTestRecord : memberTestRecordCollection) {
            dto = mapper.map(memberTestRecord, MemberTestRecordReadDTO.class);

            /* Add dependency */
            dto.setMember(memberIdMemberDTOMap.get(memberTestRecord.getMemberId()));

            dto.setClazzTest(clazzTestIdClazzTestDTOMap.get(memberTestRecord.getClazzTestId()));

            dtoList.add(dto);
        }

        return dtoList;
    }

    @Override
    public Page<MemberTestRecordReadDTO> wrapPageDTO(
            Page<MemberTestRecord> memberTestRecordPage, Collection<DtoOption> options) throws Exception {
        return new PageImpl<>(
                wrapListDTO(memberTestRecordPage.getContent(), options),
                memberTestRecordPage.getPageable(),
                memberTestRecordPage.getTotalPages());
    }
}
