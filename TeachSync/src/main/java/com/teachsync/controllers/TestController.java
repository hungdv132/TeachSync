package com.teachsync.controllers;

import com.teachsync.dtos.answer.AnswerCreateDTO;
import com.teachsync.dtos.answer.AnswerReadDTO;
import com.teachsync.dtos.clazzTest.ClazzTestReadDTO;
import com.teachsync.dtos.course.CourseReadDTO;
import com.teachsync.dtos.memberTestRecord.MemberTestRecordReadDTO;
import com.teachsync.dtos.question.QuestionCreateDTO;
import com.teachsync.dtos.question.QuestionReadDTO;
import com.teachsync.dtos.test.MarkTestDTO;
import com.teachsync.dtos.test.TestCreateDTO;
import com.teachsync.dtos.test.TestReadDTO;
import com.teachsync.dtos.user.UserReadDTO;
import com.teachsync.entities.*;
import com.teachsync.repositories.*;
import com.teachsync.services.answer.AnswerService;
import com.teachsync.services.clazz.ClazzService;
import com.teachsync.services.clazzMember.ClazzMemberService;
import com.teachsync.services.clazzTest.ClazzTestService;
import com.teachsync.services.course.CourseService;
import com.teachsync.services.memberTestRecord.MemberTestRecordService;
import com.teachsync.services.question.QuestionService;
import com.teachsync.services.test.TestService;
import com.teachsync.utils.Constants;
import com.teachsync.utils.MiscUtil;
import com.teachsync.utils.enums.QuestionType;
import com.teachsync.utils.enums.Status;
import com.teachsync.utils.enums.TestType;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.Subject;
import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.teachsync.utils.enums.DtoOption.*;

@Controller
public class TestController {
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    private QuestionService questionService;
    @Autowired
    AnswerRepository answerRepository;
    @Autowired
    private AnswerService answerService;
    @Autowired
    CourseRepository courseRepository;
    @Autowired
    private CourseService courseService;
    @Autowired
    TestRepository testRepository;
    @Autowired
    private TestService testService;
    @Autowired
    private TestRecordRepository testRecordRepository;
    @Autowired
    private MemberTestRecordService memberTestRecordService;
    @Autowired
    private MemberTestRecordRepository memberTestRecordRepository;
    @Autowired
    private ClazzTestService clazzTestService;
    @Autowired
    private ClazzMemberService clazzMemberService;

    @Autowired
    private ClazzRepository clazzRepository;

    @Autowired
    private ClazzTestRepository clazzTestRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ClazzMemberRepository clazzMemberRepository;

    @Autowired
    private MiscUtil miscUtil;

    @GetMapping("/create-test")
    public String createTestViews(Model model, HttpSession session) {
        UserReadDTO user = (UserReadDTO) session.getAttribute("user");
        if (user == null || !user.getRoleId().equals(Constants.ROLE_ADMIN)) {
            return "redirect:/";
        }
        List<Course> lst = courseRepository.findAllByStatusNot(Status.DELETED);
        model.addAttribute("lstCourse", lst);
        return "test/create-test";
    }

    @PostMapping("/process-question")
    public String processQuestion(
            HttpSession session,
            @RequestParam Long courseId,
            @RequestParam TestType testType,
            @RequestParam Integer timeLimit,
            @RequestParam Integer numQuestions,
            @RequestParam QuestionType questionType,
            @RequestParam Map<String, String> requestParams,
            @SessionAttribute(value = "user", required = false) UserReadDTO userDTO) {

        if (userDTO == null || !userDTO.getRoleId().equals(Constants.ROLE_ADMIN)) {
            return "redirect:/";
        }

        try {
            CourseReadDTO courseDTO = courseService.getDTOById(courseId, List.of(TEST_LIST));

            TestCreateDTO createDTO = new TestCreateDTO();

            createDTO.setCourseId(courseId);
//            createDTO.setTestName(courseDTO.getCourseAlias().concat(" - test " + courseDTO.getTestList().size()));
            createDTO.setTestName(courseRepository.findById(courseId).orElse(null).getCourseName());
            createDTO.setTestType(testType);
            createDTO.setTestDesc(questionType.getStringValue());
            createDTO.setNumQuestion(numQuestions);
            createDTO.setQuestionType(questionType);
            createDTO.setTimeLimit(timeLimit);
            createDTO.setCreatedBy(userDTO.getId());
            switch (testType) {
                case FIFTEEN_MINUTE -> {
                    createDTO.setTimeLimit(15);
                    createDTO.setMinScore(1.0);
                    createDTO.setTestWeight(1);
                }

                case MIDTERM -> {
                    createDTO.setMinScore(1.0);
                    createDTO.setTestWeight(3);
                }

                case FINAL -> {
                    createDTO.setMinScore(4.0);
                    createDTO.setTestWeight(5);
                }
            }

            switch (questionType) {
                case ESSAY -> {
                    List<QuestionCreateDTO> questionCreateDTOList = new ArrayList<>();
                    for (int i = 0; i < numQuestions; i++) {
                        QuestionCreateDTO questionCreateDTO = new QuestionCreateDTO();
                        questionCreateDTO.setQuestionDesc(requestParams.get("essayQuestion" + i));

                        questionCreateDTO.setQuestionType(QuestionType.ESSAY);
                        questionCreateDTO.setCreatedBy(userDTO.getId());
                        questionCreateDTO.setQuestionScore(1.0);

                        questionCreateDTOList.add(questionCreateDTO);
                    }
                    createDTO.setQuestionList(questionCreateDTOList);
                }

                case MULTIPLE -> {
                    List<QuestionCreateDTO> questionCreateDTOList = new ArrayList<>();
                    for (int i = 0; i < numQuestions; i++) {
                        QuestionCreateDTO questionCreateDTO = new QuestionCreateDTO();
                        questionCreateDTO.setQuestionDesc(requestParams.get("multipleChoiceQuestion" + i));
                        questionCreateDTO.setQuestionType(QuestionType.MULTIPLE);
                        questionCreateDTO.setCreatedBy(userDTO.getId());
                        questionCreateDTO.setQuestionScore(1.0);

                        List<AnswerCreateDTO> answerCreateDTOList = new ArrayList<>();
                        int numAnswer = Integer.parseInt(requestParams.get("numOptions" + i));
                        boolean isCorrect;
                        for (int j = 0; j < numAnswer; j++) {
                            AnswerCreateDTO answerCreateDTO = new AnswerCreateDTO();
                            answerCreateDTO.setAnswerDesc(requestParams.get("answer" + i + "-" + j));
                            isCorrect = requestParams.get("isCorrect" + i + "-" + j) != null;
                            answerCreateDTO.setIsCorrect(isCorrect);
                            if (isCorrect) {
                                answerCreateDTO.setAnswerScore(1.0);
                            } else {
                                answerCreateDTO.setAnswerScore(0.0);
                            }
                            answerCreateDTOList.add(answerCreateDTO);
                        }
                        questionCreateDTO.setAnswerList(answerCreateDTOList);
                        questionCreateDTOList.add(questionCreateDTO);
                    }

                    createDTO.setQuestionList(questionCreateDTOList);
                }
            }

            testService.createTestByDTO(createDTO);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Redirect to a success page or do any other necessary actions

        return "redirect:/";
    }


    @GetMapping("/edit-test")
    public String editTestView(
            Model model,
            @RequestParam("id") Long testId,
            @SessionAttribute(value = "user", required = false) UserReadDTO userDTO) {

        if (userDTO == null || !userDTO.getRoleId().equals(Constants.ROLE_ADMIN)) {
            return "redirect:/";
        }

        try {
            Test test = testRepository.findAllById(Collections.singleton(testId)).get(0);
            List<Question> lstQuestion = questionRepository.findAllByTestId(test.getId());
            HashMap<Question, List<Answer>> hm = new HashMap<>();
            for (Question qs : lstQuestion) {
                qs.setQuestionDesc(qs.getQuestionDesc().replace('\r', ' ').replace('\n', ' '));
                hm.put(qs, answerRepository.findAllByQuestionId(qs.getId()));
                System.out.println(qs.getQuestionDesc());
            }
            List<Course> lst = courseRepository.findAllByStatusNot(Status.DELETED);
            model.addAttribute("lstCourse", lst);
            model.addAttribute("testType", test.getTestType().getStringValue());
            model.addAttribute("questionType", test.getTestDesc());

            model.addAttribute("test", test);

            model.addAttribute("questionAnswer", hm);
            System.out.println("size cua hashmap: " + hm.size());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "test/edit-test";
    }

    @PostMapping("/update-answer")
    public String updateAnswer(
            Model model,
            @RequestParam("idTest") Long idTest,
            @RequestParam("idQuestion") String idQuestionS,
            @RequestParam("testType") String testType,
            @RequestParam("timeLimit") Integer timeLimit,
            @RequestParam(value = "numQuestions", required = false) Integer numQuestions,
            @RequestParam("questionAll") String questionAll,
            @RequestParam Map<String, String> requestParams,
            @SessionAttribute(value = "user", required = false) UserReadDTO userDTO) {

        if (userDTO == null || !userDTO.getRoleId().equals(Constants.ROLE_ADMIN)) {
            return "redirect:/";
        }

        try {
            Long idQuestion = Long.parseLong(idQuestionS);
//        TODO: This function is update answer, not update test, or else rename function
            Test test = testRepository.findById(idTest).orElse(null);

            if (testType.equals("FIFTEEN_MINUTE")) {
                test.setMinScore(1.0);
                test.setTestWeight(1);
            } else if (testType.equals("MIDTERM")) {
                test.setMinScore(1.0);
                test.setTestWeight(3);
            } else {
                test.setMinScore(4.0);
                test.setTestWeight(5);
            }
            test.setStatus(Status.UPDATED);
            test.setTimeLimit(timeLimit);
//        test.setCourseId(Long.parseLong(courseName));
            testRepository.save(test);


            Question question = questionService.getById(idQuestion);
            if (question == null) {
                throw new IllegalArgumentException("Update error. No Question found with id: " + idQuestion);
            }
            question.setQuestionDesc(questionAll);
            questionRepository.save(question);

        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Test test = testRepository.findAllById(Collections.singleton(idTest)).get(0);
            List<Question> lstQuestion = questionRepository.findAllByTestId(test.getId());
            HashMap<Question, List<Answer>> hm = new HashMap<>();
            for (Question qs : lstQuestion) {
                qs.setQuestionDesc(qs.getQuestionDesc().replace('\r', ' ').replace('\n', ' '));
                hm.put(qs, answerRepository.findAllByQuestionId(qs.getId()));
                System.out.println(qs.getQuestionDesc());
            }
            List<Course> lst = courseRepository.findAllByStatusNot(Status.DELETED);
            model.addAttribute("lstCourse", lst);
            model.addAttribute("testType", test.getTestType().getStringValue());
            model.addAttribute("questionType", test.getTestDesc());

            model.addAttribute("test", test);

            model.addAttribute("questionAnswer", hm);
            System.out.println("size cua hashmap: " + hm.size());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "test/edit-test";
    }

    @GetMapping("/tests")
    public String listTestPage(@RequestParam(value = "page", required = false) Integer page, Model model,
                               @SessionAttribute(value = "user", required = false) UserReadDTO userDTO) {
        /* Test thuộc về Course, nhưng nếu muốn làm bài thì phải dùng ClazzTest nối tới Clazz và ClazzMember
        để biết có học lớp môn này không để cho phép làm */

        if (userDTO == null || !userDTO.getRoleId().equals(Constants.ROLE_ADMIN)) {
            return "redirect:/";
        }

        try {
            if (page == null || page < 0) {
                page = 0;
            }
//            Pageable pageable = miscUtil.makePaging(page, 3, "id", true);
            PageRequest pageable = PageRequest.of(page, 3);
            Page<Test> testPage = testRepository.findAllByOrderByCreatedAtDesc(pageable);

            model.addAttribute("tests", testPage);
            model.addAttribute("pageNo", testPage.getPageable().getPageNumber());
            model.addAttribute("pageTotal", testPage.getTotalPages());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "test/list-test";
    }

    @GetMapping("/searchbycourse")
    public String searchByCourse(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam("searchText") String courseName,
            Model model,
            @SessionAttribute(value = "user", required = false) UserReadDTO userDTO) {
        if (userDTO == null || !userDTO.getRoleId().equals(Constants.ROLE_ADMIN)) {
            return "redirect:/";
        }
        if (page == null) {
            page = 0;
        }
        if (page < 0) {
            page = 0;
        }
        PageRequest pageable = PageRequest.of(page, 3);
        Page<Test> tests = testRepository.findByTestNameContainingOrderByCreatedAtDesc(courseName, pageable);
        model.addAttribute("tests", tests);
        model.addAttribute("pageNo", tests.getPageable().getPageNumber());
        model.addAttribute("pageTotal", tests.getTotalPages());
        model.addAttribute("searchText", courseName);
        return "test/list-test-search";
    }

    @GetMapping("/take-test")
    public String takeTestPage(
            Model model,
            @RequestParam("clazzId") Long clazzId,
            @RequestParam("clazzTestId") Long clazzTestId,
            @SessionAttribute(value = "user", required = false) UserReadDTO userDTO) {

        if (userDTO == null) {
            return "redirect:/";
        }

        try {
            ClazzMember member = clazzMemberService.getByClazzIdAndUserId(clazzId, userDTO.getId());
            if (member == null) {
                return "redirect:/";
            }

            ClazzTestReadDTO clazzTestDTO =
                    clazzTestService.getDTOByClazzIdAndTestId(clazzId, clazzTestId, List.of(TEST, QUESTION_LIST, ANSWER_LIST));
            if (!(clazzTestDTO != null && clazzTestDTO.getTest() != null)) {
                throw new IllegalArgumentException("Không thấy bài test nào với Id: " + clazzTestId);
            }
            TestReadDTO testDTO = clazzTestDTO.getTest();


            Map<QuestionReadDTO, List<AnswerReadDTO>> lstQs = new HashMap<>();
            if (testDTO.getTestDesc().equals("MULTIPLE")) {
                lstQs = testDTO.getQuestionList().stream()
                        .collect(Collectors.toMap(Function.identity(), QuestionReadDTO::getAnswerList));
            } else {
                for (QuestionReadDTO key : testDTO.getQuestionList()) {
                    lstQs.put(key, null);
                }
            }


            model.addAttribute("idClazzTest", clazzTestDTO.getId());
            model.addAttribute("idTest", testDTO.getId());
            model.addAttribute("idMember", member.getId());
            model.addAttribute("test", testDTO);
            model.addAttribute("hmQA", lstQs);
            List<Status> lstStt = new ArrayList<>();
            lstStt.add(Status.DONE);
            lstStt.add(Status.ONGOING);

//            MemberTestRecord memberTestRecord = memberTestRecordService.getByMemberIdAndClazzTestId(member.getId(), clazzTestId);
            List<MemberTestRecord> mtr = memberTestRecordRepository.findByMemberIdAndClazzTestIdAndStatusIn(member.getId(), clazzTestId, lstStt);
            if (mtr != null) {
                System.out.println("Người dùng này đang làm bài");
                return "redirect:/";
            }

            /* Bắt đầu làm bài và lưu thời gian làm */
            MemberTestRecord memberTestRecord = new MemberTestRecord();

            memberTestRecord.setMemberId(member.getId());
            memberTestRecord.setClazzTestId(clazzTestDTO.getId());
            memberTestRecord.setStartAt(LocalDateTime.now());
            memberTestRecord.setStatus(Status.ONGOING);

            memberTestRecordRepository.save(memberTestRecord);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "test/dothetest";
    }

    @PostMapping("/submitTest")
    public String submitTest(
            Model model,
            @RequestParam("idMember") Long idMember,
            @RequestParam("idTest") Long idTest,
            @RequestParam("idClazzTest") Long idClazzTest,
            @RequestParam("questionType") QuestionType questionType,
            @RequestParam Map<String, String> requestParams,
            @SessionAttribute(value = "user", required = false) UserReadDTO userDTO) {
        try {
            TestReadDTO testDTO = testService.getDTOById(idTest, List.of(QUESTION_LIST, ANSWER_LIST));

            List<QuestionReadDTO> questionDTOList = testDTO.getQuestionList();

            MemberTestRecord memberTestRecord =
                    memberTestRecordService.getByMemberIdAndClazzTestId(idMember, idClazzTest);

            List<TestRecord> testRecordList = new ArrayList<>();
            TestRecord testRecord;
            for (QuestionReadDTO questionDTO : questionDTOList) {
                testRecord = new TestRecord();
                testRecord.setMemberTestRecordId(memberTestRecord.getId());
                testRecord.setStatus(Status.CREATED);

                switch (questionType) {
                    case MULTIPLE -> {
                        Long answerId = Long.parseLong(requestParams.get("question" + questionDTO.getId()));
                        Answer as = answerService.getById(answerId);
                        testRecord.setQuestionId(questionDTO.getId());
                        testRecord.setAnswerId(as.getId());
                        testRecord.setScore(as.getAnswerScore());
                    }

                    case ESSAY -> {
                        String essayAnswer = requestParams.get("question" + questionDTO.getId());
                        testRecord.setQuestionId(questionDTO.getId());
                        testRecord.setAnswerTxt(essayAnswer);
                    }
                }

                testRecordList.add(testRecord);
            }
            testRecordList = testRecordRepository.saveAll(testRecordList);

            if (questionType == QuestionType.MULTIPLE) {
                Double totalScore = 0.0;
                for (QuestionReadDTO questionDTO : questionDTOList) {
                    totalScore += questionDTO.getQuestionScore();
                }

                Double earnedScore = 0.0;
                for (TestRecord tR : testRecordList) {
                    earnedScore += tR.getScore();
                }

                Double finalScore = (earnedScore / totalScore) * 10.0;
                memberTestRecord.setScore(finalScore);
            }

            memberTestRecord.setSubmitAt(LocalDateTime.now());
            memberTestRecord.setStatus(Status.DONE);
            memberTestRecord.setUpdatedAt(LocalDateTime.now());
            memberTestRecord.setUpdatedBy(userDTO.getId());

            memberTestRecordRepository.save(memberTestRecord);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/";
    }

    @GetMapping("/test-session")
    public String listTestSession(@RequestParam(value = "page", required = false) Integer page, Model model, @SessionAttribute(value = "user", required = false) UserReadDTO userDTO) {
        if (userDTO == null || !userDTO.getRoleId().equals(Constants.ROLE_ADMIN)) {
            return "redirect:/";
        }
        if (page == null || page < 0) {
            page = 0;
        }
        Pageable pageable = miscUtil.makePaging(page, 3, "StartAt", false);
        try {
            Page<MemberTestRecordReadDTO> mTRDTO =
                    memberTestRecordService.getPageAllDTO(
                            pageable,
                            List.of(MEMBER, USER, CLAZZ, COURSE_SEMESTER, COURSE_NAME));

            model.addAttribute("testSessions", mTRDTO.getContent());
            model.addAttribute("pageNo", mTRDTO.getPageable().getPageNumber());
            model.addAttribute("pageTotal", mTRDTO.getTotalPages());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "test/list-test-session";
    }

    @GetMapping("/search-test-session")
    public String searchTestSession(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam("searchText") String name,
            @RequestParam("searchType") String searchType,
            Model model, @SessionAttribute(value = "user", required = false) UserReadDTO userDTO) throws Exception {
        if (userDTO == null || !userDTO.getRoleId().equals(Constants.ROLE_ADMIN)) {
            return "redirect:/";
        }
        if (page == null || page < 0) {
            page = 0;
        }

        PageRequest pageable = PageRequest.of(page, 3);
        Page<MemberTestRecordReadDTO> mTRDTO = null;
        Page<MemberTestRecord> tests;
        List<Long> lstId = new ArrayList<>();
        if (searchType.equals("class")) {
            List<Clazz> clazz = clazzRepository.findAllByClazzNameContaining(name);
            List<Long> lstIdClazz = clazz.stream().map(Clazz::getId).collect(Collectors.toList());
            List<ClazzTest> clazzTests = clazzTestRepository.findAllByClazzIdIn(lstIdClazz);
            List<Long> lstIdClazzTest = clazzTests.stream().map(ClazzTest::getId).collect(Collectors.toList());
            mTRDTO = memberTestRecordService.getPageByClassDTO(pageable, List.of(MEMBER, USER, CLAZZ, COURSE_SEMESTER, COURSE_NAME), lstIdClazzTest);
        } else if (searchType.equals("subject")) {
            List<Course> courses = courseRepository.findAllByCourseNameContainsAndStatusNot(name, Status.DELETED);
            List<Long> lstIdCourse = courses.stream().map(Course::getId).collect(Collectors.toList());
            List<Test> lstTests = testRepository.findAllByCourseIdInAndStatusNot(lstIdCourse, Status.DELETED);
            List<Long> lstTestId = lstTests.stream().map(Test::getId).collect(Collectors.toList());
            List<ClazzTest> lstClazzTest = clazzTestRepository.findAllByTestIdIn(lstTestId);
            List<Long> lstIdClazzTest = lstClazzTest.stream().map(ClazzTest::getId).collect(Collectors.toList());
            mTRDTO = memberTestRecordService.getPageByClassDTO(pageable, List.of(MEMBER, USER, CLAZZ, COURSE_SEMESTER, COURSE_NAME), lstIdClazzTest);
        } else {
            List<User> userList = userRepository.findAllByUsernameContaining(name);
            List<Long> lstIdUser = userList.stream().map(User::getId).collect(Collectors.toList());
            List<ClazzMember> members = clazzMemberRepository.findAllByUserIdInAndStatusNot(lstIdUser, Status.DELETED);
            List<Long> lstIdClazzMember = members.stream().map(ClazzMember::getId).collect(Collectors.toList());
            mTRDTO = memberTestRecordService.getPageByUserDTO(pageable, List.of(MEMBER, USER, CLAZZ, COURSE_SEMESTER, COURSE_NAME), lstIdClazzMember);

        }


        model.addAttribute("testSessions", mTRDTO.getContent());
        model.addAttribute("pageNo", mTRDTO.getPageable().getPageNumber());
        model.addAttribute("pageTotal", mTRDTO.getTotalPages());
        model.addAttribute("searchText", name);
        model.addAttribute("searchType", searchType);
        return "test/list-test-session-search";
    }

    @GetMapping("/update-test-session")
    public String updateTestSession(
            Model model,
            HttpSession session,
            @RequestParam("idSession") Long idSession,
            @RequestParam("newStatus") String newStatus,
            @SessionAttribute(value = "user", required = false) UserReadDTO userDTO) {
        if (userDTO == null || !userDTO.getRoleId().equals(Constants.ROLE_ADMIN)) {
            return "redirect:/";
        }


//        TestSession testSession = testSessionRepository.findById(Long.parseLong(idSession)).orElse(null);
        MemberTestRecord mtr = memberTestRecordRepository.findById(idSession).orElse(null);
        if (newStatus.equals(Status.ALLOWED_REDO.getStringValue())) {
            mtr.setStatus(Status.ALLOWED_REDO);
        } else if (newStatus.equals(Status.SUSPENDED.getStringValue())) {
            mtr.setStatus(Status.SUSPENDED);
        }

        mtr.setUpdatedAt(LocalDateTime.now());
        mtr.setUpdatedBy(userDTO.getId());

        memberTestRecordRepository.save(mtr);

        Pageable pageable = miscUtil.makePaging(0, 3, "StartAt", false);
        try {
            Page<MemberTestRecordReadDTO> mTRDTO =
                    memberTestRecordService.getPageAllDTO(pageable, List.of(MEMBER, USER, CLAZZ, COURSE_SEMESTER, COURSE_NAME));

            model.addAttribute("testSessions", mTRDTO.getContent());
            model.addAttribute("pageNo", mTRDTO.getPageable().getPageNumber());
            model.addAttribute("pageTotal", mTRDTO.getTotalPages());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "test/list-test-session";
    }

    @GetMapping("/test-score")
    public String testScore(
            Model model,
            HttpSession session,
            @RequestParam("idClazz") Long idClazz,
            @RequestParam("idTest") Long idTest,
            @SessionAttribute(value = "user", required = false) UserReadDTO userDTO) {
        if (userDTO == null || !userDTO.getRoleId().equals(Constants.ROLE_TEACHER)) {
            return "redirect:/";
        }
        try {
            ClazzTestReadDTO clazzTestDTO =
                    clazzTestService.getDTOByClazzIdAndTestId(idClazz, idTest, List.of(TEST));

            List<MemberTestRecordReadDTO> memberTestRecordDTOList =
                    memberTestRecordService.getAllDTOByClazzTestId(
                            clazzTestDTO.getId(),
                            List.of(MEMBER, USER));

            if (clazzTestDTO.getTest().getQuestionType() == QuestionType.MULTIPLE) {
                model.addAttribute("testType", "multipleChoice");
            } else {
                model.addAttribute("testType", "essay");
            }

            model.addAttribute("test", clazzTestDTO.getTest());
            model.addAttribute("memberTestRecordList", memberTestRecordDTOList);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "test/testscore-class";
    }

    @GetMapping("/mark-essay")
    public String markEssay(Model model,
                            HttpSession session,
                            @RequestParam("memberTestRecordId") Long memberTestRecordId,
                            @SessionAttribute(value = "user", required = false) UserReadDTO userDTO) {
        if (userDTO == null || !userDTO.getRoleId().equals(Constants.ROLE_TEACHER)) {
            return "redirect:/";
        }

        MemberTestRecord mtr = memberTestRecordRepository.findAllByIdAndStatus(memberTestRecordId, Status.DONE).orElse(null);
        List<TestRecord> testRecords = testRecordRepository.findAllByMemberTestRecordId(memberTestRecordId);
        List<MarkTestDTO> markTestDTOS = new ArrayList<>();
        for (TestRecord testRecord : testRecords) {
            Question question = questionRepository.findAllById(testRecord.getQuestionId()).orElse(null);
            markTestDTOS.add(new MarkTestDTO(question.getQuestionDesc(), testRecord.getAnswerTxt(), testRecord.getId()));
        }
        model.addAttribute("markTestDTOS", markTestDTOS);
        model.addAttribute("memberTestRecordId", memberTestRecordId);
        return "test/mark-essay";
    }

    @PostMapping("/mark")
    public String mark(
            Model model,
            @RequestParam("memberTestRecordId") Long memberTestRecordId,
            @RequestParam Map<String, String> requestParams,
            @SessionAttribute(value = "user", required = false) UserReadDTO userDTO) throws Exception {

        MemberTestRecord memberTestRecord = memberTestRecordRepository.findAllByIdAndStatus(memberTestRecordId, Status.DONE).orElse(null);
        ClazzTest clazzTest = clazzTestService.getById(memberTestRecord.getClazzTestId());
        List<TestRecord> testRecords = testRecordRepository.findAllByMemberTestRecordId(memberTestRecordId);
        List<Question> questions = questionRepository.findAllByTestIdAndStatusNot(clazzTest.getTestId(), Status.DELETED);

        double totalScore = 0;
        double maxScore = 0;
        for (Question qs : questions) {
            maxScore = maxScore + qs.getQuestionScore();
        }
        for (TestRecord testRecord : testRecords) {
            Double score = Double.parseDouble(requestParams.get("scores[" + testRecord.getId() + "]"));
            testRecord.setScore(score);
            totalScore = totalScore + score;
            testRecordRepository.save(testRecord);
        }
        memberTestRecord.setScore((totalScore / maxScore) * 10);
        memberTestRecordRepository.save(memberTestRecord);
        return "redirect:/";
    }


    @GetMapping("/api/getLstAnswer")
    @ResponseBody
    public List<Answer> getListAnswer(@RequestParam Long questionId) {
        List<Answer> lst = answerRepository.findAllByQuestionIdAndStatusNot(questionId, Status.DELETED);
        return lst;
    }

    @PostMapping("/api/editAnswer")
    @ResponseBody
    public boolean editAnswer(@RequestParam Long id, @RequestParam String content, @RequestParam boolean isTrue) {
        Answer answer = answerRepository.findByIdAndStatusNot(id, Status.DELETED).orElse(null);
        answer.setAnswerDesc(content);
        List<Answer> lstAnswer = answerRepository.findAllByQuestionIdAndStatusNot(answer.getQuestionId(), Status.DELETED);
        if (isTrue) {
            List<Answer> lstTr = lstAnswer.stream().map(answer1 -> {
                if (answer1.getIsCorrect()) {
                    answer1.setIsCorrect(false);
                }
                return answer1;
            }).collect(Collectors.toList());

            answerRepository.saveAll(lstTr);
        }
        answer.setIsCorrect(isTrue);
        answerRepository.save(answer);
        return true;
    }

    @PostMapping("api/deleteAnswer")
    @ResponseBody
    public boolean changeStatusAnswerToDelete(@RequestParam Long id) {
        Answer answer = answerRepository.findByIdAndStatusNot(id, Status.DELETED).orElse(null);
        answer.setStatus(Status.DELETED);
        answerRepository.save(answer);
        return true;
    }

    @PostMapping("api/addNewAnswer")
    @ResponseBody
    public boolean addNewAnswer(@RequestParam Long id, @RequestParam String answerDesc, @RequestParam boolean isCorrect) {
        Answer answer = new Answer(id, answerDesc, 1.0, isCorrect);
        List<Answer> lstAnswer = answerRepository.findAllByQuestionIdAndStatusNot(answer.getQuestionId(), Status.DELETED);
        if (isCorrect) {
            List<Answer> lstTr = lstAnswer.stream().map(answer1 -> {
                if (answer1.getIsCorrect()) {
                    answer1.setIsCorrect(false);
                }
                return answer1;
            }).collect(Collectors.toList());

            answerRepository.saveAll(lstTr);
        }
        answer.setStatus(Status.CREATED);
        answerRepository.save(answer);
        return true;
    }
}
