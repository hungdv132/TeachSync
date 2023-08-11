package com.teachsync.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "member_test_record")
public class MemberTestRecord extends BaseEntity {
    @Column(name = "memberId", nullable = false)
    private Long memberId;

    @Column(name = "clazzTestId", nullable = false)
    private Long clazzTestId;

    @Column(name = "score", nullable = true, precision = 0)
    private Double score;
}