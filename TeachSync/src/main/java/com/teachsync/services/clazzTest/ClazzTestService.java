package com.teachsync.services.clazzTest;

import com.teachsync.dtos.clazzTest.ClazzTestReadDTO;
import com.teachsync.entities.ClazzTest;
import com.teachsync.utils.enums.DtoOption;
import org.springframework.data.domain.Page;

import java.util.Collection;
import java.util.List;

public interface ClazzTestService {
    /* =================================================== CREATE =================================================== */


    /* =================================================== READ ===================================================== */


    /* =================================================== UPDATE =================================================== */


    /* =================================================== DELETE =================================================== */


    /* =================================================== WRAPPER ================================================== */
    ClazzTestReadDTO wrapDTO(ClazzTest clazzTest, Collection<DtoOption> options) throws Exception;
    List<ClazzTestReadDTO> wrapListDTO(Collection<ClazzTest> clazzTestCollection, Collection<DtoOption> options) throws Exception;
    Page<ClazzTestReadDTO> wrapPageDTO(Page<ClazzTest> clazzTestPage, Collection<DtoOption> options) throws Exception;
}
