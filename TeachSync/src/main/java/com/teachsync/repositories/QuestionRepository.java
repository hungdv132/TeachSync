package com.teachsync.repositories;


import com.teachsync.entities.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends
        JpaRepository<Question, Long> {

    List<Question> findAllByTestId(Long testId);
}
