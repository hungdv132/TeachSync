package com.teachsync.repositories;

import com.teachsync.entities.PriceLog;
import com.teachsync.utils.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface PriceLogRepository extends JpaRepository<PriceLog, Long> {

    Page<PriceLog> findAllByStatusNot(Status status, Pageable pageable);
    @Query("SELECT pL FROM PriceLog  pL " +
            "WHERE (pL.validFrom <= ?1) AND (pL.validTo = null OR pL.validTo >= ?1) AND (pL.isPromotion = true) AND (pL.status <> ?2)")
    Page<PriceLog> findAllByValidBetweenAndIsPromotionTrueAndStatusNot(
            LocalDateTime validDateTime, Status status, Pageable pageable);

    @Query("SELECT pL FROM PriceLog  pL " +
            "WHERE (pL.validFrom <= ?1) AND (pL.validTo = null OR pL.validTo >= ?1) AND (pL.isPromotion = true) AND (pL.status <> ?2)")
    List<PriceLog> findAllByValidBetweenAndIsPromotionTrueAndStatusNot(
            LocalDateTime validDateTime, Status status);


    /* id */
    Optional<PriceLog> findByIdAndStatusNot(Long id, Status status);
    List<PriceLog> findAllByIdInAndStatusNot(Collection<Long> idCollection, Status status);

    /* courseId */
    @Query("SELECT pL FROM PriceLog  pL " +
            "WHERE (pL.courseId = ?1) AND ((pL.validFrom <= ?2) AND (pL.validTo = null OR pL.validTo >= ?2)) AND (pL.status <> ?3)")
    Optional<PriceLog> findByCourseIdAndValidBetweenAndStatusNot(
            Long courseId, LocalDateTime validDateTime, Status status);
    List<PriceLog> findAllByCourseIdAndStatusNot(Long courseId, Status status);

    @Query("SELECT pL FROM PriceLog  pL " +
            "WHERE (pL.courseId IN ?1) AND ((pL.validFrom <= ?2) AND (pL.validTo = null OR pL.validTo >= ?2)) AND (pL.status <> ?3)")
    List<PriceLog> findAllByCourseIdInAndValidBetweenAndStatusNot(
            Collection<Long> courseIdCollection, LocalDateTime validDateTime, Status status);
    List<PriceLog> findAllByCourseIdInAndStatusNot(Collection<Long> courseIdCollection, Status status);


    /* Check duplicate */
}