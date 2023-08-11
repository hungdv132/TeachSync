package com.teachsync.repositories;

import com.teachsync.entities.LocationUnit;
import com.teachsync.utils.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocationUnitRepository extends JpaRepository<LocationUnit, Long> {


    /* id */
    Optional<LocationUnit> findByIdAndStatusNot(Long id, Status status);


    /* parentId (id) */
    List<LocationUnit> findAllByParentIdAndStatusNot(Long parentId, Status status);

    /* level */
    List<LocationUnit> findAllByLevelAndStatusNot(Integer level, Status status);
}