package com.teachsync.controllers;

import com.teachsync.dtos.clazz.ClazzReadDTO;
import com.teachsync.dtos.course.CourseCreateDTO;
import com.teachsync.dtos.course.CourseReadDTO;
import com.teachsync.dtos.course.CourseUpdateDTO;
import com.teachsync.dtos.courseSemester.CourseSemesterReadDTO;
import com.teachsync.dtos.priceLog.PriceLogReadDTO;
import com.teachsync.dtos.user.UserReadDTO;
import com.teachsync.entities.Course;
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

import static com.teachsync.utils.Constants.*;
import static com.teachsync.utils.enums.PromotionType.*;
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
    @ResponseBody
    public Map<String, Object> addCourse(
            Model model,
            @RequestBody CourseCreateDTO createDTO,
            RedirectAttributes redirect,
            @SessionAttribute(value = "user", required = false) UserReadDTO userDTO) {
        Map<String, Object> response = new HashMap<>();

        CourseReadDTO courseDTO = null;

        if (Objects.isNull(userDTO)) {
            redirect.addAttribute("mess", "Làm ơn đăng nhập");
            response.put("view", "/index");
            return response;
        }

        if (!userDTO.getRoleId().equals(ROLE_ADMIN)) {
            redirect.addAttribute("mess", "Bạn không đủ quyền");
            response.put("view", "/index");
            return response;
        }

        try {
            createDTO.setCreatedBy(userDTO.getId());
            courseDTO = courseService.createCourseByDTO(createDTO);
        } catch (Exception e) {
            model.addAttribute("mess", "Lỗi : " + e.getMessage());
            response.put("mess", "Lỗi : " + e.getMessage());
            response.put("view", "/add-course");
            return response;
        }

        redirect.addAttribute("mess", "Tạo mới khóa học thành công");
        response.put("view", "/course-detail?id=" + courseDTO.getId());
        return response;
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
                    options = List.of(MATERIAL_LIST, TEST_LIST, CLAZZ_LIST, CURRENT_PRICE);
                }
            }

            CourseReadDTO courseDTO = courseService.getDTOById(courseId, options);

            if (courseDTO == null) {
                /* Not found by id */
                return "redirect:/course";
            }

            List<CourseSemesterReadDTO> courseSemesterList =
                    courseSemesterService.getAllLatestDTOByCourseId(courseId, null);

            model.addAttribute("course", courseDTO);
            model.addAttribute(
                    "hasLatestSchedule",
                    (courseSemesterList != null && !courseSemesterList.isEmpty()));

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
    @ResponseBody
    public Map<String, Object> editCourse(
            Model model,
            @RequestBody CourseUpdateDTO updateDTO,
            RedirectAttributes redirect,
            @SessionAttribute(value = "user", required = false) UserReadDTO userDTO) {
        Map<String, Object> response = new HashMap<>();

        CourseReadDTO courseDTO = null;

        if (Objects.isNull(userDTO)) {
            redirect.addAttribute("mess", "Làm ơn đăng nhập");
            response.put("view", "/index");
            return response;
        }

        if (!userDTO.getRoleId().equals(ROLE_ADMIN)) {
            redirect.addAttribute("mess", "Bạn không đủ quyền");
            response.put("view", "/index");
            return response;
        }

        try {
            updateDTO.setUpdatedBy(userDTO.getId());
            courseDTO = courseService.updateCourseByDTO(updateDTO);
        } catch (Exception e) {
            model.addAttribute("mess", "Lỗi : " + e.getMessage());
            response.put("view", "/edit-course");
            return response;
        }

        redirect.addAttribute("mess", "Sửa khóa học thành công");
        response.put("view", "/course-detail?id=" + courseDTO.getId());
        return response;
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
