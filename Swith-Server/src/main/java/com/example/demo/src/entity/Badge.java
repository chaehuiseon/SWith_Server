package com.example.demo.src.entity;


import lombok.*;

import javax.persistence.*;

import static javax.persistence.FetchType.*;


@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity  // 디비에 테이블을 생성
@Table(name = "BADGE")
public class Badge extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long badgeIdx;

    @ManyToOne(fetch = LAZY) // N:1 단방향
    @JoinColumn(name = "userIdx")
    private User user; //뱃지가 표시될 프로필의 소유자 인덱스

    private Double attendanceRate; //종료된 스터디에서의 출석율

    @ManyToOne(fetch = LAZY) // N:1 단방향
    @JoinColumn(name = "interest")
    private Interest interest; //종료된 스터디 그룹의 분류




}
