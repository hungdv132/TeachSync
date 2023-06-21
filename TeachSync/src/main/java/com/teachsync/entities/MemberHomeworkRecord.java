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
@Table(name = "member_homework_record", schema = "teachsync")
public class MemberHomeworkRecord extends BaseEntity {
    @Column(name = "memberId", nullable = false)
    private Long memberId;
    
    @Column(name = "homeworkId", nullable = false)
    private Long homeworkId;
    
    @Column(name = "submission", nullable = true)
    private byte[] submission;

    @Lob
    @Column(name = "submissionLink", nullable = true, length = -1)
    private String submissionLink;
    
    @Column(name = "score", nullable = true, precision = 0)
    private Double score;
}