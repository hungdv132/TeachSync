package com.teachsync.repositories;

import com.teachsync.entities.Clazz;
import com.teachsync.utils.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;


@Repository
public interface ClazzRepository extends JpaRepository<Clazz, Long> {

    List<Clazz> findAllByStatusIn(
            Collection<Status> statusIn);
    List<Clazz> findAllByStatusNotIn(
            Collection<Status> statusNotIn);

    Page<Clazz> findAllByStatusIn(
            Collection<Status> statusIn, Pageable pageable);
    Page<Clazz> findAllByStatusNotIn(
            Collection<Status> statusNotIn, Pageable pageable);

    /* id */
    Boolean existsByIdAndStatusNotIn(
            long id, Collection<Status> statusNotIn);
    Boolean existsAllByIdInAndStatusNotIn(
            Collection<Long> idCollection, Collection<Status> statusNotIn);

    Optional<Clazz> findByIdAndStatusIn(
            long id, Collection<Status> statusIn);
    
    Optional<Clazz> findByIdAndStatusNotIn(
            long id, Collection<Status> statusNotIn);

    List<Clazz> findAllByIdInAndStatusIn(
            Collection<Long> idCollection, Collection<Status> statusIn);
    Page<Clazz> findAllByIdInAndStatusIn(
            Collection<Long> idCollection, Collection<Status> statusIn, Pageable pageable);

    List<Clazz> findAllByIdInAndStatusNotIn(
            Collection<Long> idCollection, Collection<Status> statusNotIn);
    Page<Clazz> findAllByIdInAndStatusNotIn(
            Collection<Long> idCollection, Collection<Status> statusNotIn, Pageable pageable);

//    /* courseSemesterId */
//    List<Clazz> findAllByCourseSemesterIdAndStatusNot(
//            Long scheduleId, Status statusNot);
//    List<Clazz> findAllByCourseSemesterIdInAndStatusNot(
//            Collection<Long> scheduleIdCollection, Status statusNot);

    /* courseId */
    List<Clazz> findAllByCourseIdAndStatusIn(
            Long courseId, Collection<Status> statusIn);
    Page<Clazz> findAllByCourseIdAndStatusIn(
            Long courseId, Collection<Status> statusIn, Pageable pageable);

    List<Clazz> findAllByCourseIdAndStatusNotIn(
            Long courseId, Collection<Status> statusNotIn);
    Page<Clazz> findAllByCourseIdAndStatusNotIn(
            Long courseId, Collection<Status> statusNotIn, Pageable pageable);
    
    List<Clazz> findAllByCourseIdInAndStatusIn(
            Collection<Long> courseIdCollection, Collection<Status> statusIn);
    Page<Clazz> findAllByCourseIdInAndStatusIn(
            Collection<Long> courseIdCollection, Collection<Status> statusIn, Pageable pageable);
    
    List<Clazz> findAllByCourseIdInAndStatusNotIn(
            Collection<Long> courseIdCollection, Collection<Status> statusNotIn);
    Page<Clazz> findAllByCourseIdInAndStatusNotIn(
            Collection<Long> courseIdCollection, Collection<Status> statusNotIn, Pageable pageable);

    /* centerId */
    List<Clazz> findAllByCenterIdAndStatusIn(
            Long centerId, Collection<Status> statusIn);
    Page<Clazz> findAllByCenterIdAndStatusIn(
            Long centerId, Collection<Status> statusIn, Pageable pageable);

    List<Clazz> findAllByCenterIdAndStatusNotIn(
            Long centerId, Collection<Status> statusNotIn);
    Page<Clazz> findAllByCenterIdAndStatusNotIn(
            Long centerId, Collection<Status> statusNotIn, Pageable pageable);

    List<Clazz> findAllByCenterIdInAndStatusIn(
            Collection<Long> centerIdCollection, Collection<Status> statusIn);
    Page<Clazz> findAllByCenterIdInAndStatusIn(
            Collection<Long> centerIdCollection, Collection<Status> statusIn, Pageable pageable);

    List<Clazz> findAllByCenterIdInAndStatusNotIn(
            Collection<Long> centerIdCollection, Collection<Status> statusNotIn);
    Page<Clazz> findAllByCenterIdInAndStatusNotIn(
            Collection<Long> centerIdCollection, Collection<Status> statusNotIn, Pageable pageable);
    
    /* courseId && centerId */
    List<Clazz> findAllByCourseIdAndCenterIdAndStatusIn(
            Long courseId, Long centerId, Collection<Status> statusIn);
    Page<Clazz> findAllByCourseIdAndCenterIdAndStatusIn(
            Long courseId, Long centerId, Collection<Status> statusIn, Pageable pageable);
    
    List<Clazz> findAllByCourseIdAndCenterIdAndStatusNotIn(
            Long courseId, Long centerId, Collection<Status> statusNotIn);
    Page<Clazz> findAllByCourseIdAndCenterIdAndStatusNotIn(
            Long courseId, Long centerId, Collection<Status> statusNotIn, Pageable pageable);

    /* staffId */
    List<Clazz> findAllByStaffIdAndStatusIn(
            Long staffId, Collection<Status> statusIn);
    Page<Clazz> findAllByStaffIdAndStatusIn(
            Long staffId, Collection<Status> statusIn, Pageable pageable);

    List<Clazz> findAllByStaffIdAndStatusNotIn(
            Long staffId, Collection<Status> statusNotIn);
    Page<Clazz> findAllByStaffIdAndStatusNotIn(
            Long staffId, Collection<Status> statusNotIn, Pageable pageable);

    List<Clazz> findAllByStaffIdInAndStatusIn(
            Collection<Long> staffIdCollection, Collection<Status> statusIn);
    Page<Clazz> findAllByStaffIdInAndStatusIn(
            Collection<Long> staffIdCollection, Collection<Status> statusIn, Pageable pageable);

    List<Clazz> findAllByStaffIdInAndStatusNotIn(
            Collection<Long> staffIdCollection, Collection<Status> statusNotIn);
    Page<Clazz> findAllByStaffIdInAndStatusNotIn(
            Collection<Long> staffIdCollection, Collection<Status> statusNotIn, Pageable pageable);

    /* clazzAlias*/
    List<Clazz> findAllByClazzAliasContainsAndStatusIn(
            String clazzAlias, Collection<Status> statusNotIn);
    Page<Clazz> findAllByClazzAliasContainsAndStatusIn(
            String clazzAlias, Collection<Status> statusNotIn, Pageable pageable);

    List<Clazz> findAllByClazzAliasContainsAndStatusNotIn(
            String clazzAlias, Collection<Status> statusNotIn);
    Page<Clazz> findAllByClazzAliasContainsAndStatusNotIn(
            String clazzAlias, Collection<Status> statusNotIn, Pageable pageable);
    
    /* clazzName*/
    List<Clazz> findAllByClazzNameContainsAndStatusIn(
            String clazzName, Collection<Status> statusNotIn);
    Page<Clazz> findAllByClazzNameContainsAndStatusIn(
            String clazzName, Collection<Status> statusNotIn, Pageable pageable);

    List<Clazz> findAllByClazzNameContainsAndStatusNotIn(
            String clazzName, Collection<Status> statusNotIn);
    Page<Clazz> findAllByClazzNameContainsAndStatusNotIn(
            String clazzName, Collection<Status> statusNotIn, Pageable pageable);
    
    /* Check duplicate */
    Boolean existsByClazzAliasAndStatusNotIn(
            String clazzAlias, Collection<Status> statusNotIn);
    Boolean existsByIdNotAndClazzAliasAndStatusNotIn(
            Long id, String clazzAlias, Collection<Status> statusNotIn);

}