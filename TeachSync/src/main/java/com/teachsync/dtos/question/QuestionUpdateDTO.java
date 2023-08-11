package com.teachsync.dtos.question;

import com.teachsync.dtos.BaseUpdateDTO;
import com.teachsync.dtos.answer.AnswerCreateDTO;
import com.teachsync.dtos.answer.AnswerUpdateDTO;
import com.teachsync.utils.enums.QuestionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for {@link com.teachsync.entities.Question}
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionUpdateDTO extends BaseUpdateDTO {
    private Long testId;
    private QuestionType questionType;
    private String questionDesc;
    private String questionPrompt;
    private Double questionScore;
    private List<AnswerCreateDTO> newAnswerList;
    private List<AnswerUpdateDTO> updatedAnswerList;
}