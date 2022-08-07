package com.example.demo.src.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class GetUserAttendanceRes {
    private Long userIdx;
    private String nickname;
    private Integer attendanceRate;

}
