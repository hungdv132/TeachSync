package com.teachsync.services.scheduleCategory;

import com.teachsync.dtos.clazzSchedule.ClazzScheduleReadDTO;
import com.teachsync.dtos.room.RoomReadDTO;
import com.teachsync.dtos.scheduleCategory.ScheduleCaReadDTO;
import com.teachsync.entities.ClazzSchedule;
import com.teachsync.entities.Room;
import com.teachsync.entities.ScheduleCategory;
import com.teachsync.utils.enums.DtoOption;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;

public interface ScheduleCateService {
    /* =================================================== READ ===================================================== */
    Page<ScheduleCategory> getPageAll(Pageable paging) throws Exception;
    @Deprecated
    Page<ScheduleCaReadDTO> getPageDTOAll(Pageable paging) throws Exception;
    Page<ScheduleCaReadDTO> getPageDTOAll(Pageable paging, Collection<DtoOption> options) throws Exception;

    /* id */
    ScheduleCategory getById(Long id) throws Exception;

    List<ScheduleCategory> getAllByIdIn(Collection<Long> idCollection) throws Exception;

    List<ScheduleCategory> getAllByScheduleCaId(Long id) throws Exception;
    List<ScheduleCaReadDTO> getAllDTOByScheduleCaId(Long id, Collection<DtoOption> options) throws Exception;

    /* =================================================== WRAPPER ================================================== */

    ScheduleCaReadDTO wrapDTO(ScheduleCategory scheduleCategory, Collection<DtoOption> options) throws Exception;

    List<ScheduleCaReadDTO> wrapListDTO(
            Collection<ScheduleCategory> scheduleCategoryCollection, Collection<DtoOption> options) throws Exception;

    Page<ScheduleCaReadDTO> wrapPageDTO(
            Page<ScheduleCategory> scheduleCategoryPage, Collection<DtoOption> options) throws Exception;


}
