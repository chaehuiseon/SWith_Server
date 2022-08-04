package com.example.demo.src.dto;

import com.example.demo.src.entity.Interest;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostGroupInfoReq {

    private Long adminIdx;
    private String groupImgUrl;
    private String title;

    // 스터디 예정 주기    0: 주별 1: 월별 2: 자유
    private int meet;

    // 주/월별 스터디 횟수 (meet가 0,1일때만 값을 저장할 수 있음)
    private int frequency;

    // meet가 2일 경우 예정 주기에 대한 자유로운 문자열 입력 받음.
    private String periods;
    //----------------------

    // 0: 오프라인 1: 온라인
    private int online;

    // 활동 예정 지역코드 2개 (온라인값이 1이면 둘 다 디폴트 값)

    private long regionIdx1;
    private long regionIdx2;

    //-----------------------


    private int interest; //스터디 그룹의 분류

    private String topic; //스터디 세부 주제


    @Min(2) @Max(10)
    private int memberLimit; //정원(2~10)

    private int applicationMethod;  //모집 방식 -- 0: 선착순 1: 지원


    //--------------------

    // 모집 기간 종료일
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate recruitmentEndDate;

    // 스터디 활동 시작예정일
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate groupStart;

    // 스터디 활동 종료예정일
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate groupEnd;

    //--------------------

    // 출석 유효 시간 (10분 단위)
    private int attendanceValidTime;

    private String groupContent;


}
