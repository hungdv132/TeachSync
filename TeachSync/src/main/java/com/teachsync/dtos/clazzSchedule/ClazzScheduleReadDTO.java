package com.teachsync.dtos.clazzSchedule;

import com.teachsync.dtos.BaseReadDTO;
import com.teachsync.utils.enums.ScheduleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * DTO for {@link com.teachsync.entities.ClazzSchedule}
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClazzScheduleReadDTO extends BaseReadDTO {
    private Long clazzId;

    private String clazzName;

    private Long roomId;

    private String roomName;

    private Long schedulecaId;

    private String schedulecaName;

    private ScheduleType scheduleType;

    private LocalDate startDate;

    private LocalDate endDate;

    private Integer slot;

    private LocalTime sessionStart;

    private LocalTime sessionEnd;
}