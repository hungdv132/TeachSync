package com.teachsync.repositories;

import com.teachsync.entities.Course;
import com.teachsync.utils.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {


    Page<Course> findAllByStatusNot(Status status, Pageable pageable);
    List<Course> findAllByStatusNot(Status status);

    /* id */
    Optional<Course> findByIdAndStatusNot(long id, Status status);
    Page<Course> findAllByIdInAndStatusNot(Collection<Long> idCollection, Status status, Pageable pageable);
    List<Course> findAllByIdInAndStatusNot(Collection<Long> idCollection, Status status);

    /* Check duplicate */
    @Query("SELECT case when count(c) > 0 then true else false end " +
            "FROM Course c " +
            "WHERE (c.courseName = ?1 or c.courseAlias = ?2) and c.status <> ?3 ")
    Boolean existsByCourseNameOrCourseAliasAndStatusNot(String name, String alias, Status status);

    @Query("SELECT case when count(c) > 0 then true else false end " +
            "FROM Course c " +
            "WHERE c.id <> ?1 and (c.courseName = ?2 or c.courseAlias = ?3) and c.status <> ?4 ")
    Boolean existsByIdNotAndCourseNameOrCourseAliasAndStatusNot(Long id, String name, String alias, Status status);
}