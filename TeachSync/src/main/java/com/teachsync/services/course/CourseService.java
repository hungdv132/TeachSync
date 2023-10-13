package com.teachsync.services.course;

import com.teachsync.dtos.course.CourseCreateDTO;
import com.teachsync.dtos.course.CourseReadDTO;
import com.teachsync.dtos.course.CourseUpdateDTO;
import com.teachsync.entities.Course;
import com.teachsync.utils.enums.DtoOption;
import com.teachsync.utils.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface CourseService {
    /* =================================================== CREATE =================================================== */
    Course createCourse(
            Course course) throws Exception;
    CourseReadDTO createCourseByDTO(
            CourseCreateDTO createDTO) throws Exception;


    /* =================================================== READ ===================================================== */
    List<Course> getAll(
            Collection<Status> statuses, boolean isStatusIn) throws Exception;
    List<CourseReadDTO> getAllDTO(
            Collection<Status> statuses, boolean isStatusIn,
            Collection<DtoOption> options) throws Exception;
    Map<Long, CourseReadDTO> mapIdDTO(
            Collection<Status> statuses, boolean isStatusIn,
            Collection<DtoOption> options) throws Exception;


    Page<Course> getPageAll(
            Pageable paging, Collection<Status> statuses, boolean isStatusIn) throws Exception;
    Page<CourseReadDTO> getPageAllDTO(
            Pageable paging, Collection<Status> statuses, boolean isStatusIn,
            Collection<DtoOption> options) throws Exception;
    Page<CourseReadDTO> getPageAllDTOOnSale(
            Pageable paging, Collection<Status> statuses, boolean isStatusIn,
            Collection<DtoOption> options) throws Exception;

    /* id */
    Boolean existsById(
            Long id) throws Exception;
    Boolean existsAllByIdIn(
            Collection<Long> ids) throws Exception;

    Course getById(
            Long id, Collection<Status> statuses, boolean isStatusIn) throws Exception;
    CourseReadDTO getDTOById(
            Long id, Collection<Status> statuses, boolean isStatusIn, Collection<DtoOption> options) throws Exception;

    List<Course> getAllByIdIn(
            Collection<Long> ids, Collection<Status> statuses, boolean isStatusIn) throws Exception;
    Map<Long, String> mapCourseIdCourseAliasByIdIn(
            Collection<Long> ids, Collection<Status> statuses, boolean isStatusIn) throws Exception;
    Map<Long, String> mapCourseIdCourseNameByIdIn(
            Collection<Long> ids, Collection<Status> statuses, boolean isStatusIn) throws Exception;
    List<CourseReadDTO> getAllDTOByIdIn(
            Collection<Long> ids, Collection<Status> statuses, boolean isStatusIn,
            Collection<DtoOption> options) throws Exception;
    Map<Long, CourseReadDTO> mapIdDTOByIdIn(
            Collection<Long> ids, Collection<Status> statuses, boolean isStatusIn,
            Collection<DtoOption> options) throws Exception;

    Page<Course> getPageAllByIdIn(
            Pageable paging, Collection<Long> ids, Collection<Status> statuses, boolean isStatusIn) throws Exception;
    Page<CourseReadDTO> getPageAllDTOByIdIn(
            Pageable paging, Collection<Long> ids, Collection<Status> statuses, boolean isStatusIn,
            Collection<DtoOption> options) throws Exception;

    /* courseAlias */
    List<Course> getAllByAliasContains(
            String courseAlias, Collection<Status> statuses, boolean isStatusIn) throws Exception;
    List<CourseReadDTO> getAllDTOByAliasContains(
            String courseAlias, Collection<Status> statuses, boolean isStatusIn,
            Collection<DtoOption> options) throws Exception;


    Page<Course> getPageAllByAliasContains(
            Pageable paging, String courseAlias, Collection<Status> statuses, boolean isStatusIn) throws Exception;
    Page<CourseReadDTO> getPageAllDTOByAliasContains(
            Pageable paging, String courseAlias, Collection<Status> statuses, boolean isStatusIn,
            Collection<DtoOption> options) throws Exception;

    /* courseName */
    List<Course> getAllByNameContains(
            String courseName, Collection<Status> statuses, boolean isStatusIn) throws Exception;
    List<CourseReadDTO> getAllDTOByNameContains(
            String courseName, Collection<Status> statuses, boolean isStatusIn,
            Collection<DtoOption> options) throws Exception;

    Page<Course> getPageAllByNameContains(
            Pageable paging, String courseName, Collection<Status> statuses, boolean isStatusIn) throws Exception;
    Page<CourseReadDTO> getPageAllDTOByNameContains(
            Pageable paging, String courseName, Collection<Status> statuses, boolean isStatusIn,
            Collection<DtoOption> options) throws Exception;


    /* =================================================== UPDATE =================================================== */
    Course updateCourse(
            Course course) throws Exception;
    CourseReadDTO updateCourseByDTO(
            CourseUpdateDTO updateDTO) throws Exception;


    /* =================================================== DELETE =================================================== */
    Boolean deleteCourse(
            Long id) throws Exception;


    /* =================================================== WRAPPER ================================================== */
    CourseReadDTO wrapDTO(
            Course course, Collection<DtoOption> options) throws Exception;
    List<CourseReadDTO> wrapListDTO(
            Collection<Course> courseCollection, Collection<DtoOption> options) throws Exception;
    Page<CourseReadDTO> wrapPageDTO(
            Page<Course> coursePage, Collection<DtoOption> options) throws Exception;
}
