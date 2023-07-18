package com.teachsync.services.memberHomeworkRecord;

import com.teachsync.dtos.memberHomeworkRecord.MemberHomeworkRecordReadDTO;
import com.teachsync.entities.MemberHomeworkRecord;
import com.teachsync.utils.enums.DtoOption;
import org.springframework.data.domain.Page;

import java.util.Collection;
import java.util.List;

public interface MemberHomeworkRecordService {
    /* =================================================== CREATE =================================================== */


    /* =================================================== READ ===================================================== */


    /* =================================================== UPDATE =================================================== */


    /* =================================================== DELETE =================================================== */


    /* =================================================== WRAPPER ================================================== */
    MemberHomeworkRecordReadDTO wrapDTO(MemberHomeworkRecord memberHomeworkRecord, Collection<DtoOption> options) throws Exception;
    List<MemberHomeworkRecordReadDTO> wrapListDTO(Collection<MemberHomeworkRecord> memberHomeworkRecordCollection, Collection<DtoOption> options) throws Exception;
    Page<MemberHomeworkRecordReadDTO> wrapPageDTO(Page<MemberHomeworkRecord> memberHomeworkRecordPage, Collection<DtoOption> options) throws Exception;
}