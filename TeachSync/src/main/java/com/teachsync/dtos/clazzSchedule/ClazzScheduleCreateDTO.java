package com.teachsync.dtos.clazzSchedule;

import com.teachsync.dtos.BaseCreateDTO;
import com.teachsync.dtos.session.SessionCreateDTO;
import com.teachsync.utils.enums.ScheduleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClazzScheduleCreateDTO extends BaseCreateDTO {
    private Long clazzId;

    private Long roomId;

    private Long schedulecaId;

    private ScheduleType scheduleType;

    private LocalDate startDate;

    private LocalDate endDate;

    private Integer slot;

    private LocalTime sessionStart;

    private LocalTime sessionEnd;

    private List<SessionCreateDTO> sessionCreateDTOList;
}
