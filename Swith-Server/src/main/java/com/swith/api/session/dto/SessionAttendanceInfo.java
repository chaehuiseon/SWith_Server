package com.swith.api.session.dto;

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
