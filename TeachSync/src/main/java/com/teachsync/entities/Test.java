package com.teachsync.entities;

import com.teachsync.utils.enums.QuestionType;
import com.teachsync.utils.enums.TestType;
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
@Table(name = "test")
public class Test extends BaseEntity {
    @Column(name = "courseId", nullable = true)
    private Long courseId;

    @Column(name = "testName", nullable = false, length = 45)
    private String testName;

    @Column(name = "testType", nullable = false, length = 45)
    private TestType testType;

    @Lob
    @Column(name = "testImg", nullable = true, length = -1)
    private String testImg;

    @Lob
    @Column(name = "testDesc", nullable = true, length = -1)
    private String testDesc;

    /** In Minutes */
    @Column(name = "timeLimit", nullable = false)
    private Integer timeLimit;

    @Column(name = "numQuestion", nullable = false)
    private Integer numQuestion;

    @Column(name = "questionType", nullable = false)
    private QuestionType questionType;

    @Column(name = "minScore", nullable = false, precision = 0)
    private Double minScore;

    @Column(name = "testWeight", nullable = false)
    private Integer testWeight;

    @Column(name = "totalScore", nullable = true, precision = 0)
    private Double totalScore;
}