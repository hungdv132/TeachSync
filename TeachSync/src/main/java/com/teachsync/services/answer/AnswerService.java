package com.teachsync.services.answer;

import com.teachsync.dtos.answer.AnswerReadDTO;
import com.teachsync.entities.Answer;
import com.teachsync.utils.enums.DtoOption;
import org.springframework.data.domain.Page;

import java.util.Collection;
import java.util.List;

public interface AnswerService {
    /* =================================================== CREATE =================================================== */


    /* =================================================== READ ===================================================== */


    /* =================================================== UPDATE =================================================== */


    /* =================================================== DELETE =================================================== */


    /* =================================================== WRAPPER ================================================== */
    AnswerReadDTO wrapDTO(Answer answer, Collection<DtoOption> options) throws Exception;
    List<AnswerReadDTO> wrapListDTO(Collection<Answer> answerCollection, Collection<DtoOption> options) throws Exception;
    Page<AnswerReadDTO> wrapPageDTO(Page<Answer> answerPage, Collection<DtoOption> options) throws Exception;
}
