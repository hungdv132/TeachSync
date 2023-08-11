package com.teachsync.entities;

import com.teachsync.utils.enums.MaterialType;
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
@Table(name = "material")
public class Material extends BaseEntity {
    @Column(name = "materialName", nullable = false, length = 45)
    private String materialName;

    @Lob
    @Column(name = "materialImg", nullable = false, length = -1)
    private String materialImg;

    @Lob
    @Column(name = "materialLink", nullable = true, length = -1)
    private String materialLink;

    @Column(name = "materialContent", nullable = true)
    private byte[] materialContent;

    @Column(name = "materialType", nullable = false, length = 45)
    private MaterialType materialType;


    @Column(name = "isFree", nullable = false)
    private Boolean isFree;
}