package com.teachsync.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "homework")
public class Homework extends BaseEntity {
    @Column(name = "clazzId", nullable = false)
    private Long clazzId;

    @Column(name = "homeworkName", nullable = false, length = 45)
    private String homeworkName;

    @Lob
    @Column(name = "homeworkDesc", nullable = true, length = -1)
    private String homeworkDesc;

    @Lob
    @Column(name = "homeworkDoc", nullable = true, length = -1)
    private String homeworkDoc;

    @Column(name = "homeworkContent", nullable = true)
    private String homeworkContent;

    @Column(name = "openAt", nullable = true)
    private LocalDateTime openAt;

    @Column(name = "deadline", nullable = true)
    private LocalDateTime deadline;
}