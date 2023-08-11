package com.teachsync.controllers;

import com.teachsync.dtos.center.CenterReadDTO;
import com.teachsync.dtos.clazz.ClazzCreateDTO;
import com.teachsync.dtos.clazz.ClazzReadDTO;
import com.teachsync.dtos.clazz.ClazzUpdateDTO;
import com.teachsync.dtos.course.CourseReadDTO;
import com.teachsync.dtos.semester.SemesterReadDTO;
import com.teachsync.dtos.staff.StaffReadDTO;
import com.teachsync.dtos.user.UserReadDTO;
import com.teachsync.services.center.CenterService;
import com.teachsync.services.clazz.ClazzService;
import com.teachsync.services.course.CourseService;
import com.teachsync.services.courseSemester.CourseSemesterService;
import com.teachsync.services.semester.SemesterService;
import com.teachsync.services.staff.StaffService;
import com.teachsync.utils.Constants;
import com.teachsync.utils.enums.DtoOption;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
public class ClazzController {
    @Autowired
    private ClazzService clazzService;

    @Autowired
    private CourseService courseService;
    @Autowired
    private SemesterService semesterService;
    @Autowired
    private CourseSemesterService courseSemesterService;
    @Autowired
    private CenterService centerService;
    @Autowired
    private StaffService staffService;

    @Autowired
    private ModelMapper mapper;

    @GetMapping("/clazz")
    public String clazzListPage(
            Model model,
            @ModelAttribute("mess") String mess){
        try {
            Page<ClazzReadDTO> dtoPage =
                    clazzService.getPageDTOAll(
                            null,
                            List.of(DtoOption.COURSE_SEMESTER,
                                    DtoOption.SEMESTER,
                                    DtoOption.COURSE_NAME,
                                    DtoOption.COURSE_ALIAS,
                                    DtoOption.CENTER));

            if (dtoPage != null) {
                model.addAttribute("clazzList", dtoPage.getContent());
                model.addAttribute("pageNo", dtoPage.getPageable().getPageNumber());
                model.addAttribute("pageTotal", dtoPage.getTotalPages());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        model.addAttribute("mess", mess);
        return "clazz/list-clazz";
    }

    @GetMapping("/clazz-detail")
    public String clazzDetailPage(
            Model model,
            @RequestParam(value = "id", required = false) Long clazzId) {
        try {
            ClazzReadDTO clazzDTO =
                    clazzService.getDTOById(
                            clazzId,
                            List.of(DtoOption.STAFF,
                                    DtoOption.USER,
                                    DtoOption.COURSE_SEMESTER,
                                    DtoOption.SEMESTER,
                                    DtoOption.COURSE_NAME,
                                    DtoOption.COURSE_ALIAS,
                                    DtoOption.CENTER));

            model.addAttribute("clazz", clazzDTO);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "clazz/clazz-detail";
    }

    @GetMapping("/add-clazz")
    public String addClazzPage(
            Model model,
            @RequestParam(value = "id", required = false) Long clazzId,
            @RequestParam("option") String option) {

        try {
            /* Nếu Id => Edit, Lấy dữ liệu cũ */
            ClazzReadDTO clazzReadDTO = null;
            if (Objects.nonNull(clazzId)) {
                clazzReadDTO =
                        clazzService.getDTOById(clazzId, 
                                List.of(DtoOption.COURSE_SEMESTER,
                                        DtoOption.STAFF, 
                                        DtoOption.USER));
                
                model.addAttribute("clazz", clazzReadDTO);
            }

            /* List Course (môn nào) */
            List<CourseReadDTO> courseDTOList = courseService.getAllDTO(null);
            model.addAttribute("courseList", courseDTOList);

            /* List Semester (kỳ nào) */
            List<SemesterReadDTO> semesterDTOList;
            if (option.equals("add")) {
                /* Các kỳ học nào ngày bắt đàu cách 10 ngày từ hiện tại (Để học sinh còn có thời gian đăng ký) */
                semesterDTOList =
                        semesterService.getAllDTOByStartDateAfter(LocalDate.now().plusDays(10), null);
            } else {
                semesterDTOList =
                        semesterService.getAllDTO(null);
            }
            model.addAttribute("semesterList", semesterDTOList);

            /* List Center (Cơ sở nào) */
            List<CenterReadDTO> centerDTOList = centerService.getAllDTO(null);
            model.addAttribute("centerList", centerDTOList);

            /* List Staff (Ai dạy) */
            List<StaffReadDTO> staffDTOList;
            if (option.equals("add")) {
                /* Suy ra từ Center đầu tiên trong list */
                staffDTOList =
                        staffService.getAllDTOByCenterId(centerDTOList.get(0).getId(), List.of(DtoOption.USER));
            } else {
                staffDTOList =
                        staffService.getAllDTOByCenterId(clazzReadDTO.getCourseSemester().getCenterId(), List.of(DtoOption.USER));
            }
            model.addAttribute("staffList", staffDTOList);

            model.addAttribute("option", option);
        } catch (Exception e) {
            e.printStackTrace();
            /* Log Error or return error msg */
        }

        return "clazz/add-clazz";
    }

    /* TODO: move to Staff restController */
    @GetMapping("/api/staff")
    @ResponseBody
    public Map<String, Object> getStaffList(
            @RequestParam Long centerId) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<StaffReadDTO> staffDTOList =
                    staffService.getAllDTOByCenterId(centerId, List.of(DtoOption.USER));
            response.put("staffList", staffDTOList);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    @PostMapping(value = "/add-clazz", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> addClazz(
            @RequestBody ClazzCreateDTO createDTO,
            @SessionAttribute(value = "user", required = false) UserReadDTO userDTO,
            RedirectAttributes redirect) throws Exception {
        Map<String, Object> response = new HashMap<>();

        //check login
        if (ObjectUtils.isEmpty(userDTO)) {
            redirect.addAttribute("mess", "Làm ơn đăng nhập");
            response.put("view", "/index");
            return response;
        }

        if (!userDTO.getRoleId().equals(Constants.ROLE_ADMIN)) {
            redirect.addAttribute("mess", "bạn không đủ quyền");
            response.put("view", "/index");
            return response;
        }
        ClazzReadDTO readDTO;
        try {
            readDTO = clazzService.createClazzByDTO(createDTO);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("error", e.getMessage());
            return response;
        }

        response.put("view", "/clazz-detail?id=" + readDTO.getId());
        return response;
    }

    @PutMapping(value = "/edit-clazz", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> editClazz(
            @RequestBody ClazzUpdateDTO updateDTO,
            @SessionAttribute(value = "user", required = false) UserReadDTO userDTO,
            RedirectAttributes redirect) throws Exception {
        Map<String, Object> response = new HashMap<>();

        //check login
        if (ObjectUtils.isEmpty(userDTO)) {
            redirect.addAttribute("mess", "Làm ơn đăng nhập");
            response.put("view", "/index");
            return response;
        }

        if (!userDTO.getRoleId().equals(Constants.ROLE_ADMIN)) {
            redirect.addAttribute("mess", "bạn không đủ quyền");
            response.put("view", "/index");
            return response;
        }

        ClazzReadDTO readDTO;
        try {
            readDTO = clazzService.updateClazzByDTO(updateDTO);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("error", e.getMessage());
            return response;
        }

        response.put("view", "/clazz-detail?id=" + readDTO.getId());
        return response;
    }

    @GetMapping("/delete-clazz")
    public String deleteClazz(
            @SessionAttribute(value = "user", required = false) UserReadDTO userDTO,
            HttpServletRequest request,
            Model model,
            RedirectAttributes redirect) {
        //check login
        if (ObjectUtils.isEmpty(userDTO)) {
            redirect.addAttribute("mess", "Làm ơn đăng nhập");
            return "redirect:/";
        }

        if (!userDTO.getRoleId().equals(Constants.ROLE_ADMIN)) {
            redirect.addAttribute("mess", "bạn không đủ quyền");
            return "redirect:/";
        }
        Long Id = Long.parseLong(request.getParameter("Id"));
        String result = clazzService.deleteClazz(Id);
        if (result.equals("success")) {
            redirect.addAttribute("mess", "Xóa class room thành công");
            return "redirect:/clazz";
        } else {
            model.addAttribute("mess", "Xóa class room thất bại");
            return "clazz/add-clazz";
        }
    }
}
