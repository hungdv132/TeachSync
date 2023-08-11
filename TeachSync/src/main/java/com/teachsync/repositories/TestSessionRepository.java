package com.teachsync.repositories;

import com.teachsync.entities.TestSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestSessionRepository extends JpaRepository<TestSession, Long> {
    Page<TestSession> findAllByOrderByStartDateDesc(Pageable pageable);

    TestSession findAllByUsernameAndStatusNotAndTestId(String userName, int status, Long testid);

    Page<TestSession> findAllByClazzContainingOrderByStartDateDesc(Pageable pageable, String clazz);
    Page<TestSession> findAllBySubjectContainingOrderByStartDateDesc(Pageable pageable, String username);
    Page<TestSession> findAllByUsernameContainingOrderByStartDate(Pageable pageable, String username);
}
