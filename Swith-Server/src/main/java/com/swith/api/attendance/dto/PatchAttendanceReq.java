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
public class PatchAttendanceReq {

    @ApiModelProperty(notes = "출석의 Idx", example = "21")
    private Long attendanceIdx;

    @ApiModelProperty(notes = "출석의 상태", example = "1")
    private Integer status;
}
