package com.teachsync.entities;

import com.teachsync.dtos.question.QuestionCreateDTO;
import com.teachsync.dtos.question.QuestionReadDTO;
import com.teachsync.utils.enums.QuestionType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.*;

import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "question")
public class Question extends BaseEntity {
    @Column(name = "testId", nullable = false)
    private Long testId;

    @Column(name = "questionType", nullable = false, length = 255)
    private QuestionType questionType;

    @Lob
    @Column(name = "questionDesc", nullable = false, length = -1)
    private String questionDesc;

    @Column(name = "questionPrompt", nullable = false, length = 45)
    private String questionPrompt;

    @Column(name = "questionScore", nullable = false)
    private Double questionScore;

    public boolean equalCreateDTO(QuestionCreateDTO dto) {
        return questionType == dto.getQuestionType() &&
                Objects.equals(questionDesc, dto.getQuestionDesc()) &&
                Objects.equals(questionPrompt, dto.getQuestionPrompt());
    }
}