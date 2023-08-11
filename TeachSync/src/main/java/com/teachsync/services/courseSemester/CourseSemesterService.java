package com.teachsync.services.courseSemester;

import com.teachsync.dtos.courseSemester.CourseSemesterReadDTO;
import com.teachsync.entities.CourseSemester;
import com.teachsync.utils.enums.DtoOption;
import org.springframework.data.domain.Page;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface CourseSemesterService {
    /* =================================================== CREATE =================================================== */
    CourseSemester createCourseSemester(CourseSemester courseSemester) throws Exception;


    /* =================================================== READ ===================================================== */
    /* id */
    CourseSemester getById(Long id) throws Exception;
    CourseSemesterReadDTO getDTOById(Long id, Collection<DtoOption> options) throws Exception;

    List<CourseSemester> getAllByIdIn(Collection<Long> idCollection) throws Exception;
    List<CourseSemesterReadDTO> getAllDTOByIdIn(
            Collection<Long> idCollection, Collection<DtoOption> options) throws Exception;
    Map<Long, CourseSemesterReadDTO> mapIdDTOByIdIn(
            Collection<Long> idCollection, Collection<DtoOption> options) throws Exception;

    /* courseId */
    List<CourseSemester> getAllByCourseId(Long courseId) throws Exception;
    List<CourseSemesterReadDTO> getAllDTOByCourseId(
            Long courseId, Collection<DtoOption> options) throws Exception;

    List<CourseSemesterReadDTO> getAllLatestDTOByCourseId(
            Long courseId, Collection<DtoOption> options) throws Exception;
    Map<Long, CourseSemesterReadDTO> mapIdLatestDTOByCourseId(
            Long courseId, Collection<DtoOption> options) throws Exception;

    /* courseId, semesterId */
    List<CourseSemester> getAllByCourseIdAndSemesterIdIn(
            Long courseId, Collection<Long> semesterIdCollection) throws Exception;
    Map<Long, String> mapIdSemesterIdCenterIdStringByCourseIdAndSemesterIdIn(
            Long courseId, Collection<Long> semesterIdCollection) throws Exception;
    List<CourseSemesterReadDTO> getAllDTOByCourseIdAndSemesterIdIn(
            Long courseId, Collection<Long> semesterIdCollection, Collection<DtoOption> options) throws Exception;

    /* semesterId */
    List<CourseSemester> getAllBySemesterId(Long semesterId) throws Exception;
    List<CourseSemesterReadDTO> getAllDTOBySemesterId(Long semesterId, Collection<DtoOption> options) throws Exception;

    List<CourseSemester> getAllBySemesterIdIn(Collection<Long> semesterIdCollection) throws Exception;
    Map<Long, Map<Long, Set<Long>>> mapSemesterIdMapCenterIdListCourseIdBySemesterIdIn(
            Collection<Long> semesterIdCollection) throws Exception;
    List<CourseSemesterReadDTO> getAllDTOBySemesterIdIn(
            Collection<Long> semesterIdCollection, Collection<DtoOption> options) throws Exception;

    /* courseId, semesterId, centerId */
    CourseSemester getByCourseIdAndSemesterIdAndCenterId(Long courseId, Long semesterId, Long centerId) throws Exception;

    /* =================================================== UPDATE =================================================== */


    /* =================================================== DELETE =================================================== */


    /* =================================================== WRAPPER ================================================== */
    CourseSemesterReadDTO wrapDTO(
            CourseSemester courseSemester, Collection<DtoOption> options) throws Exception;
    List<CourseSemesterReadDTO> wrapListDTO(
            Collection<CourseSemester> courseSemesterCollection, Collection<DtoOption> options) throws Exception;
    Page<CourseSemesterReadDTO> wrapPageDTO(
            Page<CourseSemester> courseSemesterPage, Collection<DtoOption> options) throws Exception;
}
