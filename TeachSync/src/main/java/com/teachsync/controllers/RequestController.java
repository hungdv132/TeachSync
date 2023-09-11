package com.teachsync.controllers;

import com.teachsync.dtos.center.CenterReadDTO;
import com.teachsync.dtos.clazz.ClazzReadDTO;
import com.teachsync.dtos.clazzMember.ClazzMemberCreateDTO;
import com.teachsync.dtos.clazzMember.ClazzMemberReadDTO;
import com.teachsync.dtos.course.CourseReadDTO;
import com.teachsync.dtos.courseSemester.CourseSemesterReadDTO;
import com.teachsync.dtos.payment.PaymentCreateDTO;
import com.teachsync.dtos.payment.PaymentReadDTO;
import com.teachsync.dtos.priceLog.PriceLogReadDTO;
import com.teachsync.dtos.request.RequestCreateDTO;
import com.teachsync.dtos.request.RequestReadDTO;
import com.teachsync.dtos.request.RequestUpdateDTO;
import com.teachsync.dtos.semester.SemesterReadDTO;
import com.teachsync.dtos.user.UserReadDTO;
import com.teachsync.entities.ClazzMember;
import com.teachsync.entities.Payment;
import com.teachsync.entities.Request;
import com.teachsync.services.center.CenterService;
import com.teachsync.services.clazz.ClazzService;
import com.teachsync.services.clazzMember.ClazzMemberService;
import com.teachsync.services.course.CourseService;
import com.teachsync.services.payment.PaymentService;
import com.teachsync.services.priceLog.PriceLogService;
import com.teachsync.services.request.RequestService;
import com.teachsync.services.semester.SemesterService;
import com.teachsync.utils.Constants;
import com.teachsync.utils.MiscUtil;
import com.teachsync.utils.enums.PaymentType;
import com.teachsync.utils.enums.Status;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.teachsync.utils.enums.DtoOption.*;

@Controller
public class RequestController {
    @Autowired
    private CourseService courseService;
    @Autowired
    private SemesterService semesterService;
    @Autowired
    private CenterService centerService;
    @Autowired
    private PriceLogService priceLogService;
    @Autowired
    private ClazzService clazzService;
    @Autowired
    private ClazzMemberService clazzMemberService;
    @Autowired
    private RequestService requestService;
    @Autowired
    private PaymentService paymentService;

    @Autowired
    private MiscUtil miscUtil;
    @Autowired
    private ModelMapper modelMapper;


    /* =================================================== CREATE =================================================== */
    @GetMapping("/add-request")
    public String addRequestPage(
            Model model,
            @RequestHeader(value = "Referer", required = false) String referer,
            @SessionAttribute(name = "user", required = false) UserReadDTO userDTO) {
        /* Check login */
        if (userDTO == null) {
            return "redirect:/index";
        }

        if (!userDTO.getRoleId().equals(Constants.ROLE_STUDENT)) {
            /* Quay về trang cũ */
            if (referer != null) {
                return "redirect:" + referer;
            }
            return "redirect:/request";
        }

        try {
            /* List Course (môn nào) */
            List<CourseReadDTO> courseDTOList = courseService.getAllDTO(null);
            model.addAttribute("courseList", courseDTOList);

            /* List Semester (kỳ nào) */
            /* Các kỳ học nào ngày bắt đàu cách 10 ngày từ hiện tại (Để học sinh còn có thời gian đăng ký) */
            List<SemesterReadDTO> semesterDTOList =
                    semesterService.getAllDTOByStartDateAfter(LocalDate.now().plusDays(10), null);
            model.addAttribute("semesterList", semesterDTOList);

            /* List Center (Cơ sở nào) */
            List<CenterReadDTO> centerDTOList = centerService.getAllDTO(null);
            model.addAttribute("centerList", centerDTOList);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "request/add-request";
    }

    @PostMapping("/add-request/enroll")
    public String addRequestEnroll(
            Model model,
            @RequestParam Long clazzId,
            @ModelAttribute RequestCreateDTO createDTO,
            @RequestHeader(value = "Referer", required = false) String referer,
            @SessionAttribute(name = "user", required = false) UserReadDTO userDTO) {
        /* Check login */
        if (userDTO == null) {
            return "redirect:/index";
        }

        if (!userDTO.getRoleId().equals(Constants.ROLE_STUDENT)) {
            /* Quay về trang cũ */
            if (referer != null) {
                return "redirect:" + referer;
            }
            return "redirect:/request";
        }

        RequestReadDTO requestDTO = null;
        try {
            ClazzReadDTO clazzDTO =
                    clazzService.getDTOById(clazzId, List.of(COURSE_SEMESTER, COURSE_NAME, CENTER_NAME, SEMESTER_NAME));

            CourseSemesterReadDTO courseSemesterDTO = clazzDTO.getCourseSemester();

            String requestName = "hoc sinh '" + userDTO.getId() +
                    "' xin hoc lop '" + clazzDTO.getId() +
                    "' mon '" + clazzDTO.getCourseSemester().getCourseId() + "'";
            createDTO.setRequestName(requestName);

            String requestDesc = "Học sinh '" + userDTO.getFullName() +
                    "' xin nhập học Lớp '" + clazzDTO.getClazzName() +
                    "' cho Khóa học '" + courseSemesterDTO.getCourseName() +
                    "' vào Kỳ '" + courseSemesterDTO.getSemesterName() +
                    "' tại Cơ Sở '" + courseSemesterDTO.getCenterName() + "'.";
            createDTO.setRequestDesc(requestDesc);
            createDTO.setCreatedBy(userDTO.getId());
            createDTO.setStatus(Status.PENDING_PAYMENT);

            requestDTO = requestService.createRequestByDTO(createDTO);

        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:" + referer;
        }

        return "redirect:/request-detail?id=" + requestDTO.getId();
    }


    /* =================================================== READ ===================================================== */
    @GetMapping("/request")
    public String requestListPage(
            Model model,
            @RequestParam(value = "pageNo", required = false) Integer pageNo,
            @RequestParam(value = "clazzId", required = false) Long clazzId,
            @RequestHeader(value = "Referer", required = false) String referer,
            @SessionAttribute(name = "user", required = false) UserReadDTO userDTO) {
        /* Check login */
        if (userDTO == null) {
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
                                List.of(CLAZZ, COURSE_SEMESTER, COURSE_ALIAS, SEMESTER_ALIAS, CENTER_NAME));
            } else if (roleId.equals(Constants.ROLE_ADMIN)) {
                if (clazzId != null) {
                    requestReadDTOPage =
                            requestService.getPageAllDTO(
                                    pageable,
                                    List.of(REQUESTER_FULL_NAME, CLAZZ, COURSE_SEMESTER,
                                            COURSE_ALIAS, SEMESTER_ALIAS, CENTER_NAME));
                } else {
                    requestReadDTOPage =
                            requestService.getPageAllDTO(
                                    pageable,
                                    List.of(REQUESTER_FULL_NAME, CLAZZ, COURSE_SEMESTER,
                                            COURSE_ALIAS, SEMESTER_ALIAS, CENTER_NAME));
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
            return "redirect:" + referer;
        }

        return "request/list-request";
    }


    /* =================================================== UPDATE =================================================== */
    @GetMapping("/edit-request")
    public String editDetailPage(
            Model model,
            @RequestParam("id") Long requestId,
            @RequestHeader(value = "Referer", required = false) String referer,
            @SessionAttribute(name = "user", required = false) UserReadDTO userDTO) {

        /* Check login */
        if (userDTO == null) {
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
                    List.of(REQUESTER, PAYMENT, CLAZZ, CLAZZ_SCHEDULE, ROOM_NAME,
                            COURSE_SEMESTER, SEMESTER, COURSE, CENTER, ADDRESS));

            CourseReadDTO courseDTO = requestDTO.getClazz().getCourseSemester().getCourse();

            LocalDateTime requestAt = requestDTO.getCreatedAt();
            PriceLogReadDTO priceDTO = priceLogService.getDTOByCourseIdAt(courseDTO.getId(), requestAt);

            courseDTO.setCurrentPrice(priceDTO);

            requestDTO.getClazz().getCourseSemester().setCourse(courseDTO);

            model.addAttribute("request", requestDTO);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "request/edit-request";
    }

    @PostMapping("/edit-request/enroll")
    public String editDetail(
            Model model,
            @RequestParam("id") Long requestId,
            @RequestParam(value = "paymentType", required = false) PaymentType paymentType,
            @RequestParam(value = "paymentAmount", required = false) Double paymentAmount,
            @RequestParam(value = "paymentAt", required = false) LocalDateTime paymentAt,
            @RequestParam(value = "paymentInfo", required = false) String paymentInfoLink,
            @RequestParam(value = "paymentStatus", required = false) Status paymentStatus,
            @RequestParam(value = "paymentDesc", required = false) String paymentDesc,
            @RequestHeader(value = "Referer", required = false) String referer,
            @SessionAttribute(name = "user", required = false) UserReadDTO userDTO) {

        /* Check login */
        if (userDTO == null) {
            return "redirect:/index";
        }

        try {
            Long currentUserId = userDTO.getId();

            RequestReadDTO requestDTO = null;

            Long roleId = userDTO.getRoleId();
            if (roleId.equals(Constants.ROLE_STUDENT)) {
                /* Student cập nhập thanh toán cho Request với chứng cứ (ảnh hoá đơn, pdf giao dịch, ...) */
                requestDTO = requestService.getDTOById(requestId, null);

                if (requestDTO == null) {
                    throw new IllegalArgumentException(
                            "Cập nhập thất bại. Không thấy đơn nào với id: " + requestId);
                }

                if (!requestDTO.getRequesterId().equals(userDTO.getId())) {
                    /* TODO: Phụ huynh nộp hộ no throw exception */
                    throw new IllegalArgumentException(
                            "Cập nhập thất bại. Đây không phải là đơn của bạn.");
                }
                
                RequestUpdateDTO updateDTO = modelMapper.map(requestDTO, RequestUpdateDTO.class);

                updateDTO.setContentLink(paymentInfoLink);
                updateDTO.setStatus(Status.AWAIT_CONFIRM);
                updateDTO.setUpdatedBy(currentUserId);
                
                requestService.updateRequestByDTO(updateDTO);
                
            } else if (roleId.equals(Constants.ROLE_ADMIN)) {
                /* Admin xác nhận thanh toán Request. Chấp nhận hoặc từ chối với chứng cứ */
                requestDTO = requestService.getDTOById(
                        requestId,
                        List.of(REQUESTER, CLAZZ, CLAZZ_SCHEDULE, ROOM_NAME,
                                COURSE_SEMESTER, SEMESTER, CENTER, COURSE));

                if (requestDTO == null) {
                    throw new IllegalArgumentException(
                            "Cập nhập thất bại. Không thấy đơn nào với id: " + requestId);
                }

                RequestUpdateDTO updateDTO = modelMapper.map(requestDTO, RequestUpdateDTO.class);
                updateDTO.setUpdatedBy(currentUserId);
                updateDTO.setResolverId(currentUserId);
                updateDTO.setStatus(paymentStatus);

                switch (paymentStatus) {
                    case APPROVED -> {
                        /* Xác nhận thanh toán */
                        PaymentCreateDTO paymentCreateDTO = new PaymentCreateDTO();
                        paymentCreateDTO.setPayerId(requestDTO.getRequesterId());
                        paymentCreateDTO.setRequestId(requestId);
                        paymentCreateDTO.setPaymentType(paymentType);
                        paymentCreateDTO.setPaymentDesc(paymentDesc);
                        paymentCreateDTO.setPaymentAmount(paymentAmount);
                        paymentCreateDTO.setPaymentAt(paymentAt);
                        paymentCreateDTO.setPaymentDocLink(paymentInfoLink);
                        paymentCreateDTO.setVerifierId(currentUserId);
                        paymentCreateDTO.setCreatedBy(currentUserId);

                        paymentService.createPaymentByDTO(paymentCreateDTO);

                        /* Thêm vào lớp học */
                        ClazzMemberCreateDTO clazzMemberCreateDTO = new ClazzMemberCreateDTO();
                        clazzMemberCreateDTO.setClazzId(requestDTO.getClazzId());
                        clazzMemberCreateDTO.setUserId(requestDTO.getRequesterId());
                        clazzMemberCreateDTO.setCreatedBy(currentUserId);

                        clazzMemberService.createClazzMemberByDTO(clazzMemberCreateDTO);
                    }
                    case DENIED -> {
                        /* TODO: Something, anything, haven't a clue yet */
                    }
                }

                requestService.updateRequestByDTO(updateDTO);

                model.addAttribute("request", requestDTO);
            } else {
                /* Quay về trang cũ */
                if (referer != null) {
                    return "redirect:" + referer;
                }
                return "redirect:/request";
            }

        } catch (Exception e) {
            e.printStackTrace();
            if (referer != null) {
                return "redirect:" + referer;
            }
            return "redirect:/request";
        }

        return "redirect:/edit-request?id="+requestId;
    }


    /* =================================================== DELETE =================================================== */

}
