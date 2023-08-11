package com.teachsync.repositories;

import com.teachsync.entities.MemberTestRecord;
import com.teachsync.utils.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberTestRecordRepository extends JpaRepository<MemberTestRecord, Long> {

    Page<MemberTestRecord> findAllByStatusNot(Status status, Pageable pageable);

    /* clazzTestId */
    List<MemberTestRecord> findAllByClazzTestIdAndStatusNot(Long clazzTestId, Status status);

    /* memberId & clazzTestId */
    Optional<MemberTestRecord> findByMemberIdAndClazzTestIdAndStatusNot(Long memberId, Long clazzTestId, Status status);
}