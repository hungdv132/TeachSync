package com.teachsync.controllers;

import com.teachsync.dtos.center.CenterReadDTO;
import com.teachsync.dtos.clazz.ClazzCreateDTO;
import com.teachsync.dtos.clazz.ClazzReadDTO;
import com.teachsync.dtos.clazz.ClazzUpdateDTO;
import com.teachsync.dtos.clazzSchedule.ClazzScheduleReadDTO;
import com.teachsync.dtos.clazzTest.ClazzTestReadDTO;
import com.teachsync.dtos.course.CourseReadDTO;
import com.teachsync.dtos.homework.HomeworkReadDTO;
import com.teachsync.dtos.news.NewsReadDTO;
import com.teachsync.dtos.semester.SemesterReadDTO;
import com.teachsync.dtos.staff.StaffReadDTO;
import com.teachsync.dtos.test.TestReadDTO;
import com.teachsync.dtos.user.UserReadDTO;
import com.teachsync.entities.*;
import com.teachsync.repositories.ClazzMemberRepository;
import com.teachsync.repositories.ClazzTestRepository;
import com.teachsync.repositories.MemberTestRecordRepository;
import com.teachsync.repositories.TestRepository;
import com.teachsync.services.center.CenterService;
import com.teachsync.services.clazz.ClazzService;
import com.teachsync.services.clazzMember.ClazzMemberService;
import com.teachsync.services.clazzSchedule.ClazzScheduleService;
import com.teachsync.services.course.CourseService;
import com.teachsync.services.courseSemester.CourseSemesterService;
import com.teachsync.services.homework.HomeworkService;
import com.teachsync.services.news.NewsService;
import com.teachsync.services.request.RequestService;
import com.teachsync.services.semester.SemesterService;
import com.teachsync.services.staff.StaffService;
import com.teachsync.utils.Constants;
import com.teachsync.utils.MiscUtil;
import com.teachsync.utils.enums.RequestType;
import com.teachsync.utils.enums.Status;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.teachsync.utils.Constants.*;
import static com.teachsync.utils.enums.DtoOption.*;
import static com.teachsync.utils.enums.Status.*;

@Controller
public class ClazzController {
    @Autowired
    private ClazzService clazzService;

    @Autowired
    private ClazzMemberService clazzMemberService;
    @Autowired
    private ClazzScheduleService clazzScheduleService;
    @Autowired
    private RequestService requestService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private CenterService centerService;
    @Autowired
    private StaffService staffService;
    @Autowired
    private TestRepository testRepository;
    @Autowired
    private ClazzTestRepository clazzTestRepository;
    @Autowired
    private MemberTestRecordRepository memberTestRecordRepository;
    @Autowired
    private ClazzMemberRepository clazzMemberRepository;

    @Autowired
    private MiscUtil miscUtil;

    @Autowired
    NewsService newsService;

    @Autowired
    HomeworkService homeworkService;

    /* =================================================== API ====================================================== */

    /**
     * Return JSON
     */
    @GetMapping("/api/clazz")
    @ResponseBody
    public Map<String, Object> getClazzList(
            @RequestParam Long courseId,
            @RequestParam Long centerId) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<ClazzReadDTO> clazzDTOList =
                    clazzService.getAllDTOByCourseIdAndCenterId(
                            courseId, 
                            centerId, 
                            List.of(OPENED),
                            true,
                            null);

            response.put("clazzList", clazzDTOList);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    @GetMapping("/api/clazz-detail")
    @ResponseBody
    public Map<String, Object> getClazzDetail(
            @RequestParam Long clazzId) {
        Map<String, Object> response = new HashMap<>();
        try {
            ClazzReadDTO clazzDTO =
                    clazzService.getDTOById(
                            clazzId,
                            List.of(OPENED),
                            true,
                            List.of(MEMBER_LIST, STAFF, USER,
                                    CLAZZ_SCHEDULE, SCHEDULE_CAT, ROOM_NAME));

            response.put("clazz", clazzDTO);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    @GetMapping("/api/check-clazz/status")
    @ResponseBody
    public Map<String, Object> checkClazzStatus(
            @RequestParam("clazzId") Long clazzId,
            @RequestParam("status") Status newStatus,
            @SessionAttribute("user") UserReadDTO userDTO) {

        Map<String, Object> response = new HashMap<>();

        try {
            boolean error = false;
            String showBtn = null;
            Long userId = userDTO.getId();
            String message = null;

            ClazzReadDTO clazzDTO =
                    clazzService.getDTOById(
                            clazzId,
                            List.of(DELETED),
                            false,
                            List.of(CLAZZ_SCHEDULE));
            if (clazzDTO == null) {
                /* No valid Clazz found */
                error = true;
                message = "Lỗi kiểm tra. Không tìm thấy Lớp Học nào với id: " + clazzId;
            } else {
                Status oldStatus = clazzDTO.getStatus();

                switch (oldStatus) {
                    case DESIGNING -> {
                        if (newStatus.equals(AWAIT_REVIEW)) {
                            if (clazzDTO.getClazzSchedule() == null) {
                                error = true;
                                message = "Lớp Học đang thiếu Lịch Học để hoàn thành thiết kế";
                                showBtn = "addSchedule";
                            }
                        }
                    }

                    case OPENED -> {
                        if (newStatus.equals(CLOSED)) {
                            List<Request> pendingRequestList =
                                    requestService.getAllByClazzIdAndRequestType(
                                            clazzId,
                                            RequestType.ENROLL,
                                            List.of(PENDING_PAYMENT, AWAIT_CONFIRM),
                                            true);

                            List<ClazzMember> memberList = clazzMemberService.getAllByClazzId(clazzId);

                            if (memberList.size() < clazzDTO.getMinCapacity()) {
                                LocalDate date = LocalDate.now();
                                date = date.minusDays(5);
                                /* 5 day before now */

                                /* If Clazz only have 5 day till Start */
                                if (!clazzDTO.getClazzSchedule().getStartDate().isBefore(date)) {
                                    if (!ObjectUtils.isEmpty(memberList)
                                            || !ObjectUtils.isEmpty(pendingRequestList)) {
                                        error = true;
                                        showBtn = "rejectRequest";
                                        message = "Lớp Học đang thiếu so với số tối thiểu để mở lớp. Xác nhận đóng Lớp?";
                                    } else {
                                        message = "Hiện Lớp chưa có thành viên hay đơn xin nhập học. Xác nhận đóng Lớp?";
                                    }
                                }
                            } else {
                                if (!ObjectUtils.isEmpty(memberList)) {
                                    error = true;
                                    showBtn = "rejectRequest";
                                    message = "Xác nhận đóng Lớp?";
                                } else if (!ObjectUtils.isEmpty(pendingRequestList)) {
                                    error = true;
                                    showBtn = "rejectRequest";
                                    message = "Xác nhận đóng Lớp?";
                                } else {
                                    message = "Hiện Lớp không có thành viên hay đơn xin nhập học. Xác nhận đóng Lớp?";
                                }
                            }
                        }
                    }

                    case ONGOING -> {
                        switch (newStatus) {
                            case SUSPENDED -> {
                                message = "Xác nhận tạm ngưng Lớp?";
                            }

                            case CLOSED -> {
                                LocalDate date = LocalDate.now();
                                if (clazzDTO.getClazzSchedule().getEndDate().isAfter(date)) {
                                    message = "Xác nhận đóng Lớp?";
                                } else {
                                    error = true;
                                    message = "Hiện Lớp chưa đến ngày kết thúc. Chưa thể đóng Lớp.";
                                }
                            }
                        }
                    }
                }
            }

            response.put("error", error);
            response.put("message", message);
            response.put("showBtn", showBtn);

        } catch (Exception e) {
            e.printStackTrace();

            response.put("errorMsg", e.getMessage());

            response.put("error", true);
        }

        return response;
    }

    /* TODO: move to StaffController */
    @GetMapping("/api/staff")
    @ResponseBody
    public Map<String, Object> getStaffList(
            @RequestParam Long centerId) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<StaffReadDTO> staffDTOList =
                    staffService.getAllDTOByCenterId(centerId, List.of(USER));
            response.put("staffList", staffDTOList);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }


    /* =================================================== CREATE =================================================== */
    @GetMapping("/add-clazz")
    public String addClazzPage(
            Model model,
            RedirectAttributes redirect,
            @SessionAttribute(value = "user", required = false) UserReadDTO userDTO) {

        if (Objects.isNull(userDTO)) {
            redirect.addFlashAttribute("mess", "Làm ơn đăng nhập");
            return "redirect:/index";
        }

        if (!userDTO.getRoleId().equals(ROLE_ADMIN)) {
            redirect.addFlashAttribute("mess", "Bạn không đủ quyền");
            return "redirect:/index";
        }

        try {
            /* List Course (môn nào) */
            List<CourseReadDTO> courseDTOList =
                    courseService.getAllDTO(
                            List.of(OPENED),
                            true,
                            null);
            model.addAttribute("courseList", courseDTOList);

            /* List Center (Cơ sở nào) */
            List<CenterReadDTO> centerDTOList =
                    centerService.getAllDTO(null);
            model.addAttribute("centerList", centerDTOList);

            /* List Staff (Ai dạy) */
            List<StaffReadDTO> staffDTOList;
            /* Suy ra từ Center đầu tiên trong list */
            staffDTOList =
                    staffService.getAllDTOByCenterId(centerDTOList.get(0).getId(), List.of(USER));

            model.addAttribute("staffList", staffDTOList);

        } catch (Exception e) {
            e.printStackTrace();
            /* Log Error or return error msg */
        }

        return "clazz/add-clazz";
    }

    @PostMapping("/add-clazz")
    public String addClazz(
            RedirectAttributes redirect,
            @ModelAttribute ClazzCreateDTO createDTO,
            @SessionAttribute(value = "user", required = false) UserReadDTO userDTO) throws Exception {

        //check login
        if (ObjectUtils.isEmpty(userDTO)) {
            redirect.addFlashAttribute("mess", "Làm ơn đăng nhập");
            return "redirect:/index";
        }

        if (!userDTO.getRoleId().equals(Constants.ROLE_ADMIN)) {
            redirect.addFlashAttribute("mess", "bạn không đủ quyền");
            return "redirect:/index";
        }

        ClazzReadDTO clazzDTO;

        try {
            createDTO.setCreatedBy(userDTO.getId());

            clazzDTO = clazzService.createClazzByDTO(createDTO);

        } catch (Exception e) {
            e.printStackTrace();
            redirect.addFlashAttribute("mess", e.getMessage());
            return "redirect:/clazz";
        }

        redirect.addFlashAttribute("mess", "Tạo lớp học thành công");

        return "redirect:/clazz-detail" + "?id=" + clazzDTO.getId();
    }


    /* =================================================== READ ===================================================== */
    @GetMapping("/clazz")
    public String clazzListPage(
            RedirectAttributes redirect,
            Model model,
            @ModelAttribute("mess") String mess,
            @RequestParam(value = "pageNo", required = false) Integer pageNo,
            @SessionAttribute(value = "user", required = false) UserReadDTO userDTO) {
        try {
            Page<ClazzReadDTO> dtoPage = null;


            if (Objects.isNull(userDTO)) {
                redirect.addFlashAttribute("mess", "Làm ơn đăng nhập");
                return "redirect:/index";
            }

//            if (!userDTO.getRoleId().equals(ROLE_ADMIN)) {
//                redirect.addFlashAttribute("mess", "Bạn không đủ quyền");
//                return "redirect:/index";
//            }

            if (pageNo == null || pageNo < 0) {
                pageNo = 0;
            }

            Pageable pageable = miscUtil.makePaging(pageNo, 10, "status", true);

            Long roleId = userDTO.getRoleId();
            if (roleId.equals(Constants.ROLE_STUDENT)) {
                List<ClazzMember> clazzMemberList = clazzMemberService.getAllByUserId(userDTO.getId());
                Set<Long> clazzIdSet =
                        clazzMemberList.stream()
                                .map(ClazzMember::getClazzId)
                                .collect(Collectors.toSet());

                /* TODO: lọc clazz nào học xong rồi ẩn hay hiện hay sort */

                dtoPage = clazzService.getPageAllDTOByIdIn(
                        pageable,
                        clazzIdSet,
                        List.of(OPENED, ONGOING, SUSPENDED, CLOSED),
                        true,
                        List.of(COURSE, COURSE_NAME, COURSE_ALIAS, CENTER));

            } else if (roleId.equals(Constants.ROLE_TEACHER)) {
                List<Staff> staffList = staffService.getAllByUserId(userDTO.getId());
                Set<Long> staffIdSet = staffList.stream().map(BaseEntity::getId).collect(Collectors.toSet());

                /* TODO: lọc clazz nào dạy xong rồi ẩn hay hiện hay sort */

                dtoPage = clazzService.getPageAllDTOByStaffIdIn(
                        pageable,
                        staffIdSet,
                        List.of(OPENED, ONGOING, SUSPENDED, CLOSED),
                        true,
                        List.of(COURSE_SEMESTER, COURSE_NAME, COURSE_ALIAS, CENTER));

            } else if (roleId.equals(Constants.ROLE_ADMIN)) {
                dtoPage = clazzService.getPageAllDTO(
                        pageable,
                        List.of(DELETED),
                        false,
                        List.of(COURSE_SEMESTER, COURSE_NAME, COURSE_ALIAS, CENTER));

            }

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
            RedirectAttributes redirect,
            Model model,
            @RequestParam(value = "id", required = false) Long clazzId,
            @SessionAttribute(value = "user", required = false) UserReadDTO userDTO) {

        //check login
        if (ObjectUtils.isEmpty(userDTO)) {
            redirect.addFlashAttribute("mess", "Làm ơn đăng nhập");
            return "redirect:/index";
        }

        Long roleId = userDTO.getRoleId();
        if (!List.of(ROLE_ADMIN, ROLE_TEACHER, ROLE_STUDENT).contains(roleId)) {
            redirect.addFlashAttribute("mess", "bạn không đủ quyền");
            return "redirect:/index";
        }

        try {
            ClazzReadDTO clazzDTO =
                    clazzService.getDTOById(
                            clazzId,
                            List.of(DELETED),
                            false,
                            List.of(STAFF, USER, COURSE_NAME,
                                    COURSE_ALIAS, CENTER, TEST_LIST,
                                    CLAZZ_SCHEDULE, SCHEDULE_CAT));
            if (clazzDTO == null) {
                redirect.addFlashAttribute("mess",
                        "Không tìm thấy bất cứ Lớp nào với id: " + clazzId);

                return "redirect:/clazz";
            }
            model.addAttribute("clazz", clazzDTO);

            if (roleId.equals(ROLE_STUDENT)) {
                ClazzMember member =
                        clazzMemberService.getByClazzIdAndUserId(clazzId, userDTO.getId());

                if (member == null) {
                    redirect.addFlashAttribute("mess",
                            "Bạn không phải là học sinh của Lớp này nên không được quyền xem thông tin của Lớp.");

                    return "redirect:/clazz";
                }
            }

            //get news of class
//            List<NewsReadDTO> newsReadDTOList = newsService.getAllNewsByClazz(clazzDTO.getId());


        } catch (Exception e) {
            e.printStackTrace();

            redirect.addFlashAttribute("mess", e.getMessage());

            return "redirect:/index";
        }

        return "clazz/clazz-detail";
    }


    /* =================================================== UPDATE =================================================== */
    @GetMapping("/edit-clazz")
    public String editClazzPage(
            Model model,
            RedirectAttributes redirect,
            @RequestParam("id") Long clazzId,
            @SessionAttribute(value = "user", required = false) UserReadDTO userDTO) {

        if (Objects.isNull(userDTO)) {
            redirect.addFlashAttribute("mess", "Làm ơn đăng nhập");
            return "redirect:/index";
        }

        if (!userDTO.getRoleId().equals(ROLE_ADMIN)) {
            redirect.addFlashAttribute("mess", "Bạn không đủ quyền");
            return "redirect:/index";
        }

        try {
            /* Clazz by id */
            ClazzReadDTO clazzReadDTO =
                    clazzService.getDTOById(
                            clazzId,
                            List.of(DELETED),
                            false,
                            List.of(COURSE, CENTER, STAFF, USER));

            model.addAttribute("clazz", clazzReadDTO);

            /* List Course (môn nào) */
            List<CourseReadDTO> courseDTOList =
                    courseService.getAllDTO(
                            List.of(OPENED),
                            true,
                            null);

            model.addAttribute("courseList", courseDTOList);

            /* List Center (Cơ sở nào) */
            List<CenterReadDTO> centerDTOList =
                    centerService.getAllDTO(null);
            model.addAttribute("centerList", centerDTOList);

            /* List Staff (Ai dạy) */
            List<StaffReadDTO> staffDTOList;
            /* Suy ra từ Center đầu tiên trong list */
            staffDTOList =
                    staffService.getAllDTOByCenterId(
                            clazzReadDTO.getCenterId(),
                            List.of(USER));

            model.addAttribute("staffList", staffDTOList);

        } catch (Exception e) {
            e.printStackTrace();
            /* Log Error or return error msg */
        }

        return "clazz/edit-clazz";
    }

    @PostMapping("/edit-clazz")
    public String editClazz(
            @ModelAttribute ClazzUpdateDTO updateDTO,
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

        ClazzReadDTO clazzDTO;

        try {
            updateDTO.setUpdatedBy(userDTO.getId());

            clazzDTO = clazzService.updateClazzByDTO(updateDTO);

        } catch (Exception e) {
            e.printStackTrace();
            redirect.addFlashAttribute("mess", e.getMessage());
            return "redirect:/clazz";
        }

        redirect.addFlashAttribute("mess", "Sửa lớp học thành công");

        return "redirect:/clazz-detail" + "?id=" + clazzDTO.getId();
    }


    /* =================================================== DELETE =================================================== */
    @GetMapping("/delete-clazz")
    public String deleteClazz(
            @SessionAttribute(value = "user", required = false) UserReadDTO userDTO,
            HttpServletRequest request,
            Model model,
            RedirectAttributes redirect,
            @RequestParam("id") Long clazzId){
        //check login
        if (ObjectUtils.isEmpty(userDTO)) {
            redirect.addFlashAttribute("mess", "Làm ơn đăng nhập");
            return "redirect:/";
        }

        if (!userDTO.getRoleId().equals(Constants.ROLE_ADMIN)) {
            redirect.addFlashAttribute("mess", "bạn không đủ quyền");
            return "redirect:/";
        }

        try {
            if (clazzService.deleteClazz(clazzId)) {
                redirect.addFlashAttribute("mess", "Xóa Lớp Học thành công.");
            } else {
                redirect.addFlashAttribute("mess", "Xóa Lớp Học thất bại");
            }

            return "redirect:/clazz";

        } catch (Exception e) {
            e.printStackTrace();

            redirect.addFlashAttribute("mess", e.getMessage());

            return "redirect:/clazz";
        }
    }


    @PostMapping("/finish-class")
    public String finishClass(
            Model model,
            @RequestParam(value = "id", required = false) Long clazzId,
            @RequestParam(value = "courseId", required = false) Long courseId) throws Exception {
        List<ClazzMember> clazzMemberList = clazzMemberService.getAllByClazzId(clazzId);
        for (ClazzMember clazzMember : clazzMemberList) {
            List<Test> listTest = testRepository.findAllByCourseIdAndStatusNot(courseId, Status.DELETED);
            long totalScore = 0;
            long totalWeight = 0;
            for (Test test : listTest) {
                totalWeight = totalWeight + test.getTestWeight();
            }
            for (Test test : listTest) {

                ClazzTest clazzTest = clazzTestRepository.findByClazzIdAndTestIdAndStatusNot(clazzId, test.getId(), Status.DELETED).orElse(null);
                if (clazzTest == null) {
                    System.out.println("Bài test chưa bắt đầu. Không cho kết thúc");
                    return "redirect:/";
                }
                MemberTestRecord memberTestRecord = memberTestRecordRepository.findByMemberIdAndClazzTestIdAndStatusNot(clazzMember.getId(), clazzTest.getTestId(), Status.DELETED).orElse(null);
                if (memberTestRecord.getScore() < test.getMinScore()) {
                    clazzMember.setIsPassed(false);
                }
                totalScore = (long) (totalScore + memberTestRecord.getScore() * (test.getTestWeight() / totalWeight));

            }
            clazzMember.setScore((double) totalScore);
            if (totalScore < 5) {
                clazzMember.setIsPassed(false);
            } else if (clazzMember.getIsPassed() == null && totalScore >= 5){
                clazzMember.setIsPassed(true);
            }
                clazzMemberRepository.save(clazzMember);
        }
        return "redirect:/";
    }
}
