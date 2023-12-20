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

    List<Request> findAllByStatusIn(
            Collection<Status> statusIn);
    Page<Request> findAllByStatusIn(
            Collection<Status> statusIn, Pageable paging);
    
    List<Request> findAllByStatusNotIn(
            Collection<Status> statusNotIn);
    Page<Request> findAllByStatusNotIn(
            Collection<Status> statusNotIn, Pageable paging);

    /* id */
    Optional<Request> findByIdAndStatusIn(
            Long id, Collection<Status> statusIn);
    List<Request> findAllByIdInAndStatusIn(
            Collection<Long> idCollection, Collection<Status> statusIn);

    Optional<Request> findByIdAndStatusNotIn(
            Long id, Collection<Status> statusNotIn);
    List<Request> findAllByIdInAndStatusNotIn(
            Collection<Long> idCollection, Collection<Status> statusNotIn);

    /* requesterId (User.id) */
    List<Request> findAllByRequesterIdAndStatusIn(
            Long requesterId, Collection<Status> statusIn);
    Page<Request> findAllByRequesterIdAndStatusIn(
            Long requesterId, Collection<Status> statusIn, Pageable paging);
    List<Request> findAllByRequesterIdInAndStatusIn(
            Collection<Long> requesterIdCollection, Collection<Status> statusIn);
    Page<Request> findAllByRequesterIdInAndStatusIn(
            Collection<Long> requesterIdCollection, Collection<Status> statusNotIn, Pageable paging);

    List<Request> findAllByRequesterIdAndStatusNotIn(
            Long requesterId, Collection<Status> statusNotIn);
    Page<Request> findAllByRequesterIdAndStatusNotIn(
            Long requesterId, Collection<Status> statusNotIn, Pageable paging);
    List<Request> findAllByRequesterIdInAndStatusNotIn(
            Collection<Long> requesterIdCollection, Collection<Status> statusNotIn);
    Page<Request> findAllByRequesterIdInAndStatusNotIn(
            Collection<Long> requesterIdCollection, Collection<Status> statusNotIn, Pageable paging);

    /* id & requesterId */
    Optional<Request> findByIdAndRequesterIdAndStatusIn(
            Long id, Long requesterId, Collection<Status> statusIn);
    Optional<Request> findByIdAndRequesterIdAndStatusNotIn(
            Long id, Long requesterId, Collection<Status> statusNotIn);

    /* clazzId */
    List<Request> findAllByClazzIdAndStatusIn(
            Long clazzId, Collection<Status> statusIn);
    Page<Request> findAllByClazzIdAndStatusIn(
            Long clazzId, Collection<Status> statusIn, Pageable paging);
    List<Request> findAllByClazzIdAndRequestTypeAndStatusIn(
            Long clazzId, RequestType requestType, Collection<Status> statusIn);
    Page<Request> findAllByClazzIdAndRequestTypeAndStatusIn(
            Long clazzId, RequestType requestType, Collection<Status> statusIn, Pageable paging);
    List<Request> findAllByClazzIdInAndStatusIn(
            Collection<Long> clazzIdCollection, Collection<Status> statusIn);
    Page<Request> findAllByClazzIdInAndStatusIn(
            Collection<Long> clazzIdCollection, Collection<Status> statusIn, Pageable paging);

    List<Request> findAllByClazzIdAndStatusNotIn(
            Long clazzId, Collection<Status> statusNotIn);
    Page<Request> findAllByClazzIdAndStatusNotIn(
            Long clazzId, Collection<Status> statusNotIn, Pageable paging);
    List<Request> findAllByClazzIdAndRequestTypeAndStatusNotIn(
            Long clazzId, RequestType requestType, Collection<Status> statusNotIn);
    Page<Request> findAllByClazzIdAndRequestTypeAndStatusNotIn(
            Long clazzId, RequestType requestType, Collection<Status> statusNotIn, Pageable paging);
    List<Request> findAllByClazzIdInAndStatusNotIn(
            Collection<Long> clazzIdCollection, Collection<Status> statusNotIn);
    Page<Request> findAllByClazzIdInAndStatusNotIn(
            Collection<Long> clazzIdCollection, Collection<Status> statusNotIn, Pageable paging);

    /* requesterId & clazzId */
    List<Request> findAllByRequesterIdAndClazzIdAndRequestTypeAndStatusIn(
            Long requesterId, Long clazzId, RequestType requestType, Collection<Status> statusIn);
    Page<Request> findAllByRequesterIdAndClazzIdAndRequestTypeAndStatusIn(
            Long requesterId, Long clazzId, RequestType requestType, Collection<Status> statusIn, Pageable paging);

    List<Request> findAllByRequesterIdAndClazzIdAndRequestTypeAndStatusNotIn(
            Long requesterId, Long clazzId, RequestType requestType, Collection<Status> statusNotIn);
    Page<Request> findAllByRequesterIdAndClazzIdAndRequestTypeAndStatusNotIn(
            Long requesterId, Long clazzId, RequestType requestType, Collection<Status> statusNotIn, Pageable paging);
    
    /* Check duplicate */
    /** Check duplicate insert */
    Boolean existsByRequesterIdAndClazzIdAndRequestTypeAndStatusNotIn(
            Long requesterId, Long clazzId, RequestType requestType, Collection<Status> statusNotIn);

    /** Check duplicate update */
    Boolean existsByIdNotAndRequesterIdAndClazzIdAndRequestTypeAndStatusNotIn(
            Long id, Long requesterId, Long clazzId, RequestType requestType, Collection<Status> statusNotIn);
}