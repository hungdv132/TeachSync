package com.teachsync.dtos.course;

import com.teachsync.dtos.BaseUpdateDTO;
import com.teachsync.dtos.priceLog.PriceLogCreateDTO;
import com.teachsync.utils.enums.Status;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * DTO for {@link com.teachsync.entities.Course}
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseUpdateDTO extends BaseUpdateDTO {
    @NotBlank
    @Size(min = 1, max = 45)
    private String courseName;

    @NotBlank
    @Size(min = 1, max = 10)
    private String courseAlias;

    @NotBlank
    @Lob
    private String courseImg;

    @Lob
    private String courseDesc;

    @NotNull
    private Integer numSession;

    /** 0 - 10 */
    @NotNull
    @Size(min = 0, max = 10)
    private Double minScore;

    /** 0% - 100% */
    @NotNull
    @Size(min = 0, max = 100)
    private Double minAttendant;

    private PriceLogCreateDTO price;
}
