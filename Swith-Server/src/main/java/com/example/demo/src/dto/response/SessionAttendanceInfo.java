package com.example.demo.src.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SessionAttendanceInfo {
    private Long userIdx;
    private String nickname;
    private Integer status;
}
