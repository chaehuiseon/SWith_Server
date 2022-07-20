package com.example.demo.src.entity;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "STUDY")
@Entity // 디비에 테이블을 생성
public class GroupInfo extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int groupIdx;

    @ManyToOne //  N:1 단방향
    @JoinColumn(name = "adminIdx")
    private User user;

    @Column(length = 100)
    private String groupImgUrl;

    @Column( length = 45)
    private String title;

    //----------------------
    // 스터디 예정 주기    0: 주별 1: 월별 2: 자유
    @Column(columnDefinition = "TINYINT")
    private Integer meettime;

    // 주/월별 스터디 횟수 (meettime가 0,1일때만 값을 저장)
    @Column(columnDefinition = "TINYINT")
    private Integer frequency;

    // meet가 2일 경우 예정 주기에 대한 자유로운 문자열 입력
    @Column( length = 45)
    private String periods;
    //----------------------

    // 0: 오프라인 1: 온라인
    @Column(columnDefinition = "TINYINT")
    private Integer online;

    // 활동 예정 지역코드 2개 (온라인값이 1이면 둘 다 디폴트 값)
    @Builder.Default
    private Long regionIdx1 = 0000000000L;
    @Builder.Default
    private Long regionIdx2 = 0000000000L;


    //스터디 그룹의 분류
    @ManyToOne // N:1 단방향
    @JoinColumn(name = "interest")
    private Interest interest;

    //스터디 세부 주제
    @Column(length = 45)
    private String topic;

    @Min(2)
    @Max(10)
    @Column(columnDefinition = "TINYINT")
    private Integer memberLimit;

    //0: 선착순 1: 지원  모집방식
    @Column(columnDefinition = "TINYINT")
    private Integer applicationImethod;


    // 모집 기간 종료일
    @Temporal(TemporalType.TIMESTAMP)
    private Date recruitmentEndDate;

    // 스터디 활동 시작예정일
    @Temporal(TemporalType.TIMESTAMP)
    private Date groupStart;

    // 스터디 활동 종료예정일
    @Temporal(TemporalType.TIMESTAMP)
    private Date groupEnd;


    @Column(columnDefinition = "TINYINT")
    private Integer attendanceVaildTime; //출석 유효 시간

    @Column(length = 200)
    private String content; //소개글

    // 스터디 상태 // 0: 모집중 1: 진행중 2: 스터디 종료
    @Column(columnDefinition = "TINYINT")
    @Builder.Default
    private Integer status = 0;

    //Register 1:N 단방향
    //@OneToMany(mappedBy = "groupInfo")
    //private List<Register> registerList = new ArrayList<>();




}

