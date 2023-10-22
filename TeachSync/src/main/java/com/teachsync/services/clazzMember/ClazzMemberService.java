package com.teachsync.services.clazzMember;

import com.teachsync.dtos.clazzMember.ClazzMemberCreateDTO;
import com.teachsync.dtos.clazzMember.ClazzMemberReadDTO;
import com.teachsync.dtos.clazzMember.ClazzMemberUpdateDTO;
import com.teachsync.entities.ClazzMember;
import com.teachsync.utils.enums.DtoOption;
import org.springframework.data.domain.Page;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface ClazzMemberService {
    /* =================================================== CREATE =================================================== */
    ClazzMember createClazzMember(
            ClazzMember member) throws Exception;
    ClazzMemberReadDTO createClazzMemberByDTO(
            ClazzMemberCreateDTO createDTO) throws Exception;
    

    /* =================================================== READ ===================================================== */
    /* id */
    ClazzMember getById(
            Long id) throws Exception;
    ClazzMemberReadDTO getDTOById(
            Long id, Collection<DtoOption> options) throws Exception;

    List<ClazzMember> getAllByIdIn(
            Collection<Long> ids) throws Exception;
    List<ClazzMemberReadDTO> getAllDTOByIdIn(
            Collection<Long> ids, Collection<DtoOption> options) throws Exception;
    Map<Long, ClazzMemberReadDTO> mapIdDTOByIdIn(
            Collection<Long> ids, Collection<DtoOption> options) throws Exception;

    /* clazzId */
    List<ClazzMember> getAllByClazzId(
            Long clazzId) throws Exception;
    List<ClazzMemberReadDTO> getAllDTOByClazzId(
            Long clazzId, Collection<DtoOption> options) throws Exception;

    List<ClazzMember> getAllByClazzIdIn(
            Collection<Long> clazzIds) throws Exception;
    List<ClazzMemberReadDTO> getAllDTOByClazzIdIn(
            Collection<Long> clazzIds, Collection<DtoOption> options) throws Exception;
    Map<Long, List<ClazzMemberReadDTO>> mapClazzIdListDTOByClazzIdIn(
            Collection<Long> clazzIds, Collection<DtoOption> options) throws Exception;

    /* userId */
    List<ClazzMember> getAllByUserId(
            Long userId) throws Exception;
    Map<Long, ClazzMember> mapClazzIdClazzMemberByUserId(
            Long userId) throws Exception;
    List<ClazzMemberReadDTO> getAllDTOByUserId(
            Long userId, Collection<DtoOption> options) throws Exception;

    List<ClazzMember> getAllByUserIdIn(
            Collection<Long> userIds) throws Exception;
    List<ClazzMemberReadDTO> getAllDTOByUserIdIn(
            Collection<Long> userIds, Collection<DtoOption> options) throws Exception;

    /* clazzId & userId */
    ClazzMember getByClazzIdAndUserId(
            Long clazzId, Long userId) throws Exception;
    ClazzMemberReadDTO getDTOByClazzIdAndUserId(
            Long clazzId, Long userId, Collection<DtoOption> options) throws Exception;

    List<ClazzMember> getAllByClazzIdInAndUserId(
            Collection<Long> clazzIds, Long userId) throws Exception;
    List<ClazzMemberReadDTO> getAllDTOByClazzIdInAndUserId(
            Collection<Long> clazzIds, Long userId, Collection<DtoOption> options) throws Exception;

    /* =================================================== UPDATE =================================================== */
    ClazzMember updateClazzMember(
            ClazzMember member) throws Exception;
    ClazzMemberReadDTO updateClazzMemberByDTO(
            ClazzMemberUpdateDTO updateDTO) throws Exception;

    
    /* =================================================== DELETE =================================================== */
    Boolean deleteClazzMember(Long id) throws Exception;

    Boolean deleteAllByClazzId(Long clazzId) throws Exception;


    /* =================================================== WRAPPER ================================================== */
    ClazzMemberReadDTO wrapDTO(
            ClazzMember clazzMember, Collection<DtoOption> options) throws Exception;
    List<ClazzMemberReadDTO> wrapListDTO(
            
            Collection<ClazzMember> clazzMemberCollection, Collection<DtoOption> options) throws Exception;
    Page<ClazzMemberReadDTO> wrapPageDTO(
            Page<ClazzMember> clazzMemberPage, Collection<DtoOption> options) throws Exception;
}
