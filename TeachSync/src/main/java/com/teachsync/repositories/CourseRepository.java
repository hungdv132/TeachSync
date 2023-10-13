package com.teachsync.repositories;

import com.teachsync.entities.Course;
import com.teachsync.utils.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    List<Course> findAllByStatusNotIn(
            Collection<Status> statusNotIn);
    Page<Course> findAllByStatusNotIn(
            Collection<Status> statusNotIn, Pageable pageable);

    List<Course> findAllByStatusIn(
            Collection<Status> statusIn);
    Page<Course> findAllByStatusIn(
            Collection<Status> statusIn, Pageable pageable);

    /* id */
    Boolean existsByIdAndStatusNotIn(
            Long id, Collection<Status> statusNotIn);
    Boolean existsAllByIdInAndStatusNotIn(
            Collection<Long> idCollection, Collection<Status> statusNotIn);

    Optional<Course> findByIdAndStatusNotIn(
            Long id, Collection<Status> statusNotIn);

    Optional<Course> findByIdAndStatusIn(
            Long id, Collection<Status> statusIn);

    List<Course> findAllByIdInAndStatusNotIn(
            Collection<Long> idCollection, Collection<Status> statusNotIn);
    Page<Course> findAllByIdInAndStatusNotIn(
            Collection<Long> idCollection, Collection<Status> statusNotIn, Pageable pageable);

    List<Course> findAllByIdInAndStatusIn(
            Collection<Long> idCollection, Collection<Status> statusIn);
    Page<Course> findAllByIdInAndStatusIn(
            Collection<Long> idCollection, Collection<Status> statusIn, Pageable pageable);

    /* courseAlias */
    List<Course> findAllByCourseAliasContainsAndStatusNotIn(
            String alias, Collection<Status> statusNotIn);
    Page<Course> findAllByCourseAliasContainsAndStatusNotIn(
            String alias, Collection<Status> statusNotIn, Pageable pageable);

    List<Course> findAllByCourseAliasContainsAndStatusIn(
            String alias, Collection<Status> statusIn);
    Page<Course> findAllByCourseAliasContainsAndStatusIn(
            String alias, Collection<Status> statusIn, Pageable pageable);
    
    /* courseName */
    List<Course> findAllByCourseNameContainsAndStatusNotIn(
            String name, Collection<Status> statusNotIn);
    Page<Course> findAllByCourseNameContainsAndStatusNotIn(
            String name, Collection<Status> statusNotIn, Pageable pageable);

    List<Course> findAllByCourseNameContainsAndStatusIn(
            String name, Collection<Status> statusIn);
    Page<Course> findAllByCourseNameContainsAndStatusIn(
            String name, Collection<Status> statusIn, Pageable pageable);

    /* Check duplicate */
    Boolean existsByCourseAliasAndStatusNotIn(
            String alias, Collection<Status> statusNotIn);

    Boolean existsByIdNotAndCourseAliasAndStatusNotIn(
            Long id, String alias, Collection<Status> statusNotIn);
}