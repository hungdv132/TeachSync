package com.teachsync.repositories;


import com.teachsync.entities.Question;
import com.teachsync.utils.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionRepository extends
        JpaRepository<Question, Long> {

    /* id */
    Optional<Question> findByIdAndStatusNot(Long id, Status status);
    List<Question> findAllByIdInAndStatusNot(Collection<Long> idCollection, Status status);

    /* testId */
    List<Question> findAllByTestIdAndStatusNot(Long testId, Status status);
    List<Question> findAllByTestIdInAndStatusNot(Collection<Long> testIdCollection, Status status);
}
