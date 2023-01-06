package com.swith.src.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class GetUserAttendanceRes {
    @ApiModelProperty(notes = "user id", example = "1")
    private Long userIdx;
    @ApiModelProperty(notes = "user nickname", example = "Nick")
    private String nickname;
    @ApiModelProperty(notes = "개인별 출석율", example = "80")
    private Integer attendanceRate;

}
