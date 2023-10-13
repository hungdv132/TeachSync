package com.teachsync.controllers;

import com.teachsync.dtos.clazz.ClazzReadDTO;
import com.teachsync.dtos.course.CourseCreateDTO;
import com.teachsync.dtos.course.CourseReadDTO;
import com.teachsync.dtos.course.CourseUpdateDTO;
import com.teachsync.dtos.courseSemester.CourseSemesterReadDTO;
import com.teachsync.dtos.user.UserReadDTO;
import com.teachsync.entities.Course;
import com.teachsync.services.course.CourseService;
import com.teachsync.services.courseSemester.CourseSemesterService;
import com.teachsync.utils.Constants;
import com.teachsync.utils.MiscUtil;
import com.teachsync.utils.enums.DtoOption;
import com.teachsync.utils.enums.Status;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

import static com.teachsync.utils.Constants.*;
import static com.teachsync.utils.enums.DtoOption.*;
import static com.teachsync.utils.enums.Status.*;


@Controller
public class CourseController {
    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseSemesterService courseSemesterService;
    @Autowired
    private MiscUtil miscUtil;


    /* =================================================== API ====================================================== */
    @GetMapping("/api/course-detail")
    @ResponseBody
    public Map<String, Object> getCourseDetail(
            @RequestParam Long courseId) {

        Map<String, Object> response = new HashMap<>();

        try {
            CourseReadDTO courseDTO =
                    courseService.getDTOById(
                            courseId,
                            List.of(DELETED),
                            false,
                            List.of(CURRENT_PRICE));

            response.put("course", courseDTO);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    /* =================================================== CREATE =================================================== */
    @GetMapping("/add-course")
    public String addCoursePage(
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

        return "course/add-course";
    }

    @PostMapping("/add-course")
    public String addCourse(
            RedirectAttributes redirect,
            Model model,
            @ModelAttribute CourseCreateDTO createDTO,
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
            createDTO.setCreatedBy(userDTO.getId());

            courseDTO = courseService.createCourseByDTO(createDTO);

        } catch (Exception e) {
            model.addAttribute("mess", "Lỗi : " + e.getMessage());

            return "/course/add-course";
        }

        return "/course/course-detail" + "?id=" + courseDTO.getId();
    }


    /* =================================================== READ ===================================================== */
    @GetMapping("/course")
    public String courseListPage(
            RedirectAttributes redirect,
            Model model,
            @ModelAttribute("mess") String mess,
            @RequestParam(value = "pageNo", required = false) Integer pageNo,
            @SessionAttribute(name = "user", required = false) UserReadDTO userDTO) {

        try {
            Page<CourseReadDTO> dtoPage;

            int pageType = 0;

            if (Objects.isNull(userDTO)) {
                pageType = 1;
            } else {

                Long roleId = userDTO.getRoleId();

                if (ROLE_STUDENT.equals(roleId)) {
                    pageType = 1;
                } else if (ROLE_PARENTS.equals(roleId)) {
                    pageType = 1;
                } else if (ROLE_TEACHER.equals(roleId)) {
                    pageType = 2;
                } else if (ROLE_ADMIN.equals(roleId)) {
                    pageType = 3;
                }
            }

            List<Status> statuses;
            boolean isStatusIn;

            switch (pageType) {
                case 1 -> {
                    /* Is guest, student, parent => All OPENED Course */
                    statuses = List.of(OPENED);
                    isStatusIn = true;

                    /* Hot course */
                    dtoPage =
                            courseService.getPageAllDTOOnSale(
                                    null,
                                    statuses,
                                    isStatusIn,
                                    null);

                    if (dtoPage != null) {
                        model.addAttribute("hotCourseList", dtoPage.getContent());
                        model.addAttribute("hotPageNo", dtoPage.getPageable().getPageNumber());
                        model.addAttribute("hotPageTotal", dtoPage.getTotalPages());
                    }
                }
                case 2 -> {
                    /* Is teacher => All OPENED & CLOSED Course */
                    statuses = List.of(OPENED, CLOSED);
                    isStatusIn = true;
                }
                case 3 -> {
                    /* Is Admin => All Course NOT DELETED */
                    statuses = List.of(DELETED);
                    isStatusIn = false;
                }
                default -> {
                    throw new IllegalArgumentException(
                            "Invalid role");
                }
            }

            if (Objects.isNull(pageNo)) {
                pageNo = 0;
            }

            Pageable pageable = miscUtil.makePaging(pageNo, 10, "status", true);

            /* All course */
            dtoPage =
                    courseService.getPageAllDTO(
                            pageable,
                            statuses,
                            isStatusIn,
                            null);

            if (dtoPage != null) {
                model.addAttribute("courseList", dtoPage.getContent());
                model.addAttribute("pageNo", dtoPage.getPageable().getPageNumber());
                model.addAttribute("pageTotal", dtoPage.getTotalPages());
            }

        } catch (Exception e) {
            e.printStackTrace();

            redirect.addAttribute("mess", e.getMessage());

            return "redirect:/index";
        }

        model.addAttribute("mess", mess);

        return "course/list-course";
    }

    @GetMapping("/course-detail")
    public String courseDetailPage(
            RedirectAttributes redirect,
            Model model,
            @RequestParam(name = "id") Long courseId,
            @SessionAttribute(name = "user", required = false) UserReadDTO userDTO) {

        try {
            int pageType = 0;
            if (Objects.isNull(userDTO)) {
                pageType = 1;
            }

            Long roleId = userDTO.getRoleId();

            if (ROLE_STUDENT.equals(roleId)) {
                pageType = 2;
            } else if (ROLE_PARENTS.equals(roleId)) {
                pageType = 1;
            } else if (ROLE_TEACHER.equals(roleId)) {
                pageType = 2;
            } else if (ROLE_ADMIN.equals(roleId)) {
                pageType = 3;
            }

            List<Status> statuses;
            boolean isStatusIn;
            List<DtoOption> options;

            switch (pageType) {
                case 1 -> {
                    /* Is guest, parent => All OPENED Course */
                    statuses = List.of(OPENED);
                    isStatusIn = true;
                    options = List.of(CURRENT_PRICE, CERTIFICATE, CLAZZ_LIST);
                }
                case 2 -> {
                    /* Is teacher, student => All OPENED & CLOSED Course */
                    statuses = List.of(OPENED, CLOSED);
                    isStatusIn = true;
                    options = List.of(CURRENT_PRICE, CERTIFICATE, CLAZZ_LIST_ALL,
                            MATERIAL_LIST, TEST_LIST);
                }
                case 3 -> {
                    /* Is Admin => All Course NOT DELETED */
                    statuses = List.of(DELETED);
                    isStatusIn = false;
                    options = List.of(CURRENT_PRICE, CERTIFICATE, CLAZZ_LIST_ALL,
                            MATERIAL_LIST, TEST_LIST, PRICE_LOG);
                }
                default -> {
                    throw new IllegalArgumentException(
                            "Invalid role");
                }
            }

            CourseReadDTO courseDTO =
                    courseService.getDTOById(
                            courseId,
                            statuses,
                            isStatusIn,
                            options);

            if (courseDTO == null) {
                /* Not found by id */
                redirect.addAttribute("mess","Không tìm thấy khóa học nào với id: " + courseId);

                return "redirect:/course";
            }

            model.addAttribute("course", courseDTO);

            model.addAttribute("hasClazz", ObjectUtils.isEmpty(courseDTO.getClazzList()));

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMsg", "Server error, please try again later");
        }

        return "course/course-detail";
    }


    /* =================================================== UPDATE =================================================== */
    @GetMapping("/edit-course")
    public String editCoursePageById(
            RedirectAttributes redirect,
            Model model,
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

        CourseReadDTO courseDTO =
                courseService.getDTOById(
                        courseId, 
                        List.of(DELETED),
                        false,
                        List.of(CURRENT_PRICE));

        model.addAttribute("course", courseDTO);

        return "course/edit-course";
    }

    @PostMapping("/edit-course")
    public String editCourse(
            Model model,
            @RequestParam(name = "id") Long courseId,
            @ModelAttribute CourseUpdateDTO updateDTO,
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
            updateDTO.setUpdatedBy(userDTO.getId());

            courseDTO = courseService.updateCourseByDTO(updateDTO);

        } catch (Exception e) {
            redirect.addAttribute("mess", "Lỗi : " + e.getMessage());

            return "redirect:/edit-course" + "?id=" + courseId;
        }

        redirect.addAttribute("mess", "Sửa khóa học thành công");

        return "redirect:/course-detail" + "?id=" + courseId;
    }


    /* =================================================== DELETE =================================================== */
    @GetMapping("/delete-course")
    public String deleteCourse(
            RedirectAttributes redirect,
            @RequestParam(name = "id") Long courseId,
            @SessionAttribute(value = "user", required = false) UserReadDTO userDTO) {

        if (Objects.isNull(userDTO)) {
            redirect.addAttribute("mess", "Làm ơn đăng nhập");
            return "redirect:/index";
        }

        if (!userDTO.getRoleId().equals(ROLE_ADMIN)) {
            redirect.addAttribute("mess", "Bạn không đủ quyền");
            return "redirect:/index";
        }

        try {
            courseService.deleteCourse(courseId);

        } catch (Exception e) {
            redirect.addAttribute("mess", "Lỗi : " + e.getMessage());

            return "redirect:/course";
        }

        redirect.addAttribute("mess", "Xóa khóa học thành công");

        return "redirect:/course";
    }
}