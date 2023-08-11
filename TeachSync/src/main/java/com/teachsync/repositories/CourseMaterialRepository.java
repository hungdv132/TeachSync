package com.teachsync.repositories;

import com.teachsync.entities.CourseMaterial;
import com.teachsync.utils.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface CourseMaterialRepository extends JpaRepository<CourseMaterial, Long> {

    /* materialId */
    List<CourseMaterial> findAllByMaterialIdAndStatusNot(Long materialId, Status status);
    List<CourseMaterial> findAllByMaterialIdInAndStatusNot(Collection<Long> materialIdCollection, Status status);
}