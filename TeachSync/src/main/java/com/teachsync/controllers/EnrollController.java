package com.teachsync.controllers;

import com.teachsync.dtos.center.CenterReadDTO;
import com.teachsync.dtos.clazz.ClazzReadDTO;
import com.teachsync.dtos.course.CourseReadDTO;
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
import com.teachsync.utils.enums.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.*;

import static com.teachsync.utils.enums.DtoOption.*;

@Controller
public class EnrollController {
    @Autowired
    private CourseService courseService;
    @Autowired
    private SemesterService semesterService;
    @Autowired
    private ClazzService clazzService;
    @Autowired
    private CenterService centerService;
    @Autowired
    private RequestService requestService;

    @Autowired
    private MiscUtil miscUtil;


    /* =================================================== CREATE =================================================== */
    @GetMapping("/enroll")
    public String enrollPage (
            RedirectAttributes redirect,
            Model model,
            @RequestParam(name = "id") Long courseId,
            @RequestHeader(value = "Referer", required = false) String referer,
            @SessionAttribute(name = "user", required = false) UserReadDTO userDTO) {

        if (Objects.isNull(userDTO)) {
            redirect.addFlashAttribute("mess", "Làm ơn đăng nhập");
            return "redirect:/course";
        }

        if (!userDTO.getRoleId().equals(Constants.ROLE_STUDENT)) {
            /* Quay về trang cũ */
            if (referer != null) {
                return "redirect:" + referer;
            }
            return "redirect:/course";
        }


        try {
            CourseReadDTO courseDTO =  courseService.getDTOById(
                    courseId,
                    List.of(Status.OPENED),
                    true,
                    null);

            if (Objects.isNull(courseDTO)) {
                throw new IllegalArgumentException(
                        "Lỗi truy cập, Khóa học với id: " + courseId + " không phải là khóa đang mở để đăng ký.");
            }

            model.addAttribute("course", courseDTO);

            Map<Long, CenterReadDTO> centerIdCenterDTOMap =
                    centerService.mapIdDTO(List.of(ADDRESS));
            model.addAttribute("centerIdCenterDTOMap", centerIdCenterDTOMap);

            Set<Long> centerIdSet = new HashSet<>();
            if (Objects.nonNull(centerIdCenterDTOMap)) {
                centerIdSet = centerIdCenterDTOMap.keySet();
            }

            Map<Long, List<ClazzReadDTO>> centerIdClazzDTOListMap =
                    clazzService.mapCenterIdListDTOByCourseIdAndCenterIdIn(
                            courseId,
                            centerIdSet,
                            List.of(Status.OPENED),
                            true,
                            List.of(CLAZZ_SCHEDULE, SCHEDULE_CAT, MEMBER_LIST, ROOM_NAME));
            model.addAttribute("centerIdClazzListMap", centerIdClazzDTOListMap);

        } catch (Exception e) {
            e.printStackTrace();

            redirect.addFlashAttribute("mess", e.getMessage());

            return "redirect:/course-detail" + "?id=" + courseId;
        }

        return "request/enroll";
    }


    @PostMapping(value = "/enroll")
    public String enroll(
            RedirectAttributes redirect,
            Model model,
            @RequestParam Long clazzId,
            @RequestHeader(value = "Referer", required = false) String referer,
            @SessionAttribute(name = "user", required = false) UserReadDTO userDTO) {

        if (userDTO == null) {
            redirect.addFlashAttribute("mess", "Làm ơn đăng nhập");
            return "redirect:/course";
        }

        if (!userDTO.getRoleId().equals(Constants.ROLE_STUDENT)) {
            /* Quay về trang cũ */
            if (referer != null) {
                return "redirect:" + referer;
            }
            return "redirect:/course";
        }

        try {
            /* Trả về list vì theo controller chính của request */
            /* Clazz (Lớp được chọn) */
            ClazzReadDTO clazzDTO = clazzService.getDTOById(
                    clazzId,
                    List.of(Status.OPENED),
                    true,
                    List.of(MEMBER_LIST, STAFF, USER, CLAZZ_SCHEDULE, SCHEDULE_CAT, ROOM_NAME,
                            COURSE_SEMESTER, CENTER, ADDRESS, SEMESTER, COURSE, CURRENT_PRICE));

            if (Objects.isNull(clazzDTO)) {
                throw new IllegalArgumentException(
                        "Lỗi truy cập, Lớp học với id: " + clazzId + " không phải là lớp đang mở để đăng ký.");
            }

            model.addAttribute("clazzList", List.of(clazzDTO));

            /* Course (môn nào) */
            model.addAttribute("courseList", List.of(clazzDTO.getCourse()));

            /* Center (Cơ sở nào) */
            model.addAttribute("centerList", List.of(clazzDTO.getCenter()));

            model.addAttribute("fromEnroll", true);

        } catch (Exception e) {
            e.printStackTrace();

            redirect.addFlashAttribute("mess", e.getMessage());

            return "redirect:/course";
        }

        return "request/add-request";
    }


    /* =================================================== READ ===================================================== */


    /* =================================================== UPDATE =================================================== */


    /* =================================================== DELETE =================================================== */
}
