package com.example.demo.src.testlogic;

import com.example.demo.src.entity.Interest;
import com.example.demo.src.entity.User;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PostGroupReq {


    private Long adminIx;

    private String groupImgUrl;

    private String title;

    //----------------------
    // 스터디 예정 주기    0: 주별 1: 월별 2: 자유
    private int meettime;

    // 주/월별 스터디 횟수 (meettime가 0,1일때만 값을 저장)
    private int frequency;

    // meet가 2일 경우 예정 주기에 대한 자유로운 문자열 입력
    private String periods;
    //----------------------

    // 0: 오프라인 1: 온라인
    private Integer online;

    // 활동 예정 지역코드 2개 (온라인값이 1이면 둘 다 디폴트 값)
    private Long regionIdx1 ;
    private Long regionIdx2 ;


    //스터디 그룹의 분류
    private int interest;

    //스터디 세부 주제
    private String topic;
    private Integer memberLimit;

    //0: 선착순 1: 지원  모집방식
    private Integer applicationImethod;

    // 모집 기간 종료일
    private String recruitmentEndDate;

    // 스터디 활동 시작예정일
    private String groupStart;

    // 스터디 활동 종료예정일
    private String groupEnd;

    private Integer attendanceVaildTime; //출석 유효 시간

    private String content; //소개글

}
