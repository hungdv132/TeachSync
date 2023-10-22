package com.teachsync.dtos.clazzSchedule;

import com.teachsync.dtos.BaseUpdateDTO;
import com.teachsync.dtos.session.SessionCreateDTO;
import com.teachsync.utils.enums.ScheduleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * DTO for {@link com.teachsync.entities.ClazzSchedule}
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClazzScheduleUpdateDTO extends BaseUpdateDTO {
    private Long clazzId;

    private Long roomId;

    private Long scheduleCategoryId;

    private ScheduleType scheduleType;

    private LocalDate startDate;

    private LocalDate endDate;

    private Integer slot;

    private LocalTime sessionStart;

    private LocalTime sessionEnd;

    private List<SessionCreateDTO> sessionCreateDTOList;
}
