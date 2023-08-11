package com.teachsync.services.clazz;

import com.teachsync.dtos.clazz.ClazzCreateDTO;
import com.teachsync.dtos.clazz.ClazzReadDTO;
import com.teachsync.dtos.clazz.ClazzUpdateDTO;
import com.teachsync.entities.Clazz;
import com.teachsync.utils.enums.DtoOption;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;
import java.util.Map;


public interface ClazzService {
    /* =================================================== CREATE =================================================== */
    String addClazz(ClazzCreateDTO createDTO);

    Clazz createClazz(Clazz clazz) throws Exception;
    ClazzReadDTO createClazzByDTO(ClazzCreateDTO createDTO) throws Exception;


    /* =================================================== READ ===================================================== */
    Page<Clazz> getPageAll(Pageable paging) throws Exception;
    @Deprecated
    Page<ClazzReadDTO> getPageDTOAll(Pageable paging) throws Exception;
    Page<ClazzReadDTO> getPageDTOAll(Pageable paging, Collection<DtoOption> options) throws Exception;

    /* id */
    Clazz getById(Long id) throws Exception;
    @Deprecated
    ClazzReadDTO getDTOById(Long id) throws Exception;
    ClazzReadDTO getDTOById(Long id, Collection<DtoOption> options) throws Exception;

    List<Clazz> getAllByIdIn(Collection<Long> idCollection) throws Exception;
    Map<Long, String> mapClazzIdClazzNameByIdIn(Collection<Long> idCollection) throws Exception;
    List<ClazzReadDTO> getAllDTOByIdIn(Collection<Long> idCollection, Collection<DtoOption> options) throws Exception;
    Map<Long, ClazzReadDTO> mapIdDTOByIdIn(Collection<Long> idCollection, Collection<DtoOption> options) throws Exception;

    /* courseSemesterId */
    List<Clazz> getAllByCourseSemesterIdIn(Collection<Long> scheduleIdCollection) throws Exception;
    List<ClazzReadDTO> getAllDTOByCourseSemesterIdIn(
            Collection<Long> scheduleIdCollection, Collection<DtoOption> options) throws Exception;
    Map<Long, List<ClazzReadDTO>> mapCourseSemesterIdListDTOByCourseSemesterIdIn(
            Collection<Long> courseSemesterIdCollection, Collection<DtoOption> options) throws Exception;


    /* =================================================== UPDATE =================================================== */
    String editClazz(ClazzUpdateDTO updateDTO);

    Clazz updateClazz(Clazz clazz) throws Exception;
    ClazzReadDTO updateClazzByDTO(ClazzUpdateDTO updateDTO) throws Exception;

    /* =================================================== DELETE =================================================== */
    String deleteClazz(Long Id);


    /* =================================================== WRAPPER ================================================== */
    ClazzReadDTO wrapDTO(Clazz clazz, Collection<DtoOption> options) throws Exception;
    List<ClazzReadDTO> wrapListDTO(Collection<Clazz> clazzCollection, Collection<DtoOption> options) throws Exception;
    Page<ClazzReadDTO> wrapPageDTO(Page<Clazz> clazzPage, Collection<DtoOption> options) throws Exception;
}
