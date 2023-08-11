package com.teachsync.repositories;

import com.teachsync.entities.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestRepository extends JpaRepository<Test, Long> {
    Page<Test> findByTestNameContainingOrderByCreatedAtDesc(String course, Pageable pageable);
    Page<Test> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
