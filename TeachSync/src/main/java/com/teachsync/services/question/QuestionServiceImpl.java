package com.teachsync.services.question;


import com.teachsync.dtos.answer.AnswerCreateDTO;
import com.teachsync.dtos.answer.AnswerReadDTO;
import com.teachsync.dtos.answer.AnswerUpdateDTO;
import com.teachsync.dtos.question.QuestionCreateDTO;
import com.teachsync.dtos.question.QuestionReadDTO;
import com.teachsync.dtos.question.QuestionUpdateDTO;
import com.teachsync.entities.BaseEntity;
import com.teachsync.entities.Question;
import com.teachsync.repositories.QuestionRepository;
import com.teachsync.services.answer.AnswerService;
import com.teachsync.utils.enums.DtoOption;
import com.teachsync.utils.enums.Status;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.teachsync.utils.enums.DtoOption.*;

@Service
public class QuestionServiceImpl implements QuestionService {
    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerService answerService;

    @Autowired
    private ModelMapper mapper;


    /* =================================================== CREATE =================================================== */
    @Override
    public Question createQuestion(Question question) throws Exception {
        //TODO: Kiểm tra khóa ngoại, chính tả,..

        return questionRepository.saveAndFlush(question);
    }
    @Override
    public QuestionReadDTO createQuestionByDTO(QuestionCreateDTO createDTO) throws Exception {
        Question question = mapper.map(createDTO, Question.class);

        question = createQuestion(question);

        /* Create dependency */
        List<AnswerCreateDTO> answerCreateDTOList = createDTO.getAnswerList();
        if (answerCreateDTOList != null) {
            /* Về lý thuyết luôn phải có nhưng vẫn phải tránh null exception */
            Long questionId = question.getId();
            Long createdBy = question.getCreatedBy();
            answerCreateDTOList =
                    answerCreateDTOList.stream()
                            .peek(dto -> {
                                dto.setQuestionId(questionId);
                                dto.setCreatedBy(createdBy); })
                            .collect(Collectors.toList());

            answerService.createBulkAnswerByDTO(answerCreateDTOList);
        }

        return wrapDTO(question, List.of(ANSWER_LIST));
    }

    @Override
    public List<Question> createBulkQuestion(Collection<Question> questionCollection) throws Exception {
        //TODO: Kiểm tra khóa ngoại, chính tả,..

        return questionRepository.saveAllAndFlush(questionCollection);
    }

    @Override
    public List<QuestionReadDTO> createBulkQuestionByDTO(Collection<QuestionCreateDTO> createDTOCollection) throws Exception {
        List<Question> questionList = new ArrayList<>();
        List<AnswerCreateDTO> answerCreateDTOList = new ArrayList<>();

        for (QuestionCreateDTO createDTO : createDTOCollection) {
            questionList.add(mapper.map(createDTO, Question.class));
            answerCreateDTOList.addAll(createDTO.getAnswerList());
        }

        questionList = createBulkQuestion(questionList);

        /* Create dependency */
        if (!answerCreateDTOList.isEmpty()) {
            /* Về lý thuyết luôn phải có nhưng vẫn phải tránh null exception */

            answerCreateDTOList = new ArrayList<>();
            List<AnswerCreateDTO> tmpAnswerCreateDTOList;

            for (Question question : questionList) {
                for (QuestionCreateDTO createDTO : createDTOCollection) {
                    if (question.equalCreateDTO(createDTO)) {
                        tmpAnswerCreateDTOList = createDTO.getAnswerList();
                        tmpAnswerCreateDTOList =
                                tmpAnswerCreateDTOList
                                        .stream().peek(dto -> dto.setQuestionId(question.getId()))
                                        .collect(Collectors.toList());

                        answerCreateDTOList.addAll(tmpAnswerCreateDTOList);
                    }
                }
            }

            answerService.createBulkAnswerByDTO(answerCreateDTOList);
        }

        return wrapListDTO(questionList, List.of(ANSWER_LIST));
    }

    /* =================================================== READ ===================================================== */
    /* id */
    @Override
    public Question getById(Long id) throws Exception {
        return questionRepository
                .findByIdAndStatusNot(id, Status.DELETED)
                .orElse(null);
    }
    @Override
    public QuestionReadDTO getDTOById(Long id, Collection<DtoOption> options) throws Exception {
        Question question = getById(id);
        
        if (question == null) {
            return null;
        }
        
        return wrapDTO(question, options);
    }

    @Override
    public List<Question> getAllByIdIn(Collection<Long> idCollection) throws Exception {
        List<Question> questionList = 
                questionRepository.findAllByIdInAndStatusNot(idCollection, Status.DELETED);
        
        if(questionList.isEmpty()){
            return null;
        }

        return questionList;
    }
    @Override
    public List<QuestionReadDTO> getAllDTOByIdIn(
            Collection<Long> idCollection, Collection<DtoOption> options) throws Exception {
        List<Question> questionList =
                questionRepository.findAllByIdInAndStatusNot(idCollection, Status.DELETED);

        if(questionList == null){
            return null;
        }

        return wrapListDTO(questionList, options);
    }

    /* testId */
    @Override
    public List<Question> getAllByTestId(Long testId) throws Exception {
        List<Question> questionList =
                questionRepository.findAllByTestIdAndStatusNot(testId, Status.DELETED);

        if(questionList.isEmpty()){
            return null;
        }

        return questionList;
    }
    @Override
    public List<QuestionReadDTO> getAllDTOByTestId(Long testId, Collection<DtoOption> options) throws Exception {
        List<Question> questionList = getAllByTestId(testId);

        if(questionList == null){
            return null;
        }

        return wrapListDTO(questionList, options);
    }

    @Override
    public List<Question> getAllByTestIdIn(Collection<Long> testIdCollection) throws Exception {
        List<Question> questionList =
                questionRepository.findAllByTestIdInAndStatusNot(testIdCollection, Status.DELETED);

        if(questionList.isEmpty()){
            return null;
        }

        return questionList;
    }
    @Override
    public List<QuestionReadDTO> getAllDTOByTestIdIn(
            Collection<Long> testIdCollection, Collection<DtoOption> options) throws Exception {
        List<Question> questionList = getAllByTestIdIn(testIdCollection);

        if(questionList == null){
            return null;
        }

        return wrapListDTO(questionList, options);
    }
    @Override
    public Map<Long, List<QuestionReadDTO>> mapTestIdListDTOByTestIdIn(
            Collection<Long> testIdCollection, Collection<DtoOption> options) throws Exception {
        List<QuestionReadDTO> questionDTOList = getAllDTOByTestIdIn(testIdCollection, options);

        if (questionDTOList == null) {
            return new HashMap<>();
        }
        Map<Long, List<QuestionReadDTO>> testIdDTOListMap = new HashMap<>();
        Long tmpTestId;
        List<QuestionReadDTO> tmpList;
        for (QuestionReadDTO dto : questionDTOList) {
            tmpTestId = dto.getTestId();
            tmpList = testIdDTOListMap.get(tmpTestId);

            if (tmpList == null) {
                testIdDTOListMap.put(tmpTestId, new ArrayList<>(List.of(dto)));
            } else {
                tmpList.add(dto);
                testIdDTOListMap.put(tmpTestId, tmpList);
            }
        }

        return testIdDTOListMap;
    }


    /* =================================================== UPDATE =================================================== */
    @Override
    public Question updateQuestion(Question question) throws Exception {
        /* Check exists */
        Question oldQuestion = getById(question.getId());
        if (oldQuestion == null) {
            throw new IllegalArgumentException("Update Error. No Question found with id: " + question.getId());
        }
        question.setCreatedAt(oldQuestion.getCreatedAt());
        question.setCreatedBy(oldQuestion.getCreatedBy());

        /* Validate input */

        /* Check fk */

        return questionRepository.saveAndFlush(question);
    }
    @Override
    public QuestionReadDTO updateQuestionByDTO(QuestionUpdateDTO updateDTO) throws Exception {
        Question question = mapper.map(updateDTO, Question.class);

        question = createQuestion(question);

        /* Create dependency */
        if (updateDTO.getNewAnswerList() != null) {
            answerService.createBulkAnswerByDTO(updateDTO.getNewAnswerList());
        }

        /* Update dependency */
        if (updateDTO.getUpdatedAnswerList() != null) {
            answerService.updateBulkAnswerByDTO(updateDTO.getUpdatedAnswerList());
        }

        return wrapDTO(question, List.of(ANSWER_LIST));
    }

    @Override
    public List<Question> updateBulkQuestion(Collection<Question> questionCollection) throws Exception {
        Map<Long, Question> idQuestionMap =
                questionCollection.stream()
                        .collect(Collectors.toMap(BaseEntity::getId, Function.identity()));

        /* Check exist */
        List<Question> oldQuestionList = getAllByIdIn(idQuestionMap.keySet());
        if (oldQuestionList == null) {
            throw new IllegalArgumentException("Update Error. No Question found within id given");
        } else if (oldQuestionList.size() != idQuestionMap.size()) {
            throw new IllegalArgumentException("Update Error. Some Question found within id given");
        }
        Map<Long, Question> idOldQuestionMap = oldQuestionList.stream()
                .collect(Collectors.toMap(BaseEntity::getId, Function.identity()));

        Question tmpQuestion;
        Question tmpOldQuestion;
        for (Long id : idQuestionMap.keySet()) {
            tmpQuestion = idQuestionMap.get(id);
            tmpOldQuestion = idOldQuestionMap.get(id);

            tmpQuestion.setCreatedAt(tmpOldQuestion.getCreatedAt());
            tmpQuestion.setCreatedBy(tmpOldQuestion.getCreatedBy());

            idQuestionMap.put(id, tmpQuestion);
        }

        /* Validate input */

        /* Check fk */

        return questionRepository.saveAllAndFlush(idQuestionMap.values());
    }
    @Override
    public List<QuestionReadDTO> updateBulkQuestionByDTO(Collection<QuestionUpdateDTO> updateDTOCollection) throws Exception {
        List<Question> questionList = new ArrayList<>();
        List<AnswerCreateDTO> newAnswerList = new ArrayList<>();
        List<AnswerUpdateDTO> updatedAnswerList = new ArrayList<>();

        for (QuestionUpdateDTO updateDTO : updateDTOCollection) {
            questionList.add(mapper.map(updateDTO, Question.class));

            if (updateDTO.getNewAnswerList() != null) {
                newAnswerList.addAll(updateDTO.getNewAnswerList());
            }

            if (updateDTO.getUpdatedAnswerList() != null) {
                updatedAnswerList.addAll(updateDTO.getUpdatedAnswerList());
            }
        }

        questionList = createBulkQuestion(questionList);

        /* Create dependency */
        if (!newAnswerList.isEmpty()) {
            answerService.createBulkAnswerByDTO(newAnswerList);
        }

        /* Update dependency */
        if (!updatedAnswerList.isEmpty()) {
            answerService.updateBulkAnswerByDTO(updatedAnswerList);
        }

        return wrapListDTO(questionList, List.of(ANSWER_LIST));
    }
    
    
    /* =================================================== DELETE =================================================== */


    /* =================================================== WRAPPER ================================================== */
    @Override
    public QuestionReadDTO wrapDTO(Question question, Collection<DtoOption> options) throws Exception {
        QuestionReadDTO dto = mapper.map(question, QuestionReadDTO.class);

        /* Add dependency */
        if (options != null && !options.isEmpty()) {
            if (options.contains(ANSWER_LIST)) {
                List<AnswerReadDTO> answerDTOList = answerService.getAllDTOByQuestionId(question.getId(), options);
                dto.setAnswerList(answerDTOList);
            }
        }

        return dto;
    }

    @Override
    public List<QuestionReadDTO> wrapListDTO(Collection<Question> questionCollection, Collection<DtoOption> options) throws Exception {
        List<QuestionReadDTO> dtoList = new ArrayList<>();
        QuestionReadDTO dto;

        Map<Long, List<AnswerReadDTO>> questionIdAnswerDTOListMap = new HashMap<>();

        if (options != null && !options.isEmpty()) {
            Set<Long> idSet = new HashSet<>();

            for (Question question : questionCollection) {
                idSet.add(question.getId());
            }

            if (options.contains(ANSWER_LIST)) {
                questionIdAnswerDTOListMap = answerService.mapQuestionIdListDTOByQuestionIdIn(idSet, options);
            }
        }

        for (Question question : questionCollection) {
            dto = mapper.map(question, QuestionReadDTO.class);

            /* Add dependency */
            dto.setAnswerList(questionIdAnswerDTOListMap.get(question.getId()));

            dtoList.add(dto);
        }

        return dtoList;
    }

    @Override
    public Page<QuestionReadDTO> wrapPageDTO(Page<Question> questionPage, Collection<DtoOption> options) throws Exception {
        return new PageImpl<>(
                wrapListDTO(questionPage.getContent(), options),
                questionPage.getPageable(),
                questionPage.getTotalPages());
    }
}
