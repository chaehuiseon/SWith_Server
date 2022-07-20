package com.example.demo.src.entity;


import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity // 디비에 테이블을 생성
@Table(name = "BADGE")
public class Badge extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer badgeIdx;

    @ManyToOne // N:1 단방향
    @JoinColumn(name = "userIdx")
    private User user;

    @Column(nullable = false)
    private double attendanceRate; //종료된 스터디에서의 출석율

    @ManyToOne // N:1 단방향
    @JoinColumn(name = "interest")
    private Interest interest; //종료된 스터디 그룹의 분류


//    //-> Timestamp 형과 비교해봐야됨.
//    @CreationTimestamp // INSERT 시 자동으로 값을 채워줌
//    private LocalDateTime createdAt = LocalDateTime.now(); //스터디 종료후 평가 완료 시 생성








}
