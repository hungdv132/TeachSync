package com.teachsync.controllers;

import com.teachsync.dtos.course.CourseReadDTO;
import com.teachsync.dtos.material.MaterialCreateDTO;
import com.teachsync.dtos.material.MaterialReadDTO;
import com.teachsync.dtos.material.MaterialUpdateDTO;
import com.teachsync.dtos.user.UserReadDTO;
import com.teachsync.repositories.MaterialRepository;
import com.teachsync.repositories.UserRepository;
import com.teachsync.services.course.CourseService;
import com.teachsync.services.material.MaterialService;
import com.teachsync.utils.Constants;
import com.teachsync.utils.MiscUtil;
import com.teachsync.utils.enums.DtoOption;
import com.teachsync.utils.enums.MaterialType;
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

import java.util.List;
import java.util.Objects;

import static com.teachsync.utils.Constants.ROLE_ADMIN;
import static com.teachsync.utils.Constants.ROLE_STUDENT;

@Controller
public class MaterialController {


    private MaterialRepository materialRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MaterialService materialService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private MiscUtil miscUtil;

    @GetMapping("/material")
    public String material(
            Model model,
            @RequestParam(required = false) Integer pageNo,
            @ModelAttribute("mess") String mess,
            @SessionAttribute(name = "user", required = false) UserReadDTO userDTO) {

        try {
            Page<MaterialReadDTO> dtoPage;
            Pageable pageable = null;
            if (pageNo != null) {
                pageable = miscUtil.makePaging(pageNo, 10, "id", true);
            }

            if (Objects.isNull(userDTO) || userDTO.getRoleId().equals(ROLE_STUDENT)) {
                dtoPage = materialService.getPageAllDTOByIsFree(true, pageable, null);
            } else {
                dtoPage = materialService.getPageAllDTO(pageable, List.of(DtoOption.COURSE_LIST));
            }

            if (dtoPage != null) {
                model.addAttribute("materialList", dtoPage.getContent());
                model.addAttribute("pageNo", dtoPage.getPageable().getPageNumber());
                model.addAttribute("pageTotal", dtoPage.getTotalPages());
            }
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMsg", "Server error, please try again later");
        }
        model.addAttribute("mess", mess);

        return "material/list-material";
    }

    /* =================================================== CREATE =================================================== */
    @GetMapping("/create-material")
        public String addMaterialPage(
            Model model,
            @RequestParam("id") Long materialId,
           @ModelAttribute("mess") String mess,
            @SessionAttribute(name = "user", required = false) UserReadDTO userDTO) {
        if (userDTO == null ) {
            return "redirect:/index";
        }

        if (!userDTO.getRoleId().equals(ROLE_ADMIN)) {
            return "redirect:/index";
        }

        try {

//            /* List Course (môn nào) */
//            List<CourseReadDTO> courseDTOList = courseService.getAllDTO(null);
//            model.addAttribute("courseList", courseDTOList);

            MaterialReadDTO material = materialService.getDTOById(materialId, null);
            model.addAttribute("material", material);
        } catch (Exception e) {
            e.printStackTrace();
            /* Log Error or return error msg */
        }

        return "material/create-material";
    }

    @PostMapping("/create-material")
    public String addMaterial(
            Model model,
            @ModelAttribute MaterialCreateDTO createDTO,
            @SessionAttribute(value = "user", required = false) UserReadDTO userDTO,
            RedirectAttributes redirect) throws Exception {

        //check login
        if (ObjectUtils.isEmpty(userDTO)) {
            redirect.addAttribute("mess", "Làm ơn đăng nhập");
            return "redirect:/index";
        }

        if (!userDTO.getRoleId().equals(Constants.ROLE_ADMIN)) {
            redirect.addAttribute("mess", "bạn không đủ quyền");
            return "redirect:/index";
        }

        try {
            materialService.createMaterialByDTO(createDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/create-material";
        }

        return "redirect:/material";
    }


    @GetMapping("/edit-material")
    public String editMaterialPage(
            Model model,
            @RequestParam("id") Long materialId,
           @ModelAttribute("mess") String mess,
            @SessionAttribute(name = "user", required = false) UserReadDTO userDTO) {
        if (userDTO == null ) {
            return "redirect:/index";
        }

        if (!userDTO.getRoleId().equals(ROLE_ADMIN)) {
            return "redirect:/index";
        }

        try {

//            List<CourseReadDTO> courseDTOList = courseService.getAllDTO(null);
//            model.addAttribute("courseList", courseDTOList);

            MaterialReadDTO material = materialService.getDTOById(materialId, null);
            model.addAttribute("material", material);

        } catch (Exception e) {
            e.printStackTrace();
            /* Log Error or return error msg */
        }

        return "material/edit-material";
    }

    @PostMapping("/edit-material")
    public String editMaterial(
            Model model,
            @ModelAttribute MaterialUpdateDTO updateDTO,
            @SessionAttribute(value = "user", required = false) UserReadDTO userDTO,
            RedirectAttributes redirect) throws Exception {

        //check login
        if (ObjectUtils.isEmpty(userDTO)) {
            redirect.addAttribute("mess", "Làm ơn đăng nhập");
            return "redirect:/index";
        }

        if (!userDTO.getRoleId().equals(Constants.ROLE_ADMIN)) {
            redirect.addAttribute("mess", "bạn không đủ quyền");
            return "redirect:/index";
        }

        try {
            materialService.updateMaterialByDTO(updateDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/edit-material";
        }

        return "redirect:/material";
    }

    @GetMapping("/delete-material")
    public String deleteMaterial(Model model, HttpServletRequest request, RedirectAttributes redirect) {
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
            materialService.deleteMaterial(Id);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMsg", "Server error, please try again later");
        }
        model.addAttribute("mess", "Xóa tài liệu thành công");

        return "material/list-material";
    }

    @GetMapping("/material-detail")
    public String getDetailById(
            @RequestParam(name = "id") Long courseId,
            Model model,
            @SessionAttribute(name = "user", required = false) UserReadDTO userDTO) {
        try {
            MaterialReadDTO material = materialService.getDTOById(courseId, null);

            if (material == null) {
                /* Not found by Id */
                return "redirect:/material";
            }

            model.addAttribute("material", material);

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMsg", "Server error, please try again later");
        }


        return "material/material-detail";
    }
}
