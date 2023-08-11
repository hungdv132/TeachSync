package com.teachsync.dtos.memberTestRecord;
import com.teachsync.dtos.BaseReadDTO;
import com.teachsync.dtos.clazzMember.ClazzMemberReadDTO;
import com.teachsync.dtos.clazzTest.ClazzTestReadDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for {@link com.teachsync.entities.MemberTestRecord}
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberTestRecordReadDTO extends BaseReadDTO {
    private Long memberId;
    private ClazzMemberReadDTO member;
    private Long clazzTestId;
    private ClazzTestReadDTO clazzTest;
    private LocalDateTime startAt;
    private LocalDateTime submitAt;
    private Double score;
}