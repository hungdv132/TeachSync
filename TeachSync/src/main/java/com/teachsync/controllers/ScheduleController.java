package com.teachsync.controllers;


import com.teachsync.dtos.center.CenterReadDTO;
import com.teachsync.dtos.clazz.ClazzReadDTO;
import com.teachsync.dtos.clazzSchedule.ClazzScheduleCreateDTO;
import com.teachsync.dtos.clazzSchedule.ClazzScheduleReadDTO;
import com.teachsync.dtos.clazzSchedule.ClazzScheduleUpdateDTO;
import com.teachsync.dtos.room.RoomReadDTO;
import com.teachsync.dtos.scheduleCategory.ScheduleCategoryReadDTO;
import com.teachsync.dtos.session.SessionCreateDTO;
import com.teachsync.dtos.session.SessionUpdateDTO;
import com.teachsync.dtos.user.UserReadDTO;

import com.teachsync.entities.ClazzSchedule;
import com.teachsync.services.clazz.ClazzService;
import com.teachsync.services.clazzSchedule.ClazzScheduleService;
import com.teachsync.services.room.RoomService;
import com.teachsync.services.scheduleCategory.ScheduleCateService;
import com.teachsync.services.session.SessionService;
import com.teachsync.utils.Constants;
import com.teachsync.utils.MiscUtil;
import com.teachsync.utils.enums.Slot;
import com.teachsync.utils.enums.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static com.teachsync.utils.Constants.*;
import static com.teachsync.utils.enums.DtoOption.*;

@Controller
public class ScheduleController {

    @Autowired
    private ClazzScheduleService clazzScheduleService;

    @Autowired
    private ClazzService clazzService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private SessionService sessionService;

    @Autowired
    private ScheduleCateService scheduleCateService;

    @Autowired
    private MiscUtil miscUtil;


    /* =================================================== API ====================================================== */
    @GetMapping("/api/clazz-schedule")
    @ResponseBody
    public Map<String, Object> getClazzSchedule(
            @RequestParam("clazzId") Long clazzId) {
        Map<String, Object> response = new HashMap<>();
        try {
            ClazzScheduleReadDTO scheduleDTO =
                    clazzScheduleService.getDTOByClazzId(clazzId, List.of(SCHEDULE_CAT));

            response.put("clazzSchedule", scheduleDTO);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }


    /* =================================================== CREATE =================================================== */
    @GetMapping("/add-schedule")
    public String addSchedulePage(
            RedirectAttributes redirect,
            Model model,
            @RequestParam("id") Long clazzId,
            @ModelAttribute("mess") String mess,
            @SessionAttribute(name = "user", required = false) UserReadDTO userDTO) {
        //check login
        if (userDTO == null ) {
            redirect.addFlashAttribute("mess", "Làm ơn đăng nhập");
            return "redirect:/index";
        }

        if (!userDTO.getRoleId().equals(ROLE_ADMIN)) {
            redirect.addFlashAttribute("mess", "bạn không đủ quyền");
            return "redirect:/index";
        }

        try {
            /* Clazz */
            ClazzReadDTO clazzReadDTO =
                    clazzService.getDTOById(
                            clazzId,
                            List.of(Status.DELETED),
                            false,
                            List.of(CLAZZ_SCHEDULE, SCHEDULE_CAT, COURSE, CENTER));
            model.addAttribute("clazz", clazzReadDTO);

            /* Room List */
            CenterReadDTO centerReadDTO = clazzReadDTO.getCenter();
            List<RoomReadDTO> roomReadDTOList = roomService.getAllDTOByCenterId(centerReadDTO.getId(), null);
            model.addAttribute("roomList", roomReadDTOList);

            /* Schedule Category List */
            List<ScheduleCategoryReadDTO> scheduleCateDTOList = scheduleCateService.getAllDTO();
            model.addAttribute("scheduleCateList", scheduleCateDTOList);

        } catch (Exception e) {
            e.printStackTrace();
            /* Log Error or return error msg */

            redirect.addFlashAttribute("mess", e.getMessage());
        }

        return "schedule/add-schedule";
    }

    @PostMapping("/add-schedule")
    public String addClazzSchedule(
            @RequestParam("clazzId") Long clazzId,
            @ModelAttribute("createDTO") ClazzScheduleCreateDTO createDTO,
            @SessionAttribute(value = "user", required = false) UserReadDTO userDTO,
            RedirectAttributes redirect) throws Exception {
        //check login
        if (ObjectUtils.isEmpty(userDTO)) {
            redirect.addFlashAttribute("mess", "Làm ơn đăng nhập");
            return "redirect:/index";
        }
        if (!userDTO.getRoleId().equals(Constants.ROLE_ADMIN)) {
            redirect.addFlashAttribute("mess", "bạn không đủ quyền");
            return "redirect:/index";
        }

        try {
            /* Create Schedule */
            Slot slot = Slot.fromInt(createDTO.getSlot());

            assert slot != null;
            createDTO.setSessionStart(LocalTime.parse(slot.getStart()));
            createDTO.setSessionEnd(LocalTime.parse(slot.getEnd()));
            createDTO.setCreatedBy(userDTO.getId());

            ClazzScheduleReadDTO clazzScheduleReadDTO = clazzScheduleService.createClazzScheduleByDTO(createDTO);

            /* Create Session */
            List<SessionCreateDTO> sessionCreateDTOList = createDTO.getSessionCreateDTOList();

            if (!ObjectUtils.isEmpty(sessionCreateDTOList)) {
                sessionCreateDTOList =
                        sessionCreateDTOList.stream()
                                .peek(sessionCreateDTO -> {
                                    sessionCreateDTO.setScheduleId(clazzScheduleReadDTO.getId());
                                    sessionCreateDTO.setCreatedBy(userDTO.getId()); })
                                .toList();

                sessionService.createBulkSessionByDTO(sessionCreateDTOList);
            }
        } catch (Exception e) {
            e.printStackTrace();

            redirect.addFlashAttribute("mess", e.getMessage());

            return "redirect:/add-schedule"+"?id="+clazzId;

//            return "redirect:/schedule-clazz";
        }

        return "redirect:/schedule-clazz";
    }


    /* =================================================== READ ===================================================== */
    @GetMapping("/schedule-clazz")
    public String scheduleListPage(
            Model model,
            @RequestParam(value = "pageNo", required = false) Integer pageNo,
            @ModelAttribute("mess") String mess,
            @SessionAttribute(name = "user", required = false) UserReadDTO userDTO) {

        if (userDTO == null) {
//            redirect.addFlashAttribute("mess", "Làm ơn đăng nhập");
            return "redirect:/index";
        }

        try {
            Page<ClazzReadDTO> dtoPage;
            if (userDTO.getRoleId().equals(ROLE_STUDENT)) {
//                redirect.addFlashAttribute("mess", "bạn không đủ quyền");
                return "redirect:/index";
            } else if (userDTO.getRoleId().equals(ROLE_TEACHER) || userDTO.getRoleId().equals(ROLE_ADMIN)) {
                if (pageNo == null || pageNo < 0) {
                    pageNo = 0;
                }

                Pageable paging = miscUtil.makePaging(pageNo, 10, "id", true);
                dtoPage = clazzService.getPageAllDTO(paging,
                        List.of(Status.DELETED),
                        false,
                        List.of(CLAZZ_SCHEDULE, ROOM_NAME, SCHEDULE_CAT));

                if (dtoPage != null) {
                    model.addAttribute("localDateNow", LocalDate.now());
                    model.addAttribute("clazzList", dtoPage.getContent());
                    model.addAttribute("pageNo", dtoPage.getPageable().getPageNumber());
                    model.addAttribute("pageTotal", dtoPage.getTotalPages());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMsg", "Server error, please try again later");
        }
        model.addAttribute("mess", mess);

        return "schedule/list-schedule";
    }


    /* =================================================== UPDATE =================================================== */
    @GetMapping("/edit-schedule")
    public String editSchedulePage(
            RedirectAttributes redirect,
            Model model,
            @RequestParam("id") Long clazzId,
            @ModelAttribute("mess") String mess,
            @SessionAttribute(name = "user", required = false) UserReadDTO userDTO) {
        //check login
        if (userDTO == null ) {
            redirect.addFlashAttribute("mess", "Làm ơn đăng nhập");
            return "redirect:/index";
        }

        if (!userDTO.getRoleId().equals(ROLE_ADMIN)) {
            redirect.addFlashAttribute("mess", "bạn không đủ quyền");
            return "redirect:/index";
        }

        try {
            /* Clazz */
            ClazzReadDTO clazzReadDTO =
                    clazzService.getDTOById(
                            clazzId,
                            List.of(Status.DELETED),
                            false,
                            List.of(CLAZZ_SCHEDULE, SCHEDULE_CAT, COURSE, CENTER));
            model.addAttribute("clazz", clazzReadDTO);

            /* ClazzSchedule */
            model.addAttribute("schedule", clazzReadDTO.getClazzSchedule());

            /* Room List */
            CenterReadDTO centerReadDTO = clazzReadDTO.getCenter();
            List<RoomReadDTO> roomReadDTOList = roomService.getAllDTOByCenterId(centerReadDTO.getId(), null);
            model.addAttribute("roomList", roomReadDTOList);

            /*Schedule Category List*/
            List<ScheduleCategoryReadDTO> scheduleCateDTOList = scheduleCateService.getAllDTO();
            model.addAttribute("scheduleCateList", scheduleCateDTOList);

        } catch (Exception e) {
            e.printStackTrace();
            /* Log Error or return error msg */
        }

        return "schedule/edit-schedule";
    }

    @PostMapping("/edit-schedule")
    public String editClazzSchedule(
            @RequestParam("id") Long requestId,
            @ModelAttribute ClazzScheduleUpdateDTO updateDTO,
//            @ModelAttribute List<SessionUpdateDTO> sessionUpdateDTOList,
            @SessionAttribute(value = "user", required = false) UserReadDTO userDTO,
            RedirectAttributes redirect) throws Exception {

        //check login
        if (ObjectUtils.isEmpty(userDTO)) {
            redirect.addFlashAttribute("mess", "Làm ơn đăng nhập");
            return "redirect:/index";
        }

        if (!userDTO.getRoleId().equals(Constants.ROLE_ADMIN)) {
            redirect.addFlashAttribute("mess", "bạn không đủ quyền");
            return "redirect:/index";
        }

        try {
            ClazzSchedule oldSchedule = clazzScheduleService.getById(updateDTO.getId());

            /* Update Schedule */
            ClazzScheduleReadDTO clazzScheduleReadDTO = clazzScheduleService.updateClazzScheduleByDTO(updateDTO);

            if (!oldSchedule.getSlot().equals(updateDTO.getSlot())
                    || !oldSchedule.getScheduleCategoryId().equals(updateDTO.getScheduleCategoryId())) {
//                /* Delete future Session */
//                sessionService.deleteAllByScheduleIdAndAfter(oldSchedule.getId(), LocalDateTime.now());

                /* Update Session */
//                sessionUpdateDTOList =
//                        sessionUpdateDTOList.stream()
//                                .peek(sessionCreateDTO -> sessionCreateDTO.setScheduleId(clazzScheduleReadDTO.getId()))
//                                .toList();

//                sessionService.updateBulkSessionByDTO(sessionUpdateDTOList);
                sessionService.createBulkSessionByDTO(updateDTO.getSessionCreateDTOList());
            }


        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/edit-schedule?id="+requestId;
        }

        return "redirect:/schedule-clazz";
    }

    /* =================================================== DELETE =================================================== */


    /* =================================================== API ====================================================== */
    @GetMapping("/api/check-conflict-schedule")
    @ResponseBody
    public Boolean isConflictSchedule(
            @RequestParam Long roomId,
            @RequestParam Long scheduleCaId,
            @RequestParam Integer slot,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate
    ) {
        try {
            List<ClazzSchedule> conflictSchedule =
                    clazzScheduleService.getAllByRoomIdAndScheduleCaIdAndSlotAndInRange(
                            roomId, scheduleCaId, slot, startDate, endDate);

            return conflictSchedule != null;
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

}
