package com.teachsync.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "test_record", schema = "teachsync")
public class TestRecord extends BaseEntity {
    @Column(name = "memberTestRecordId", nullable = false)
    private Long memberTestRecordId;
    
    @Column(name = "answerId", nullable = false)
    private Long answerId;

    @Lob
    @Column(name = "answerTxt", nullable = true, length = -1)
    private String answerTxt;
    
    @Column(name = "score", nullable = true, precision = 0)
    private Double score;
}