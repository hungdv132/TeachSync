package com.teachsync.dtos.courseSemester;

import com.teachsync.dtos.BaseReadDTO;
import com.teachsync.dtos.center.CenterReadDTO;
import com.teachsync.dtos.clazz.ClazzReadDTO;
import com.teachsync.dtos.course.CourseReadDTO;
import com.teachsync.dtos.semester.SemesterReadDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for {@link com.teachsync.entities.CourseSemester}
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseSemesterReadDTO extends BaseReadDTO {
    private Long courseId;
    private String courseName;
    private String courseAlias;
    private CourseReadDTO course;
    
    private Long centerId;
    private String centerName;
    private CenterReadDTO center;

    private Long semesterId;
    private String semesterName;
    private String semesterAlias;
    private SemesterReadDTO semester;

    private List<ClazzReadDTO> clazzList;
}