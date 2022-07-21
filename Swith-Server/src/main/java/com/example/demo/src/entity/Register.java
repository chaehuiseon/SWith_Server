package com.example.demo.src.entity;


import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;


@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "REGISTER")
public class Register extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long registerIdx;

    @ManyToOne // N : 1 양방향
    @JoinColumn(name = "userIdx")
    private User user; //스터디에 가입된 유저 Idx

    @ManyToOne// N : 1 단방향
    @JoinColumn(name = "groupIdx")
    private GroupInfo groupInfo; //유저가 가입한 스터디 Idx

    @Column(columnDefinition = "TINYINT")
    @Builder.Default
    private Integer status = 0; //0:가입 1:탈퇴


}
