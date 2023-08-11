package com.teachsync.dtos.answer;

import com.teachsync.dtos.BaseCreateDTO;
import com.teachsync.dtos.BaseUpdateDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * DTO for {@link com.teachsync.entities.Answer}
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnswerUpdateDTO extends BaseUpdateDTO {
    private Long questionId;
    private String answerDesc;
    private Double answerScore;
    private Boolean isCorrect;
}