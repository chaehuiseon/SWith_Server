package com.example.demo.src.entity;


import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "USER")
@Entity // 디비에 테이블을 생성
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userIdx;

    @Column(nullable = false, length = 45)
    private String email;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false)
    private byte interest1;

    @Column(nullable = false)
    private byte interest2;

    @Column(nullable = false, length = 80)
    private String introduction;

    @Column(nullable = false, length = 100)
    private String profileImgUrl;

    @Column(nullable = false)
    private double averageStar;

    //-> Timestamp 형과 비교해봐야됨.
    @CreationTimestamp // INSERT 시 자동으로 값을 채워줌
    private LocalDateTime createdAt = LocalDateTime.now();

    @UpdateTimestamp // UPDATE 시 자동으로 값을 채워줌
    private LocalDateTime updatedAt = LocalDateTime.now();


    @OneToMany(mappedBy = "user")
    private List<UserStudy> userStudyList = new ArrayList<>();





}
