package com.teachsync.services.answer;

import com.teachsync.dtos.answer.AnswerCreateDTO;
import com.teachsync.dtos.answer.AnswerReadDTO;
import com.teachsync.dtos.answer.AnswerUpdateDTO;
import com.teachsync.entities.Answer;
import com.teachsync.utils.enums.DtoOption;
import org.springframework.data.domain.Page;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface AnswerService {
    /* =================================================== CREATE =================================================== */
    Answer createAnswer(Answer answer) throws Exception;
    AnswerReadDTO createAnswerByDTO(AnswerCreateDTO createDTO) throws Exception;

    List<Answer> createBulkAnswer(Collection<Answer> answerCollection) throws Exception;
    List<AnswerReadDTO> createBulkAnswerByDTO(Collection<AnswerCreateDTO> createDTOCollection) throws Exception;


    /* =================================================== READ ===================================================== */
    /* id */
    Answer getById(Long id) throws Exception;
    AnswerReadDTO getDTOById(Long id, Collection<DtoOption> options) throws Exception;

    List<Answer> getAllByIdIn(Collection<Long> idCollection) throws Exception;
    List<AnswerReadDTO> getAllDTOByIdIn(
            Collection<Long> idCollection, Collection<DtoOption> options) throws Exception;

    /* questionId */
    List<Answer> getAllByQuestionId(Long questionId) throws Exception;
    List<AnswerReadDTO> getAllDTOByQuestionId(Long questionId, Collection<DtoOption> options) throws Exception;

    List<Answer> getAllByQuestionIdIn(Collection<Long> questionIdCollection) throws Exception;
    List<AnswerReadDTO> getAllDTOByQuestionIdIn(
            Collection<Long> questionIdCollection, Collection<DtoOption> options) throws Exception;
    Map<Long, List<AnswerReadDTO>> mapQuestionIdListDTOByQuestionIdIn(
            Collection<Long> questionIdCollection, Collection<DtoOption> options) throws Exception;


    /* =================================================== UPDATE =================================================== */
    Answer updateAnswer(Answer answer) throws Exception;
    AnswerReadDTO updateAnswerByDTO(AnswerUpdateDTO updateDTO) throws Exception;

    List<Answer> updateBulkAnswer(Collection<Answer> answerCollection) throws Exception;
    List<AnswerReadDTO> updateBulkAnswerByDTO(Collection<AnswerUpdateDTO> updateDTOCollection) throws Exception;


    /* =================================================== DELETE =================================================== */
    /* id */
    Boolean deleteById(Long id) throws Exception;
    Boolean deleteAllByIdIn(Collection<Long> idCollection) throws Exception;

    /* questionId */
    Boolean deleteAllByQuestionId(Long questionId) throws Exception;
    Boolean deleteAllByQuestionIdIn(Collection<Long> questionIdCollection) throws Exception;

    /* =================================================== WRAPPER ================================================== */
    AnswerReadDTO wrapDTO(Answer answer, Collection<DtoOption> options) throws Exception;
    List<AnswerReadDTO> wrapListDTO(Collection<Answer> answerCollection, Collection<DtoOption> options) throws Exception;
    Page<AnswerReadDTO> wrapPageDTO(Page<Answer> answerPage, Collection<DtoOption> options) throws Exception;
}
