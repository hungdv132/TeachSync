package com.teachsync.dtos.material;

import com.teachsync.dtos.BaseReadDTO;
import com.teachsync.dtos.course.CourseReadDTO;
import com.teachsync.utils.enums.MaterialType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for {@link com.teachsync.entities.Material}
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MaterialReadDTO extends BaseReadDTO {
    private String materialName;

    private String materialLink;

    private byte[] materialContent;

    private String materialImg;

    private MaterialType materialType;

    private Boolean isFree;

    private List<CourseReadDTO> courseList;
}
