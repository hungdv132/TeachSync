package com.teachsync.repositories;

import com.teachsync.entities.ClazzMember;
import com.teachsync.utils.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ClazzMemberRepository extends JpaRepository<ClazzMember, Long> {


    /* clazzId */
    List<ClazzMember> findAllByClazzIdAndStatusNot(Long clazzId, Status status);

    List<ClazzMember> findAllByClazzIdInAndStatusNot(Collection<Long> clazzIdCollection, Status status);


    /* userId */
    List<ClazzMember> findAllByUserIdAndStatusNot(Long clazzId, Status status);
    List<ClazzMember> findAllByUserIdInAndStatusNot(Collection<Long> clazzIdCollection, Status status);


}