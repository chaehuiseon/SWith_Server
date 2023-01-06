package com.swith.src.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GetSessionAttendanceRes {
    @ApiModelProperty(notes = "sessiond의 Id", example = "1")
    private Long sessionIdx;

    @ApiModelProperty(notes = "몇회차인지 순서를 나타냄", example = "5")
    private Integer sessionNum;

    @ApiModelProperty(notes = "session의 출석정보", example = "")
    private List<GetAttendanceInfo>  getAttendanceInfos;
}
