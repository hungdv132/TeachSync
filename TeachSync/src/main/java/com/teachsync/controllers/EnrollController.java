package com.teachsync.controllers;

import com.teachsync.dtos.BaseReadDTO;
import com.teachsync.dtos.center.CenterReadDTO;
import com.teachsync.dtos.clazz.ClazzReadDTO;
import com.teachsync.dtos.course.CourseReadDTO;
import com.teachsync.dtos.courseSemester.CourseSemesterReadDTO;
import com.teachsync.dtos.request.RequestCreateDTO;
import com.teachsync.dtos.semester.SemesterReadDTO;
import com.teachsync.dtos.user.UserReadDTO;
import com.teachsync.services.center.CenterService;
import com.teachsync.services.clazz.ClazzService;
import com.teachsync.services.course.CourseService;
import com.teachsync.services.courseSemester.CourseSemesterService;
import com.teachsync.services.request.RequestService;
import com.teachsync.services.semester.SemesterService;
import com.teachsync.utils.Constants;
import com.teachsync.utils.MiscUtil;
import com.teachsync.utils.enums.RequestType;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.teachsync.utils.enums.DtoOption.*;

@Controller
public class EnrollController {
    @Autowired
    private CourseService courseService;
    @Autowired
    private SemesterService semesterService;
    @Autowired
    private CourseSemesterService courseSemesterService;
    @Autowired
    private CenterService centerService;
    @Autowired
    private ClazzService clazzService;
    @Autowired
    private RequestService requestService;

    @Autowired
    private MiscUtil miscUtil;


    /* =================================================== CREATE =================================================== */
    @GetMapping("/enroll")
    public String enrollPage (
            Model model,
            @RequestParam(name = "id") Long courseId,
            @RequestHeader("Referer") String referer,
            @SessionAttribute(name = "user", required = false) UserReadDTO userDTO) {
        if (userDTO == null) {
            return "redirect:/course";
        }

        if (!userDTO.getRoleId().equals(Constants.ROLE_STUDENT)) {
            /* Quay về trang cũ */
            return "redirect:" + referer;
        }

        try {
            CourseReadDTO courseDTO =  courseService.getDTOById(courseId, null);

            Map<Long, CenterReadDTO> centerIdCenterDTOMap =
                    centerService.mapIdDTO(List.of(ADDRESS));
            model.addAttribute("centerIdCenterDTOMap", centerIdCenterDTOMap);

            Map<Long, SemesterReadDTO> semesterDTOPage =
                    semesterService.mapIdDTOByStartDateAfter(LocalDate.now(), null);
            model.addAttribute("semesterIdSemesterDTOMap", semesterDTOPage);

            Map<Long, String> courseSemesterIdSemesterIdCenterIdStringMap =
                    courseSemesterService.mapIdSemesterIdCenterIdStringByCourseIdAndSemesterIdIn(
                            courseId, semesterDTOPage.keySet());

            Map<Long, List<ClazzReadDTO>> courseSemesterIdClazzDTOListMap =
                    clazzService.mapCourseSemesterIdListDTOByCourseSemesterIdIn(
                            courseSemesterIdSemesterIdCenterIdStringMap.keySet(),
                            Arrays.asList(CLAZZ_SCHEDULE, MEMBER_LIST, ROOM_NAME));

            Map<String, List<ClazzReadDTO>> semesterIdCenterIdStringClassDTOListMap = new HashMap<>();

            for (Long courseSemesterId : courseSemesterIdSemesterIdCenterIdStringMap.keySet()) {
                semesterIdCenterIdStringClassDTOListMap.put(
                        courseSemesterIdSemesterIdCenterIdStringMap.get(courseSemesterId),
                        courseSemesterIdClazzDTOListMap.get(courseSemesterId));
            }

            model.addAttribute("course", courseDTO);

            model.addAttribute("semesterIdCenterIdStringClazzListMap", semesterIdCenterIdStringClassDTOListMap);

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMsg", e.getMessage());
        }

        return "request/enroll";
    }


    @PostMapping(value = "/enroll")
    public String enroll(
            Model model,
            @RequestParam Long clazzId,
            @RequestHeader("Referer") String referer,
            @SessionAttribute(name = "user", required = false) UserReadDTO userDTO) {
//        if (userDTO == null) {
//            return "redirect:/course";
//        }
//
//        if (!userDTO.getRoleId().equals(Constants.ROLE_STUDENT)) {
//            /* Quay về trang cũ */
//            return "redirect:" + referer;
//        }

        try {
            /* Trả về list vì theo controller chính của request */
            /* Clazz (Lớp được chọn) */
            ClazzReadDTO clazzDTO = clazzService.getDTOById(
                    clazzId,
                    List.of(MEMBER_LIST, STAFF, USER, CLAZZ_SCHEDULE, ROOM_NAME,
                            COURSE_SEMESTER, CENTER, ADDRESS, SEMESTER, COURSE, CURRENT_PRICE));
            model.addAttribute("clazzList", List.of(clazzDTO));

            /* Course (môn nào) */
            model.addAttribute("courseList", List.of(clazzDTO.getCourseSemester().getCourse()));

            /* Semester (kỳ nào) */
            model.addAttribute("semesterList", List.of(clazzDTO.getCourseSemester().getSemester()));

            /* Center (Cơ sở nào) */
            model.addAttribute("centerList", List.of(clazzDTO.getCourseSemester().getCenter()));

            model.addAttribute("fromEnroll", true);
            model.addAttribute("type", RequestType.ENROLL);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "request/add-request";
    }


    /* =================================================== READ ===================================================== */


    /* =================================================== UPDATE =================================================== */


    /* =================================================== DELETE =================================================== */
}
