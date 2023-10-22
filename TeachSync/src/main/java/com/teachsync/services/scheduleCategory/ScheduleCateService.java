package com.teachsync.services.scheduleCategory;

import com.teachsync.dtos.scheduleCategory.ScheduleCategoryReadDTO;
import com.teachsync.entities.ScheduleCategory;
import com.teachsync.utils.enums.DtoOption;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface ScheduleCateService {
    /* =================================================== READ ===================================================== */
    Page<ScheduleCategory> getPageAll(Pageable paging) throws Exception;
    @Deprecated
    Page<ScheduleCategoryReadDTO> getPageAllDTO(Pageable paging) throws Exception;
    Page<ScheduleCategoryReadDTO> getPageAllDTO(Pageable paging, Collection<DtoOption> options) throws Exception;

    List<ScheduleCategory> getAll() throws Exception;

    List<ScheduleCategoryReadDTO> getAllDTO() throws Exception;

    /* id */
    ScheduleCategory getById(Long id) throws Exception;

    ScheduleCategoryReadDTO getDTOById(Long id) throws Exception;

    List<ScheduleCategory> getAllByIdIn(Collection<Long> idCollection) throws Exception;

    List<ScheduleCategoryReadDTO> getAllDTOByIdIn(Collection<Long> idCollection) throws Exception;


    Map<Long, ScheduleCategoryReadDTO> mapScheduleIdScheduleDescByIdIn(Collection<Long> idCollection) throws Exception;

    /* =================================================== WRAPPER ================================================== */

    ScheduleCategoryReadDTO wrapDTO(ScheduleCategory scheduleCategory, Collection<DtoOption> options) throws Exception;

    List<ScheduleCategoryReadDTO> wrapListDTO(
            Collection<ScheduleCategory> scheduleCategoryCollection, Collection<DtoOption> options) throws Exception;

    Page<ScheduleCategoryReadDTO> wrapPageDTO(
            Page<ScheduleCategory> scheduleCategoryPage, Collection<DtoOption> options) throws Exception;


}
