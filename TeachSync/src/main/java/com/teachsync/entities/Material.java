package com.teachsync.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "material")
public class Material {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "courseId", referencedColumnName = "id")
    private Course course;
    @Basic
    @Column(name = "courseId", insertable = false, updatable = false)
    private Long courseId;

    @Basic
    @Column(name = "materialLink")
    private String materialLink;
    @Basic
    @Column(name = "status")
    private String status;
}
