package com.example.demo.src.entity;


import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "USERSTUDY")
@Entity // 디비에 테이블을 생성
public class Register extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userGroupIdx;

    @ManyToOne // N : 1 단방향
    @JoinColumn(name = "userIdx")
    private User user;

    @ManyToOne// N : 1 단방향
    @JoinColumn(name = "groupIdx")
    private GroupInfo groupInfo;

    @Column(columnDefinition = "TINYINT")
    @Builder.Default
    private Integer status = 0; //0:가입 1:탈퇴

//    //-> Timestamp 형과 비교해봐야됨.
//    @CreationTimestamp // INSERT 시 자동으로 값을 채워줌
//    private LocalDateTime createdAt = LocalDateTime.now();
//
//    @UpdateTimestamp // UPDATE 시 자동으로 값을 채워줌
//    private LocalDateTime updatedAt = LocalDateTime.now();


}
