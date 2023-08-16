package com.teachsync.entities;

import com.teachsync.utils.enums.LocationUnitType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "location_unit")
public class LocationUnit extends BaseEntity {
    @Column(name = "parentId", nullable = true)
    private Long parentId;

    @Column(name = "level", nullable = false)
    private Integer level;

    @Column(name = "unitName", nullable = false, length = 255)
    private String unitName;

    @Column(name = "unitAlias", nullable = false, length = 45)
    private String unitAlias;

    @Column(name = "unitType", nullable = false, length = 45)
    private LocationUnitType unitType;
}