package com.teachsync.controllers;

import com.teachsync.dtos.clazz.ClazzReadDTO;
import com.teachsync.dtos.homework.HomeworkReadDTO;
import com.teachsync.dtos.request.RequestCreateDTO;
import com.teachsync.dtos.request.RequestReadDTO;
import com.teachsync.dtos.user.UserReadDTO;
import com.teachsync.entities.Homework;
import com.teachsync.services.clazz.ClazzService;
import com.teachsync.services.homework.HomeworkService;
import com.teachsync.utils.Constants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/homework")
public class HomeworkController {

    private Logger logger = LoggerFactory.getLogger(HomeworkController.class);

    @Autowired
    HomeworkService homeworkService;

    @Autowired
    ClazzService clazzService;

    @GetMapping("/list")
    public String viewHomeWork(HttpServletRequest request, RedirectAttributes redirect, Model model
            , @ModelAttribute("mess") String mess) {
        //check login
        HttpSession session = request.getSession();
        if (ObjectUtils.isEmpty(session.getAttribute("user"))) {
            redirect.addAttribute("mess", "Làm ơn đăng nhập");
            return "redirect:/";
        }

        try {
            Page<HomeworkReadDTO> dtoPage = homeworkService.getPageAll(null);
            model.addAttribute("homeworkList", dtoPage.getContent());
            model.addAttribute("pageNo", dtoPage.isEmpty() ? 0 : dtoPage.getPageable().getPageNumber());
            model.addAttribute("pageTotal", dtoPage.getTotalPages());
            model.addAttribute("mess", mess);
        } catch (Exception e) {
            logger.error(e.getMessage());
            redirect.addAttribute("mess", "hiển thị danh sách bài tập về nhà thất bại ,lỗi : " + e.getMessage());
            return "redirect:/";

        }

        return "homework";
    }

    @GetMapping("/add-homework")
    public String addHomework(HttpSession session, RedirectAttributes redirect, Model model, HttpServletRequest request
            , @ModelAttribute("mess") String mess) {
        //check login
        if (ObjectUtils.isEmpty(session.getAttribute("user"))) {
            redirect.addAttribute("mess", "Làm ơn đăng nhập");
            return "redirect:/";
        }

        try {
            Page<ClazzReadDTO> dtoPage = clazzService.getPageDTOAll(null);
            if (!ObjectUtils.isEmpty(request.getParameter("id"))) {
                HomeworkReadDTO homeworkReadDTO = homeworkService.findById(Long.parseLong(request.getParameter("id")));
                model.addAttribute("homework", homeworkReadDTO);
                model.addAttribute("option", "edit");
            } else {
                model.addAttribute("option", "add");
            }
            model.addAttribute("clazzList", dtoPage.getContent());
            model.addAttribute("mess", mess);

        } catch (Exception e) {
            logger.error(e.getMessage());
            redirect.addAttribute("mess", "hiển thị danh sách lớp thất bại ,lỗi : " + e.getMessage());
            return "redirect:/";

        }

        return "add-homework";
    }

    @PostMapping("/add-homework")
    public String addHomework(
            HttpServletRequest request,
            HttpSession session,
            RedirectAttributes redirect) {
        //check login
        if (ObjectUtils.isEmpty(session.getAttribute("user"))) {
            redirect.addAttribute("mess", "Làm ơn đăng nhập");
            return "redirect:/";
        }
        UserReadDTO userDTO = (UserReadDTO) session.getAttribute("user");

        HomeworkReadDTO homeworkReadDTO = new HomeworkReadDTO();

        homeworkReadDTO.setHomeworkName(request.getParameter("name"));
        homeworkReadDTO.setClazzId(Long.parseLong(request.getParameter("clazzId")));
        homeworkReadDTO.setHomeworkDesc(request.getParameter("desc"));
        homeworkReadDTO.setHomeworkDoc(null);//TODO : upload file
        homeworkReadDTO.setHomeworkDocLink(request.getParameter("homeworkDocLink"));
        String deadlineString = request.getParameter("deadline");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime deadline = LocalDateTime.parse(deadlineString, formatter);
        homeworkReadDTO.setDeadline(deadline);
        String options = "";
        try {

            if (!ObjectUtils.isEmpty(request.getParameter("homeworkId"))) {
                //edit
                homeworkReadDTO.setId(Long.parseLong(request.getParameter("homeworkId")));
                homeworkService.editHomework(homeworkReadDTO, userDTO);
                options = "Sửa";
            } else {
                //add
                homeworkService.addHomework(homeworkReadDTO, userDTO);
                options = "Thêm";
            }

        } catch (Exception e) {
            logger.error(e.getMessage());
            redirect.addAttribute("mess", options + " bài tập về nhà thất bại ,lỗi : " + e.getMessage());
            return "redirect:/homework/add-homework";

        }

        redirect.addAttribute("mess", options + " bài tập về nhà thành công");
        return "redirect:/homework/list";
    }
}
