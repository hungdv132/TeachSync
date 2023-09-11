package com.teachsync.repositories;

import com.teachsync.entities.News;
import com.teachsync.utils.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {

    News findAllById(Long id);

    Page<News> findAllByStatusNot(Status status, Pageable pageable);

    /* id */
    Optional<News> findByIdAndStatusNot(Long id, Status status);
    List<News> findAllByIdInAndStatusNot(Collection<Long> idCollection, Status status);

    List<News> findAllByClazzIdAndStatusNot(Long clazzId, Status status);
}
