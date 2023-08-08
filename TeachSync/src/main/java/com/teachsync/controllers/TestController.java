package com.teachsync.controllers;

import com.teachsync.dtos.answer.AnswerCreateDTO;
import com.teachsync.dtos.answer.AnswerReadDTO;
import com.teachsync.dtos.clazzTest.ClazzTestReadDTO;
import com.teachsync.dtos.course.CourseReadDTO;
import com.teachsync.dtos.memberTestRecord.MemberTestRecordReadDTO;
import com.teachsync.dtos.question.QuestionCreateDTO;
import com.teachsync.dtos.question.QuestionReadDTO;
import com.teachsync.dtos.test.TestCreateDTO;
import com.teachsync.dtos.test.TestReadDTO;
import com.teachsync.dtos.test.TestScoreDTO;
import com.teachsync.dtos.user.UserReadDTO;
import com.teachsync.entities.*;
import com.teachsync.repositories.*;
import com.teachsync.services.answer.AnswerService;
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
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

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
            createDTO.setTestName(courseDTO.getCourseAlias().concat(" - test " + courseDTO.getTestList().size()));
            createDTO.setTestType(testType);
            createDTO.setTestDesc(courseDTO.getCourseName().concat(" - test " + courseDTO.getTestList().size()));
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

                        }
                        questionCreateDTO.setAnswerList(answerCreateDTOList);
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

//        if (userDTO == null || userDTO.getRoleId().equals(Constants.ROLE_ADMIN)) {
//            return "redirect:/";
//        }

        try {
            List<CourseReadDTO> courseDTOList = courseService.getAllDTO(null);

            model.addAttribute("courseList", courseDTOList);

            TestReadDTO testDTO = testService.getDTOById(testId, List.of(QUESTION_LIST, ANSWER_LIST));

            model.addAttribute("test", testDTO);

            Map<QuestionReadDTO, List<AnswerReadDTO>> questionAnswerListMap =
                    testDTO.getQuestionList().stream()
                            .collect(Collectors.toMap(Function.identity(), QuestionReadDTO::getAnswerList));

            model.addAttribute("questionAnswer", questionAnswerListMap);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "test/edit-test";
    }

    @PostMapping("/update-answer")
    public String updateAnswer(
            Model model,
            @RequestParam("courseName") String courseName,
            @RequestParam("idTest") Long idTest,
            @RequestParam("idQuestion") Long idQuestion,
            @RequestParam("testType") TestType testType,
            @RequestParam("timeLimit") Integer timeLimit,
            @RequestParam("numQuestions") Integer numQuestions,
            @RequestParam("questionType") String questionType,
            @RequestParam Map<String, String> requestParams,
            @SessionAttribute(value = "user", required = false) UserReadDTO userDTO) {

//        if (userDTO == null || userDTO.getRoleId().equals(Constants.ROLE_ADMIN)) {
//            return "redirect:/";
//        }

//        TODO: This function is update answer, not update test, or else rename function
//        Test test = testRepository.findById(Long.parseLong(idTest)).orElse(null);
//
//        test.setTestName(testType);
//        if (testType.equals("15min")) {
//            test.setMinScore(1.0);
//            test.setTestWeight(1);
//        } else if (testType.equals("midterm")) {
//            test.setMinScore(1.0);
//            test.setTestWeight(3);
//        } else {
//            test.setMinScore(4.0);
//            test.setTestWeight(5);
//        }
//        test.setStatus(Status.UPDATED);
//        test.setTimeLimit(timeLimit);
//        test.setCourseId(Long.parseLong(courseName));
//        testRepository.save(test);

        try {
            Question question = questionService.getById(idQuestion);
            if (question == null) {
                throw new IllegalArgumentException("Update error. No Question found with id: " + idQuestion);
            }

            if (questionType.equals("multipleChoice")) {
                answerService.deleteAllByQuestionId(question.getId());

                int numAnswer = Integer.parseInt(requestParams.get("numOfOptions"));

                List<AnswerCreateDTO> answerCreateDTOList = new ArrayList<>();
                for (int i = 1; i < numAnswer; i++) {
                    AnswerCreateDTO answerCreateDTO = new AnswerCreateDTO();
                    answerCreateDTO.setQuestionId(question.getId());
                    answerCreateDTO.setAnswerDesc(requestParams.get("answer" + i));
                    answerCreateDTO.setIsCorrect(requestParams.get("correctAnswer" + i) != null);
                    answerCreateDTO.setCreatedBy(userDTO.getId());

                    answerCreateDTOList.add(answerCreateDTO);
                }

                answerService.createBulkAnswerByDTO(answerCreateDTOList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Redirect to a success page or do any other necessary actions

        return "redirect:/";
    }

    @GetMapping("/tests")
    public String listTestPage(@RequestParam(value = "page", required = false) Integer page, Model model) {
        /* Test thuộc về Course, nhưng nếu muốn làm bài thì phải dùng ClazzTest nối tới Clazz và ClazzMember
        để biết có học lớp môn này không để cho phép làm */

        try {
            if (page == null || page < 0) {
                page = 0;
            }
            Pageable pageable = miscUtil.makePaging(page, 3, "id", true);

            Page<Test> testPage = testService.getPageAll(pageable);

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
            @RequestParam("courseName") String courseName,
            Model model) {
        try {
            if (page == null || page < 0) {
                page = 0;
            }
            Pageable pageable = miscUtil.makePaging(page, 3, "id", true);

            List<Course> courseList = courseService.getAllByNameContains(courseName);
            if (courseList == null) {
                throw new IllegalArgumentException("No course found with name containing: " + courseName);
            }

            Set<Long> courseIdSet = courseList.stream().map(BaseEntity::getId).collect(Collectors.toSet());

            Page<Test> testPage = testService.getPageAllByCourseIdIn(pageable, courseIdSet);

            model.addAttribute("tests", testPage);
            model.addAttribute("pageNo", testPage.getPageable().getPageNumber());
            model.addAttribute("pageTotal", testPage.getTotalPages());
            model.addAttribute("searchText", courseName);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "test/list-test";
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
                throw new AccessDeniedException("Bạn không phải là thành viên của lớp học này để làm bài này.");
            }

            ClazzTestReadDTO clazzTestDTO =
                    clazzTestService.getDTOById(clazzTestId, List.of(TEST, QUESTION_LIST, ANSWER_LIST));
            if (!(clazzTestDTO != null && clazzTestDTO.getTest() != null)) {
                throw new IllegalArgumentException("Không thấy bài test nào với Id: " + clazzTestId);
            }
            TestReadDTO testDTO = clazzTestDTO.getTest();

            Map<QuestionReadDTO, List<AnswerReadDTO>> lstQs =
                    testDTO.getQuestionList().stream()
                            .collect(Collectors.toMap(Function.identity(), QuestionReadDTO::getAnswerList));

            model.addAttribute("idClazzTest", clazzTestDTO.getId());
            model.addAttribute("idTest", testDTO.getId());
            model.addAttribute("idMember", member.getId());
            model.addAttribute("test", testDTO);
            model.addAttribute("hmQA", lstQs);

            MemberTestRecord memberTestRecord =
                    memberTestRecordService.getByMemberIdAndClazzTestId(member.getId(), clazzTestId);

            if (memberTestRecord != null) {
                /* Đã làm bài, không cho làm nữa */
                return "redirect:/";
            }

            /* Bắt đầu làm bài và lưu thời gian làm */
            memberTestRecord = new MemberTestRecord();

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

                switch (questionType) {
                    case MULTIPLE -> {
                        Long answerId = Long.parseLong(requestParams.get("question" + questionDTO.getId()));
                        Answer as = answerService.getById(answerId);
                        testRecord.setAnswerId(as.getId());
                        testRecord.setScore(as.getAnswerScore());
                    }

                    case  ESSAY -> {
                        String essayAnswer = requestParams.get("question" + questionDTO.getId());
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

                Double finalScore = (earnedScore / totalScore ) * 10.0;
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
    public String listTestSession(@RequestParam(value = "page", required = false) Integer page, Model model, HttpSession session) {
        if (page == null || page < 0) {
            page = 0;
        }
        Pageable pageable = miscUtil.makePaging(page, 3, "StartAt", false);
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

    @GetMapping("/search-test-session")
    public String searchTestSession(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam("searchText") String name,
            @RequestParam("searchType") String searchType,
            Model model) {
//        if (page == null || page < 0) {
//            page = 0;
//        }
//
//        PageRequest pageable = PageRequest.of(page, 3);
//
//        Page<TestSession> tests;
//        if (searchType.equals("class")) {
//            tests = testSessionRepository.findAllByClazzContainingOrderByStartDateDesc(pageable, name);
//        } else if (searchType.equals("subject")) {
//            tests = testSessionRepository.findAllBySubjectContainingOrderByStartDateDesc(pageable, name);
//        } else {
//            tests = testSessionRepository.findAllByUsernameContainingOrderByStartDate(pageable, name);
//        }
//
//        model.addAttribute("tests", tests);
//        model.addAttribute("pageNo", tests.getPageable().getPageNumber());
//        model.addAttribute("pageTotal", tests.getTotalPages());
//        model.addAttribute("searchText", name);
//        model.addAttribute("searchType", searchType);
        return "test/list-test-search";
    }

    @GetMapping("/update-test-session")
    public String updateTestSession(
            Model model,
            HttpSession session,
            @RequestParam("idSession") String idSession,
            @RequestParam("newStatus") String newStatus,
            @SessionAttribute(value = "user", required = false) UserReadDTO userDTO) {
//        UserReadDTO user = (UserReadDTO) session.getAttribute("user");
//
//        Date date = new Date();
//
//        TestSession testSession = testSessionRepository.findById(Long.parseLong(idSession)).orElse(null);
//
//        testSession.setStatus(Long.parseLong(newStatus));
//        testSession.setUpdateDate(date);
//        testSession.setUserUpdate(user.getUsername());
//
//        testSessionRepository.save(testSession);
//
//        PageRequest pageable = PageRequest.of(0, 3);
//
//        Page<TestSession> tests = testSessionRepository.findAllByOrderByStartDateDesc(pageable);
//
//        model.addAttribute("tests", tests);
//        model.addAttribute("pageNo", tests.getPageable().getPageNumber());
//        model.addAttribute("pageTotal", tests.getTotalPages());
//
        return "test/list-test-session";
    }

    @GetMapping("/test-score")
    public String testScore(
            Model model,
            HttpSession session,
            @RequestParam("idClazz") Long idClazz,
            @RequestParam("idTest") Long idTest) {
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


}
