package com.teachsync.repositories;

import com.teachsync.entities.TestRecord2;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestRecord2Repository extends JpaRepository<TestRecord2, Long> {
    List<TestRecord2> findAllByClazzAndTestId(String clazz, Long testId);

    List<TestRecord2> findAllByClazzAndTestIdAndUserId(String clazz, Long testId, Long userId);
}
