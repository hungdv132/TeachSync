package com.teachsync.repositories;

import com.teachsync.entities.ClazzMember;
import com.teachsync.utils.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClazzMemberRepository extends JpaRepository<ClazzMember, Long> {

    /* id */
    Optional<ClazzMember> findByIdAndStatusNot(Long id, Status status);
    List<ClazzMember> findAllByIdInAndStatusNot(Collection<Long> idCollection, Status status);


    /* clazzId */
    List<ClazzMember> findAllByClazzIdAndStatusNot(Long clazzId, Status status);


    List<ClazzMember> findAllByClazzIdInAndStatusNot(Collection<Long> clazzIdCollection, Status status);


    /* userId */
    List<ClazzMember> findAllByUserIdAndStatusNot(Long userId, Status status);
    List<ClazzMember> findAllByUserIdInAndStatusNot(Collection<Long> userIdCollection, Status status);

    /* clazzId & userId */
    Optional<ClazzMember> findByClazzIdAndUserIdAndStatusNot(Long clazzId, Long userId, Status status);

}