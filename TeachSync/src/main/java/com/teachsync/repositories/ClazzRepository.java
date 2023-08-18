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

    Page<Clazz> findAllByStatusNot(Status status, Pageable pageable);

    /* id */
    Optional<Clazz> findByIdAndStatusNot(long id, Status status);
    List<Clazz> findAllByIdInAndStatusNot(Collection<Long> idCollection, Status status);

    /* courseSemesterId */
    List<Clazz> findAllByCourseSemesterIdAndStatusNot(Long scheduleId, Status status);
    List<Clazz> findAllByCourseSemesterIdInAndStatusNot(Collection<Long> scheduleIdCollection, Status status);


    /* Check duplicate */



    List<Clazz> findAllByClazzNameContaining(String text);

}