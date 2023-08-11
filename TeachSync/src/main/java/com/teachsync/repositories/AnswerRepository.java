package com.teachsync.repositories;

import com.teachsync.entities.Answer;
import com.teachsync.utils.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {

    /* id */
    Optional<Answer> findByIdAndStatusNot(Long id, Status status);
    List<Answer> findAllByIdInAndStatusNot(Collection<Long> idCollection, Status status);

    /* questionId */
    List<Answer> findAllByQuestionIdAndStatusNot(Long questionId, Status status);
    List<Answer> findAllByQuestionIdInAndStatusNot(Collection<Long> questionIdCollection, Status status);

    void deleteAllByQuestionId(Long id);
}
