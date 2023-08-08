package com.teachsync.dtos.homework;

import com.teachsync.dtos.BaseReadDTO;
import com.teachsync.dtos.clazz.ClazzReadDTO;
import com.teachsync.dtos.memberHomeworkRecord.MemberHomeworkRecordReadDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HomeworkReadDTO extends BaseReadDTO {

    private Long clazzId;
    private String clazzName;
    private ClazzReadDTO clazz;

    private String homeworkName;

    private String homeworkDesc;

    private String homeworkDoc;

    private byte[] homeworkContent;

    private LocalDateTime deadline;

    private LocalDateTime openAt;

    private List<MemberHomeworkRecordReadDTO> memberHomeworkRecordList;

}