package com.teachsync.dtos.scheduleCategory;

import com.teachsync.dtos.BaseReadDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleCaReadDTO extends BaseReadDTO {

    private String roomDesc;

    private String roomName;
}
