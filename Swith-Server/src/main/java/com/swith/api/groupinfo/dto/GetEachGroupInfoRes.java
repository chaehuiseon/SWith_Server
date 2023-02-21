package com.swith.api.groupinfo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.swith.domain.groupinfo.entity.GroupInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDate;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GetEachGroupInfoRes {

    @ApiModelProperty(notes = "스터디 개설 하는 유저의 PK",example = "1")
    private Long adminIdx;
    @ApiModelProperty(notes = "스터디 그룹 대표 이미지",example = "S3 경로")
    private String groupImgUrl;
    @ApiModelProperty(notes = "스터디 그룹의 제목", example = "자격증 시험 스터디")
    private String title;

    // 스터디 예정 주기    0: 주별 1: 월별 2: 자유
    @ApiModelProperty(notes = "스터디 예정 주기 0: 주별 1: 월별 2: 자유",example = "0")
    private Integer meet;

    // 주/월별 스터디 횟수 (meet가 0,1일때만 값을 저장할 수 있음)
    @ApiModelProperty(notes = "주/월별 스터디 횟수",example = "3")
    private Integer frequency;

    // meet가 2일 경우 예정 주기에 대한 자유로운 문자열 입력 받음.
    @ApiModelProperty(notes = "주기가 자유인 경우, 문자열로 입력",example = "격주에 2번")
    private String periods;
    //----------------------

    // 0: 오프라인 1: 온라인
    @ApiModelProperty(notes = "0: 오프라인 1: 온라인",example = "0")
    private Integer online;

    // 활동 예정 지역코드 2개 (온라인값이 1이면 둘 다 디폴트 값)

    @ApiModelProperty(notes = "스터디 활동 지역 코드",example = "1120000000")
    private String regionIdx1;
    @ApiModelProperty(notes = "스터디 활동 지역 코드",example = "1120000000")
    private String regionIdx2;

    //-----------------------


    @ApiModelProperty(notes = "스터디 그룹 분류",example = "알고리즘")
    private Integer interest; //스터디 그룹의 분류

    @ApiModelProperty(notes = "스터디 세부 주제",example = "백준 실버1~5단계 문제 풀이")
    private String topic; //스터디 세부 주제


    @ApiModelProperty(notes = "스터디 정원(2~10)",example = "10")
    @Min(2) @Max(10)
    private Integer memberLimit; //정원(2~10)

    @ApiModelProperty(notes = "스터디 신청 승인된 인원",example = "3")
    private Long NumOfApplicants; //스터디 신청 승인된 인원.

    @ApiModelProperty(notes = "모집방식 0: 선착순 1: 지원",example = "1")
    private Integer applicationMethod;  //모집 방식 -- 0: 선착순 1: 지원


    //--------------------

    // 모집 기간 종료일
    @ApiModelProperty(notes = "모집기간 종료일",example = "2022-08-04")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate recruitmentEndDate;

    // 스터디 활동 시작예정일
    @ApiModelProperty(notes = "스터디 활동 시작 예정일",example = "2022-08-04")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate groupStart;

    // 스터디 활동 종료예정일
    @ApiModelProperty(notes = "스터디 활동 종료 예정일",example = "2022-08-04")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate groupEnd;

    //--------------------

    // 출석 유효 시간 (10분 단위)
    @ApiModelProperty(notes = "출석 유효 시간 (10분 단위)",example = "20")
    private Integer attendanceValidTime;

    @ApiModelProperty(notes = "스터디 소개",example = "알고리즘 및 코딩테스트 스터디원 모집합니다. 백준 실버1~5단계를 같이 풀고, 리뷰하실분!")
    private String groupContent;



    public static GetEachGroupInfoRes of (GroupInfo groupInfo, Long NumOfApplicants){
        return GetEachGroupInfoRes.builder()
                .adminIdx(groupInfo.getUser().getUserIdx())
                .groupImgUrl(groupInfo.getGroupImgUrl())
                .title(groupInfo.getTitle())
                .meet(groupInfo.getMeet())
                .frequency(groupInfo.getFrequency())
                .periods(groupInfo.getPeriods())
                .online(groupInfo.getOnline())
                .regionIdx1(groupInfo.getRegionIdx1())
                .regionIdx2(groupInfo.getRegionIdx2())
                .interest(groupInfo.getInterest().getInterestIdx())
                .topic(groupInfo.getTopic())
                .memberLimit(groupInfo.getMemberLimit())
                .NumOfApplicants(NumOfApplicants)
                .applicationMethod(groupInfo.getApplicationMethod())
                .recruitmentEndDate(groupInfo.getRecruitmentEndDate())
                .groupStart(groupInfo.getGroupStart())
                .groupEnd(groupInfo.getGroupEnd())
                .attendanceValidTime(groupInfo.getAttendanceValidTime())
                .groupContent(groupInfo.getGroupContent())
                .build();


    }
}
