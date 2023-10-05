package com.teachsync.controllers;

import com.teachsync.dtos.BaseReadDTO;
import com.teachsync.dtos.clazz.ClazzReadDTO;
import com.teachsync.dtos.course.CourseCreateDTO;
import com.teachsync.dtos.course.CourseReadDTO;
import com.teachsync.dtos.course.CourseUpdateDTO;
import com.teachsync.dtos.courseSemester.CourseSemesterReadDTO;
import com.teachsync.dtos.user.UserReadDTO;
import com.teachsync.entities.Course;
import com.teachsync.services.clazz.ClazzService;
import com.teachsync.services.course.CourseService;
import com.teachsync.services.courseSemester.CourseSemesterService;
import com.teachsync.utils.Constants;
import com.teachsync.utils.MiscUtil;
import com.teachsync.utils.enums.DtoOption;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;
import java.util.stream.Collectors;

import static com.teachsync.utils.Constants.*;
import static com.teachsync.utils.enums.DtoOption.*;

@Controller
public class CourseController {
    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseSemesterService courseSemesterService;
    @Autowired
    private MiscUtil miscUtil;


    /* =================================================== CREATE =================================================== */
    @GetMapping("/add-course")
    public String addCoursePage(
            RedirectAttributes redirect,
            @SessionAttribute(value = "user", required = false) UserReadDTO userDTO) {
//        if (Objects.isNull(userDTO)) {
//            redirect.addAttribute("mess", "Làm ơn đăng nhập");
//            return "redirect:/index";
//        }
//
//        if (!userDTO.getRoleId().equals(ROLE_ADMIN)) {
//            redirect.addAttribute("mess", "Bạn không đủ quyền");
//            return "redirect:/index";
//        }

        return "course/add-course";
    }

    @PostMapping("/add-course")
    public String addCourse(
            Model model,
            @ModelAttribute CourseCreateDTO createDTO,
            RedirectAttributes redirect,
            @SessionAttribute(value = "user", required = false) UserReadDTO userDTO) {

        if (Objects.isNull(userDTO)) {
            redirect.addAttribute("mess", "Làm ơn đăng nhập");
            return "redirect:/index";
        }

        if (!userDTO.getRoleId().equals(ROLE_ADMIN)) {
            redirect.addAttribute("mess", "Bạn không đủ quyền");
            return "redirect:/index";
        }

        CourseReadDTO courseDTO = null;

        try {
            StringBuilder errorMsg = new StringBuilder();

            /* Validate input */
            errorMsg.append(
                    miscUtil.validateString(
                            "Mã khóa học", createDTO.getCourseAlias(), 1, 10,
                            List.of("required", "minLength", "maxLength", "onlyBlank", "startBlank", "endBlank", "specialChar")));
            /* courseName */
            errorMsg.append(
                    miscUtil.validateString(
                            "Tên khóa học", createDTO.getCourseName(), 1, 45,
                            List.of("required", "minLength", "maxLength", "onlyBlank", "startBlank", "endBlank", "specialChar")));
            /* courseDesc */
            errorMsg.append(
                    miscUtil.validateString(
                            "Miêu tả khóa học", createDTO.getCourseDesc(), 1, 9999,
                            List.of("nullOrMinLength", "maxLength", "onlyBlank", "startBlank", "endBlank", "specialChar")));
            /* courseImg */
            /* TODO: check valid link */
            /* numSession */
            errorMsg.append(
                    miscUtil.validateNumber(
                            "Số tiết học", Double.valueOf(createDTO.getNumSession()), 1.0, 100.0, 1.0,
                            List.of("min", "max", "onlyBlank", "step")));
            /* minScore */
            errorMsg.append(
                    miscUtil.validateNumber(
                            "Điểm tối thiểu", createDTO.getMinScore(), 0.0, 10.0, 0.01,
                            List.of("min", "max", "onlyBlank", "step")));
            /* minAttendant */
            errorMsg.append(
                    miscUtil.validateNumber(
                            "Điểm danh tối thiểu", createDTO.getMinAttendant(), 0.0, 100.0, 0.01,
                            List.of("min", "max", "onlyBlank", "step")));

            if (!errorMsg.isEmpty()) {
                throw new IllegalArgumentException(errorMsg.toString());
            }

            createDTO.setCreatedBy(userDTO.getId());

            courseDTO = courseService.createCourseByDTO(createDTO);

            /* TODO: process price */
        } catch (Exception e) {
            model.addAttribute("mess", "Lỗi : " + e.getMessage());
            return "/course/add-course";
        }

        return "/course/course-detail" + "?id=" + courseDTO.getId();
    }

    /* =================================================== READ ===================================================== */
    @GetMapping("/course")
    public String courseListPage(
            Model model,
            @ModelAttribute("mess") String mess,
            @SessionAttribute(name = "user", required = false) UserReadDTO userDTO) {
        try {
            Page<CourseReadDTO> dtoPage;
            if (Objects.isNull(userDTO) || userDTO.getRoleId().equals(Constants.ROLE_STUDENT)) {
                /* Là khách hoặc học sinh */
                /* Hot course */
                dtoPage = courseService.getPageDTOAllHotCourse(null);
                if (dtoPage != null) {
                    model.addAttribute("hotCourseList", dtoPage.getContent());
                    model.addAttribute("hotPageNo", dtoPage.getPageable().getPageNumber());
                    model.addAttribute("hotPageTotal", dtoPage.getTotalPages());
                }
            }

            /* All course */
            dtoPage = courseService.getPageDTOAll(null);
            if (dtoPage != null) {
                model.addAttribute("courseList", dtoPage.getContent());
                model.addAttribute("pageNo", dtoPage.getPageable().getPageNumber());
                model.addAttribute("pageTotal", dtoPage.getTotalPages());
            }

            /* TODO: set up searchable course */
            model.addAttribute(
                    "searchableFieldList",
                    miscUtil.sortSearchableField(Course.class.getDeclaredFields()));
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMsg", "Server error, please try again later");
        }
        model.addAttribute("mess", mess);

        return "course/list-course";
    }

    @GetMapping("/course-detail")
    public String courseDetailPageById(
            Model model,
            @RequestParam(name = "id") Long courseId,
            @SessionAttribute(name = "user", required = false) UserReadDTO userDTO) {
        try {
            List<DtoOption> options = List.of(CURRENT_PRICE);

            if (Objects.nonNull(userDTO)) {
                /* TODO: lấy bài thi, tài liệu, kỳ học, lớp học */
                if (List.of(ROLE_STUDENT, ROLE_PARENTS).contains(userDTO.getRoleId())) {
                    options = List.of(MATERIAL_LIST, CURRENT_PRICE);

                } else if (userDTO.getRoleId().equals(ROLE_TEACHER)) {
                    options = List.of(MATERIAL_LIST, TEST_LIST, CURRENT_PRICE);

                } else if (userDTO.getRoleId().equals(ROLE_ADMIN)) {
                    options = List.of(MATERIAL_LIST, TEST_LIST, CURRENT_PRICE);
                }
            }

            CourseReadDTO courseDTO = courseService.getDTOById(courseId, options);

            if (courseDTO == null) {
                /* Not found by id */
                return "redirect:/course";
            }

            List<CourseSemesterReadDTO> courseSemesterList =
                    courseSemesterService.getAllLatestDTOByCourseId(courseId, List.of(CLAZZ_LIST));

            model.addAttribute("course", courseDTO);

            boolean hasLatestSchedule = !ObjectUtils.isEmpty(courseSemesterList);
            model.addAttribute("hasLatestSchedule", hasLatestSchedule);

            boolean hasClazz = false;
            if (hasLatestSchedule) {
                List<ClazzReadDTO> clazzDTOList = new ArrayList<>();
                List<ClazzReadDTO> tmpClazzDTOlist = new ArrayList<>();
                for (CourseSemesterReadDTO courseSemester : courseSemesterList) {
                    tmpClazzDTOlist = courseSemester.getClazzList();
                    if (!ObjectUtils.isEmpty(tmpClazzDTOlist)) {
                        clazzDTOList.addAll(tmpClazzDTOlist);
                    }
                }
                hasClazz = !ObjectUtils.isEmpty(clazzDTOList);
            }

            model.addAttribute("hasClazz", hasClazz);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMsg", "Server error, please try again later");
        }

        return "course/course-detail";
    }


    @GetMapping("/api/course-detail")
    @ResponseBody
    public Map<String, Object> getCourseDetail(
            @RequestParam Long courseId) {
        Map<String, Object> response = new HashMap<>();
        try {
            CourseReadDTO courseDTO = courseService.getDTOById(courseId, List.of(CURRENT_PRICE));
            response.put("course", courseDTO);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    /* =================================================== UPDATE =================================================== */
    @GetMapping("/edit-course")
    public String editCoursePageById(
            Model model,
            RedirectAttributes redirect,
            @RequestParam(name = "id") Long courseId,
            @SessionAttribute(name = "user", required = false) UserReadDTO userDTO) throws Exception {
        if (Objects.isNull(userDTO)) {
            redirect.addAttribute("mess", "Làm ơn đăng nhập");
            return "redirect:/index";
        }

        if (!userDTO.getRoleId().equals(ROLE_ADMIN)) {
            redirect.addAttribute("mess", "Bạn không đủ quyền");
            return "redirect:/index";
        }

        CourseReadDTO courseDTO = courseService.getDTOById(courseId, List.of(CURRENT_PRICE));

        model.addAttribute("course", courseDTO);

        return "course/edit-course";
    }

    @PutMapping("/edit-course")
    public String editCourse(
            Model model,
            @RequestParam(name = "id") Long courseId,
            @ModelAttribute CourseUpdateDTO updateDTO,
            RedirectAttributes redirect,
            @SessionAttribute(value = "user", required = false) UserReadDTO userDTO) {
        Map<String, Object> response = new HashMap<>();

        CourseReadDTO courseDTO = null;

        if (Objects.isNull(userDTO)) {
            redirect.addAttribute("mess", "Làm ơn đăng nhập");
            return "redirect:/index";
        }

        if (!userDTO.getRoleId().equals(ROLE_ADMIN)) {
            redirect.addAttribute("mess", "Bạn không đủ quyền");
            return "redirect:/index";
        }

        try {
            StringBuilder errorMsg = new StringBuilder();

            /* Validate input */
            errorMsg.append(
                    miscUtil.validateString(
                            "Mã khóa học", updateDTO.getCourseAlias(), 1, 10,
                            List.of("required", "minLength", "maxLength", "onlyBlank", "startBlank", "endBlank", "specialChar")));
            /* courseName */
            errorMsg.append(
                    miscUtil.validateString(
                            "Tên khóa học", updateDTO.getCourseName(), 1, 45,
                            List.of("required", "minLength", "maxLength", "onlyBlank", "startBlank", "endBlank", "specialChar")));
            /* courseDesc */
            errorMsg.append(
                    miscUtil.validateString(
                            "Miêu tả khóa học", updateDTO.getCourseDesc(), 1, 9999,
                            List.of("nullOrMinLength", "maxLength", "onlyBlank", "startBlank", "endBlank", "specialChar")));
            /* courseImg */
            /* TODO: check valid link */
            /* numSession */
            errorMsg.append(
                    miscUtil.validateNumber(
                            "Số tiết học", Double.valueOf(updateDTO.getNumSession()), 1.0, 100.0, 1.0,
                            List.of("min", "max", "onlyBlank", "step")));
            /* minScore */
            errorMsg.append(
                    miscUtil.validateNumber(
                            "Điểm tối thiểu", updateDTO.getMinScore(), 0.0, 10.0, 0.01,
                            List.of("min", "max", "onlyBlank", "step")));
            /* minAttendant */
            errorMsg.append(
                    miscUtil.validateNumber(
                            "Điểm danh tối thiểu", updateDTO.getMinAttendant(), 0.0, 100.0, 0.01,
                            List.of("min", "max", "onlyBlank", "step")));

            if (!errorMsg.isEmpty()) {
                throw new IllegalArgumentException(errorMsg.toString());
            }

            /* TODO: check status condition */


            updateDTO.setUpdatedBy(userDTO.getId());
            courseDTO = courseService.updateCourseByDTO(updateDTO);
        } catch (Exception e) {
            model.addAttribute("mess", "Lỗi : " + e.getMessage());
            return "redirect:/edit-course" + "?id=" + courseId;
        }

        redirect.addAttribute("mess", "Sửa khóa học thành công");
        return "redirect:/course-detail" + "?id=" + courseId;
    }


    /* =================================================== DELETE =================================================== */
    @GetMapping("/delete-course")
    public String deleteCourse(Model model, HttpServletRequest request, RedirectAttributes redirect) {
        HttpSession session = request.getSession();
        if (ObjectUtils.isEmpty(session.getAttribute("user"))) {
            redirect.addAttribute("mess", "Làm ơn đăng nhập");
            return "redirect:/";
        }
        UserReadDTO userDTO = (UserReadDTO) session.getAttribute("user");
        if (!userDTO.getRoleId().equals(ROLE_ADMIN)) {
            redirect.addAttribute("mess", "Bạn không đủ quyền");
            return "redirect:/";
        }
        Long Id = Long.parseLong(request.getParameter("id"));
        try {
            courseService.deleteCourse(Id,userDTO.getId());
        } catch (Exception e) {
            redirect.addAttribute("mess", "Lỗi : " + e.getMessage());
            return "redirect:/course";
        }
        redirect.addAttribute("mess", "Xóa khóa học thành công");
        return "redirect:/course";
    }
}