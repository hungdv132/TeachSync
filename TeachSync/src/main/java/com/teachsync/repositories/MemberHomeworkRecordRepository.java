package com.teachsync.repositories;

import com.teachsync.entities.MemberHomeworkRecord;
import com.teachsync.utils.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberHomeworkRecordRepository extends JpaRepository<MemberHomeworkRecord, Long> {

    List<MemberHomeworkRecord> findAllByStatusNotOrderByCreatedAtDesc(Status status);

    List<MemberHomeworkRecord> findAllByStatusNotAndCreatedByOrderByCreatedAtDesc(Status status,Long memberId);

    List<MemberHomeworkRecord> findAllByStatusNotAndHomeworkIdOrderByCreatedAtDesc(Status status,Long memberId);
}