package com.teachsync.dtos.staff;

import com.teachsync.dtos.BaseReadDTO;
import com.teachsync.dtos.center.CenterReadDTO;
import com.teachsync.dtos.user.UserReadDTO;
import com.teachsync.utils.enums.StaffType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * DTO for {@link com.teachsync.entities.Staff}
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StaffCreateDTO extends BaseReadDTO {
    private Long centerId;
    private CenterReadDTO center;
    private Long userId;
    private UserReadDTO user;
    private StaffType staffType;
}