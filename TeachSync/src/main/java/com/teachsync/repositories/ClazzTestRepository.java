package com.teachsync.repositories;

import com.teachsync.entities.ClazzTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClazzTestRepository extends JpaRepository<ClazzTest, Long> {
}