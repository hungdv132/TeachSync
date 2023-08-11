package com.teachsync.controllers;

import com.teachsync.dtos.course.CourseReadDTO;
import com.teachsync.dtos.material.MaterialCreateDTO;
import com.teachsync.dtos.material.MaterialReadDTO;
import com.teachsync.dtos.material.MaterialUpdateDTO;
import com.teachsync.dtos.user.UserReadDTO;
import com.teachsync.repositories.MaterialRepository;
import com.teachsync.repositories.UserRepository;
import com.teachsync.services.course.CourseService;
import com.teachsync.utils.MiscUtil;
import com.teachsync.utils.enums.DtoOption;
import com.teachsync.utils.enums.MaterialType;
import com.teachsync.services.material.MaterialService;
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

import static com.teachsync.utils.Constants.*;

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
    public String createMaterial(
            HttpServletRequest request,
            RedirectAttributes redirect) {
        HttpSession session = request.getSession();

        UserReadDTO userDTO = (UserReadDTO) session.getAttribute("user");
        if (ObjectUtils.isEmpty(userDTO)) {
            redirect.addAttribute("mess", "Làm ơn đăng nhập");
            return "redirect:/";
        }

        if (!userDTO.getRoleId().equals(ROLE_ADMIN)) {
            redirect.addAttribute("mess", "Bạn không đủ quyền");
            return "redirect:/";
        }

        return "material/create-material";
    }

    @PostMapping("/create-material")
    public String submitCreateMaterial(Model model, HttpServletRequest request, RedirectAttributes redirect) throws Exception {

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

        /* List Course (môn nào) */
        List<CourseReadDTO> courseDTOList = courseService.getAllDTO(null);
        model.addAttribute("courseList", courseDTOList);

        /* Thay = modelAttr or json (RequestBody) */
        MaterialCreateDTO createDTO = new MaterialCreateDTO();
        createDTO.setMaterialName(request.getParameter("name"));

        createDTO.setMaterialLink(request.getParameter("link"));
        createDTO.setMaterialContent(new byte[]{Byte.parseByte(request.getParameter("content"))});
        //TODO : process upload file
        createDTO.setMaterialImg("https://th.bing.com/th/id/OIP.R7Wj-CVruj2Gcx-MmaxmZAHaKe?pid=ImgDet&rs=1");
        createDTO.setMaterialType(MaterialType.valueOf(request.getParameter("type")));
        createDTO.setIsFree(Boolean.parseBoolean(request.getParameter("free")));


        try {
            materialService.createMaterialByDTO(createDTO);
        } catch (Exception e) {
            model.addAttribute("mess", "Lỗi : " + e.getMessage());
            return "material/create-material";
        }

        redirect.addAttribute("mess", "Tạo mới tài liệu thành công");
        return "redirect:/material";
    }


    @GetMapping("/edit-material")
    public String editNews(Model model, HttpServletRequest request, RedirectAttributes redirect) throws Exception{
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
        MaterialReadDTO material = materialService.getDTOById(Id, null);
        model.addAttribute("material", material);

        return "material/edit-material";
    }

    @PostMapping("/edit-material")
    public String submitEditMaterial(Model model, HttpServletRequest request, RedirectAttributes redirect ) throws Exception {
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
        /* List Course (môn nào) */
        List<CourseReadDTO> courseDTOList = courseService.getAllDTO(null);
        model.addAttribute("courseList", courseDTOList);

        MaterialUpdateDTO updateDTO = new MaterialUpdateDTO();
        updateDTO.setId(Long.parseLong(request.getParameter("id")));
        updateDTO.setMaterialName(request.getParameter("name"));
        //TODO : process upload file
        updateDTO.setMaterialLink(request.getParameter("link"));
        updateDTO.setMaterialContent(new byte[]{Byte.parseByte(request.getParameter("content"))});
        updateDTO.setMaterialImg("https://th.bing.com/th/id/OIP.R7Wj-CVruj2Gcx-MmaxmZAHaKe?pid=ImgDet&rs=1");
        updateDTO.setMaterialType(MaterialType.valueOf(request.getParameter("type")));
        updateDTO.setIsFree(Boolean.parseBoolean(request.getParameter("free")));

        try {
            materialService.updateMaterialByDTO(updateDTO);
        } catch (Exception e) {
            model.addAttribute("mess", "Lỗi : " + e.getMessage());
            return "material/edit-material";
        }

        redirect.addAttribute("mess", "Sửa tài liệu thành công");

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
