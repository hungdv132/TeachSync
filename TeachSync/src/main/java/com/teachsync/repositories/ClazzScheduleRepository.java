package com.teachsync.repositories;

import com.teachsync.entities.ClazzSchedule;
import com.teachsync.utils.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClazzScheduleRepository extends JpaRepository<ClazzSchedule, Long> {

    Page<ClazzSchedule> findAllByStatusNot(Status status, Pageable pageable);

    Page<ClazzSchedule> findAllByEndDateAfterAndStatusNot(LocalDate after, Status status, Pageable pageable);

    /* id */
    Boolean existsByIdAndStatusNot(long id, Status status);
    Optional<ClazzSchedule> findByIdAndStatusNot(long id, Status status);
    Boolean existsByIdInAndStatusNot(Collection<Long> idCollection, Status status);
    List<ClazzSchedule> findAllByIdInAndStatusNot(Collection<Long> idCollection, Status status);

    /* clazzId */
    Boolean existsByClazzIdAndStatusNot(Long clazzId, Status status);
    Optional<ClazzSchedule> findByClazzIdAndStatusNot(Long clazzId, Status status);
    List<ClazzSchedule> findAllByClazzIdInAndStatusNot(Collection<Long> clazzIdCollection, Status status);

    /* roomId & slot & startDate & endDate */
    @Query("SELECT cS " +
            "FROM ClazzSchedule cS " +
            "WHERE cS.roomId = ?1 and cS.slot = ?2 and cS.scheduleCategoryId = ?3 and " +
            "((cS.startDate between ?4 and ?5) or (cS.endDate between ?4 and ?5) or " +
            "(cS.startDate >= ?4 and cS.endDate <= ?5) or" +
            "(cS.startDate <= ?4 and cS.endDate >= ?5)) ")
    List<ClazzSchedule> findAllByRoomIdAndScheduleCaIdAndSlotAndInRange(
            Long roomId, Long scheduleCaId, Integer slot, LocalDate from, LocalDate to);
}