package com.teachsync.dtos.memberHomeworkRecord;
import com.teachsync.dtos.BaseReadDTO;
import com.teachsync.dtos.clazzMember.ClazzMemberReadDTO;
import com.teachsync.dtos.homework.HomeworkReadDTO;
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
public class MemberHomeworkRecordReadDTO extends BaseReadDTO {
    private Long memberId;
    private ClazzMemberReadDTO member;
    private Long homeworkId;
    private HomeworkReadDTO homework;
    private String name;
    private String submission;
    private String submissionLink;
    private Double score;
}
