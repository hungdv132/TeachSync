package com.teachsync.dtos.memberTestRecord;
import com.teachsync.dtos.BaseReadDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * DTO for {@link com.teachsync.entities.MemberTestRecord}
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberTestRecordReadDTO extends BaseReadDTO {
    private Long memberId;
    private Long clazzTestId;
    private Double score;
}