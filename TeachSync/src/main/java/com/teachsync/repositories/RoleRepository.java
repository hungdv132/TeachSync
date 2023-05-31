package com.teachsync.repositories;

import com.teachsync.entities.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>  {

    Page<Role> findAllByStatusNot(String status, Pageable pageable);

    /* id */
    Optional<Role> findByIdAndStatusNot(Long id, String status);
    List<Role> findAllByIdInAndStatusNot(Collection<Long> idCollection, String status);

    /* Check duplicate */

}
