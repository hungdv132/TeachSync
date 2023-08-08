package com.teachsync.controllers;

import com.teachsync.dtos.test.TestScoreDTO;
import com.teachsync.dtos.user.UserReadDTO;
import com.teachsync.entities.*;
import com.teachsync.repositories.*;
import com.teachsync.utils.Constants;
import com.teachsync.utils.enums.QuestionType;
import com.teachsync.utils.enums.Status;
import com.teachsync.utils.enums.TestType;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.*;

@Controller
public class TestController {

    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    AnswerRepository answerRepository;
    @Autowired
    CourseRepository courseRepository;
    @Autowired
    TestRepository testRepository;
    @Autowired
    TestRecord2Repository testRecord2Repository;
    @Autowired
    TestSessionRepository testSessionRepository;

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
    public String processQuestion(Model model, HttpSession session,
                                  @RequestParam("courseName") String courseName,
                                  @RequestParam("testType") TestType testType,
                                  @RequestParam("timeLimit") int timeLimit,
                                  @RequestParam("numQuestions") int numQuestions,
                                  @RequestParam("questionType") String questionType,
                                  @RequestParam Map<String, String> requestParams) {
        UserReadDTO user = (UserReadDTO) session.getAttribute("user");
        if (user == null || !user.getRoleId().equals(Constants.ROLE_ADMIN)) {
            return "redirect:/";
        }
        LocalDateTime currentDate = LocalDateTime.now();

        Test test = new Test();
        test.setCourseId(Long.parseLong(courseName));
        test.setTestName(courseRepository.findById(Long.parseLong(courseName)).orElse(null).getCourseName());
        test.setTestType(testType);
        test.setNumQuestion(numQuestions);
        test.setTimeLimit(timeLimit);
        test.setTestDesc(questionType);

        switch (testType) {
            case FIFTEEN_MINUTE -> {
                test.setMinScore(1.0);
                test.setTestWeight(1);
            }

            case MIDTERM -> {
                test.setMinScore(1.0);
                test.setTestWeight(3);
            }

            case FINAL -> {
                test.setMinScore(4.0);
                test.setTestWeight(5);
            }
        }

        test.setStatus(Status.CREATED);
        Test rsTest = testRepository.save(test);

        if (questionType.equals("essay")) {
            for (int i = 0; i < numQuestions; i++) {
                Question question = new Question();
                question.setTestId(rsTest.getId());
                question.setQuestionDesc(requestParams.get("essayQuestion" + i));
                question.setQuestionType(QuestionType.ESSAY);
                question.setStatus(Status.CREATED);

                question.setCreatedAt(currentDate);
                question.setCreatedBy(user.getId());
                questionRepository.save(question);
            }

        } else if (questionType.equals("multipleChoice")) {
            for (int i = 0; i < numQuestions; i++) {
                int numAnswer = Integer.parseInt(requestParams.get("numOptions" + i));
                Question question = new Question();
                question.setTestId(rsTest.getId());
                question.setQuestionDesc(requestParams.get("multipleChoiceQuestion" + i));
                question.setQuestionType(QuestionType.MULTIPLE);
                question.setStatus(Status.CREATED);
                question.setCreatedAt(currentDate);
                question.setCreatedBy(user.getId());
                Question result = questionRepository.save(question);
                for (int j = 0; j < numAnswer; j++) {
                    Answer answer = new Answer();
                    answer.setQuestionId(result.getId());
                    answer.setAnswerDesc(requestParams.get("answer" + i + "-" + j));
                    answer.setIsCorrect(requestParams.get("isCorrect" + i + "-" + j) != null);
                    answer.setStatus(Status.CREATED);
                    answer.setCreatedAt(currentDate);
                    answer.setCreatedBy(user.getId());
                    answerRepository.save(answer);
                }
            }
        }


        // Redirect to a success page or do any other necessary actions

        return "redirect:/";
    }


    @GetMapping("/edit-test")
    public String editTestView(Model model, HttpSession session, @RequestParam("id") String idTest) {
//        UserReadDTO user = (UserReadDTO) session.getAttribute("user");
//        if (user == null || user.getRoleId() != 1) {
//            return "redirect:/";
//        }

        Test test = testRepository.findAllById(Collections.singleton(Long.parseLong(idTest))).get(0);
        List<Question> lstQuestion = questionRepository.findAllByTestId(test.getId());
        HashMap<Question, List<Answer>> hm = new HashMap<>();
        for (Question qs : lstQuestion) {
            hm.put(qs, answerRepository.findAllByQuestionId(qs.getId()));
        }
        List<Course> lst = courseRepository.findAllByStatusNot(Status.DELETED);
        model.addAttribute("lstCourse", lst);

        model.addAttribute("test", test);

        model.addAttribute("questionAnswer", hm);
        System.out.println("size cua hashmap: " + hm.size());
        return "test/edit-test";

    }

    @PostMapping("/updateAnswer")
    public String updateAnswer(Model model, HttpSession session,
                               @RequestParam("courseName") String courseName,
                               @RequestParam("idTest") String idTest,
                               @RequestParam("idQuestion") String idQuestion,
                               @RequestParam("testType") String testType,
                               @RequestParam("timeLimit") int timeLimit,
                               @RequestParam("numQuestions") int numQuestions,
                               @RequestParam("questionType") String questionType,
                               @RequestParam Map<String, String> requestParams) {
//        UserReadDTO user = (UserReadDTO) session.getAttribute("user");
//        if (user == null || user.getRoleId() != 1) {
//            return "redirect:/";
//        }
        LocalDateTime currentDate = LocalDateTime.now();

        Test test = testRepository.findById(Long.parseLong(idTest)).orElse(null);
        test.setTestName(testType);
        if (testType.equals("15min")) {
            test.setMinScore(1.0);
            test.setTestWeight(1);
        } else if (testType.equals("midterm")) {
            test.setMinScore(1.0);
            test.setTestWeight(3);
        } else {
            test.setMinScore(4.0);
            test.setTestWeight(5);
        }
        test.setStatus(Status.UPDATED);
        test.setTimeLimit(timeLimit);
        test.setCourseId(Long.parseLong(courseName));
        testRepository.save(test);

        Question question = questionRepository.findById(Long.parseLong(idQuestion)).orElse(null);
        question.setQuestionDesc(requestParams.get("questionAll"));

        if (questionType.equals("multipleChoice")) {
            answerRepository.deleteAllByQuestionId(question.getId());
            int numAnswer = Integer.parseInt(requestParams.get("numOfOptions"));
            for (int i = 1; i < numAnswer; i++) {
                Answer answer = new Answer();
                answer.setQuestionId(question.getId());
                answer.setAnswerDesc(requestParams.get("answer" + i));
                answer.setIsCorrect(requestParams.get("correctAnswer" + i) != null);
                answer.setStatus(Status.UPDATED);
                answer.setCreatedAt(currentDate);
//                answer.setCreatedBy(user.getId());
                answerRepository.save(answer);
            }
        }

        // Redirect to a success page or do any other necessary actions

        return "redirect:/";
    }

    @GetMapping("/tests")
    public String lstTest(@RequestParam(value = "page", required = false) Integer page, Model model) {
        if (page == null) {
            page = 0;
        }
        if (page < 0) {
            page = 0;
        }
        PageRequest pageable = PageRequest.of(page, 3);
        Page<Test> tests = testRepository.findAllByOrderByCreatedAtDesc(pageable);
        model.addAttribute("tests", tests);
        model.addAttribute("pageNo", tests.getPageable().getPageNumber());
        model.addAttribute("pageTotal", tests.getTotalPages());
        return "test/list-test";
    }

    @GetMapping("/doTheTest")
    public String doTheTest(Model model, HttpSession session,
                            @RequestParam("idTest") String idTest,
                            @RequestParam("classTest") String classTest) {

        UserReadDTO user = (UserReadDTO) session.getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }

        Test test = testRepository.findById(Long.parseLong(idTest)).orElse(null);
        Date date = new Date();
        TestSession t = testSessionRepository.findAllByUsernameAndStatusNotAndTestId(user.getUsername(), 0, Long.parseLong(idTest));
        if (t != null) {
            return "redirect:/";
        }

        HashMap<Question, List<Answer>> lstQs = new HashMap<>();
        List<Question> lstQ = questionRepository.findAllByTestId(Long.parseLong(idTest));

        for (Question qs : lstQ) {
            List<Answer> lstA = answerRepository.findAllByQuestionId(qs.getId());
            lstQs.put(qs, lstA);
        }
        model.addAttribute("idTest", idTest);
        model.addAttribute("test", test);
        model.addAttribute("hmQA", lstQs);
        model.addAttribute("classTest", classTest);

        TestSession testSession = new TestSession();
        testSession.setStartDate(date);

        testSession.setUserId(user.getId());
        testSession.setUsername(user.getUsername());
        testSession.setStatus(1L);
        testSession.setTestId(test.getId());
        testSession.setSubject(test.getTestName());
        testSession.setClazz(classTest);

        testSessionRepository.save(testSession);

        return "test/dothetest";
    }

    @PostMapping("/submitTest")
    public String submitTest(Model model, HttpSession session,
                             @RequestParam("idTest") String idTest,
                             @RequestParam("typeTest") String testType,
                             @RequestParam("classTest") String classTest,
                             @RequestParam Map<String, String> requestParams) {
        System.out.println("Id test la: " + idTest);
        UserReadDTO user = (UserReadDTO) session.getAttribute("user");
        Test test = testRepository.findById(Long.parseLong(idTest)).orElse(null);
        List<Question> lstQ = questionRepository.findAllByTestId(Long.parseLong(idTest));
        for (Question qs : lstQ) {
            TestRecord2 testRecord = new TestRecord2();
            testRecord.setTestId(Long.parseLong(idTest));
            testRecord.setUserId(user.getId());
            testRecord.setQuestionId(qs.getId());
            testRecord.setClazz(classTest);
            testRecord.setUsername(user.getUsername());
            if (testType.equals("multipleChoice")) {
                testRecord.setQuestionType("multipleChoice");
                Long answerId = Long.parseLong(requestParams.get("question" + qs.getId()));
                Answer as = answerRepository.findById(answerId).orElse(null);
                testRecord.setAnswerMCId(as.getId());
                testRecord.setCorrect(as.getIsCorrect());
            } else {
                testRecord.setQuestionType("essay");
                String essayAnswer = requestParams.get("question" + qs.getId());
                testRecord.setEssayAnswer(essayAnswer);
            }
            testRecord2Repository.save(testRecord);

        }
        return "redirect:/";
    }


    @GetMapping("/searchbycourse")
    public String searchByCourse(@RequestParam(value = "page", required = false) Integer page, @RequestParam("searchText") String name, Model model) {
        if (page == null) {
            page = 0;
        }
        if (page < 0) {
            page = 0;
        }
        PageRequest pageable = PageRequest.of(page, 3);
        Page<Test> tests = testRepository.findByTestNameContainingOrderByCreatedAtDesc(name, pageable);
        model.addAttribute("tests", tests);
        model.addAttribute("pageNo", tests.getPageable().getPageNumber());
        model.addAttribute("pageTotal", tests.getTotalPages());
        model.addAttribute("searchText", name);
        return "test/list-test-search";
    }

    @GetMapping("/test-sessison")
    public String lstTestSession(@RequestParam(value = "page", required = false) Integer page, Model model, HttpSession session) {
        if (page == null) {
            page = 0;
        }
        if (page < 0) {
            page = 0;
        }
        PageRequest pageable = PageRequest.of(page, 3);
        Page<TestSession> tests = testSessionRepository.findAllByOrderByStartDateDesc(pageable);
        model.addAttribute("tests", tests);
        model.addAttribute("pageNo", tests.getPageable().getPageNumber());
        model.addAttribute("pageTotal", tests.getTotalPages());
        return "test/list-test-session";
    }

    @GetMapping("/search-test-session")
    public String searchTestSession(@RequestParam(value = "page", required = false) Integer page, @RequestParam("searchText") String name, @RequestParam("searchType") String searchType, Model model) {
        if (page == null) {
            page = 0;
        }
        if (page < 0) {
            page = 0;
        }
        PageRequest pageable = PageRequest.of(page, 3);
        Page<TestSession> tests;
        if (searchType.equals("class")) {
            tests = testSessionRepository.findAllByClazzContainingOrderByStartDateDesc(pageable, name);
        } else if (searchType.equals("subject")) {
            tests = testSessionRepository.findAllBySubjectContainingOrderByStartDateDesc(pageable, name);
        } else {
            tests = testSessionRepository.findAllByUsernameContainingOrderByStartDate(pageable, name);
        }

        model.addAttribute("tests", tests);
        model.addAttribute("pageNo", tests.getPageable().getPageNumber());
        model.addAttribute("pageTotal", tests.getTotalPages());
        model.addAttribute("searchText", name);
        model.addAttribute("searchType", searchType);
        return "test/list-test-search";
    }

    @GetMapping("/update-test-sessison")
    public String updateTestSession(Model model, HttpSession session, @RequestParam("idSession") String idSession, @RequestParam("newStatus") String newStatus) {
        UserReadDTO user = (UserReadDTO) session.getAttribute("user");
        Date date = new Date();
        TestSession testSession = testSessionRepository.findById(Long.parseLong(idSession)).orElse(null);
        testSession.setStatus(Long.parseLong(newStatus));
        testSession.setUpdateDate(date);
        testSession.setUserUpdate(user.getUsername());

        testSessionRepository.save(testSession);

        PageRequest pageable = PageRequest.of(0, 3);
        Page<TestSession> tests = testSessionRepository.findAllByOrderByStartDateDesc(pageable);
        model.addAttribute("tests", tests);
        model.addAttribute("pageNo", tests.getPageable().getPageNumber());
        model.addAttribute("pageTotal", tests.getTotalPages());
        return "test/list-test-session";
    }

    @GetMapping("/test-score")
    public String lstTestSession(Model model, HttpSession session,
                                 @RequestParam("class") String classTest,
                                 @RequestParam("idTest") String idTest) {

        Test test = testRepository.findById(Long.parseLong(idTest)).orElse(null);

        List<TestScoreDTO> lstReturn = new ArrayList<>();
        List<TestRecord2> lst = testRecord2Repository.findAllByClazzAndTestId(classTest, Long.parseLong(idTest));
        List<Long> lstUser = new ArrayList<>();
        for (TestRecord2 t2 : lst) {
            if (!lstUser.contains(t2.getUserId())) {
                lstUser.add(t2.getUserId());
            }
        }
        System.out.println("Danh sách người dùng:" + lstUser);
        if (test.getTestDesc().equals("multipleChoice")) {
            for (Long user : lstUser) {
                TestScoreDTO testScoreDTO = new TestScoreDTO();
                List<TestRecord2> result = testRecord2Repository.findAllByClazzAndTestIdAndUserId(classTest, Long.parseLong(idTest), user);
                System.out.println("Số lượng câu hỏi tìm thấy: "+result.size());
                int trueAnswer = 0;
                for (TestRecord2 rc : result) {
                    if (rc.isCorrect()) {
                        trueAnswer++;
                    }
                }
                testScoreDTO.setUserTest(result.get(0).getUsername());
                testScoreDTO.setType("Trắc nghiệm");
                testScoreDTO.setScore(trueAnswer + "/" + result.size());
                lstReturn.add(testScoreDTO);

            }
            model.addAttribute("testType", "multipleChoice");

        } else {
            for (Long user : lstUser) {
                TestScoreDTO testScoreDTO = new TestScoreDTO();
                List<TestRecord2> result = testRecord2Repository.findAllByClazzAndTestIdAndUserId(classTest, Long.parseLong(idTest), user);
                testScoreDTO.setUserTest(result.get(0).getUsername());
                testScoreDTO.setType("Tự luận");
                lstReturn.add(testScoreDTO);
            }
            model.addAttribute("testType", "essay");
        }
        model.addAttribute("testScore", lstReturn);

        return "test/testscore-class";
    }


}
