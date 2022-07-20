package com.example.demo.src.entity;


import javax.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "USER")
public class User extends BaseTimeEntity { //유저 테이블

    //@Id는 PK를 의미, @GeneratedValue 어노테이션은 기본키를 설정하는 전략으로
    //아래와 같이 설정하면 DB에 위임하는 방식(AUTO_INCREMENT)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userIdx;

    @Column(length = 45)
    private String email; //가입 email

    @Column(length = 100)
    private String password; //가입 password

    @ManyToOne // N:1 단방향
    @JoinColumn(name = "interestIdx1")
    private Interest interest1; //관심 분류

    @ManyToOne // N:1 단방향
    @JoinColumn(name = "interestIdx2")
    private Interest interest2; //관심 분류


    @Column(length = 80)
    private String introduction; //소개글

    @Column(length = 100)
    private String profileImgUrl; //프로필 사진의 Url , S3연결해야됨.

    private Double averageStar; // 평점

    //DB에서는 TINYINT 타입으로 저장
    @Column(columnDefinition = "TINYINT")
    @Builder.Default
    private Integer status = 0; //0:활성화, 1:탈퇴


    //가입한 그룹을 불러올 때 쓰일 것
    @Builder.Default
    @OneToMany(mappedBy = "user") // N:1 양방향
    private List<Register> registerList = new ArrayList<>();





}
