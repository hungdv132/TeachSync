package com.teachsync.dtos.clazzSchedule;

import com.teachsync.dtos.BaseReadDTO;
import com.teachsync.dtos.room.RoomReadDTO;
import com.teachsync.dtos.scheduleCategory.ScheduleCategoryReadDTO;
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

    private RoomReadDTO room;

    private Long scheduleCategoryId;

    private ScheduleCategoryReadDTO scheduleCategory;

    private ScheduleType scheduleType;

    private LocalDate startDate;

    private LocalDate endDate;

    private Integer slot;

    private LocalTime sessionStart;

    private LocalTime sessionEnd;

    public boolean isConflict(ClazzScheduleReadDTO scheduleDTO) throws Exception {
        boolean check = false;

        if(this.getStartDate().isAfter(scheduleDTO.getStartDate())
                && this.getStartDate().isBefore(scheduleDTO.getEndDate())) {
            check = true;
        } else if(this.getEndDate().isAfter(scheduleDTO.getStartDate())
                && this.getEndDate().isBefore(scheduleDTO.getEndDate())) {
            check = true;
        } else if (this.getStartDate().isEqual(scheduleDTO.getStartDate())
                || this.getStartDate().isEqual(scheduleDTO.getEndDate())
                || this.getEndDate().isEqual(scheduleDTO.getStartDate())
                || this.getEndDate().isEqual(scheduleDTO.getEndDate())) {
            check = true;
        }

        /* Does it have any share time */
        if (check) {
            if (this.getScheduleCategoryId().equals(scheduleDTO.getScheduleCategoryId())) {
                return this.getSlot().equals(scheduleDTO.getSlot());
            }

            if (this.getScheduleCategory() == null
                    || scheduleDTO.getScheduleCategory() == null) {
                throw new IllegalArgumentException(
                        "Invalid check. Both ClazzScheduleReadDTO need ScheduleCategoryReadDTO not null");
            }

            if (this.getScheduleCategory().isConflict(scheduleDTO.getScheduleCategory())) {
                return this.getSlot().equals(scheduleDTO.getSlot());
            }
        }

        return false;
    }
}