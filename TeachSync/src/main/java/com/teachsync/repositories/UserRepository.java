package com.teachsync.repositories;

import com.teachsync.entities.Test;
import com.teachsync.entities.User;
import com.teachsync.utils.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /* id */
    /** For finding unactivated teacher */
    Optional<User> findByIdAndStatus(Long id, Status status);
    Optional<User> findByIdAndStatusNot(Long id, Status status);
    List<User> findAllByIdInAndStatusNot(Collection<Long> idCollection, Status status);


    /** For login */
    Optional<User> findByUsernameAndStatusNot(String username, Status status);

    List<User> findAllByRoleId(Long roleId);

    List<User> findAllByUsernameContaining(String username);

//    @Query("SELECT u FROM User u WHERE u.email = ?1")
    public User findByEmail(String email);

    public User findByResetPasswordToken(String token);


    /* Check duplicate */
    boolean existsByUsernameAndStatusNot(String username, Status status);

    boolean existsByEmailAndStatusNot(String email, Status status);

    Page<User> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Page<User> findAllByUsernameContainingOrderByCreatedAtDesc(String username, Pageable pageable);
}

