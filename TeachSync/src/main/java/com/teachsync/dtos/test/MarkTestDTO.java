package com.teachsync.dtos.test;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MarkTestDTO {
    private String questionContent;
    private String answerContent;
    private Long testRecordId;
}
