package com.teachsync.repositories;

import com.teachsync.entities.Test;
import com.teachsync.utils.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface TestRepository extends JpaRepository<Test, Long> {

    Page<Test> findAllByStatusNot(Status status, Pageable pageable);

    Page<Test> findByTestNameContainingOrderByCreatedAtDesc(String course, Pageable pageable);
    Page<Test> findAllByOrderByCreatedAtDesc(Pageable pageable);

    /* id */
    Optional<Test> findByIdAndStatusNot(Long id, Status status);
    List<Test> findAllByIdInAndStatusNot(Collection<Long> idCollection, Status status);

    /* courseId */
    List<Test> findAllByCourseIdAndStatusNot(Long courseId, Status status);
    Page<Test> findAllByCourseIdInAndStatusNot(Collection<Long> courseIdCollection, Status status, Pageable pageable);
    List<Test> findAllByCourseIdInAndStatusNot(Collection<Long> courseIdCollection, Status status);
}
