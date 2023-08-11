package com.teachsync.dtos.test;
import com.teachsync.dtos.BaseReadDTO;
import com.teachsync.utils.enums.TestType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * DTO for {@link com.teachsync.entities.Test}
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestReadDTO extends BaseReadDTO {
    private Long courseId;
    private String testName;
    private TestType testType;
    private String testImg;
    private String testDesc;
    private Integer timeLimit;
    private Integer numQuestion;
    private Double minScore;
    private Integer testWeight;
    private Double totalScore;
}