package com.teachsync.repositories;

import com.teachsync.entities.Staff;
import com.teachsync.utils.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {


    /* id */
    Boolean existsByIdAndStatusNot(Long id, Status status);
    Optional<Staff> findByIdAndStatusNot(Long id, Status status);
    Boolean existsAllByIdInAndStatusNot(Collection<Long> idCollection, Status status);
    List<Staff> findAllByIdInAndStatusNot(Collection<Long> idCollection, Status status);

    /* centerId */
    List<Staff> findAllByCenterIdAndStatusNot(Long userId, Status status);
    List<Staff> findAllByCenterIdInAndStatusNot(Collection<Long> centerIdCollection, Status status);

    /* userId */
    List<Staff> findAllByUserIdAndStatusNot(Long userId, Status status);
    List<Staff> findAllByUserIdInAndStatusNot(Collection<Long> userIdCollection, Status status);
}