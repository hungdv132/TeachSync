package com.teachsync.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "testrecord")
public class TestRecord2 {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "testId", nullable = false)
    private Long testId;

    @Column(name = "userId", nullable = false)
    private Long userId;

    @Column(name = "username")
    private String username;

    @Column(name = "class")
    private String clazz;

    @Column(name = "questionId", nullable = false)
    private Long questionId;

    @Column(name = "questionType")
    private String questionType;

    @Column(name = "essayAnswer")
    private String essayAnswer;

    @Column(name = "answerMCId")
    private Long answerMCId;

    @Column(name = "correct")
    private boolean correct;
}
