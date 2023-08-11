package com.teachsync.dtos.question;

import com.teachsync.dtos.BaseReadDTO;
import com.teachsync.utils.enums.QuestionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * DTO for {@link com.teachsync.entities.Question}
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionReadDTO extends BaseReadDTO {
    private Long testId;
    private QuestionType questionType;
    private String questionDesc;
    private String questionPrompt;
}