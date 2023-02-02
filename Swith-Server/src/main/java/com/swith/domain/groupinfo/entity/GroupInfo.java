package com.swith.domain.groupinfo.entity;

import com.swith.domain.common.BaseTimeEntity;
import com.swith.domain.interest.entity.Interest;
import com.swith.domain.user.entity.User;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDate;

import static javax.persistence.FetchType.*;


@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
//@DynamicUpdate
@Table(name = "GROUPINFO", indexes = @Index(columnList = "recruitmentEndDate"))
public class GroupInfo extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groupIdx; //스터디 그룹의 Idx

    @ManyToOne(fetch = LAZY) //  N:1 단방향
    @JoinColumn(name = "adminIdx")
    private User user; //그룹장의 userIdx - 관리자 PK

    @Column(length = 100)
    private String groupImgUrl; //스터디 그룹을 대표하는 이미지 -S3

    @Column( length = 45)
    private String title; //스터디 그룹의 제목

    //----------------------
    // 스터디 예정 주기    0: 주별 1: 월별 2: 자유
    @Column(columnDefinition = "TINYINT")
    private Integer meet;

    // 주/월별 스터디 횟수 (meet가 0,1일때만 값을 저장할 수 있음)
    @Column(columnDefinition = "TINYINT")
    private Integer frequency;

    // meet가 2일 경우 예정 주기에 대한 자유로운 문자열 입력 받음.
    @Column( length = 45)
    private String periods;
    //----------------------

    // 0: 오프라인 1: 온라인
    @Column(columnDefinition = "TINYINT")
    private Integer online;

    // 활동 예정 지역코드 2개 (온라인값이 1이면 둘 다 디폴트 값)
    @Builder.Default
    private String regionIdx1 = "0000000000"; //활동하는 지역 1
    @Builder.Default
    private String regionIdx2 = "0000000000"; //활동하는 지역 2

    //-----------------------


    @ManyToOne(fetch = LAZY) // N:1 단방향
    @JoinColumn(name = "interest")
    private Interest interest; //스터디 그룹의 분류

    @Column(length = 45)
    private String topic; //스터디 세부 주제


    @Min(2) @Max(10)
    @Column(columnDefinition = "TINYINT")
    private Integer memberLimit; //정원(2~10)

    @Column(columnDefinition = "TINYINT")
    private Integer applicationMethod;  //모집 방식 -- 0: 선착순 1: 지원


    //--------------------

    // 모집 기간 종료일
    private LocalDate recruitmentEndDate;

    // 스터디 활동 시작예정일
    private LocalDate groupStart;

    // 스터디 활동 종료예정일
    private LocalDate groupEnd;

    //--------------------

    // 출석 유효 시간 (10분 단위)
    @Column(columnDefinition = "TINYINT")
    private Integer attendanceValidTime;


    // 스터디에 대한 학습 예정 내용, 소개글
    @Column(length = 200)
    private String groupContent;


    // 스터디 상태 // 0: 모집중 1: 진행중 2: 스터디 종료
    @Column(columnDefinition = "TINYINT")
    @Builder.Default
    private Integer status = 0;


    public void changeStatus(Integer status){
        this.status = status;

    }

}