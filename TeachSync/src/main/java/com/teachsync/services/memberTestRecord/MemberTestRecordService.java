package com.teachsync.services.memberTestRecord;

import com.teachsync.dtos.memberTestRecord.MemberTestRecordReadDTO;
import com.teachsync.entities.MemberTestRecord;
import com.teachsync.utils.enums.DtoOption;
import org.springframework.data.domain.Page;

import java.util.Collection;
import java.util.List;

public interface MemberTestRecordService {
    /* =================================================== CREATE =================================================== */


    /* =================================================== READ ===================================================== */


    /* =================================================== UPDATE =================================================== */


    /* =================================================== DELETE =================================================== */


    /* =================================================== WRAPPER ================================================== */
    MemberTestRecordReadDTO wrapDTO(MemberTestRecord memberTestRecord, Collection<DtoOption> options) throws Exception;
    List<MemberTestRecordReadDTO> wrapListDTO(Collection<MemberTestRecord> memberTestRecordCollection, Collection<DtoOption> options) throws Exception;
    Page<MemberTestRecordReadDTO> wrapPageDTO(Page<MemberTestRecord> memberTestRecordPage, Collection<DtoOption> options) throws Exception;
}
