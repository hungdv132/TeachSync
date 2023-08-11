package com.teachsync.controllers;

import com.teachsync.dtos.clazz.ClazzReadDTO;
import com.teachsync.dtos.homework.HomeworkReadDTO;
import com.teachsync.dtos.memberHomeworkRecord.MemberHomeworkRecordCreateDTO;
import com.teachsync.dtos.memberHomeworkRecord.MemberHomeworkRecordReadDTO;
import com.teachsync.dtos.user.UserReadDTO;
import com.teachsync.entities.ClazzMember;
import com.teachsync.services.clazz.ClazzService;
import com.teachsync.services.clazzMember.ClazzMemberService;
import com.teachsync.services.homework.HomeworkService;
import com.teachsync.services.memberHomeworkRecord.MemberHomeworkRecordService;
import com.teachsync.utils.Constants;
import com.teachsync.utils.MiscUtil;
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

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/homework")
public class HomeworkController {

    private Logger logger = LoggerFactory.getLogger(HomeworkController.class);

    @Autowired
    HomeworkService homeworkService;

    @Autowired
    ClazzService clazzService;
    @Autowired
    ClazzMemberService clazzMemberService;

    @Autowired
    MemberHomeworkRecordService memberHomeworkRecordService;

    @GetMapping("/list")
    public String viewHomeWork(HttpServletRequest request, RedirectAttributes redirect, Model model
            , @ModelAttribute("mess") String mess) {
        //check login
        HttpSession session = request.getSession();
        if (ObjectUtils.isEmpty(session.getAttribute("user"))) {
            redirect.addAttribute("mess", "Làm ơn đăng nhập");
            return "redirect:/";
        }
        UserReadDTO userDTO = (UserReadDTO) session.getAttribute("user");
        try {
            Page<HomeworkReadDTO> dtoPage = homeworkService.getPageAll(null, userDTO);
            model.addAttribute("homeworkList", dtoPage.getContent());
            model.addAttribute("pageNo", dtoPage.isEmpty() ? 0 : dtoPage.getPageable().getPageNumber());
            model.addAttribute("pageTotal", dtoPage.getTotalPages());
            model.addAttribute("mess", mess);
        } catch (Exception e) {
            logger.error(e.getMessage());
            redirect.addAttribute("mess", "hiển thị danh sách bài tập về nhà thất bại ,lỗi : " + e.getMessage());
            return "redirect:/";

        }

        return "homework/homework";
    }

    @GetMapping("/add-homework")
    public String addHomework(
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

        if (!List.of(Constants.ROLE_ADMIN, Constants.ROLE_TEACHER).contains(userDTO.getRoleId())) {
            /* Nếu roleId ko có trong list role được cấp phép */
            redirect.addAttribute("mess", "bạn không đủ quyền");
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

        return "homework/add-homework";
    }

    @GetMapping("/detail-homework")
    public String detailHomework(HttpSession session, RedirectAttributes redirect, Model model, HttpServletRequest request
            , @ModelAttribute("mess") String mess) {
        //check login
        if (ObjectUtils.isEmpty(session.getAttribute("user"))) {
            redirect.addAttribute("mess", "Làm ơn đăng nhập");
            return "redirect:/";
        }
        UserReadDTO userDTO = (UserReadDTO) session.getAttribute("user");
        try {
            Page<ClazzReadDTO> dtoPage = clazzService.getPageDTOAll(null);
            if (!ObjectUtils.isEmpty(request.getParameter("id"))) {
                HomeworkReadDTO homeworkReadDTO = new HomeworkReadDTO();
                if (userDTO.getRoleId().equals(Constants.ROLE_STUDENT)) {
                    homeworkReadDTO = homeworkService.findById(Long.parseLong(request.getParameter("id")), userDTO);
                } else {
                    homeworkReadDTO = homeworkService.findById(Long.parseLong(request.getParameter("id")));
                }
                model.addAttribute("homework", homeworkReadDTO);
                model.addAttribute("option", "detail");
            }
            model.addAttribute("clazzList", dtoPage.getContent());
            model.addAttribute("mess", mess);

        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            redirect.addAttribute("mess", "xem chi tiêt bài tập thất bại ,lỗi : " + e.getMessage());
            return "redirect:/";

        }

        return "homework/add-homework";
    }

    @PostMapping("/add-homework")
    public String addHomework(
            HttpServletRequest request,
            @SessionAttribute(value = "user", required = false) UserReadDTO userDTO,
            RedirectAttributes redirect) {
        //check login
        if (ObjectUtils.isEmpty(userDTO)) {
            redirect.addAttribute("mess", "Làm ơn đăng nhập");
            return "redirect:/";
        }

        if (!List.of(Constants.ROLE_ADMIN, Constants.ROLE_TEACHER).contains(userDTO.getRoleId())) {
            /* Nếu roleId ko có trong list role được cấp phép */
            redirect.addAttribute("mess", "bạn không đủ quyền");
            return "redirect:/";
        }

        HomeworkReadDTO homeworkReadDTO = new HomeworkReadDTO();

        homeworkReadDTO.setHomeworkName(request.getParameter("name"));
        homeworkReadDTO.setClazzId(Long.parseLong(request.getParameter("clazzId")));
        homeworkReadDTO.setHomeworkDesc(request.getParameter("desc"));
        homeworkReadDTO.setHomeworkDoc(request.getParameter("homeworkDoc"));
        homeworkReadDTO.setHomeworkContent(null);//TODO : upload file
        String deadlineString = request.getParameter("deadline");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime deadline = LocalDateTime.parse(deadlineString, formatter);
        homeworkReadDTO.setDeadline(deadline);
        if (!ObjectUtils.isEmpty(request.getParameter("openAt"))) {
            String openAtString = request.getParameter("openAt");
            LocalDateTime openAt = LocalDateTime.parse(openAtString, formatter);
            homeworkReadDTO.setOpenAt(openAt);
        }
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

    @GetMapping("/delete-homework")
    public String deleteHomework(HttpSession session, RedirectAttributes redirect, Model model, HttpServletRequest request
            , @ModelAttribute("mess") String mess) {
        //check login
        if (ObjectUtils.isEmpty(session.getAttribute("user"))) {
            redirect.addAttribute("mess", "Làm ơn đăng nhập");
            return "redirect:/";
        }
        UserReadDTO userDTO = (UserReadDTO) session.getAttribute("user");

        if (!userDTO.getRoleId().equals(Constants.ROLE_ADMIN) && !userDTO.getRoleId().equals(Constants.ROLE_TEACHER)) {
            redirect.addAttribute("mess", "bạn không đủ quyền");
            return "redirect:/";
        }

        try {
            homeworkService.deleteHomework(Long.parseLong(request.getParameter("id")), userDTO);
        } catch (Exception e) {
            logger.error(e.getMessage());
            redirect.addAttribute("mess", e.getMessage());
            return "redirect:/homework/add-homework";

        }

        redirect.addAttribute("mess", "Xóa bài tập về nhà thành công");
        return "redirect:/homework/list";
    }


    @GetMapping("/record-homework")
    public String addRecordHomeworkPage(HttpSession session, RedirectAttributes redirect, Model model, HttpServletRequest request
            , @ModelAttribute("mess") String mess) {
        //check login
        if (ObjectUtils.isEmpty(session.getAttribute("user"))) {
            redirect.addAttribute("mess", "Làm ơn đăng nhập");
            return "redirect:/";
        }
        UserReadDTO userDTO = (UserReadDTO) session.getAttribute("user");

        if (!userDTO.getRoleId().equals(Constants.ROLE_STUDENT)) {
            redirect.addAttribute("mess", "bạn không đủ quyền");
            return "redirect:/";
        }

        /* TODO: Kiểm tra user này có học lớp này hay không mà cho trang nộp bài (Sử dụng ClazzMember) */

        try {
            if (!ObjectUtils.isEmpty(request.getParameter("id"))) {
                HomeworkReadDTO homeworkReadDTO =
                        homeworkService.findById(Long.parseLong(request.getParameter("id")));

                model.addAttribute("homework", homeworkReadDTO);
            }
            model.addAttribute("mess", mess);
            model.addAttribute("option", "add");
        } catch (Exception e) {
            logger.error(e.getMessage());
            redirect.addAttribute("mess", "lỗi : " + e.getMessage());
            return "redirect:/";

        }
        return "homework/add-record-homework";
    }

    @PostMapping("/record-homework")
    public String addRecordHomework(
            HttpSession session,
            RedirectAttributes redirect,
            Model model,
            HttpServletRequest request) {
        //check login
        if (ObjectUtils.isEmpty(session.getAttribute("user"))) {
            redirect.addAttribute("mess", "Làm ơn đăng nhập");
            return "redirect:/";
        }

        UserReadDTO userDTO = (UserReadDTO) session.getAttribute("user");

        if (!userDTO.getRoleId().equals(Constants.ROLE_STUDENT)) {
            redirect.addAttribute("mess", "bạn không đủ quyền");
            return "redirect:/";
        }
        Long homeworkId = Long.parseLong(request.getParameter("homeworkId"));

        /* clazzId có trong HomeworkReadDTO, truyền qua input hidden */
        Long clazzId = Long.parseLong(request.getParameter("clazzId"));
        try {
            MemberHomeworkRecordCreateDTO recordDTO = new MemberHomeworkRecordCreateDTO();
            recordDTO.setHomeworkId(homeworkId);

            /* Vì sao dùng memberId => vì có ClazzMember mới biết là có học lớp này hay không */
            ClazzMember member = clazzMemberService.getByClazzIdAndUserId(clazzId, userDTO.getId());
            if (member == null) {
                throw new AccessDeniedException("Bạn không phải học sinh của lớp này.");
            }
            recordDTO.setMemberId(member.getId());

            recordDTO.setName("Bài tập - " + MiscUtil.generateRandomName() + " - " + userDTO.getFullName());

            recordDTO.setSubmission(request.getParameter("submissionFile"));
            recordDTO.setSubmissionLink(request.getParameter("submissionLink"));

            recordDTO.setCreatedBy(userDTO.getId());

            memberHomeworkRecordService.add(recordDTO);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            redirect.addAttribute("mess", "lỗi : " + e.getMessage());
            return "redirect:/";

        }
        redirect.addAttribute("mess", "Nộp bài tập thành công");
        return "redirect:/homework/detail-homework?id=" + homeworkId;
    }

    @GetMapping("/detail-record-homework")
    public String detailRecordHomework(HttpSession session, RedirectAttributes redirect, Model model, HttpServletRequest request
            , @ModelAttribute("mess") String mess) {
        //check login
        if (ObjectUtils.isEmpty(session.getAttribute("user"))) {
            redirect.addAttribute("mess", "Làm ơn đăng nhập");
            return "redirect:/";
        }
        UserReadDTO userDTO = (UserReadDTO) session.getAttribute("user");

        try {
            if (!ObjectUtils.isEmpty(request.getParameter("homeworkId")) && !ObjectUtils.isEmpty(request.getParameter("id"))) {
                HomeworkReadDTO homeworkReadDTO = homeworkService.findById(Long.parseLong(request.getParameter("homeworkId")));
                MemberHomeworkRecordReadDTO memberHomeworkRecordReadDTO = memberHomeworkRecordService.findById(Long.parseLong(request.getParameter("id")));
                model.addAttribute("homework", homeworkReadDTO);
                model.addAttribute("homeworkRecord", memberHomeworkRecordReadDTO);
                model.addAttribute("option", "detail");
            }
            model.addAttribute("mess", mess);
        } catch (Exception e) {
            logger.error(e.getMessage());
            redirect.addAttribute("mess", "lỗi : " + e.getMessage());
            return "redirect:/";

        }
        return "homework/add-record-homework";
    }

    @GetMapping("/delete-record-homework")
    public String deleteRecordHomework(HttpSession session, RedirectAttributes redirect, Model model, HttpServletRequest request
            , @ModelAttribute("mess") String mess) {
        //check login
        if (ObjectUtils.isEmpty(session.getAttribute("user"))) {
            redirect.addAttribute("mess", "Làm ơn đăng nhập");
            return "redirect:/";
        }
        Long homeworkId = Long.parseLong(request.getParameter("homeworkId"));
        try {

            memberHomeworkRecordService.delete(Long.parseLong(request.getParameter("id")));
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            redirect.addAttribute("mess", "lỗi : " + e.getMessage());
            return "redirect:/homework/detail-homework?id=" + homeworkId;

        }
        redirect.addAttribute("mess", "Xóa thành công ");
        return "redirect:/homework/detail-homework?id=" + homeworkId;
    }

    @PostMapping("/update-score-record-homework")
    public String updateScoreRecordHomework(HttpSession session, RedirectAttributes redirect, Model model, HttpServletRequest request
            , @ModelAttribute("mess") String mess) {
        //check login
        if (ObjectUtils.isEmpty(session.getAttribute("user"))) {
            redirect.addAttribute("mess", "Làm ơn đăng nhập");
            return "redirect:/";
        }
        UserReadDTO userDTO = (UserReadDTO) session.getAttribute("user");
        if (!userDTO.getRoleId().equals(Constants.ROLE_TEACHER)) {
            redirect.addAttribute("mess", "bạn không đủ quyền");
            return "redirect:/";
        }
        Long homeworkId = Long.parseLong(request.getParameter("homeworkId"));
        Long recordHomeworkId = Long.parseLong(request.getParameter("recordHomeworkId"));
        Double score = Double.parseDouble(request.getParameter("score"));
        try {
            memberHomeworkRecordService.updateScore(recordHomeworkId, userDTO, score);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            redirect.addAttribute("mess", "lỗi : " + e.getMessage());
            return "redirect:/homework/detail-homework?id=" + homeworkId;

        }
        redirect.addAttribute("mess", "Chấm điểm thành công ");
        return "redirect:/homework/detail-homework?id=" + homeworkId;
    }
}
