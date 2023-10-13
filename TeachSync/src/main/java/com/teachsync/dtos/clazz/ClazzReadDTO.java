package com.teachsync.dtos.clazz;

import com.teachsync.dtos.BaseReadDTO;
import com.teachsync.dtos.center.CenterReadDTO;
import com.teachsync.dtos.clazzMember.ClazzMemberReadDTO;
import com.teachsync.dtos.clazzSchedule.ClazzScheduleReadDTO;
import com.teachsync.dtos.clazzTest.ClazzTestReadDTO;
import com.teachsync.dtos.course.CourseReadDTO;
import com.teachsync.dtos.courseSemester.CourseSemesterReadDTO;
import com.teachsync.dtos.homework.HomeworkReadDTO;
import com.teachsync.dtos.session.SessionReadDTO;
import com.teachsync.dtos.staff.StaffReadDTO;
import com.teachsync.entities.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for {@link Clazz}
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClazzReadDTO extends BaseReadDTO {
//    private Long courseSemesterId;
//    private CourseSemesterReadDTO courseSemester;

    private Long courseId;
    private String courseAlias;
    private String courseName;
    private CourseReadDTO course;

    private Long centerId;
    private String centerName;
    private CenterReadDTO center;

    private Long staffId;
    private StaffReadDTO staff;

    private String clazzAlias;

    private String clazzName;

    private String clazzDesc;

//    private String statusClazz;

    private Integer minCapacity;

    private Integer maxCapacity;

    private ClazzScheduleReadDTO clazzSchedule;

    private List<SessionReadDTO> sessionList;

    private List<ClazzMemberReadDTO> memberList;

    private List<HomeworkReadDTO> homeworkList;

    private List<ClazzTestReadDTO> testList;
}