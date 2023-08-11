package com.teachsync.entities;

import com.teachsync.utils.enums.QuestionType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.*;

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
}