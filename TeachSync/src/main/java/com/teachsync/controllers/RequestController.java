package com.teachsync.controllers;

import com.teachsync.dtos.center.CenterReadDTO;
import com.teachsync.dtos.clazz.ClazzReadDTO;
import com.teachsync.dtos.clazzMember.ClazzMemberCreateDTO;
import com.teachsync.dtos.clazzSchedule.ClazzScheduleReadDTO;
import com.teachsync.dtos.course.CourseReadDTO;
import com.teachsync.dtos.payment.PaymentCreateDTO;
import com.teachsync.dtos.priceLog.PriceLogReadDTO;
import com.teachsync.dtos.request.RequestCreateDTO;
import com.teachsync.dtos.request.RequestReadDTO;
import com.teachsync.dtos.request.RequestUpdateDTO;
import com.teachsync.dtos.user.UserReadDTO;
import com.teachsync.entities.*;
import com.teachsync.services.center.CenterService;
import com.teachsync.services.clazz.ClazzService;
import com.teachsync.services.clazzMember.ClazzMemberService;
import com.teachsync.services.clazzSchedule.ClazzScheduleService;
import com.teachsync.services.course.CourseService;
import com.teachsync.services.payment.PaymentService;
import com.teachsync.services.priceLog.PriceLogService;
import com.teachsync.services.request.RequestService;
import com.teachsync.services.session.SessionService;
import com.teachsync.utils.Constants;
import com.teachsync.utils.MiscUtil;
import com.teachsync.utils.enums.DtoOption;
import com.teachsync.utils.enums.PaymentType;
import com.teachsync.utils.enums.RequestType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import static com.teachsync.utils.enums.DtoOption.*;
import static com.teachsync.utils.enums.RequestType.*;
import static com.teachsync.utils.enums.Status.*;

@Controller
public class RequestController {
    @Autowired
    private CourseService courseService;
    @Autowired
    private CenterService centerService;
    @Autowired
    private PriceLogService priceLogService;
    @Autowired
    private ClazzService clazzService;
    @Autowired
    private ClazzScheduleService clazzScheduleService;
    @Autowired
    private SessionService sessionService;
    @Autowired
    private ClazzMemberService clazzMemberService;
    @Autowired
    private RequestService requestService;
    @Autowired
    private PaymentService paymentService;

    @Autowired
    private MiscUtil miscUtil;


    /* =================================================== API ====================================================== */
    @GetMapping("/api/check-request/enroll")
    @ResponseBody
    public Map<String, Object> checkRequestEnroll(
            @RequestParam(value = "courseId",required = false) Long courseId,
            @RequestParam(value = "clazzId",required = false) Long clazzId,
            @SessionAttribute("user") UserReadDTO userDTO) {

        Map<String, Object> response = new HashMap<>();

        try {
            boolean error = false;
            Long userId = userDTO.getId();

            /* Check Enroll Request Course vs current User's Course(s) */
            if (courseId != null) {
                String message = null;

                /* Target Course */
                Course targetCourse =
                        courseService.getById(
                                courseId,
                                List.of(OPENED),
                                true);
                /* Check if Course valid for Request */
                if (targetCourse == null) {
                    /* No valid Course found */
                    error = true;
                    message = "Lỗi kiểm tra. Không tìm thấy Khóa Học nào với id: " + courseId;
                } else {
                    /* Valid Course found */

                    /* Target Course's Clazz(es) */
                    Map<Long, Clazz> clazzIdClazzMap =
                            clazzService.mapIdClazzByCourseId(
                                    courseId,
                                    List.of(DELETED),
                                    false);

                    /* Check if Course have Clazz(es) */
                    if(!clazzIdClazzMap.isEmpty()) {
                        /* Have clazz(es) */

                        /* Current User's Clazz(es) */
                        List<ClazzMember> clazzMemberList =
                                clazzMemberService.getAllByClazzIdInAndUserId(clazzIdClazzMap.keySet(), userId);

                        /* Check if current User is CURRENTLY studying */
                        if (clazzMemberList != null) {
                            int ongoing = 0;
                            int waiting = 0;
                            int passed = 0;
                            Long tmpClazzId;
                            Clazz tmpClazz;

                            for (ClazzMember member : clazzMemberList) {
                                Boolean isPassed = member.getIsPassed();
                                tmpClazzId = member.getClazzId();
                                tmpClazz = clazzIdClazzMap.get(tmpClazzId);

                                /* Check if current User is CURRENTLY studying this Course */
                                if (isPassed == null) {
                                    switch (tmpClazz.getStatus()) {
                                        case OPENED -> {
                                            /* Đang chờ học */
                                            waiting++;
                                        }
                                        case ONGOING, SUSPENDED -> {
                                            /* Đang học */
                                            ongoing++;
                                        }
                                    }
                                } else if (isPassed) {
                                    /* Đã xong, học lại? */
                                    passed++;
                                }
                            }
                            if (waiting > 0) {
                                message = "Bạn ĐANG CHỜ học Khóa Học này, bạn không được phép LẠI đăng ký học Khóa Học này.";
                                error = true;
                            } else if(ongoing > 0) {
                                message = "Bạn ĐANG học Khóa Học này, bạn không được phép LẠI đăng ký học Khóa Học này.";
                                error = true;
                            } else if (passed > 0) {
                                message = "Bạn ĐÃ hoàn thành chương trình học cho Khóa Học này. Bạn có chắc muốn đãng ký học lại?";
                            }
                        }
                    }
                }

                response.put("courseMsg", message);
            }

            /* Check Enroll Request Clazz vs current User's Clazz(es) */
            if (clazzId != null) {
                String message = null;

                /* Target Clazz */
                ClazzReadDTO targetClazzDTO =
                    clazzService.getDTOById(
                            clazzId,
                            List.of(OPENED),
                            true,
                            List.of(CLAZZ_SCHEDULE));
                /* Check if Clazz valid for Request */
                if (targetClazzDTO == null) {
                    /* No valid Clazz found */
                    error = true;
                    message = "Lỗi kiểm tra. Không tìm thấy Lớp nào với id: " + clazzId;
                } else {
                    /* Valid Clazz found */

                    List<Request> requestList =
                            requestService.getAllByRequesterIdAndClazzIdAndRequestType(
                                    userId,
                                    clazzId,
                                    RequestType.ENROLL,
                                    List.of(PENDING_PAYMENT, AWAIT_CONFIRM),
                                    true);

                    /* Check if duplicate Request */
                    if (!ObjectUtils.isEmpty(requestList)) {
                        /* Duplicated Request */
                        error = true;
                        message = "Bạn đã nộp đơn loại: " + RequestType.ENROLL.getStringValueVie()
                                +" cho lớp: " + clazzId
                                +" đang chờ xét duyệt hoặc đã duyệt thành công.\n";
                    } else {
                        /* Valid Request */

                        /* Target Clazz's Schedule */
                        ClazzScheduleReadDTO targetScheduleDTO =
                                targetClazzDTO.getClazzSchedule();

                        /* Current User's Clazz(es) */
                        Map<Long, ClazzMember> clazzIdMemberMap =
                                clazzMemberService.mapClazzIdClazzMemberByUserId(userId);

                        /* Check if current User is CURRENTLY studying */
                        if (!clazzIdMemberMap.isEmpty()) {
                            /* Is Studying */

                            /* Current User's Clazz(es) that is not yet done */
                            List<ClazzReadDTO> clazzDTOList =
                                    clazzService.getAllDTOByIdIn(
                                            clazzIdMemberMap.keySet(),
                                            List.of(OPENED, ONGOING, SUSPENDED),
                                            true,
                                            List.of(CLAZZ_SCHEDULE, SCHEDULE_CAT));

                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                            StringBuilder messageBuilder = new StringBuilder();
                            for (ClazzReadDTO clazz : clazzDTOList) {
                                ClazzScheduleReadDTO scheduleDTO = clazz.getClazzSchedule();

                                /* Check if conflict in schedule */
                                if (scheduleDTO.isConflict(targetScheduleDTO)) {
                                    error = true;
                                    message = "Lớp đạng học: " + clazz.getClazzAlias()
                                            + " tiết: " + scheduleDTO.getSlot()
                                            + " vào: " + scheduleDTO.getScheduleCategory().getCategoryName()
                                            + " từ: " + scheduleDTO.getStartDate().format(formatter)
                                            + " đến: " + scheduleDTO.getEndDate().format(formatter)
                                            + "\n";
                                    messageBuilder.append(message);
                                }
                            }

                            /* Check if conflict in schedule */
                            if (!messageBuilder.isEmpty()) {
                                message = "Bạn có mâu thuẫn về thời gian học:\n"
                                        + "Lớp muốn học: " + targetClazzDTO.getClazzAlias()
                                        + " tiết: " + targetScheduleDTO.getSlot()
                                        + " vào: " + targetScheduleDTO.getScheduleCategory().getCategoryName()
                                        + " từ: " + targetScheduleDTO.getStartDate().format(formatter)
                                        + " đến: " + targetScheduleDTO.getEndDate().format(formatter)
                                        + "\n\tSo với\t\n"
                                        + messageBuilder.toString();
                            }

                        }
                    }
                }

                response.put("clazzMsg", message);
            }

            response.put("error", error);
        } catch (Exception e) {
            e.printStackTrace();

            response.put("errorMsg", e.getMessage());

            response.put("error", true);
        }

        return response;
    }


    /* =================================================== CREATE =================================================== */
    @GetMapping("/add-request")
    public String addRequestPage(
            RedirectAttributes redirect,
            Model model,
            @RequestHeader(value = "Referer", required = false) String referer,
            @SessionAttribute(name = "user", required = false) UserReadDTO userDTO) {

        /* Check login */
        if (userDTO == null) {
            redirect.addFlashAttribute("mess", "Làm ơn đăng nhập");
            return "redirect:/index";
        }

        if (!userDTO.getRoleId().equals(Constants.ROLE_STUDENT)) {
            /* TODO: Teacher send request */
            redirect.addFlashAttribute("mess", "Bạn không đủ quyền");

            if (referer != null) {
                /* Quay về trang cũ */
                return "redirect:" + referer;
            }

            return "redirect:/request";
        }

        /* Load data for choosing */
        try {
            /* List Course (môn nào) */
            List<CourseReadDTO> courseDTOList = courseService.getAllDTO(
                    List.of(DELETED),
                    false,
                    null);
            model.addAttribute("courseList", courseDTOList);

            /* List Center (Cơ sở nào) */
            List<CenterReadDTO> centerDTOList = centerService.getAllDTO(null);
            model.addAttribute("centerList", centerDTOList);

        } catch (Exception e) {
            e.printStackTrace();

            redirect.addFlashAttribute("mess", e.getMessage());

            if (referer != null) {
                return "redirect:" + referer;
            }

            return "redirect:/request";
        }

        return "request/add-request";
    }

    @PostMapping("/add-request/enroll")
    public String addRequestEnroll(
            RedirectAttributes redirect,
            Model model,
            @ModelAttribute RequestCreateDTO createDTO,
            @RequestHeader(value = "Referer", required = false) String referer,
            @SessionAttribute(name = "user", required = false) UserReadDTO userDTO) {

        /* Check login */
        if (userDTO == null) {
            redirect.addFlashAttribute("mess", "Làm ơn đăng nhập");
            return "redirect:/index";
        }

        if (!userDTO.getRoleId().equals(Constants.ROLE_STUDENT)) {
            redirect.addFlashAttribute("mess", "Bạn không đủ quyền");

            if (referer != null) {
                /* Quay về trang cũ */
                return "redirect:" + referer;
            }

            return "redirect:/request";
        }

        RequestReadDTO requestDTO = null;
        try {
            ClazzReadDTO clazzDTO =
                    clazzService.getDTOById(
                            createDTO.getClazzId(),
                            List.of(DELETED),
                            false,
                            List.of(COURSE_SEMESTER, COURSE_NAME, CENTER_NAME, SEMESTER_NAME));

            String requestName = "hoc sinh '" + userDTO.getId() +
                    "' xin hoc lop '" + clazzDTO.getId() +
                    "' mon '" + clazzDTO.getCourseId() + "'";
            createDTO.setRequestName(requestName);

            String requestDesc = "Học sinh '" + userDTO.getFullName() +
                    "' xin nhập học Lớp '" + clazzDTO.getClazzName() +
                    "' cho Khóa học '" + clazzDTO.getCourseName() +
                    "' tại Cơ Sở '" + clazzDTO.getCenterName() + "'.";
            createDTO.setRequestDesc(requestDesc);

            createDTO.setCreatedBy(userDTO.getId());
            createDTO.setStatus(PENDING_PAYMENT);

            requestDTO = requestService.createRequestByDTO(createDTO);

        } catch (Exception e) {
            e.printStackTrace();

            redirect.addFlashAttribute("mess", e.getMessage());
            if (referer != null) {
                return "redirect:" + referer;
            }
            return "redirect:/add-request";
        }

        return "redirect:/edit-request?id=" + requestDTO.getId();
    }


    /* =================================================== READ ===================================================== */
    @GetMapping("/request")
    public String requestListPage(
            RedirectAttributes redirect,
            Model model,
            @RequestParam(value = "pageNo", required = false) Integer pageNo,
            @RequestParam(value = "clazzId", required = false) Long clazzId,
            @RequestHeader(value = "Referer", required = false) String referer,
            @SessionAttribute(name = "user", required = false) UserReadDTO userDTO) {
        /* Check login */
        if (userDTO == null) {
            redirect.addFlashAttribute("mess", "Làm ơn đăng nhập");
            return "redirect:/index";
        }

        try {
            if (pageNo == null || pageNo < 0) {
                pageNo = 0;
            }

            Pageable pageable = miscUtil.makePaging(pageNo, 10, "id", true);

            Page<RequestReadDTO> requestReadDTOPage = null;

            Long roleId = userDTO.getRoleId();
            if (roleId.equals(Constants.ROLE_STUDENT)) {
                requestReadDTOPage =
                        requestService.getPageAllDTOByRequesterId(
                                pageable,
                                userDTO.getId(),
                                List.of(DELETED),
                                false,
                                List.of(CLAZZ, COURSE_ALIAS, CENTER_NAME));

            } else if (roleId.equals(Constants.ROLE_ADMIN)) {
                List<DtoOption> options =
                        List.of(REQUESTER_FULL_NAME, CLAZZ, COURSE_ALIAS, CENTER_NAME);

                if (clazzId != null) {
                    requestReadDTOPage =
                            requestService.getPageAllDTOByClazzId(
                                    pageable,
                                    clazzId,
                                    List.of(DELETED),
                                    false,
                                    options);
                } else {
                    requestReadDTOPage =
                            requestService.getPageAllDTO(
                                    pageable,
                                    List.of(DELETED),
                                    false,
                                    options);
                }
            } else {
                /* Quay về trang cũ */
                if (referer != null) {
                    return "redirect:" + referer;
                }
                return "redirect:/request";
            }

            if (requestReadDTOPage != null) {
                model.addAttribute("requestList", requestReadDTOPage.getContent());
                model.addAttribute("pageNo", requestReadDTOPage.getPageable().getPageNumber());
                model.addAttribute("pageTotal", requestReadDTOPage.getTotalPages());
            }
        } catch (Exception e) {
            e.printStackTrace();
            redirect.addFlashAttribute("mess", e.getMessage());
            return "redirect:" + referer;
        }

        return "request/list-request";
    }


    /* =================================================== UPDATE =================================================== */
    @GetMapping("/edit-request")
    public String editRequestPage(
            RedirectAttributes redirect,
            Model model,
            @RequestParam("id") Long requestId,
            @RequestHeader(value = "Referer", required = false) String referer,
            @SessionAttribute(name = "user", required = false) UserReadDTO userDTO) {

        /* Check login */
        if (userDTO == null) {
            redirect.addFlashAttribute("mess", "Làm ơn đăng nhập");
            return "redirect:/index";
        }

        if (!List.of(Constants.ROLE_STUDENT, Constants.ROLE_ADMIN).contains(userDTO.getRoleId())) {
            /* Quay về trang cũ */
            if (referer != null) {
                return "redirect:" + referer;
            }
            return "redirect:/request";
        }

        try {
            RequestReadDTO requestDTO = requestService.getDTOById(
                    requestId,
                    List.of(DELETED),
                    false,
                    List.of(REQUESTER, PAYMENT, CLAZZ, CLAZZ_SCHEDULE, SCHEDULE_CAT,
                            ROOM_NAME, COURSE_SEMESTER, SEMESTER, COURSE, CENTER, ADDRESS));

            CourseReadDTO courseDTO = requestDTO.getClazz().getCourse();

            LocalDateTime requestAt = requestDTO.getCreatedAt();
            PriceLogReadDTO priceDTO = priceLogService.getDTOByCourseIdAt(courseDTO.getId(), requestAt);

            courseDTO.setCurrentPrice(priceDTO);

            requestDTO.getClazz().setCourse(courseDTO);

            model.addAttribute("request", requestDTO);
        } catch (Exception e) {
            e.printStackTrace();

            redirect.addFlashAttribute("mess", e.getMessage());
        }

        return "request/edit-request";
    }

    @PostMapping("/edit-request/enroll")
    public String editEnrollRequest(
            RedirectAttributes redirect,
            Model model,
            @ModelAttribute RequestUpdateDTO updateDTO,
            @ModelAttribute PaymentCreateDTO paymentCreateDTO,
            @RequestParam("id") Long requestId,
            @RequestHeader(value = "Referer", required = false) String referer,
            @SessionAttribute(name = "user", required = false) UserReadDTO userDTO) {

        /* Check login */
        if (userDTO == null) {
            redirect.addFlashAttribute("mess", "Làm ơn đăng nhập");
            return "redirect:/index";
        }

        try {
            Long currentUserId = userDTO.getId();

            Long roleId = userDTO.getRoleId();

            if (roleId.equals(Constants.ROLE_STUDENT)) {
                /* Student cập nhập thanh toán cho Request với chứng cứ (ảnh hoá đơn, pdf giao dịch, ...) */
                Request request =
                        requestService.getByIdAndRequesterId(
                                requestId,
                                userDTO.getId(),
                                List.of(PENDING_PAYMENT, AWAIT_CONFIRM),
                                true);

                if (Objects.isNull(request)) {
                    throw new IllegalArgumentException(
                            "Cập nhập thất bại. Không thấy đơn nào với id: " + requestId);
                }

                if (!request.getRequesterId().equals(userDTO.getId())) {
                    /* TODO: Phụ huynh nộp hộ no throw exception */
                    throw new IllegalArgumentException(
                            "Cập nhập thất bại. Đây không phải là đơn của bạn.");
                }

                updateDTO.setUpdatedBy(currentUserId);

                requestService.updateRequestByDTO(updateDTO);

                redirect.addFlashAttribute("mess", "Cập nhập đơn thành công");
            } else if (roleId.equals(Constants.ROLE_ADMIN)) {
                /* Admin xác nhận thanh toán Request. Chấp nhận hoặc từ chối với chứng cứ */
                Request request =
                        requestService.getById(
                                requestId,
                                List.of(PENDING_PAYMENT, AWAIT_CONFIRM),
                                true);

                if (Objects.isNull(request)) {
                    throw new IllegalArgumentException(
                            "Cập nhập thất bại. Không thấy đơn nào với id: " + requestId);
                }

                updateDTO.setUpdatedBy(currentUserId);

                switch (updateDTO.getStatus()) {
                    case APPROVED -> {
                        /* Xác nhận thanh toán */
                        paymentCreateDTO.setVerifierId(currentUserId);
                        paymentCreateDTO.setCreatedBy(currentUserId);

                        paymentService.createPaymentByDTO(paymentCreateDTO);

                        /* Thêm vào lớp học */
                        ClazzMemberCreateDTO clazzMemberCreateDTO = new ClazzMemberCreateDTO();
                        clazzMemberCreateDTO.setClazzId(request.getClazzId());
                        clazzMemberCreateDTO.setUserId(request.getRequesterId());
                        clazzMemberCreateDTO.setCreatedBy(currentUserId);

                        clazzMemberService.createClazzMemberByDTO(clazzMemberCreateDTO);
                    }
                    case DENIED -> {
                        /* Xác nhận KO thanh toán */
                        paymentCreateDTO.setPaymentType(PaymentType.DENIED);
                        paymentCreateDTO.setPaymentAmount(0.0);
                        paymentCreateDTO.setVerifierId(currentUserId);
                        paymentCreateDTO.setCreatedBy(currentUserId);

                        paymentService.createPaymentByDTO(paymentCreateDTO);
                    }
                }

                requestService.updateRequestByDTO(updateDTO);

                redirect.addFlashAttribute("mess", "Duyệt đơn thành công");

            } else {
                /* Role != STUDENT, ADMIN*/
                redirect.addFlashAttribute("mess", "Bạn không đủ quyền");

                /* Quay về trang cũ */
                if (referer != null) {
                    return "redirect:" + referer;
                }
                return "redirect:/request";
            }

        } catch (Exception e) {
            e.printStackTrace();

            redirect.addFlashAttribute("mess", e.getMessage());

            if (referer != null) {
                return "redirect:" + referer;
            }
            return "redirect:/request";
        }

        return "redirect:/request";
    }


    /* =================================================== DELETE =================================================== */
    @GetMapping("/delete-request")
    public String deleteRequest(
            RedirectAttributes redirect,
            @RequestParam("id") Long requestId,
            @SessionAttribute(name = "user", required = false) UserReadDTO userDTO) {


        /* Check login */
        if (userDTO == null) {
            redirect.addFlashAttribute("mess", "Làm ơn đăng nhập");
            return "redirect:/index";
        }

        /* Check role */
        Long roleId = userDTO.getRoleId();
        if (!roleId.equals(Constants.ROLE_STUDENT)) {
            redirect.addFlashAttribute("mess", "Bạn không đủ quyền");
            return "redirect:/index";
        }

        try {
            requestService.deleteRequest(requestId, userDTO.getId());

            redirect.addFlashAttribute("mess", "Xóa đơn thành công");

        } catch (Exception e) {
            e.printStackTrace();

            redirect.addFlashAttribute("mess", e.getMessage());

            return "redirect:/request";
        }

        return "redirect:/request";
    }

}