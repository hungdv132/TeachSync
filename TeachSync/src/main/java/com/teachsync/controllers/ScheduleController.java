package com.teachsync.controllers;


import com.teachsync.dtos.center.CenterReadDTO;
import com.teachsync.dtos.clazz.ClazzReadDTO;
import com.teachsync.dtos.clazzSchedule.ClazzScheduleCreateDTO;
import com.teachsync.dtos.clazzSchedule.ClazzScheduleReadDTO;
import com.teachsync.dtos.clazzSchedule.ClazzScheduleUpdateDTO;
import com.teachsync.dtos.room.RoomReadDTO;
import com.teachsync.dtos.scheduleCategory.ScheduleCaReadDTO;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
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

//    @Autowired
//    private CourseService courseService;
//
//    @Autowired
//    private CenterService centerService;
//
//    @Autowired
//    private SemesterService semesterService;
//
//    @Autowired
//    private CourseSemesterService courseSemesterService;

    @Autowired
    private ScheduleCateService scheduleCateService;

    @Autowired
    private MiscUtil miscUtil;


    /* =================================================== CREATE =================================================== */
    @GetMapping("/add-schedule")
    public String addSchedulePage(
            Model model,
            @RequestParam("id") Long clazzId,
            @ModelAttribute("mess") String mess,
            @SessionAttribute(name = "user", required = false) UserReadDTO userDTO) {
//        if (userDTO == null ) {
//            return "redirect:/index";
//        }
//
//        if (!userDTO.getRoleId().equals(ROLE_ADMIN)) {
//            return "redirect:/index";
//        }

        try {
            /* Clazz */
            ClazzReadDTO clazzReadDTO =
                    clazzService.getDTOById(clazzId,
                            List.of(CLAZZ_SCHEDULE, COURSE_SEMESTER, COURSE, SEMESTER, CENTER, SCHEDULE_CAT));
            model.addAttribute("clazz", clazzReadDTO);

            /* Semester (max, min for startDtae & endDate) */
            model.addAttribute("semester", clazzReadDTO.getCourseSemester().getSemester());

            CenterReadDTO centerReadDTO = clazzReadDTO.getCourseSemester().getCenter();

            /* Room List */
            List<RoomReadDTO> roomReadDTOList = roomService.getAllDTOByCenterId(centerReadDTO.getId(), null);
            model.addAttribute("roomList", roomReadDTOList);

            /*Schedule Category List*/
            List<ScheduleCaReadDTO> scheduleCateDTOList = scheduleCateService.getAllDTO();
            model.addAttribute("scheduleCateList", scheduleCateDTOList);

        } catch (Exception e) {
            e.printStackTrace();
            /* Log Error or return error msg */
        }

        return "schedule/add-schedule";
    }

    @PostMapping("/add-schedule")
    public String addClazzSchedule(
            @ModelAttribute ClazzScheduleCreateDTO createDTO,
            @ModelAttribute ArrayList<SessionCreateDTO> sessionCreateDTOList,
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
            /* Create Schedule */
            ClazzScheduleReadDTO clazzScheduleReadDTO = clazzScheduleService.createClazzScheduleByDTO(createDTO);

            /* Create Session */
            sessionCreateDTOList =
                    (ArrayList<SessionCreateDTO>) sessionCreateDTOList.stream()
                            .peek(sessionCreateDTO -> sessionCreateDTO.setScheduleId(clazzScheduleReadDTO.getId()))
                            .toList();

            sessionService.createBulkSessionByDTO(sessionCreateDTOList);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/add-schedule";
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
//            redirect.addAttribute("mess", "Làm ơn đăng nhập");
            return "redirect:/index";
        }

        try {
            Page<ClazzReadDTO> dtoPage;
            if (userDTO.getRoleId().equals(ROLE_STUDENT)) {
//                redirect.addAttribute("mess", "bạn không đủ quyền");
                return "redirect:/index";
            } else if (userDTO.getRoleId().equals(ROLE_TEACHER) || userDTO.getRoleId().equals(ROLE_ADMIN)) {
                if (pageNo == null || pageNo < 0) {
                    pageNo = 0;
                }

                Pageable paging = miscUtil.makePaging(pageNo, 10, "id", true);
                dtoPage = clazzService.getPageDTOAll(paging, List.of(CLAZZ_SCHEDULE, ROOM_NAME, SCHEDULE_CAT));

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
            Model model,
            @RequestParam("id") Long clazzId,
            @ModelAttribute("mess") String mess,
            @SessionAttribute(name = "user", required = false) UserReadDTO userDTO) {
        if (userDTO == null ) {
            return "redirect:/index";
        }

        if (!userDTO.getRoleId().equals(ROLE_ADMIN)) {
            return "redirect:/index";
        }

        try {
            /* Clazz */
            ClazzReadDTO clazzReadDTO =
                    clazzService.getDTOById(clazzId, List.of(CLAZZ_SCHEDULE, COURSE_SEMESTER, SEMESTER, CENTER, SCHEDULE_CAT));
            model.addAttribute("clazz", clazzReadDTO);

            /* Semester (max, min for startDtae & endDate) */
            model.addAttribute("semester", clazzReadDTO.getCourseSemester().getSemester());

            /* ClazzSchedule */
            model.addAttribute("schedule", clazzReadDTO.getClazzSchedule());


            CenterReadDTO centerReadDTO = clazzReadDTO.getCourseSemester().getCenter();

            /* Room List */
            List<RoomReadDTO> roomReadDTOList = roomService.getAllDTOByCenterId(centerReadDTO.getId(), null);
            model.addAttribute("roomList", roomReadDTOList);

            /*Schedule Category List*/
            List<ScheduleCaReadDTO> scheduleCateDTOList = scheduleCateService.getAllDTO();
            model.addAttribute("scheduleCateList", scheduleCateDTOList);

        } catch (Exception e) {
            e.printStackTrace();
            /* Log Error or return error msg */
        }

        return "schedule/edit-schedule";
    }

    @PostMapping("/edit-schedule")
    public String editClazzSchedule(
            @ModelAttribute ClazzScheduleUpdateDTO updateDTO,
            @ModelAttribute List<SessionUpdateDTO> sessionUpdateDTOList,
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
            ClazzSchedule oldSchedule = clazzScheduleService.getById(updateDTO.getId());

            /* Update Schedule */
            ClazzScheduleReadDTO clazzScheduleReadDTO = clazzScheduleService.updateClazzScheduleByDTO(updateDTO);

            if (!oldSchedule.getSlot().equals(updateDTO.getSlot())
                    || !oldSchedule.getSchedulecaId().equals(updateDTO.getSchedulecaId())) {
//                /* Delete future Session */
//                sessionService.deleteAllByScheduleIdAndAfter(oldSchedule.getId(), LocalDateTime.now());

                /* Update Session */
//                sessionUpdateDTOList =
//                        sessionUpdateDTOList.stream()
//                                .peek(sessionCreateDTO -> sessionCreateDTO.setScheduleId(clazzScheduleReadDTO.getId()))
//                                .toList();

                sessionService.updateBulkSessionByDTO(sessionUpdateDTOList);
            }


        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/edit-schedule";
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
