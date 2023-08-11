package com.teachsync.dtos.clazzMember;

import com.teachsync.dtos.BaseReadDTO;
import com.teachsync.dtos.clazz.ClazzReadDTO;
import com.teachsync.dtos.user.UserReadDTO;
import com.teachsync.utils.enums.MemberRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * DTO for {@link com.teachsync.entities.ClazzMember}
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClazzMemberReadDTO extends BaseReadDTO {
    private ClazzReadDTO clazz;
    private String clazzName;
    private Long clazzId;

    private UserReadDTO user;
    private String userFullName;
    private Long userId;

    private Double score;

    private Double attendant;

    private Boolean isPassed;
}