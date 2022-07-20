package com.example.demo.src.entity;


import javax.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "USER")
@Entity // 디비에 테이블을 생성
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userIdx;

    @Column(length = 45)
    private String email;

    @Column(length = 100)
    private String password;

    @ManyToOne // N:1 단방향
    @JoinColumn(name = "interest1")
    private Interest interest1; //관심 분류

    @ManyToOne // N:1 단방향
    @JoinColumn(name = "interest2")
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

//    //-> Timestamp 형과 비교해봐야됨.
//    @CreationTimestamp // INSERT 시 자동으로 값을 채워줌
//    private LocalDateTime createdAt = LocalDateTime.now();
//
//    @UpdateTimestamp // UPDATE 시 자동으로 값을 채워줌
//    private LocalDateTime updatedAt = LocalDateTime.now();

    @Builder.Default
    @OneToMany(mappedBy = "user")
    private List<Register> registerList = new ArrayList<>();





}
