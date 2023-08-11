package com.teachsync.services.test;

import com.teachsync.dtos.test.TestCreateDTO;
import com.teachsync.dtos.test.TestReadDTO;
import com.teachsync.dtos.test.TestUpdateDTO;
import com.teachsync.entities.Test;
import com.teachsync.utils.enums.DtoOption;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface TestService {
    /* =================================================== CREATE =================================================== */
    Test createTest(Test test) throws Exception;
    TestReadDTO createTestByDTO(TestCreateDTO createDTO) throws Exception;


    /* =================================================== READ ===================================================== */
    Page<Test> getPageAll(Pageable pageable) throws Exception;
    Page<TestReadDTO> getPageAllDTO(Pageable pageable, Collection<DtoOption> options) throws Exception;

    /* id */
    Test getById(Long id) throws Exception;
    TestReadDTO getDTOById(Long id, Collection<DtoOption> options) throws Exception;

    List<Test> getAllByIdIn(Collection<Long> idCollection) throws Exception;
    List<TestReadDTO> getAllDTOByIdIn(Collection<Long> idCollection, Collection<DtoOption> options) throws Exception;
    Map<Long, TestReadDTO> mapIdDTOByIdIn(Collection<Long> idCollection, Collection<DtoOption> options) throws Exception;

    /* courseId */
    List<Test> getAllByCourseId(Long courseId) throws Exception;
    List<TestReadDTO> getAllDTOByCourseId(Long courseId, Collection<DtoOption> options) throws Exception;

    Page<Test> getPageAllByCourseIdIn(Pageable pageable, Collection<Long> courseIdCollection) throws Exception;
    Page<TestReadDTO> getPageAllDTOByCourseIdIn(
            Pageable pageable, Collection<Long> courseIdCollection, Collection<DtoOption> options) throws Exception;

    List<Test> getAllByCourseIdIn(Collection<Long> courseIdCollection) throws Exception;
    List<TestReadDTO> getAllDTOByCourseIdIn(
            Collection<Long> courseIdCollection, Collection<DtoOption> options) throws Exception;
    Map<Long, List<TestReadDTO>> mapCourseIdListDTOByCourseIdIn(
            Collection<Long> courseIdCollection, Collection<DtoOption> options) throws Exception;


    /* =================================================== UPDATE =================================================== */
    Test updateTest(Test test) throws Exception;
    TestReadDTO updateTestByDTO(TestUpdateDTO updateDTO) throws Exception;


    /* =================================================== DELETE =================================================== */


    /* =================================================== WRAPPER ================================================== */
    TestReadDTO wrapDTO(Test test, Collection<DtoOption> options) throws Exception;
    List<TestReadDTO> wrapListDTO(Collection<Test> testCollection, Collection<DtoOption> options) throws Exception;
    Page<TestReadDTO> wrapPageDTO(Page<Test> testPage, Collection<DtoOption> options) throws Exception;
}
