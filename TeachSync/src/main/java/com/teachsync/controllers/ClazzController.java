package com.teachsync.controllers;

import com.teachsync.dtos.center.CenterReadDTO;
import com.teachsync.dtos.clazz.ClazzCreateDTO;
import com.teachsync.dtos.clazz.ClazzReadDTO;
import com.teachsync.dtos.clazz.ClazzUpdateDTO;
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
import com.teachsync.services.course.CourseService;
import com.teachsync.services.courseSemester.CourseSemesterService;
import com.teachsync.services.homework.HomeworkService;
import com.teachsync.services.news.NewsService;
import com.teachsync.services.semester.SemesterService;
import com.teachsync.services.staff.StaffService;
import com.teachsync.utils.Constants;
import com.teachsync.utils.MiscUtil;
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

import static com.teachsync.utils.enums.DtoOption.*;
import static com.teachsync.utils.enums.Status.DEPLOY_CLAZZ;

@Controller
public class ClazzController {
    @Autowired
    private ClazzService clazzService;

    @Autowired
    private ClazzMemberService clazzMemberService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private SemesterService semesterService;
    @Autowired
    private CourseSemesterService courseSemesterService;
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
            @RequestParam Long semesterId,
            @RequestParam Long centerId) {
        Map<String, Object> response = new HashMap<>();
        try {
            CourseSemester courseSemester =
                    courseSemesterService.getByCourseIdAndSemesterIdAndCenterId(courseId, semesterId, centerId);

            if (courseSemester == null) {
                response.put("clazzList", null);
                return response;
            }

            List<ClazzReadDTO> clazzDTOList =
                    clazzService.getAllDTOByCourseSemesterId(courseSemester.getId(), null);
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
                    clazzService.getDTOById(clazzId, List.of(MEMBER_LIST, STAFF, USER, CLAZZ_SCHEDULE, SCHEDULE_CAT, ROOM_NAME, TEST_LIST));

            response.put("clazz", clazzDTO);
        } catch (Exception e) {
            e.printStackTrace();
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


    /* =================================================== READ ===================================================== */
    @GetMapping("/clazz")
    public String clazzListPage(
            Model model,
            @ModelAttribute("mess") String mess,
            @RequestParam(value = "pageNo", required = false) Integer pageNo,
            @SessionAttribute(value = "user", required = false) UserReadDTO userDTO) {
        try {
            Page<ClazzReadDTO> dtoPage = null;

            if (Objects.isNull(userDTO)) {
                /* Chưa login */
                return "redirect:/index";
            }

            if (pageNo == null || pageNo < 0) {
                pageNo = 0;
            }

            Pageable pageable = miscUtil.makePaging(pageNo, 10, "id", true);

            Long roleId = userDTO.getRoleId();
            if (roleId.equals(Constants.ROLE_STUDENT)) {
                List<ClazzMember> clazzMemberList = clazzMemberService.getAllByUserId(userDTO.getId());
                Set<Long> clazzIdSet = clazzMemberList.stream().map(ClazzMember::getClazzId).collect(Collectors.toSet());

                /* TODO: lọc clazz nào học xong rồi ẩn hay hiện hay sort */

                dtoPage = clazzService.getPageDTOAllByIdIn(
                        pageable, clazzIdSet,
                        List.of(COURSE_SEMESTER, SEMESTER, COURSE_NAME, COURSE_ALIAS, CENTER));
            } else if (roleId.equals(Constants.ROLE_TEACHER)) {
                List<Staff> staffList = staffService.getAllByUserId(userDTO.getId());
                Set<Long> staffIdSet = staffList.stream().map(BaseEntity::getId).collect(Collectors.toSet());

                /* TODO: lọc clazz nào dạy xong rồi ẩn hay hiện hay sort */

                dtoPage = clazzService.getPageDTOAllByStaffIdIn(
                        pageable, staffIdSet,
                        List.of(COURSE_SEMESTER, SEMESTER, COURSE_NAME, COURSE_ALIAS, CENTER));
            } else if (roleId.equals(Constants.ROLE_ADMIN)) {
                dtoPage = clazzService.getPageDTOAll(
                        pageable,
                        List.of(COURSE_SEMESTER, SEMESTER, COURSE_NAME, COURSE_ALIAS, CENTER));
            }

            if (dtoPage != null) {
                model.addAttribute("clazzList", dtoPage.getContent());
                model.addAttribute("pageNo", dtoPage.getPageable().getPageNumber());
                model.addAttribute("pageTotal", dtoPage.getTotalPages());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //map option status
        Map<String, String> statusLabelMap = new HashMap<>();
        statusLabelMap.put("CREATED_CLAZZ", "Đang khởi tạo");
        statusLabelMap.put("DEPLOY_CLAZZ", "Đang triển khai");
        statusLabelMap.put("NOT_ENOUGH_CLAZZ", "Không đủ xếp lớp");
        statusLabelMap.put("FINISH_CLAZZ", "Đã hoàn thành");
        model.addAttribute("statusLabelMap", statusLabelMap);
        model.addAttribute("mess", mess);
        return "clazz/list-clazz";
    }

    @GetMapping("/clazz-detail")
    public String clazzDetailPage(
            Model model,
            @RequestParam(value = "id", required = false) Long clazzId) {
        try {
            ClazzReadDTO clazzDTO =
                    clazzService.getDTOById(
                            clazzId,
                            List.of(STAFF, USER, COURSE_SEMESTER, SEMESTER, COURSE_NAME, COURSE_ALIAS, CENTER, TEST_LIST));
            //get news of class
            List<NewsReadDTO> newsReadDTOList = newsService.getAllNewsByClazz(clazzDTO.getId());
            //get homework of class
            //get score of class
            List<HomeworkReadDTO> homeworkReadDTOList = homeworkService.getAllByClazzId(clazzDTO.getId());
            //get course
            CourseReadDTO courseReadDTO = courseService.getDTOById(clazzDTO.getCourseSemester().getCourseId(), List.of(MATERIAL_LIST));
            for (ClazzTestReadDTO clT : clazzDTO.getTestList()) {
                Test test = testRepository.findById(clT.getTestId()).orElse(null);
                TestReadDTO testReadDTO = new TestReadDTO();
                testReadDTO.setTestType(test.getTestType());
                testReadDTO.setQuestionType(test.getQuestionType());
                testReadDTO.setId(test.getId());
                clT.setTest(testReadDTO);
                if (clT.getOpenFrom().compareTo(LocalDateTime.now()) < 0 && clT.getOpenTo() == null) {
                    clT.setInTime("Đang mở");
                } else if (clT.getOpenTo() != null && clT.getOpenTo().compareTo(LocalDateTime.now()) < 0) {
                    clT.setInTime("Đã kết thúc");
                }
            }

            List<Test> lstTestTeacher = testRepository.findAllByCourseIdAndStatusNot(clazzDTO.getCourseSemester().getCourseId(), Status.DELETED);

            for (Test t : lstTestTeacher) {
                ClazzTest clazzTest = clazzTestRepository.findByClazzIdAndTestIdAndStatusNot(clazzId, t.getId(), Status.DELETED).orElse(null);
                if (clazzTest == null) {
                    t.setStatusTeacherTest(0);
                } else if (clazzTest != null && clazzTest.getOpenTo() == null) {
                    t.setStatusTeacherTest(1);
                } else {
                    t.setStatusTeacherTest(2);
                }
            }

            model.addAttribute("homeworkList", homeworkReadDTOList);
            model.addAttribute("newsList", newsReadDTOList);
            model.addAttribute("clazz", clazzDTO);
            model.addAttribute("lstTestTeacher", lstTestTeacher);
            model.addAttribute("material", courseReadDTO.getMaterialList());
        } catch (Exception e) {
            e.printStackTrace();
        }

        //map option status
        Map<String, String> statusLabelMap = new HashMap<>();
        statusLabelMap.put("CREATED_CLAZZ", "Đang khởi tạo");
        statusLabelMap.put("DEPLOY_CLAZZ", "Đang triển khai");
        statusLabelMap.put("NOT_ENOUGH_CLAZZ", "Không đủ xếp lớp");
        statusLabelMap.put("FINISH_CLAZZ", "Đã hoàn thành");
        model.addAttribute("statusLabelMap", statusLabelMap);


        return "clazz/clazz-detail";
    }


    /* =================================================== CREATE =================================================== */
    @GetMapping("/add-clazz")
    public String addClazzPage(
            Model model,
            RedirectAttributes redirect,
            @RequestParam(value = "id", required = false) Long clazzId,
            @RequestParam("option") String option) {

        try {
            /* Nếu Id => Edit, Lấy dữ liệu cũ */
            ClazzReadDTO clazzReadDTO = null;
            if (Objects.nonNull(clazzId)) {
                clazzReadDTO =
                        clazzService.getDTOById(clazzId,
                                List.of(COURSE_SEMESTER, STAFF, USER));

                model.addAttribute("clazz", clazzReadDTO);
            }

            /* List Course (môn nào) */
            List<CourseReadDTO> courseDTOList = courseService.getAllDTO(null);
            model.addAttribute("courseList", courseDTOList);

            /* List Semester (kỳ nào) */
            List<SemesterReadDTO> semesterDTOList;
            if (option.equals("add")) {
                /* Các kỳ học nào ngày bắt đàu cách 10 ngày từ hiện tại (Để học sinh còn có thời gian đăng ký) */
                semesterDTOList =
                        semesterService.getAllDTOByStartDateAfter(LocalDate.now().plusDays(10), null);
            } else {
                semesterDTOList =
                        semesterService.getAllDTO(null);
            }
            model.addAttribute("semesterList", semesterDTOList);

            /* List Center (Cơ sở nào) */
            List<CenterReadDTO> centerDTOList = centerService.getAllDTO(null);
            model.addAttribute("centerList", centerDTOList);

            /* List Staff (Ai dạy) */
            List<StaffReadDTO> staffDTOList;
            if (option.equals("add")) {
                /* Suy ra từ Center đầu tiên trong list */
                staffDTOList =
                        staffService.getAllDTOByCenterId(centerDTOList.get(0).getId(), List.of(USER));
            } else {
                staffDTOList =
                        staffService.getAllDTOByCenterId(clazzReadDTO.getCourseSemester().getCenterId(), List.of(USER));
            }
            model.addAttribute("staffList", staffDTOList);

            model.addAttribute("option", option);
            //map option status
            Map<String, String> statusLabelMap = new HashMap<>();
            statusLabelMap.put("CREATED_CLAZZ", "Đang khởi tạo");
            statusLabelMap.put("DEPLOY_CLAZZ", "Đang triển khai");
            statusLabelMap.put("NOT_ENOUGH_CLAZZ", "Không đủ xếp lớp");
            statusLabelMap.put("FINISH_CLAZZ", "Đã hoàn thành");
            model.addAttribute("statusLabelMap", statusLabelMap);
        } catch (Exception e) {
            e.printStackTrace();
            /* Log Error or return error msg */
        }

        return "clazz/add-clazz";
    }

    @PostMapping(value = "/add-clazz", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> addClazz(
            @RequestBody ClazzCreateDTO createDTO,
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
        ClazzReadDTO readDTO;
        try {
            readDTO = clazzService.createClazzByDTO(createDTO);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("error", e.getMessage());
            return response;
        }

        response.put("view", "/clazz-detail?id=" + readDTO.getId());
        return response;
    }


    /* =================================================== UPDATE =================================================== */
    @PutMapping(value = "/edit-clazz", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> editClazz(
            @RequestBody ClazzUpdateDTO updateDTO,
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

        ClazzReadDTO readDTO;
        try {
            readDTO = clazzService.updateClazzByDTO(updateDTO);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("error", e.getMessage());
            return response;
        }

        //  response.put("view", "/clazz-detail?id=" + readDTO.getId());
        response.put("view", "/clazz");
        //  return "redirect:/clazz";
        return response;
    }


    /* =================================================== DELETE =================================================== */
    @GetMapping("/delete-clazz")
    public String deleteClazz(
            @SessionAttribute(value = "user", required = false) UserReadDTO userDTO,
            HttpServletRequest request,
            Model model,
            RedirectAttributes redirect) throws Exception {
        //check login
        if (ObjectUtils.isEmpty(userDTO)) {
            redirect.addAttribute("mess", "Làm ơn đăng nhập");
            return "redirect:/";
        }

        if (!userDTO.getRoleId().equals(Constants.ROLE_ADMIN)) {
            redirect.addAttribute("mess", "bạn không đủ quyền");
            return "redirect:/";
        }
        Long Id = Long.parseLong(request.getParameter("id"));

        if (Objects.nonNull(Id)) {
            ClazzReadDTO clazzReadDTO =
                    clazzService.getDTOById(Id,
                            List.of(COURSE_SEMESTER, STAFF, USER));

            model.addAttribute("clazz", clazzReadDTO);
            if (!ObjectUtils.isEmpty(clazzReadDTO.getStatusClazz()) && clazzReadDTO.getStatusClazz().equals(DEPLOY_CLAZZ.getStringValue())) {
                redirect.addAttribute("mess", "Lớp học đang triển khai không thể thao tác");
                return "redirect:/clazz";
            }

        }


        String result = clazzService.deleteClazz(Id);
        if (result.equals("success")) {
            redirect.addAttribute("mess", "Xóa class room thành công");
            return "redirect:/clazz";
        } else {
            model.addAttribute("mess", "Xóa class room thất bại");
            return "clazz/add-clazz";
        }
    }


    @PostMapping("/finish-class")
    public String finishClass(Model model,
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
