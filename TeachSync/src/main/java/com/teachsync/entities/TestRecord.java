package com.teachsync.entities;

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
@Table(name = "test_record")
public class TestRecord extends BaseEntity {
    @Column(name = "memberTestRecordId", nullable = false)
    private Long memberTestRecordId;

    @Column(name = "questionId", nullable = false)
    private Long questionId;

    @Column(name = "answerId", nullable = false)
    private Long answerId;

    @Lob
    @Column(name = "answerTxt", nullable = true, length = -1)
    private String answerTxt;

    @Column(name = "score", nullable = true, precision = 0)
    private Double score;
}