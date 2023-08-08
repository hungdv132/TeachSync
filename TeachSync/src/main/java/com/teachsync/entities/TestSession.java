package com.teachsync.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "test_session")
public class TestSession {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "userid", nullable = false)
    private Long userId;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "testid", nullable = false)
    private Long testId;

    @Column(name = "subject", nullable = false)
    private String subject;

    @Column(name = "class", nullable = false)
    private String clazz;

    @Column(name = "start_date", nullable = false)
    private Date startDate;

    @Column(name = "submit_date")
    private Date submitDate;

    @Column(name = "status")
    private Long status;

    @Column(name = "update_date")
    private Date updateDate;

    @Column(name = "user_update")
    private String userUpdate;

}
