package com.teachsync.services.question;

import com.teachsync.dtos.question.QuestionCreateDTO;
import com.teachsync.dtos.question.QuestionReadDTO;
import com.teachsync.dtos.question.QuestionUpdateDTO;
import com.teachsync.entities.Question;
import com.teachsync.utils.enums.DtoOption;
import org.springframework.data.domain.Page;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface QuestionService {
    /* =================================================== CREATE =================================================== */
    Question createQuestion(Question question) throws Exception;
    QuestionReadDTO createQuestionByDTO(QuestionCreateDTO createDTO) throws Exception;

    List<Question> createBulkQuestion(Collection<Question> questionCollection) throws Exception;
    List<QuestionReadDTO> createBulkQuestionByDTO(Collection<QuestionCreateDTO> createDTOCollection) throws Exception;


    /* =================================================== READ ===================================================== */
    /* id */
    Question getById(Long id) throws Exception;
    QuestionReadDTO getDTOById(Long id, Collection<DtoOption> options) throws Exception;

    List<Question> getAllByIdIn(Collection<Long> idCollection) throws Exception;
    List<QuestionReadDTO> getAllDTOByIdIn(
            Collection<Long> idCollection, Collection<DtoOption> options) throws Exception;

    /* testId */
    List<Question> getAllByTestId(Long testId) throws Exception;
    List<QuestionReadDTO> getAllDTOByTestId(Long testId, Collection<DtoOption> options) throws Exception;

    List<Question> getAllByTestIdIn(Collection<Long> testIdCollection) throws Exception;
    List<QuestionReadDTO> getAllDTOByTestIdIn(
            Collection<Long> testIdCollection, Collection<DtoOption> options) throws Exception;
    Map<Long, List<QuestionReadDTO>> mapTestIdListDTOByTestIdIn(
            Collection<Long> testIdCollection, Collection<DtoOption> options) throws Exception;


    /* =================================================== UPDATE =================================================== */
    Question updateQuestion(Question question) throws Exception;
    QuestionReadDTO updateQuestionByDTO(QuestionUpdateDTO updateDTO) throws Exception;

    List<Question> updateBulkQuestion(Collection<Question> questionCollection) throws Exception;
    List<QuestionReadDTO> updateBulkQuestionByDTO(Collection<QuestionUpdateDTO> updateDTOCollection) throws Exception;


    /* =================================================== DELETE =================================================== */


    /* =================================================== WRAPPER ================================================== */
    QuestionReadDTO wrapDTO(Question question, Collection<DtoOption> options) throws Exception;
    List<QuestionReadDTO> wrapListDTO(Collection<Question> questionCollection, Collection<DtoOption> options) throws Exception;
    Page<QuestionReadDTO> wrapPageDTO(Page<Question> questionPage, Collection<DtoOption> options) throws Exception;
}
