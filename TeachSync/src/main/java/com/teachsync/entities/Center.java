package com.teachsync.entities;

import com.teachsync.utils.enums.CenterType;
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
@Table(name = "center")
public class Center extends BaseEntity {
    @Column(name = "addressId", nullable = false)
    private Long addressId;

    @Lob
    @Column(name = "centerImg", nullable = true, length = -1)
    private String centerImg;

    @Column(name = "centerName", nullable = false, length = 45)
    private String centerName;

    @Column(name = "centerType", nullable = false, length = 45)
    private CenterType centerType;

    @Lob
    @Column(name = "centerDesc", nullable = true, length = -1)
    private String centerDesc;

    @Column(name = "centerSize", nullable = false)
    private Integer centerSize;
}