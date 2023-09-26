package com.teachsync.dtos.session;
import com.teachsync.dtos.BaseCreateDTO;
import com.teachsync.dtos.BaseUpdateDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for {@link com.teachsync.entities.Session}
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SessionUpdateDTO extends BaseUpdateDTO {
    private Long roomId;
    private Long scheduleId;
    private Long staffId;
    private Integer slot;
    private LocalDateTime sessionStart;
    private LocalDateTime sessionEnd;
}