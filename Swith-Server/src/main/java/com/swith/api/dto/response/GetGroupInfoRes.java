package com.swith.api.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class GetGroupInfoRes {
    @ApiModelProperty(notes = "어드민 여부", example = "10")
    private boolean isAdmin;

    @ApiModelProperty(notes = "스터디 그룹의 제목", example = "자격증 시험 스터디")
    private String title; //스터디 그룹의 제목

    @ApiModelProperty(notes = "스터디 대표 이미지의 링크", example = "www.~~")
    private String groupImgUrl;

    @ApiModelProperty(notes = "스터디 대표 이미지의 링크", example = "www.~~")
    private Integer attendanceValidTime;

    @ApiModelProperty(notes = "가장 최근의 공지사항 내용", example = "안녕하세요 6회차는 대면으로 진행할 예정입니다.")
    private String announcementContent; //가장 최근의 공지사항 내용

    @ApiModelProperty(notes = "공지사항의 날짜", example = "5")
    private LocalDateTime announcementDate;

    @ApiModelProperty(notes = "표시될 session들의 정보", example = "")
    private List<GetSessionRes> getSessionResList;
}
