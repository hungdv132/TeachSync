package com.teachsync.repositories;

import com.teachsync.entities.ClazzTest;
import com.teachsync.utils.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClazzTestRepository extends JpaRepository<ClazzTest, Long> {

    /* id */
    Optional<ClazzTest> findByIdAndStatusNot(Long id, Status status);
    List<ClazzTest> findAllByIdInAndStatusNot(Collection<Long> clazzIdCollection, Status status);

    /* clazzId */
    List<ClazzTest> findAllByClazzIdAndStatusNot(Long clazzId, Status status);

    /* clazzId & testId */
    Optional<ClazzTest> findByClazzIdAndTestIdAndStatusNot(Long clazzId, Long testId, Status status);
}