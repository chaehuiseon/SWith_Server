package com.example.demo.src.entity;


import lombok.*;

import javax.persistence.*;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity // 디비에 테이블을 생성
@Table(name = "ATTENDANCE") //출석 테이블
public class Attendance extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attendanceIdx; //출석 Idx

    @ManyToOne // N:1 단방향
    @JoinColumn(name = "userIdx")
    private UserEntity user; //출석할, 한 유저 Idx

    @ManyToOne // N:1 양방향
    @JoinColumn(name = "sessionIdx")
    private Session session; //회차 Idx


    @Column(columnDefinition = "TINYINT")
    @Builder.Default
    private Integer status = 0; //0:예정 1:출석 2:지각 3:결석



}
