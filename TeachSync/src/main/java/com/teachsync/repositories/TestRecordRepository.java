package com.teachsync.repositories;

import com.teachsync.entities.TestRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestRecordRepository extends JpaRepository<TestRecord, Long> {
    List<TestRecord> findAllByMemberTestRecordId(Long id);
}