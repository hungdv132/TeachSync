package com.teachsync.dtos.memberHomeworkRecord;
import com.teachsync.dtos.BaseCreateDTO;
import com.teachsync.dtos.BaseReadDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * DTO for {@link com.teachsync.entities.MemberHomeworkRecord}
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberHomeworkRecordCreateDTO extends BaseCreateDTO {
    private Long memberId;
    private Long homeworkId;
    private String name;
    private String submission;
    private String submissionLink;
    private Double score;
}
