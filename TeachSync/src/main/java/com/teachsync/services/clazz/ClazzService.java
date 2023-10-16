package com.teachsync.services.clazz;

import com.teachsync.dtos.clazz.ClazzCreateDTO;
import com.teachsync.dtos.clazz.ClazzReadDTO;
import com.teachsync.dtos.clazz.ClazzUpdateDTO;
import com.teachsync.entities.Clazz;
import com.teachsync.utils.enums.DtoOption;
import com.teachsync.utils.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;
import java.util.Map;


public interface ClazzService {
    /* =================================================== CREATE =================================================== */
    Clazz createClazz(
            Clazz clazz) throws Exception;
    ClazzReadDTO createClazzByDTO(
            ClazzCreateDTO createDTO) throws Exception;


    /* =================================================== READ ===================================================== */
    List<Clazz> getAll(
            Collection<Status> statuses, boolean isStatusIn) throws Exception;
    List<ClazzReadDTO> getAllDTO(
            Collection<Status> statuses, boolean isStatusIn,
            Collection<DtoOption> options) throws Exception;

    Page<Clazz> getPageAll(
            Pageable paging, Collection<Status> statuses, boolean isStatusIn) throws Exception;
    Page<ClazzReadDTO> getPageAllDTO(
            Pageable paging, Collection<Status> statuses, boolean isStatusIn,
            Collection<DtoOption> options) throws Exception;

    /* id */
    Boolean existsById(
            Long id) throws Exception;
    Boolean existsAllByIdIn(
            Collection<Long> ids, Collection<Status> statuses, boolean isStatusIn) throws Exception;


    Clazz getById(
            Long id, Collection<Status> statuses, boolean isStatusIn) throws Exception;
    ClazzReadDTO getDTOById(
            Long id, Collection<Status> statuses, boolean isStatusIn, Collection<DtoOption> options) throws Exception;

    List<Clazz> getAllByIdIn(
            Collection<Long> ids, Collection<Status> statuses, boolean isStatusIn) throws Exception;
    Map<Long, String> mapIdClazzAliasByIdIn(
            Collection<Long> ids, Collection<Status> statuses, boolean isStatusIn) throws Exception;
    Map<Long, String> mapIdClazzNameByIdIn(
            Collection<Long> ids, Collection<Status> statuses, boolean isStatusIn) throws Exception;
    List<ClazzReadDTO> getAllDTOByIdIn(
            Collection<Long> ids, Collection<Status> statuses, boolean isStatusIn,
            Collection<DtoOption> options) throws Exception;
    Map<Long, ClazzReadDTO> mapIdDTOByIdIn(
            Collection<Long> ids, Collection<Status> statuses, boolean isStatusIn,
            Collection<DtoOption> options) throws Exception;

    Page<Clazz> getPageAllByIdIn(
            Pageable paging, Collection<Long> ids, Collection<Status> statuses, boolean isStatusIn) throws Exception;
    Page<ClazzReadDTO> getPageAllDTOByIdIn(
            Pageable paging, Collection<Long> ids, Collection<Status> statuses, boolean isStatusIn,
            Collection<DtoOption> options) throws Exception;

    /* courseId */
    List<Clazz> getAllByCourseId(
            Long courseId, Collection<Status> statuses, boolean isStatusIn) throws Exception;
    List<ClazzReadDTO> getAllDTOByCourseId(
            Long courseId, Collection<Status> statuses, boolean isStatusIn, 
            Collection<DtoOption> options) throws Exception;
    
    Page<Clazz> getPageAllByCourseId(
            Pageable paging, Long courseId, Collection<Status> statuses, boolean isStatusIn) throws Exception;
    Page<ClazzReadDTO> getPageAllDTOByCourseId(
            Pageable paging, Long courseId, Collection<Status> statuses, boolean isStatusIn, 
            Collection<DtoOption> options) throws Exception;
    
    List<Clazz> getAllByCourseIdIn(
            Collection<Long> courseIds, Collection<Status> statuses, boolean isStatusIn) throws Exception;
    List<ClazzReadDTO> getAllDTOByCourseIdIn(
            Collection<Long> courseIds, Collection<Status> statuses, boolean isStatusIn, 
            Collection<DtoOption> options) throws Exception;

    Page<Clazz> getPageAllByCourseIdIn(
            Pageable paging, Collection<Long> courseIds, Collection<Status> statuses, boolean isStatusIn) throws Exception;
    Page<ClazzReadDTO> getPageAllDTOByCourseIdIn(
            Pageable paging, Collection<Long> courseIds, Collection<Status> statuses, boolean isStatusIn, 
            Collection<DtoOption> options) throws Exception;

    /* centerId */
    List<Clazz> getAllByCenterId(
            Long centerId, Collection<Status> statuses, boolean isStatusIn) throws Exception;
    List<ClazzReadDTO> getAllDTOByCenterId(
            Long centerId, Collection<Status> statuses, boolean isStatusIn,
            Collection<DtoOption> options) throws Exception;

    Page<Clazz> getPageAllByCenterId(
            Pageable paging, Long centerId, Collection<Status> statuses, boolean isStatusIn) throws Exception;
    Page<ClazzReadDTO> getPageAllDTOByCenterId(
            Pageable paging, Long centerId, Collection<Status> statuses, boolean isStatusIn,
            Collection<DtoOption> options) throws Exception;

    List<Clazz> getAllByCenterIdIn(
            Collection<Long> centerIds, Collection<Status> statuses, boolean isStatusIn) throws Exception;
    List<ClazzReadDTO> getAllDTOByCenterIdIn(
            Collection<Long> centerIds, Collection<Status> statuses, boolean isStatusIn,
            Collection<DtoOption> options) throws Exception;

    Page<Clazz> getPageAllByCenterIdIn(
            Pageable paging, Collection<Long> centerIds, Collection<Status> statuses, boolean isStatusIn) throws Exception;
    Page<ClazzReadDTO> getPageAllDTOByCenterIdIn(
            Pageable paging, Collection<Long> centerIds, Collection<Status> statuses, boolean isStatusIn,
            Collection<DtoOption> options) throws Exception;

    /* courseId && centerId */
    List<Clazz> getAllByCourseIdAndCenterId(
            Long courseId, Long centerId, Collection<Status> statuses, boolean isStatusIn) throws Exception;
    List<ClazzReadDTO> getAllDTOByCourseIdAndCenterId(
            Long courseId, Long centerId, Collection<Status> statuses, boolean isStatusIn,
            Collection<DtoOption> options) throws Exception;

    Page<Clazz> getPageAllByCourseIdAndCenterId(
            Pageable paging, Long courseId, Long centerId, Collection<Status> statuses, boolean isStatusIn) throws Exception;
    Page<ClazzReadDTO> getPageAllDTOByCourseIdAndCenterId(
            Pageable paging, Long courseId, Long centerId, Collection<Status> statuses, boolean isStatusIn,
            Collection<DtoOption> options) throws Exception;

    List<Clazz> getAllByCourseIdInAndCenterId(
            Collection<Long> courseIds, Long centerId, Collection<Status> statuses, boolean isStatusIn) throws Exception;
    List<ClazzReadDTO> getAllDTOByCourseIdInAndCenterId(
            Collection<Long> courseIds, Long centerId, Collection<Status> statuses, boolean isStatusIn,
            Collection<DtoOption> options) throws Exception;

    Page<Clazz> getPageAllByCourseIdInAndCenterId(
            Pageable paging, Collection<Long> courseIds, Long centerId, Collection<Status> statuses, boolean isStatusIn) throws Exception;
    Page<ClazzReadDTO> getPageAllDTOByCourseIdInAndCenterId(
            Pageable paging, Collection<Long> courseIds, Long centerId, Collection<Status> statuses, boolean isStatusIn,
            Collection<DtoOption> options) throws Exception;

    List<Clazz> getAllByCourseIdAndCenterIdIn(
            Long courseId, Collection<Long> centerIds, Collection<Status> statuses, boolean isStatusIn) throws Exception;
    List<ClazzReadDTO> getAllDTOByCourseIdAndCenterIdIn(
            Long courseId, Collection<Long> centerIds, Collection<Status> statuses, boolean isStatusIn,
            Collection<DtoOption> options) throws Exception;
    Map<Long, List<ClazzReadDTO>> mapCenterIdListDTOByCourseIdAndCenterIdIn(
            Long courseId, Collection<Long> centerIds, Collection<Status> statuses, boolean isStatusIn,
            Collection<DtoOption> options) throws Exception;

    Page<Clazz> getPageAllByCourseIdAndCenterIdIn(
            Pageable paging, Long courseId, Collection<Long> centerIds, Collection<Status> statuses, boolean isStatusIn) throws Exception;
    Page<ClazzReadDTO> getPageAllDTOByCourseIdAndCenterIdIn(
            Pageable paging, Long courseId, Collection<Long> centerIds, Collection<Status> statuses, boolean isStatusIn,
            Collection<DtoOption> options) throws Exception;

    List<Clazz> getAllByCourseIdInAndCenterIdIn(
            Collection<Long> courseIds, Collection<Long> centerIds, Collection<Status> statuses, boolean isStatusIn) throws Exception;
    List<ClazzReadDTO> getAllDTOByCourseIdInAndCenterIdIn(
            Collection<Long> courseIds, Collection<Long> centerIds, Collection<Status> statuses, boolean isStatusIn,
            Collection<DtoOption> options) throws Exception;

    Page<Clazz> getPageAllByCourseIdInAndCenterIdIn(
            Pageable paging, Collection<Long> courseIds, Collection<Long> centerIds, Collection<Status> statuses, boolean isStatusIn) throws Exception;
    Page<ClazzReadDTO> getPageAllDTOByCourseIdInAndCenterIdIn(
            Pageable paging, Collection<Long> courseIds, Collection<Long> centerIds, Collection<Status> statuses, boolean isStatusIn,
            Collection<DtoOption> options) throws Exception;

    /* staffId */
    List<Clazz> getAllByStaffId(
            Long staffId, Collection<Status> statuses, boolean isStatusIn) throws Exception;
    List<ClazzReadDTO> getAllDTOByStaffId(
            Long staffId, Collection<Status> statuses, boolean isStatusIn,
            Collection<DtoOption> options) throws Exception;

    Page<Clazz> getPageAllByStaffId(
            Pageable paging, Long staffId, Collection<Status> statuses, boolean isStatusIn) throws Exception;
    Page<ClazzReadDTO> getPageAllDTOByStaffId(
            Pageable paging, Long staffId, Collection<Status> statuses, boolean isStatusIn,
            Collection<DtoOption> options) throws Exception;

    List<Clazz> getAllByStaffIdIn(
            Collection<Long> staffIds, Collection<Status> statuses, boolean isStatusIn) throws Exception;
    List<ClazzReadDTO> getAllDTOByStaffIdIn(
            Collection<Long> staffIds, Collection<Status> statuses, boolean isStatusIn,
            Collection<DtoOption> options) throws Exception;

    Page<Clazz> getPageAllByStaffIdIn(
            Pageable paging, Collection<Long> staffIds, Collection<Status> statuses, boolean isStatusIn) throws Exception;
    Page<ClazzReadDTO> getPageAllDTOByStaffIdIn(
            Pageable paging, Collection<Long> staffIds, Collection<Status> statuses, boolean isStatusIn,
            Collection<DtoOption> options) throws Exception;

    /* clazzAlias */
    List<Clazz> getAllByAliasContains(
            String clazzAlias, Collection<Status> statuses, boolean isStatusIn) throws Exception;
    List<ClazzReadDTO> getAllDTOByAliasContains(
            String clazzAlias, Collection<Status> statuses, boolean isStatusIn,
            Collection<DtoOption> options) throws Exception;

    Page<Clazz> getPageAllByAliasContains(
            Pageable paging, String clazzAlias, Collection<Status> statuses, boolean isStatusIn) throws Exception;
    Page<ClazzReadDTO> getPageAllDTOByAliasContains(
            Pageable paging, String clazzAlias, Collection<Status> statuses, boolean isStatusIn,
            Collection<DtoOption> options) throws Exception;
    
    /* clazzName */
    List<Clazz> getAllByNameContains(
            String clazzName, Collection<Status> statuses, boolean isStatusIn) throws Exception;
    List<ClazzReadDTO> getAllDTOByNameContains(
            String clazzName, Collection<Status> statuses, boolean isStatusIn,
            Collection<DtoOption> options) throws Exception;

    Page<Clazz> getPageAllByNameContains(
            Pageable paging, String clazzName, Collection<Status> statuses, boolean isStatusIn) throws Exception;
    Page<ClazzReadDTO> getPageAllDTOByNameContains(
            Pageable paging, String clazzName, Collection<Status> statuses, boolean isStatusIn,
            Collection<DtoOption> options) throws Exception;
    

    /* =================================================== UPDATE =================================================== */
    Clazz updateClazz(
            Clazz clazz) throws Exception;
    ClazzReadDTO updateClazzByDTO(
            ClazzUpdateDTO updateDTO) throws Exception;

    /* =================================================== DELETE =================================================== */
    Boolean deleteClazz(
            Long id) throws Exception;


    /* =================================================== WRAPPER ================================================== */
    ClazzReadDTO wrapDTO(
            Clazz clazz, Collection<DtoOption> options) throws Exception;
    List<ClazzReadDTO> wrapListDTO(
            Collection<Clazz> clazzCollection, Collection<DtoOption> options) throws Exception;
    Page<ClazzReadDTO> wrapPageDTO(
            Page<Clazz> clazzPage, Collection<DtoOption> options) throws Exception;
}
