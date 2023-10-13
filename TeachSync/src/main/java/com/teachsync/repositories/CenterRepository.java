package com.teachsync.repositories;

import com.teachsync.entities.Center;
import com.teachsync.utils.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface CenterRepository extends JpaRepository<Center, Long> {

    List<Center> findAllByStatusNot(Status status);

    /* id */
    Boolean existsByIdAndStatusNot(Long id, Status status);
    Optional<Center> findByIdAndStatusNot(Long id, Status status);
    Boolean existsAllByIdInAndStatusNot(Collection<Long> idCollection, Status status);
    List<Center> findAllByIdInAndStatusNot(Collection<Long> idCollection, Status status);
}