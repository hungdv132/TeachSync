package com.teachsync.services.clazzMember;

import com.teachsync.dtos.clazzMember.ClazzMemberReadDTO;
import com.teachsync.entities.ClazzMember;
import com.teachsync.utils.enums.DtoOption;
import org.springframework.data.domain.Page;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface ClazzMemberService {
    /* =================================================== CREATE =================================================== */
    

    /* =================================================== READ ===================================================== */
    /* id */
    ClazzMember getById(Long id) throws Exception;
    ClazzMemberReadDTO getDTOById(Long id, Collection<DtoOption> options) throws Exception;

    List<ClazzMember> getAllByIdIn(Collection<Long> idCollection) throws Exception;
    List<ClazzMemberReadDTO> getAllDTOByIdIn(Collection<Long> idCollection, Collection<DtoOption> options) throws Exception;
    Map<Long, ClazzMemberReadDTO> mapIdDTOByIdIn(Collection<Long> idCollection, Collection<DtoOption> options) throws Exception;

    /* clazzId */
    List<ClazzMember> getAllByClazzId(Long clazzId) throws Exception;

    List<ClazzMember> getAllByClazzIdIn(Collection<Long> clazzIdCollection) throws Exception;
    Map<Long, List<ClazzMember>> mapClazzIdClazzMemberListByClazzIdIn(Collection<Long> clazzIdCollection) throws Exception;

    /* userId */
    List<ClazzMember> getAllByUserId(Long userId) throws Exception;

    List<ClazzMember> getAllByUserIdIn(Collection<Long> userIdCollection) throws Exception;

    /* clazzId & userId */
    ClazzMember getByClazzIdAndUserId(Long clazzId, Long userId) throws Exception;

    /* =================================================== UPDATE =================================================== */

    
    /* =================================================== DELETE =================================================== */


    /* =================================================== WRAPPER ================================================== */
    ClazzMemberReadDTO wrapDTO(ClazzMember clazzMember, Collection<DtoOption> options) throws Exception;
    List<ClazzMemberReadDTO> wrapListDTO(
            Collection<ClazzMember> clazzMemberCollection, Collection<DtoOption> options) throws Exception;
    Page<ClazzMemberReadDTO> wrapPageDTO(Page<ClazzMember> clazzMemberPage, Collection<DtoOption> options) throws Exception;
}
