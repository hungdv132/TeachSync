package com.teachsync.repositories;

import com.teachsync.entities.Homework;
import com.teachsync.entities.MemberHomeworkRecord;
import com.teachsync.utils.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberHomeworkRecordRepository extends JpaRepository<MemberHomeworkRecord, Long> {

    List<MemberHomeworkRecord> findAllByStatusNot(Status status);

    List<MemberHomeworkRecord> findAllByStatusNotAndMemberId(Status status,Long memberId);
}