package com.teachsync.services.clazzTest;

import com.teachsync.dtos.clazzTest.ClazzTestReadDTO;
import com.teachsync.entities.ClazzTest;
import com.teachsync.utils.enums.DtoOption;
import org.springframework.data.domain.Page;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface ClazzTestService {
    /* =================================================== CREATE =================================================== */


    /* =================================================== READ ===================================================== */
    /* id */
    ClazzTest getById(Long id) throws Exception;
    ClazzTestReadDTO getDTOById(Long id, Collection<DtoOption> options) throws Exception;

    List<ClazzTest> getAllByIdIn(Collection<Long> idCollection) throws Exception;
    List<ClazzTestReadDTO> getAllDTOByIdIn(Collection<Long> idCollection, Collection<DtoOption> options) throws Exception;
    Map<Long, ClazzTestReadDTO> mapIdDTOByIdIn(Collection<Long> idCollection, Collection<DtoOption> options) throws Exception;

    /* clazzId */
    List<ClazzTest> getAllByClazzId(Long clazzId) throws Exception;
    List<ClazzTestReadDTO> getAllDTOByClazzId(Long clazzId, Collection<DtoOption> options) throws Exception;

    /* clazzId & testId */
    ClazzTest getByClazzIdAndTestId(Long clazzId, Long testId) throws Exception;
    ClazzTestReadDTO getDTOByClazzIdAndTestId(Long clazzId, Long testId, Collection<DtoOption> options) throws Exception;

    /* =================================================== UPDATE =================================================== */


    /* =================================================== DELETE =================================================== */


    /* =================================================== WRAPPER ================================================== */
    ClazzTestReadDTO wrapDTO(ClazzTest clazzTest, Collection<DtoOption> options) throws Exception;
    List<ClazzTestReadDTO> wrapListDTO(Collection<ClazzTest> clazzTestCollection, Collection<DtoOption> options) throws Exception;
    Page<ClazzTestReadDTO> wrapPageDTO(Page<ClazzTest> clazzTestPage, Collection<DtoOption> options) throws Exception;
}
