package com.swith.domain.user.entity;


import javax.persistence.*;

import com.swith.domain.register.entity.Register;
import com.swith.domain.common.BaseTimeEntity;
import com.swith.domain.interest.entity.Interest;
import com.swith.domain.user.constant.RoleType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.*;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "USERS")
public class User extends BaseTimeEntity { //유저 테이블

    //@Id는 PK를 의미, @GeneratedValue 어노테이션은 기본키를 설정하는 전략으로
    //아래와 같이 설정하면 DB에 위임하는 방식(AUTO_INCREMENT)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long userIdx;

    private String email; //가입 email

    private String nickname; // 닉네임

    @ManyToOne(fetch = LAZY) // N:1 단방향
    @JoinColumn(name = "interestIdx1")
    private Interest interest1; //관심 분류

    @ManyToOne(fetch = LAZY) // N:1 단방향
    @JoinColumn(name = "interestIdx2")
    private Interest interest2; //관심 분류

    private String region; // 활동 지역

    @Column(length = 80)
    private String introduction; //소개글

    @Column(length = 100)
    private String profileImgUrl; //프로필 사진의 Url , S3연결해야됨.

    private Double averageStar; // 평점

    private Long ratedCnt; // 평가받은 횟수

    //DB에서는 TINYINT 타입으로 저장
    @Column(columnDefinition = "TINYINT")
    @Builder.Default
    private Integer status = 0; //0:활성화, 1:탈퇴

    private String refreshToken;

    @Enumerated(EnumType.STRING)
    private RoleType role;

    //가입한 그룹을 불러올 때 쓰일 것
    @Builder.Default
    @OneToMany(mappedBy = "user") // N:1 양방향
    private List<Register> registerList = new ArrayList<>();

    //fcmtoken 추가되야함
    @Column(length = 300)
    private String fcmtoken;


    public User updateRating(Double averageStar, Long ratedCnt) {
        this.averageStar = averageStar;
        this.ratedCnt = ratedCnt;
        return this;
    }

    public User updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

    public String getRoleKey(){
        return this.role.getKey();
    }

    public User update(String nickname, Interest interest1, Interest interest2, String introduction, String region){
        this.nickname = nickname;
        this.interest1 = interest1;
        this.interest2 = interest2;
        this.introduction = introduction;
        this.region = region;
        return this;
    }

    public User updateInterest(Interest interest1, Interest interest2){
        this.interest1 = interest1;
        this.interest2 = interest2;
        return this;
    }

    // by elin adminid 필요해서
    public Long getUserIdx() {
        return userIdx;
    }
}