package com.teachsync.controllers;

import com.teachsync.dtos.center.CenterReadDTO;
import com.teachsync.dtos.recruitmentCampaign.RecruitmentCampaignReadDTO;
import com.teachsync.dtos.user.UserReadDTO;
import com.teachsync.services.center.CenterService;
import com.teachsync.services.recruitmentCampaign.RecruitmentCampaignService;
import com.teachsync.utils.Constants;
import com.teachsync.utils.enums.DtoOption;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/recruitment-campaign")
public class RecruitmentCampaignController {

    @Autowired
    RecruitmentCampaignService recruitmentCampaignService;

    @Autowired
    CenterService centerService;

    private Logger logger = LoggerFactory.getLogger(RecruitmentCampaignController.class);

    @GetMapping("/list")
    public String listRecruitmentCampaign(Model model,  @ModelAttribute("mess") String mess) {
        try {
            Page<RecruitmentCampaignReadDTO> dtoPage = recruitmentCampaignService.getPageAllDTO(null, List.of(DtoOption.CENTER));


            if (dtoPage != null) {
                model.addAttribute("campaignList", dtoPage.getContent());
                model.addAttribute("pageNo", dtoPage.getPageable().getPageNumber());
                model.addAttribute("pageTotal", dtoPage.getTotalPages());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        model.addAttribute("mess", mess);
        return "campaign/list-recruitment-campaign";
    }

    @GetMapping("/detail")
    public String detail(HttpSession session, RedirectAttributes redirect, Model model, HttpServletRequest request
            , @ModelAttribute("mess") String mess) {
        //check login
        if (ObjectUtils.isEmpty(session.getAttribute("user"))) {
            redirect.addAttribute("mess", "Làm ơn đăng nhập");
            return "redirect:/";
        }

        try {
            if (!ObjectUtils.isEmpty(request.getParameter("id"))) {
                RecruitmentCampaignReadDTO recruitmentCampaignReadDTO = recruitmentCampaignService.getDTOById(Long.parseLong(request.getParameter("id")), List.of(DtoOption.CENTER));
                model.addAttribute("campaign", recruitmentCampaignReadDTO);
                model.addAttribute("option", "detail");
            }
            model.addAttribute("mess", mess);
        } catch (Exception e) {
            logger.error(e.getMessage());
            redirect.addAttribute("mess", "hiển thị chi tiết chiến dịch thất bại ,lỗi : " + e.getMessage());
            return "redirect:/recruitment-campaign/list";

        }

        return "campaign/add-campaign";
    }

    @GetMapping("/add")
    public String addCampaign(
            @SessionAttribute(value = "user", required = false) UserReadDTO userDTO,
            RedirectAttributes redirect,
            Model model,
            HttpServletRequest request,
            @ModelAttribute("mess") String mess) {
        //check login
        if (ObjectUtils.isEmpty(userDTO)) {
            redirect.addAttribute("mess", "Làm ơn đăng nhập");
            return "redirect:/";
        }

        if (!List.of(Constants.ROLE_ADMIN).contains(userDTO.getRoleId())) {
            /* Nếu roleId ko có trong list role được cấp phép */
            redirect.addAttribute("mess", "bạn không đủ quyền");
            return "redirect:/";
        }

        try {
            List<CenterReadDTO> listCenter = centerService.getAllDTO(null);
            if (!ObjectUtils.isEmpty(request.getParameter("id"))) {
                RecruitmentCampaignReadDTO recruitmentCampaignReadDTO = recruitmentCampaignService.getDTOById(Long.parseLong(request.getParameter("id")), List.of(DtoOption.CENTER));
                model.addAttribute("campaign", recruitmentCampaignReadDTO);
                model.addAttribute("option", "edit");
            } else {
                model.addAttribute("option", "add");
            }
            model.addAttribute("centerList", listCenter);
            model.addAttribute("mess", mess);

        } catch (Exception e) {
            logger.error(e.getMessage());
            redirect.addAttribute("mess", "hiển thị chi tiết chiến dịch thất bại ,lỗi : " + e.getMessage());
            return "redirect:/recruitment-campaign/list";

        }

        return "campaign/add-campaign";
    }

    @PostMapping("/add")
    public String addCampaign(
            HttpServletRequest request,
            @SessionAttribute(value = "user", required = false) UserReadDTO userDTO,
            RedirectAttributes redirect) {
        //check login
        if (ObjectUtils.isEmpty(userDTO)) {
            redirect.addAttribute("mess", "Làm ơn đăng nhập");
            return "redirect:/";
        }

        if (!List.of(Constants.ROLE_ADMIN).contains(userDTO.getRoleId())) {
            /* Nếu roleId ko có trong list role được cấp phép */
            redirect.addAttribute("mess", "bạn không đủ quyền");
            return "redirect:/";
        }

        RecruitmentCampaignReadDTO recruitmentCampaignReadDTO = new RecruitmentCampaignReadDTO();
        recruitmentCampaignReadDTO.setCenterId(Long.parseLong(request.getParameter("centerId")));
        recruitmentCampaignReadDTO.setCampaignName(request.getParameter("name"));
        //TODO : upload image
        recruitmentCampaignReadDTO.setCampaignImg("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRdLpM2NuHCmmYjvWgq1W9A3P8X4UjHov04CQbKOZdWDG31Adky365iY45ABveL_sMj_vI&usqp=CAU");
        recruitmentCampaignReadDTO.setCampaignDesc(request.getParameter("campaignDesc"));
        recruitmentCampaignReadDTO.setPosition(request.getParameter("position"));
        recruitmentCampaignReadDTO.setOpeningSlot(Integer.parseInt(request.getParameter("openingSlot")));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

        String recruitFromString = request.getParameter("recruitFrom");
        LocalDateTime recruitFrom = LocalDateTime.parse(recruitFromString, formatter);
        recruitmentCampaignReadDTO.setRecruitFrom(recruitFrom);

        String recruitToString = request.getParameter("recruitTo");
        LocalDateTime recruitTo = LocalDateTime.parse(recruitToString, formatter);
        recruitmentCampaignReadDTO.setRecruitTo(recruitTo);

        String options = "";
        try {
            if (!ObjectUtils.isEmpty(request.getParameter("id"))) {
                //edit
                recruitmentCampaignReadDTO.setId(Long.parseLong(request.getParameter("id")));
                recruitmentCampaignService.edit(recruitmentCampaignReadDTO, userDTO);
                options = "Sửa";
            } else {
                //add
                recruitmentCampaignService.add(recruitmentCampaignReadDTO, userDTO);
                options = "Thêm";
            }

        } catch (Exception e) {
            logger.error(e.getMessage());
            redirect.addAttribute("mess", options + " chiến dịch thất bại ,lỗi : " + e.getMessage());
            return "redirect:/recruitment-campaign/add";

        }

        redirect.addAttribute("mess", options + " chiến dịch thành công");
        return "redirect:/recruitment-campaign/list";
    }

    @GetMapping("/delete")
    public String delete(HttpSession session, RedirectAttributes redirect, Model model, HttpServletRequest request
            , @ModelAttribute("mess") String mess) {
        //check login
        if (ObjectUtils.isEmpty(session.getAttribute("user"))) {
            redirect.addAttribute("mess", "Làm ơn đăng nhập");
            return "redirect:/";
        }
        UserReadDTO userDTO = (UserReadDTO) session.getAttribute("user");

        if (!userDTO.getRoleId().equals(Constants.ROLE_ADMIN)) {
            redirect.addAttribute("mess", "bạn không đủ quyền");
            return "redirect:/";
        }

        try {
            recruitmentCampaignService.delete(Long.parseLong(request.getParameter("id")), userDTO);
        } catch (Exception e) {
            logger.error(e.getMessage());
            redirect.addAttribute("mess", e.getMessage());
            return "redirect:/recruitment-campaign/list";

        }

        redirect.addAttribute("mess", "Xóa chiến dịch thành công");
        return "redirect:/recruitment-campaign/list";
    }
}
