package com.example.demo.src.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class GetHomeGroupInfoRes {
    @ApiModelProperty(notes = "스터디 그룹의 인덱스", example = "10")
    private Long groupIdx; //스터디 그룹의 Idx

    @ApiModelProperty(notes = "스터디 그룹의 제목", example = "자격증 시험 스터디")
    private String title; //스터디 그룹의 제목

    @ApiModelProperty(notes = "정원(2~10)", example = "8")
    private Integer memberLimit; //정원(2~10)

    @ApiModelProperty(notes = "스터디 그룹을 대표하는 이미지 -S3", example = "www.~~")
    private String groupImgUrl;

    @ApiModelProperty(notes = "활동하는 지역 1", example = "1111000000")
    private Long regionIdx1;

    @ApiModelProperty(notes = "활동하는 지역 2", example = "1111000000")
    private Long regionIdx2;

    @ApiModelProperty(notes = "활동하는 지역 2", example = "1111000000")
    private Integer online;


    @ApiModelProperty(notes = "분류의 이름(카테고리)", example = "자격증")
    private String interestContent; //분류의 이름(카테고리)

    @ApiModelProperty(notes = "가장 최근의 공지사항 내용", example = "안녕하세요 6회차는 대면으로 진행할 예정입니다.")
    private String announcementContent; //가장 최근의 공지사항 내용

    @ApiModelProperty(notes = "가장 빠르게 예정인 회차의 순서", example = "5")
    private Integer sessionNum; //가장 최근 회차의 수

    @ApiModelProperty(notes = "가장 빠르게 예정인 회차의 학습 상세 내용", example = "1.브루트 포스 알고리즘 \n -문제 1203번\n -문제 17203번\n -저번 주 푼 문제 복습")
    private String sessionContent; //가장 최근 회차의 학습 상세 내용

    @ApiModelProperty(notes = "회차 진행 시작 시각(날짜 표시 용도)[년,월,일,시,분,초(0초가 아닐때만 표시)]", example = "[2022,7,28,16,50,25]")
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd,HH:mm:ss", timezone = "Asia/Seoul") // 원하는 포맷으로 표시
    private LocalDateTime sessionStart; //회차 진행 시작 시각(날짜 표시 용도)

    @ApiModelProperty(notes = "출석(지각,결석 제외)/총 출석 * 100", example = "50")
    private Integer attendanceRate; //현재까지의 출석율
}
