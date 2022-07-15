package com.example.demo.src.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@DynamicInsert //insert시에, null인 filed 제외시켜줌. -> createapi에서 설정할건지 봐야함 이거 지울거면 아래 초기화 값도 지워야됨.
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "STUDY")
@Entity // 디비에 테이블을 생성
public class Study {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int groupIdx;

    @OneToOne
    @JoinColumn(name = "adminIdx")
    private User user;

    @Column(nullable = false, length = 100)
    private String groupImgUrl;

    @Column(nullable = false, length = 45)
    private String title;

    @Column(nullable = false)
    private Byte meettime;

    @Column(nullable = false)
    private Byte frequency;

    @Column(nullable = false, length = 45)
    private String periods;

    @Column(nullable = false)
    private Byte online; // 0: 주별 1: 월별 2: 자유

    @Column(nullable = false)
    private Long regionIdx1; //활동지역1

    @Column(nullable = false)
    private Long regionIdx2; //활동지역2

    @OneToOne
    @JoinColumn(name = "interest",nullable = false)
    private Interest interest;

    @Column(nullable = false, length = 45)
    private String topic;

    @Column(nullable = false)
    @Min(2)
    @Max(10)
    private Byte memberLimit;

    @Column(nullable = false)
    private Byte applicationImethod;

    //모집 종료일 추가 -> 날짜 비교하는 방법 찾아보기
    //recruitmentEndDate

    //활동 시작 예정일 추가
    //groupStart

    //활동 종료일 예정 추가
    //groupEnd


    @Column(nullable = false)
    private Byte attendanceVaildTime; //출석 유효 시간

    @Column(nullable = false, length = 200)
    private String content; //소개글

    private Byte status = 0; //0: 모집중 1: 진행중 2: 스터디 종료



    //그룹 생성일 추가
    //-> Timestamp 형과 비교해봐야됨.
    @CreationTimestamp // INSERT 시 자동으로 값을 채워줌
    private LocalDateTime createdAt = LocalDateTime.now();


    //그룹 세부사항 수정일 추가
    @UpdateTimestamp // UPDATE 시 자동으로 값을 채워줌
    private LocalDateTime updatedAt = LocalDateTime.now();

    @OneToMany(mappedBy = "study")
    private List<UserStudy> userStudyList = new ArrayList<>();












}

