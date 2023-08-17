package com.teachsync.repositories;


import com.teachsync.entities.Request;
import com.teachsync.utils.enums.RequestType;
import com.teachsync.utils.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;


@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

    List<Request> findAllByStatusNot(Status status);
    Page<Request> findAllByStatusNot(Status status, Pageable pageable);

    /* id */
    Optional<Request> findByIdAndStatusNot(Long id, Status status);
    List<Request> findAllByIdInAndStatusNot(Collection<Long> idCollection, Status status);

    /* requesterId (userId) */
    List<Request> findAllByRequesterIdAndStatusNot(Long requesterId, Status status);
    Page<Request> findAllByRequesterIdAndStatusNot(Long requesterId, Status status, Pageable pageable);
    List<Request> findAllByRequesterIdInAndStatusNot(Collection<Long> requesterIdCollection, Status status);

    /* clazzId */
    List<Request> findAllByClazzIdAndStatusNot(Long clazzId, Status status);
    Page<Request> findAllByClazzIdAndStatusNot(Long clazzId, Status status, Pageable pageable);
    List<Request> findAllByClazzIdInAndStatusNot(Collection<Long> clazzIdCollection, Status status);

    /* Check duplicate */
    /** Check duplicate insert */
    Boolean existsByRequesterIdAndClazzIdAndRequestTypeAndStatusNotIn(
            Long requesterId, Long clazzId, RequestType requestType, Collection<Status> statuses);

    /** Check duplicate update */
    Boolean existsByIdNotAndRequesterIdAndClazzIdAndRequestTypeAndStatusNotIn(
            Long id, Long requesterId, Long clazzId, RequestType requestType, Collection<Status> statuses);
}