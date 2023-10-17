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
import com.teachsync.utils.enums.DtoOption;
import com.teachsync.utils.enums.PaymentType;
import com.teachsync.utils.enums.Status;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

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
            /* Quay về trang cũ */
            if (referer != null) {
                return "redirect:" + referer;
            }
            return "redirect:/request";
        }

        try {
            /* List Course (môn nào) */
            List<CourseReadDTO> courseDTOList = courseService.getAllDTO(
                    List.of(Status.DELETED),
                    false,
                    null);
            model.addAttribute("courseList", courseDTOList);

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
            /* Quay về trang cũ */
            if (referer != null) {
                return "redirect:" + referer;
            }
            return "redirect:/request";
        }

        RequestReadDTO requestDTO = null;
        try {
            ClazzReadDTO clazzDTO =
                    clazzService.getDTOById(
                            createDTO.getClazzId(),
                            List.of(Status.DELETED),
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
            createDTO.setStatus(Status.PENDING_PAYMENT);

            requestDTO = requestService.createRequestByDTO(createDTO);

        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:" + referer;
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
                                List.of(CLAZZ, COURSE_ALIAS, CENTER_NAME));
            } else if (roleId.equals(Constants.ROLE_ADMIN)) {
                List<DtoOption> options =
                        List.of(REQUESTER_FULL_NAME, CLAZZ, COURSE_ALIAS, CENTER_NAME);
                if (clazzId != null) {
                    requestReadDTOPage =
                            requestService.getPageAllDTOByClazzId(
                                    pageable,
                                    clazzId,
                                    options);
                } else {
                    requestReadDTOPage =
                            requestService.getPageAllDTO(
                                    pageable,
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
                        requestService.getById(requestId);

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
                        requestService.getById(requestId);

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
                        /* TODO: Something, anything, haven't a clue yet */
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

}