package com.teachsync.services.course;

import com.teachsync.dtos.course.CourseCreateDTO;
import com.teachsync.dtos.course.CourseReadDTO;
import com.teachsync.dtos.course.CourseUpdateDTO;
import com.teachsync.entities.Course;
import com.teachsync.utils.enums.DtoOption;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface CourseService {
    /* =================================================== CREATE =================================================== */
    Course createCourse(Course course) throws Exception;
    CourseReadDTO createCourseByDTO(CourseCreateDTO createDTO) throws Exception;

    /* =================================================== READ ===================================================== */
    Page<Course> getPageAll(Pageable paging) throws Exception;

    Page<CourseReadDTO> getPageDTOAll(Pageable paging) throws Exception;
    Page<CourseReadDTO> getPageDTOAllHotCourse(Pageable paging) throws Exception;

    List<Course> getAll() throws Exception;

    @Deprecated
    List<CourseReadDTO> getAllDTO() throws Exception;
    List<CourseReadDTO> getAllDTO(Collection<DtoOption> options) throws Exception;
    Map<Long, CourseReadDTO> mapIdDTO(Collection<DtoOption> options) throws Exception;

    /* id */
    Course getById(Long id) throws Exception;
    CourseReadDTO getDTOById(Long id, Collection<DtoOption> options) throws Exception;

    Page<Course> getPageAllByIdIn(Pageable paging, Collection<Long> courseIdCollection) throws Exception;
    Page<CourseReadDTO> getPageDTOAllByIdIn(Pageable paging, Collection<Long> courseIdCollection) throws Exception;

    List<Course> getAllByIdIn(Collection<Long> courseIdCollection) throws Exception;
    Map<Long, String> mapCourseIdCourseNameByIdIn(Collection<Long> courseIdCollection) throws Exception;
    Map<Long, String> mapCourseIdCourseAliasByIdIn(Collection<Long> courseIdCollection) throws Exception;
    List<CourseReadDTO> getAllDTOByIdIn(
            Collection<Long> courseIdCollection, Collection<DtoOption> options) throws Exception;
    Map<Long, CourseReadDTO> mapIdDTOByIdIn(
            Collection<Long> courseIdCollection, Collection<DtoOption> options) throws Exception;

    /* courseName */
    List<Course> getAllByNameContains(String courseName) throws Exception;
    List<CourseReadDTO> getAllDTOByNameContains(String courseName, Collection<DtoOption> options) throws Exception;


    /* =================================================== UPDATE =================================================== */
    Course updateCourse(Course course) throws Exception;
    CourseReadDTO updateCourseByDTO(CourseUpdateDTO updateDTO) throws Exception;


    /* =================================================== DELETE =================================================== */

    void deleteCourse(Long Id, Long userId) throws Exception;

    /* =================================================== WRAPPER ================================================== */
    @Deprecated
    CourseReadDTO wrapDTO(Course course) throws Exception;
    @Deprecated
    List<CourseReadDTO> wrapListDTO(Collection<Course> courseCollection) throws Exception;
    @Deprecated
    Page<CourseReadDTO> wrapPageDTO(Page<Course> coursePage) throws Exception;

    CourseReadDTO wrapDTO(Course course, Collection<DtoOption> options) throws Exception;
    List<CourseReadDTO> wrapListDTO(Collection<Course> courseCollection, Collection<DtoOption> options) throws Exception;
    Page<CourseReadDTO> wrapPageDTO(Page<Course> coursePage, Collection<DtoOption> options) throws Exception;

}
