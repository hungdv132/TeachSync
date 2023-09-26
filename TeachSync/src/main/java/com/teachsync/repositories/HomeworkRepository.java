package com.teachsync.repositories;


import com.teachsync.entities.Homework;
import com.teachsync.utils.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface HomeworkRepository extends JpaRepository<Homework, Long> {

    Page<Homework> findAllByStatusNot(Status status, Pageable pageable);

    List<Homework> findAllByClazzIdAndStatusNot(Long clazzId,Status status);

}