package com.swith.api.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GetSessionRes {
    @ApiModelProperty(notes = "sessiond의 Id", example = "1")
    private Long sessionIdx;

    @ApiModelProperty(notes = "몇회차인지 순서를 나타냄", example = "5")
    private Integer sessionNum;

    @ApiModelProperty(notes = "회차 진행 시작 시각", example = "1997-09-12T12:30:30")
    private LocalDateTime sessionStart;

    @ApiModelProperty(notes = "회차 진행 종료 시각", example = "1997-09-12T12:30:30")
    private LocalDateTime sessionEnd;

    @ApiModelProperty(notes = "0:오프라인 1:온라인", example = "0")
    private Integer online;

    @ApiModelProperty(notes = "스터디 진행 장소", example = "상도역 스타벅스")
    private String place;

    @ApiModelProperty(notes = "해당 회차의 학습 상세 내용", example = "1.브루트 포스 알고리즘 \n -문제 1203번\n -문제 17203번\n -저번 주 푼 문제 복습")
    private String sessionContent;

    @ApiModelProperty(notes = "해당 회차에서 가입자들의 출석율(미 진행시 -1 반환)", example = "80")
    private Integer attendanceRate;
}
