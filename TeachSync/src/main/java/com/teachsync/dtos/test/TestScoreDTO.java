package com.teachsync.dtos.test;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestScoreDTO {
    private String userTest;
    private String type;
    private String score;
}
