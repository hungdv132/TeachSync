package com.teachsync.services.clazzSchedule;

import com.teachsync.dtos.clazzSchedule.ClazzScheduleCreateDTO;
import com.teachsync.dtos.clazzSchedule.ClazzScheduleReadDTO;
import com.teachsync.dtos.clazzSchedule.ClazzScheduleUpdateDTO;
import com.teachsync.entities.ClazzSchedule;
import com.teachsync.utils.enums.DtoOption;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface ClazzScheduleService {
    /* =================================================== CREATE =================================================== */
    String addClazzSchedule(ClazzScheduleCreateDTO createDTO);

    ClazzSchedule createClazzSchedule(ClazzSchedule clazzSchedule) throws Exception;
    ClazzScheduleReadDTO createClazzScheduleByDTO(ClazzScheduleCreateDTO createDTO) throws Exception;


    /* =================================================== READ ===================================================== */
    Page<ClazzSchedule> getPageAll(Pageable paging) throws Exception;
    @Deprecated
    Page<ClazzScheduleReadDTO> getPageDTOAll(Pageable paging) throws Exception;
    Page<ClazzScheduleReadDTO> getPageDTOAll(Pageable paging, Collection<DtoOption> options) throws Exception;

    /* id */
    Boolean existsById(Long id) throws Exception;
    ClazzSchedule getById(Long id) throws Exception;
    ClazzScheduleReadDTO getDTOById(Long id, Collection<DtoOption> options) throws Exception;

    Boolean existsAllByIdIn(Collection<Long> idCollection) throws Exception;
    List<ClazzSchedule> getAllByIdIn(Collection<Long> idCollection) throws Exception;
    List<ClazzScheduleReadDTO> getAllDTOByIdIn(Collection<Long> idCollection, Collection<DtoOption> options) throws Exception;
    Map<Long, ClazzScheduleReadDTO> mapIdDTOByIdIn(
            Collection<Long> idCollection, Collection<DtoOption> options) throws Exception;

    /* clazzId */
    ClazzSchedule getByClazzId(Long clazzId) throws Exception;
    ClazzScheduleReadDTO getDTOByClazzId(Long clazzId, Collection<DtoOption> options) throws Exception;

    List<ClazzSchedule> getAllByClazzIdIn(Collection<Long> clazzIdCollection) throws Exception;
    List<ClazzScheduleReadDTO> getAllDTOByClazzIdIn(
            Collection<Long> clazzIdCollection, Collection<DtoOption> options) throws Exception;
    Map<Long, ClazzScheduleReadDTO> mapClazzIdDTOByClazzIdIn(
            Collection<Long> clazzIdCollection, Collection<DtoOption> options) throws Exception;


    /* =================================================== UPDATE =================================================== */
    String editClazzSchedule(ClazzScheduleUpdateDTO updateDTO);

    ClazzSchedule updateClazzSchedule(ClazzSchedule clazzSchedule) throws Exception;
    ClazzScheduleReadDTO updateClazzScheduleByDTO(ClazzScheduleUpdateDTO updateDTO) throws Exception;


    /* =================================================== WRAPPER ================================================== */
    ClazzScheduleReadDTO wrapDTO(ClazzSchedule clazzSchedule, Collection<DtoOption> options) throws Exception;

    List<ClazzScheduleReadDTO> wrapListDTO(
            Collection<ClazzSchedule> clazzScheduleCollection, Collection<DtoOption> options) throws Exception;

    Page<ClazzScheduleReadDTO> wrapPageDTO(
            Page<ClazzSchedule> clazzSchedulePage, Collection<DtoOption> options) throws Exception;
}
