package com.teachsync.repositories;

import com.teachsync.entities.MemberTestRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberTestRecordRepository extends JpaRepository<MemberTestRecord, Long> {
}