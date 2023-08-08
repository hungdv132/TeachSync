package com.teachsync.services.test;


import com.teachsync.dtos.BaseReadDTO;
import com.teachsync.dtos.question.QuestionCreateDTO;
import com.teachsync.dtos.question.QuestionReadDTO;
import com.teachsync.dtos.test.TestCreateDTO;
import com.teachsync.dtos.test.TestReadDTO;
import com.teachsync.dtos.test.TestUpdateDTO;
import com.teachsync.entities.Test;
import com.teachsync.repositories.TestRepository;
import com.teachsync.services.question.QuestionService;
import com.teachsync.utils.MiscUtil;
import com.teachsync.utils.enums.DtoOption;
import com.teachsync.utils.enums.Status;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.teachsync.utils.enums.DtoOption.*;

@Service
public class TestServiceImpl implements TestService {
    @Autowired
    private TestRepository testRepository;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private MiscUtil miscUtil;


    /* =================================================== CREATE =================================================== */
    @Override
    public Test createTest(Test test) throws Exception {
        //TODO: Kiểm tra khóa ngoại, chính tả,..

        return testRepository.saveAndFlush(test);
    }
    @Override
    public TestReadDTO createTestByDTO(TestCreateDTO createDTO) throws Exception {
        Test test = mapper.map(createDTO, Test.class);

        test = createTest(test);

        /* Create dependency */
        List<QuestionCreateDTO> questionCreateDTOList = createDTO.getQuestionList();
        if (questionCreateDTOList != null) {
            /* Về lý thuyết luôn phải có nhưng vẫn phải tránh null exception */
            Long testId = test.getId();
            Long createdBy = test.getCreatedBy();
            questionCreateDTOList =
                    questionCreateDTOList.stream()
                            .peek(dto -> {
                                dto.setTestId(testId);
                                dto.setCreatedBy(createdBy); })
                            .collect(Collectors.toList());

            questionService.createBulkQuestionByDTO(questionCreateDTOList);
        }

        return wrapDTO(test, List.of(QUESTION_LIST));
    }


    /* =================================================== READ ===================================================== */
    @Override
    public Page<Test> getPageAll(Pageable pageable) throws Exception {
        if (pageable == null) {
            pageable = miscUtil.defaultPaging();
        }

        Page<Test> testPage = testRepository.findAllByStatusNot(Status.DELETED, pageable);

        if (testPage.isEmpty()) {
            return null;
        }

        return testPage;
    }
    @Override
    public Page<TestReadDTO> getPageAllDTO(Pageable pageable, Collection<DtoOption> options) throws Exception {
        Page<Test> testPage = getPageAll(pageable);

        if (testPage == null) {
            return null;
        }

        return wrapPageDTO(testPage, options);
    }

    /* id */
    @Override
    public Test getById(Long id) throws Exception {
        return testRepository
                .findByIdAndStatusNot(id, Status.DELETED)
                .orElse(null);
    }
    @Override
    public TestReadDTO getDTOById(Long id, Collection<DtoOption> options) throws Exception {
        Test test = getById(id);
        
        if (test == null) {
            return null;
        }
        
        return wrapDTO(test, options);
    }

    @Override
    public List<Test> getAllByIdIn(Collection<Long> idCollection) throws Exception {
        List<Test> testList = 
                testRepository.findAllByIdInAndStatusNot(idCollection, Status.DELETED);
        
        if(testList.isEmpty()){
            return null;
        }

        return testList;
    }
    @Override
    public List<TestReadDTO> getAllDTOByIdIn(
            Collection<Long> idCollection, Collection<DtoOption> options) throws Exception {
        List<Test> testList = getAllByIdIn(idCollection);

        if(testList == null){
            return null;
        }

        return wrapListDTO(testList, options);
    }

    @Override
    public Map<Long, TestReadDTO>  mapIdDTOByIdIn(
            Collection<Long> idCollection, Collection<DtoOption> options) throws Exception {
        List<TestReadDTO> testDTOList = getAllDTOByIdIn(idCollection, options);

        if(testDTOList == null){
            return new HashMap<>();
        }

        return testDTOList.stream()
                .collect(Collectors.toMap(BaseReadDTO::getId, Function.identity()));
    }

    /* courseId */
    @Override
    public List<Test> getAllByCourseId(Long courseId) throws Exception {
        List<Test> testList =
                testRepository.findAllByCourseIdAndStatusNot(courseId, Status.DELETED);

        if(testList.isEmpty()){
            return null;
        }

        return testList;
    }
    @Override
    public List<TestReadDTO> getAllDTOByCourseId(Long courseId, Collection<DtoOption> options) throws Exception {
        List<Test> testList = getAllByCourseId(courseId);

        if(testList == null){
            return null;
        }

        return wrapListDTO(testList, options);
    }

    @Override
    public Page<Test> getPageAllByCourseIdIn(Pageable pageable, Collection<Long> courseIdCollection) throws Exception {
        if (pageable == null) {
            pageable = miscUtil.defaultPaging();
        }

        Page<Test> testPage =
                testRepository.findAllByCourseIdInAndStatusNot(courseIdCollection, Status.DELETED, pageable);

        if(testPage.isEmpty()){
            return null;
        }

        return testPage;
    }
    @Override
    public Page<TestReadDTO> getPageAllDTOByCourseIdIn(
            Pageable pageable, Collection<Long> courseIdCollection, Collection<DtoOption> options) throws Exception {
        Page<Test> testPage = getPageAllByCourseIdIn(pageable, courseIdCollection);

        if(testPage == null){
            return null;
        }

        return wrapPageDTO(testPage, options);
    }

    @Override
    public List<Test> getAllByCourseIdIn(Collection<Long> courseIdCollection) throws Exception {
        List<Test> testList =
                testRepository.findAllByCourseIdInAndStatusNot(courseIdCollection, Status.DELETED);

        if(testList.isEmpty()){
            return null;
        }

        return testList;
    }
    @Override
    public List<TestReadDTO> getAllDTOByCourseIdIn(
            Collection<Long> courseIdCollection, Collection<DtoOption> options) throws Exception {
        List<Test> testList = getAllByCourseIdIn(courseIdCollection);

        if(testList == null){
            return null;
        }

        return wrapListDTO(testList, options);
    }
    @Override
    public Map<Long, List<TestReadDTO>> mapCourseIdListDTOByCourseIdIn(
            Collection<Long> courseIdCollection, Collection<DtoOption> options) throws Exception {
        List<TestReadDTO> testDTOList = getAllDTOByCourseIdIn(courseIdCollection, options);

        if (testDTOList == null) {
            return new HashMap<>();
        }
        Map<Long, List<TestReadDTO>> courseIdTestDTOListMap = new HashMap<>();
        Long tmpCourseId;
        List<TestReadDTO> tmpList;

        for (TestReadDTO dto : testDTOList) {
            tmpCourseId = dto.getCourseId();
            tmpList = courseIdTestDTOListMap.get(tmpCourseId);

            if (tmpList == null) {
                courseIdTestDTOListMap.put(tmpCourseId, new ArrayList<>(List.of(dto)));
            } else {
                tmpList.add(dto);
                courseIdTestDTOListMap.put(tmpCourseId, tmpList);
            }
        }

        return courseIdTestDTOListMap;
    }


    /* =================================================== UPDATE =================================================== */
    @Override
    public Test updateTest(Test test) throws Exception {
        /* Check exists */
        Test oldTest = getById(test.getId());
        if (oldTest == null) {
            throw new IllegalArgumentException("Update Error. No Test found with id: " + test.getId());
        }
        test.setCreatedAt(oldTest.getCreatedAt());
        test.setCreatedBy(oldTest.getCreatedBy());

        /* Validate input */

        /* Check fk */

        return testRepository.saveAndFlush(test);
    }
    @Override
    public TestReadDTO updateTestByDTO(TestUpdateDTO updateDTO) throws Exception {
        Test test = mapper.map(updateDTO, Test.class);

        test = createTest(test);

        /* Create dependency */
        if (updateDTO.getNewQuestionList() != null) {
            questionService.createBulkQuestionByDTO(updateDTO.getNewQuestionList());
        }

        /* Update dependency */
        if (updateDTO.getUpdatedQuestionList() != null) {
            questionService.updateBulkQuestionByDTO(updateDTO.getUpdatedQuestionList());
        }

        return wrapDTO(test, List.of(QUESTION_LIST));
    }


    /* =================================================== DELETE =================================================== */


    /* =================================================== WRAPPER ================================================== */
    @Override
    public TestReadDTO wrapDTO(Test test, Collection<DtoOption> options) throws Exception {
        TestReadDTO dto = mapper.map(test, TestReadDTO.class);

        /* Add dependency */
        if (options != null && !options.isEmpty()) {
            if (options.contains(QUESTION_LIST)) {
                List<QuestionReadDTO> questionDTOList = questionService.getAllDTOByTestId(test.getId(), options);
                dto.setQuestionList(questionDTOList);
            }
        }

        return dto;
    }

    @Override
    public List<TestReadDTO> wrapListDTO(Collection<Test> testCollection, Collection<DtoOption> options) throws Exception {
        List<TestReadDTO> dtoList = new ArrayList<>();
        TestReadDTO dto;

        Map<Long, List<QuestionReadDTO>> testIdQuetionDTOListMap = new HashMap<>();

        if (options != null && !options.isEmpty()) {
            Set<Long> idSet = new HashSet<>();

            for (Test test : testCollection) {
                idSet.add(test.getId());
            }

            if (options.contains(QUESTION_LIST)) {
                testIdQuetionDTOListMap = questionService.mapTestIdListDTOByTestIdIn(idSet, options);
            }
        }

        for (Test test : testCollection) {
            dto = mapper.map(test, TestReadDTO.class);

            /* Add dependency */
            dto.setQuestionList(testIdQuetionDTOListMap.get(test.getId()));

            dtoList.add(dto);
        }

        return dtoList;
    }

    @Override
    public Page<TestReadDTO> wrapPageDTO(Page<Test> testPage, Collection<DtoOption> options) throws Exception {
        return new PageImpl<>(
                wrapListDTO(testPage.getContent(), options),
                testPage.getPageable(),
                testPage.getTotalPages());
    }
}
