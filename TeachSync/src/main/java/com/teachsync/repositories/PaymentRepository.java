package com.teachsync.repositories;

import com.teachsync.entities.Payment;
import com.teachsync.utils.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    /* id */
    Optional<Payment> findByIdAndStatusNot(Long id, Status status);

    /* requestId */
    Optional<Payment> findByRequestIdAndStatusNot(Long requestId, Status status);

    /* payerId (userid) */
    List<Payment> findAllByPayerIdAndStatusNot(Long payerId, Status status);
    Page<Payment> findAllByPayerIdAndStatusNot(Long payerId, Status status, Pageable pageable);
}