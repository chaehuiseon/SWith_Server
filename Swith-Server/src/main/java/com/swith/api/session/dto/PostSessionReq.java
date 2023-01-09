package com.swith.api.session.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostSessionReq {

    @ApiModelProperty(notes = "스터디 그룹의 인덱스", example = "2")
    private Long groupIdx; //회차가 속한 스터디 그룹 Idx

    @ApiModelProperty(notes = "회차생성을 시도하는 유저의 Id", example = "1")
    private Long userIdx;

    @ApiModelProperty(notes = "회차 진행 시작 시각", example = "1997-09-12T12:30:30")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime sessionStart;

    @ApiModelProperty(notes = "회차 진행 종료 시각", example = "1997-09-12T15:30:30")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime sessionEnd;

    @ApiModelProperty(notes = "0:오프라인 1:온라인", example = "0")
    private Integer online;

    @ApiModelProperty(notes = "스터디 진행 장소", example = "상도역 스타벅스")
    private String place;

    @ApiModelProperty(notes = "해당 회차의 학습 상세 내용", example = "그리디 알고리즘 \n-1254번 문제 풀기 \n-7739번 문제 풀기\n지난 회차 학습내용 복습")
    private String sessionContent;
}
