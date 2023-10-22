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
@Table(name = "schedule_category")
public class ScheduleCategory extends BaseEntity {
    @Column(name = "categoryName", nullable = true, length = 45)
    private String categoryName;

    @Lob
    @Column(name = "categoryDesc", nullable = true, length = -1)
    private String categoryDesc;

    @Column(name = "atMon", nullable = false)
    private Boolean atMon = false;

    @Column(name = "atTue", nullable = false)
    private Boolean atTue = false;

    @Column(name = "atWed", nullable = false)
    private Boolean atWed = false;

    @Column(name = "atThu", nullable = false)
    private Boolean atThu = false;

    @Column(name = "atFri", nullable = false)
    private Boolean atFri = false;

    @Column(name = "atSat", nullable = false)
    private Boolean atSat = false;

    @Column(name = "atSun", nullable = false)
    private Boolean atSun = false;
}
