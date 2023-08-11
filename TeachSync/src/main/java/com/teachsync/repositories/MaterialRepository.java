package com.teachsync.repositories;


import com.teachsync.entities.Material;
import com.teachsync.utils.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Long> {

    Page<Material> findAllByStatusNot(Status status, Pageable pageable);
    List<Material> findAllByStatusNot(Status status);

    /* id */
    Optional<Material> findByIdAndStatusNot(Long id, Status status);
    Page<Material> findAllByIdInAndStatusNot(Collection<Long> idCollection, Status status, Pageable pageable);
    List<Material> findAllByIdInAndStatusNot(Collection<Long> idCollection, Status status);


    /* isFree */
    Page<Material> findAllByIsFreeAndStatusNot(Boolean isFree, Status status, Pageable pageable);
    List<Material> findAllByIsFreeAndStatusNot(Boolean isFree, Status status);

    /* Check duplicate */
    /** Check duplicate for CREATE Material */
    @Query("SELECT case when count(m) > 0 then true else false end " +
            "FROM Material m  " +
            "WHERE (m.materialName = ?1 or m.materialLink = ?2) and m.status <> ?3 ")
    Boolean existsByMaterialNameOrMaterialLinkAndStatusNot(
            String materialName, String materialLink, Status status);

    /** Check duplicate for UPDATE Material */
    @Query("SELECT case when count(m) > 0 then true else false end " +
            "FROM Material m " +
            "WHERE m.id <> ?1 and (m.materialName = ?2 or m.materialLink = ?3) and m.status <> ?4 ")
    Boolean existsByIdNotAndMaterialNameOrMaterialLinkAndStatusNot(
            Long id, String materialName, String materialLink, Status status);
}
