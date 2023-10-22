package com.teachsync.repositories;

import com.teachsync.entities.Session;
import com.teachsync.utils.enums.Status;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {

    /* id */
    Optional<Session> findByIdAndStatusNot(Long id, Status status);
    List<Session> findAllByIdInAndStatusNot(Collection<Long> idCollection, Status status);

    /* scheduleId */
    List<Session> findAllByScheduleIdAndStatusNot(Long scheduleId, Status status);
    List<Session> findAllByScheduleIdAndSessionStartAfterAndSessionEndBeforeAndStatusNot(
            Long scheduleId, LocalDateTime from, LocalDateTime to, Status status);
    List<Session> findAllByScheduleIdInAndSessionStartAfterAndSessionEndBeforeAndStatusNot(
            Collection<Long> scheduleIdCollection, LocalDateTime from, LocalDateTime to, Status status);
    List<Session> findAllByScheduleIdAndSessionStartAfterAndStatusNot(
            Long scheduleId, LocalDateTime from, Status status);
    
    /* roomId */
    List<Session> findAllByRoomIdAndStatusNot(Long roomId, Status status);
    List<Session> findAllByRoomIdAndSessionStartAfterAndSessionEndBeforeAndStatusNot(
            Long roomId, LocalDateTime from, LocalDateTime to, Status status);
    
    /* staffId */
    List<Session> findAllByStaffIdAndStatusNot(Long staffId, Status status);
    List<Session> findAllByStaffIdAndSessionStartAfterAndSessionEndBeforeAndStatusNot(
            Long staffId, LocalDateTime from, LocalDateTime to, Status status);

    /* check duplicate */
    Boolean existsByRoomIdAndSlotAndSessionStartAndSessionEndAndStatusNot(
            Long roomId, Integer slot, LocalDateTime start, LocalDateTime end, Status status);

    @Query("SELECT case when count(s) > 0 then true else false end " +
            "FROM Session s " +
            "WHERE ((s.roomId, s.slot, s.sessionStart, s.sessionEnd) in ?1) and s.status <> ?2 ")
    Boolean existsAllByRoomIdAndSlotAndSessionStartAndSessionEndAndStatusNot(
            Collection<Object[]> criteriaCollection, Status status);

    Boolean existsByIdNotAndRoomIdAndSlotAndSessionStartAndSessionEndAndStatusNot(
            Long id, Long roomId, Integer slot, LocalDateTime start, LocalDateTime end, Status status);

    @Query("SELECT s.id FROM Session s " +
            "WHERE ((s.roomId, s.slot, s.sessionStart, s.sessionEnd) in ?1) and s.status <> ?2 ")
    List<Long> findAllIdByRoomIdAndSlotAndSessionStartAndSessionEndAndStatusNot(
            Collection<Object[]> criteriaCollection, Status status);
}