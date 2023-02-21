package com.swith.api.attendance.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GetAttendanceInfo {

    @ApiModelProperty(notes = "session의 출석정보", example = "15")
    private Long attendanceIdx;

    @ApiModelProperty(notes = "session의 출석정보", example = "Nick")
    private String nickname;

    @ApiModelProperty(notes = "session의 출석정보", example = "1")
    private Integer status;
}
