package com.teachsync.controllers;

import com.teachsync.dtos.clazz.ClazzReadDTO;
import com.teachsync.dtos.course.CourseReadDTO;
import com.teachsync.dtos.courseSemester.CourseSemesterReadDTO;
import com.teachsync.dtos.request.RequestCreateDTO;
import com.teachsync.dtos.user.UserReadDTO;
import com.teachsync.services.clazz.ClazzService;
import com.teachsync.services.course.CourseService;
import com.teachsync.services.courseSemester.CourseSemesterService;
import com.teachsync.utils.Constants;
import com.teachsync.utils.MiscUtil;
import com.teachsync.utils.enums.DtoOption;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class EnrollController {

    @Autowired
    private CourseService courseService;
    @Autowired
    private CourseSemesterService courseSemesterService;
    @Autowired
    private ClazzService clazzService;

    @Autowired
    private MiscUtil miscUtil;



//    @PreAuthorize("hasAuthority(T(com.teachsync.utils.Constants).ROLE_STUDENT)")
    @GetMapping("/enroll")
    public String enroll(
            HttpServletRequest request,
            @RequestParam(name = "id") Long courseId,
            Model model,
            @SessionAttribute(name = "user", required = false) UserReadDTO userDTO) {
        if (userDTO == null) {
            return "redirect:/course";
        }
        if (!userDTO.getRoleId().equals(Constants.ROLE_STUDENT)) {
            /* Quay về trang cũ */
            String referer = request.getHeader("Referer");
            return "redirect:" + referer;
        }

        try {
            CourseReadDTO courseDTO =  courseService.getDTOById(courseId, null);

            Map<Long, CourseSemesterReadDTO> semesterIdLatestDTOMap =
                    courseSemesterService.mapIdLatestDTOByCourseId(
                            courseId,
                            List.of(DtoOption.CENTER, DtoOption.SEMESTER));

            Map<Long, List<ClazzReadDTO>> courseSemesterIdClazzDTOListMap =
                    clazzService.mapCourseSemesterIdListDTOByCourseSemesterIdIn(
                            semesterIdLatestDTOMap.keySet(),
                            Arrays.asList(DtoOption.CLAZZ_SCHEDULE, DtoOption.MEMBER_LIST, DtoOption.ROOM_NAME));

            Map<CourseSemesterReadDTO, List<ClazzReadDTO>> scheduleClazzListMap = new HashMap<>();

            for (Long semesterId : semesterIdLatestDTOMap.keySet()) {
                scheduleClazzListMap.put(
                        semesterIdLatestDTOMap.get(semesterId),
                        courseSemesterIdClazzDTOListMap.get(semesterId));
            }

            model.addAttribute("course", courseDTO);
            model.addAttribute("scheduleClazzListMap", scheduleClazzListMap);

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMsg", e.getMessage());
        }

        return "request/enroll";
    }


    @PostMapping(value = "/enroll", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> enroll(
            Model model,
            @RequestBody RequestCreateDTO createDTO,
            @RequestParam Long clazzId,
            @SessionAttribute(name = "user", required = false) UserReadDTO userDTO) {

        return null/*"redirect:/index"*/;
//        TODO:
//        try {
//            RequestCreateDTO createDTO = new RequestCreateDTO();
//
//            createDTO.setRequesterId(userDTO.getId());
//            createDTO.setClazzId(clazzId);
//
//            createDTO.setRequestType("ENROLL");
//
//            createDTO.setRequestName(userDTO.getFullName() + "đăng ký lớp: " + clazzId);
//
//            createDTO.setRequestDesc(userDTO.getFullName() + "đăng ký lớp: " + clazzId);
//
//
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return "request-detail";
    }
}
