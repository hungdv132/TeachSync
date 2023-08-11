package com.teachsync.repositories;

import com.teachsync.entities.MemberHomeworkRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberHomeworkRecordRepository extends JpaRepository<MemberHomeworkRecord, Long> {
}