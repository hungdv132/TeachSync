package com.teachsync.controllers;


import com.teachsync.dtos.clazz.ClazzCreateDTO;
import com.teachsync.dtos.clazz.ClazzReadDTO;
import com.teachsync.dtos.clazz.ClazzUpdateDTO;
import com.teachsync.dtos.clazzSchedule.ClazzScheduleCreateDTO;
import com.teachsync.dtos.clazzSchedule.ClazzScheduleReadDTO;
import com.teachsync.dtos.clazzSchedule.ClazzScheduleUpdateDTO;
import com.teachsync.dtos.room.RoomReadDTO;
import com.teachsync.dtos.user.UserReadDTO;

import com.teachsync.entities.ClazzSchedule;
import com.teachsync.entities.Room;
import com.teachsync.services.clazz.ClazzService;
import com.teachsync.services.clazzSchedule.ClazzScheduleService;
import com.teachsync.services.room.RoomService;
import com.teachsync.utils.Constants;
import com.teachsync.utils.MiscUtil;
import com.teachsync.utils.enums.DtoOption;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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

import static com.teachsync.utils.Constants.ROLE_ADMIN;

@Controller
public class ScheduleController {

    @Autowired
    private ClazzScheduleService clazzScheduleService;

    @Autowired
    private ClazzService clazzService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private MiscUtil miscUtil;


    @GetMapping("/schedule")
    public String courseListPage(
            Model model,
            @ModelAttribute("mess") String mess,
            @SessionAttribute(name = "user", required = false) UserReadDTO userDTO) {
        try {
            //Page<ClazzScheduleReadDTO> dtoPage;
            if (userDTO.getRoleId().equals(Constants.ROLE_STUDENT) || userDTO.getRoleId().equals(Constants.ROLE_TEACHER)) {
                /* All schedule */
                //dtoPage = clazzScheduleService.getAllDTOByClazzIdIn();
//                if (dtoPage != null) {
//                    model.addAttribute("scheduleList", dtoPage.getContent());
//                    model.addAttribute("pageNo", dtoPage.getPageable().getPageNumber());
//                    model.addAttribute("pageTotal", dtoPage.getTotalPages());
//                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMsg", "Server error, please try again later");
        }
        model.addAttribute("mess", mess);

        return "schedule/list-schedule";
    }

    /* =================================================== CREATE =================================================== */
    @GetMapping("/add-schedule")
    public String addSchedulePage(
            Model model,
            @RequestParam(value = "id", required = false) Long scheduleid,
            @RequestParam("option") String option) {

        try {
            /* Nếu Id => Edit, Lấy dữ liệu cũ */
            ClazzScheduleReadDTO clazzScheduleReadDTO = null;
            if (Objects.nonNull(scheduleid)) {
                clazzScheduleReadDTO =
                        clazzScheduleService.getDTOByClazzId(scheduleid,
                                List.of(DtoOption.CLAZZ_SCHEDULE,
                                        DtoOption.USER));

                model.addAttribute("clazzSchedule", clazzScheduleReadDTO);
            }

            /* List Clazz (lớp nào) */
            List<ClazzScheduleReadDTO> clazzScheduleDTOList = clazzScheduleService.getAllDTOByClazzIdIn(null, null);
            model.addAttribute("clazzList", clazzScheduleDTOList);


            /* List Room (Phòng nào) */
            List<Room> roomDTOList = roomService.getAllByIdIn(null);
            model.addAttribute("roomList", roomDTOList);

            model.addAttribute("option", option);
        } catch (Exception e) {
            e.printStackTrace();
            /* Log Error or return error msg */
        }

        return "schedule/add-schedule";
    }

    @PostMapping(value = "/add-schedule", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> addClazzSchedule(
            @RequestBody ClazzScheduleCreateDTO createDTO,
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
        ClazzScheduleReadDTO readDTO;
        try {
            readDTO = clazzScheduleService.createClazzScheduleByDTO(createDTO);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("error", e.getMessage());
            return response;
        }

        response.put("view", "/clazzSchedule-detail?id=" + readDTO.getId());
        return response;
    }

    @PutMapping(value = "/edit-clazzSchedule", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> editClazzSchedule(
            @RequestBody ClazzScheduleUpdateDTO updateDTO,
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

        ClazzScheduleReadDTO readDTO;
        try {
            readDTO = clazzScheduleService.updateClazzScheduleByDTO(updateDTO);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("error", e.getMessage());
            return response;
        }

        response.put("view", "/clazzSchedule-detail?id=" + readDTO.getId());
        return response;
    }

    @GetMapping("/delete-clazzSchedule")
    public String deleteClazzSchedule(
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
        String result = clazzScheduleService.deleteClazzSchedule(Id);
        if (result.equals("success")) {
            redirect.addAttribute("mess", "Xóa class schedule thành công");
            return "redirect:/schedule";
        } else {
            model.addAttribute("mess", "Xóa class schedule thất bại");
            return "schedule/add-schedule";
        }
    }

}
