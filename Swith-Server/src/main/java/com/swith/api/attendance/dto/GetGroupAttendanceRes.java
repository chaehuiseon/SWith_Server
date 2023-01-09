package com.swith.api.attendance.dto;

import com.swith.api.dto.response.GetUserAttendanceRes;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class GetGroupAttendanceRes {
    @ApiModelProperty(notes = "출석 유효 시간", example = "10")
    private Integer attendanceValidTime;

    @ApiModelProperty(notes = "유저별 출석 정보")
    private List<GetUserAttendanceRes> getUserAttendanceResList;
}
