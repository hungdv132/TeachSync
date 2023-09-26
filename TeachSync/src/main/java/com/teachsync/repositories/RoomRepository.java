package com.teachsync.repositories;


import com.teachsync.entities.Room;
import com.teachsync.utils.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    /* id */
    Boolean existsByIdAndStatusNot(Long id, Status status);
    Optional<Room> findByIdAndStatusNot(Long id, Status status);
    Boolean existsAllByIdInAndStatusNot(Collection<Long> idCollection, Status status);
    List<Room> findAllByIdInAndStatusNot(Collection<Long> idCollection, Status status);

    List<Room> getAllByCenterIdAndStatusNot(Long centerId, Status status);
    List<Room> getAllByCenterIdInAndStatusNot(Collection<Long> centerIdCollection, Status status);
}