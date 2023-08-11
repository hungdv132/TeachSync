package com.teachsync.dtos.material;

import com.teachsync.dtos.BaseCreateDTO;
import com.teachsync.utils.enums.MaterialType;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * DTO for {@link com.teachsync.entities.Material}
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaterialCreateDTO extends BaseCreateDTO {
    @NotBlank
    @Size(min = 1, max = 45)
    private String materialName;

    @Lob
    private String materialLink;

    private byte[] materialContent;

    @Lob
    private String materialImg;

    private MaterialType materialType;

    private Boolean isFree = false;
}
