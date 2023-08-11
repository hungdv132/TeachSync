package com.teachsync.repositories;

import com.teachsync.entities.CourseMaterial;
import com.teachsync.utils.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface CourseMaterialRepository extends JpaRepository<CourseMaterial, Long> {

    Page<CourseMaterial> findAllByStatusNot(Status status, Pageable pageable);
    List<CourseMaterial> findAllByStatusNot(Status status);

    /* id */
    Optional<CourseMaterial> findByIdAndStatusNot(long id, Status status);
    List<CourseMaterial> findAllByIdInAndStatusNot(Collection<Long> idCollection, Status status);

    /* courseId */
    List<CourseMaterial> findAllByCourseIdAndStatusNot(Long courseId, Status status);

    List<CourseMaterial> findAllByCourseIdAndMaterialIdInAndStatusNot(
            Long courseId, Collection<Long> semesterIdCollection, Status status);

    /* materialId */
    List<CourseMaterial> findAllByMaterialIdAndStatusNot(Long materialId, Status status);
    List<CourseMaterial> findAllByMaterialIdInAndStatusNot(Collection<Long> materialIdCollection, Status status);
}