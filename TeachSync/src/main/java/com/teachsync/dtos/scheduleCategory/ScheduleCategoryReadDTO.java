package com.teachsync.dtos.scheduleCategory;

import com.teachsync.dtos.BaseReadDTO;
import com.teachsync.dtos.clazzSchedule.ClazzScheduleReadDTO;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * DTO for {@link com.teachsync.entities.ScheduleCategory}
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleCategoryReadDTO extends BaseReadDTO {

    private String categoryName;

    private String categoryDesc;

    private Boolean atMon = false;

    private Boolean atTue = false;

    private Boolean atWed = false;

    private Boolean atThu = false;

    private Boolean atFri = false;

    private Boolean atSat = false;

    private Boolean atSun = false;

    public boolean isConflict(ScheduleCategoryReadDTO categoryDTO) throws Exception {
        if (this.getAtMon() && categoryDTO.getAtMon()) { return true; } 
        if (this.getAtTue() && categoryDTO.getAtTue()) { return true; } 
        if (this.getAtWed() && categoryDTO.getAtWed()) { return true; } 
        if (this.getAtThu() && categoryDTO.getAtThu()) { return true; } 
        if (this.getAtFri() && categoryDTO.getAtFri()) { return true; } 
        if (this.getAtSat() && categoryDTO.getAtSat()) { return true; } 
        if (this.getAtSun() && categoryDTO.getAtSun()) { return true; }

        return false;
    }
}
