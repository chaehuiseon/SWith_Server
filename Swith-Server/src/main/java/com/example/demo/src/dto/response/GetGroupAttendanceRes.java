package com.example.demo.src.dto.response;

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
    @ApiModelProperty(notes = "출석 유효 시간", example = "10분")
    private String attendanceValidTime;

    @ApiModelProperty(notes = "유저별 출석율", example = "")
    private List<GetUserAttendanceRes> getUserAttendanceResList;
}
