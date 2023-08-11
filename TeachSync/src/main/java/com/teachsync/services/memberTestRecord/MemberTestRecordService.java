package com.teachsync.services.memberTestRecord;

import com.teachsync.dtos.memberTestRecord.MemberTestRecordReadDTO;
import com.teachsync.entities.MemberTestRecord;
import com.teachsync.utils.enums.DtoOption;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;

public interface MemberTestRecordService {
    /* =================================================== CREATE =================================================== */


    /* =================================================== READ ===================================================== */
    Page<MemberTestRecord> getPageAll(Pageable pageable) throws Exception;
    Page<MemberTestRecordReadDTO> getPageAllDTO(Pageable pageable, Collection<DtoOption> options) throws Exception;

    /* clazzTestId */
    List<MemberTestRecord> getAllByClazzTestId(Long clazzTestId) throws Exception;
    List<MemberTestRecordReadDTO> getAllDTOByClazzTestId(Long clazzTestId, Collection<DtoOption> options) throws Exception;

    /* memberId & clazzTestId */
    MemberTestRecord getByMemberIdAndClazzTestId(Long memberId, Long clazzTestId) throws Exception;
    MemberTestRecordReadDTO getDTOByMemberIdAndClazzTestId(
            Long memberId, Long clazzTestId, Collection<DtoOption> options) throws Exception;

    /* =================================================== UPDATE =================================================== */


    /* =================================================== DELETE =================================================== */


    /* =================================================== WRAPPER ================================================== */
    MemberTestRecordReadDTO wrapDTO(MemberTestRecord memberTestRecord, Collection<DtoOption> options) throws Exception;
    List<MemberTestRecordReadDTO> wrapListDTO(Collection<MemberTestRecord> memberTestRecordCollection, Collection<DtoOption> options) throws Exception;
    Page<MemberTestRecordReadDTO> wrapPageDTO(Page<MemberTestRecord> memberTestRecordPage, Collection<DtoOption> options) throws Exception;
}
