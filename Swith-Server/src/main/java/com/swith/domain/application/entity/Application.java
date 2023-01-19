package com.swith.domain.application.entity;


import com.swith.domain.common.BaseTimeEntity;
import com.swith.domain.groupinfo.entity.GroupInfo;
import com.swith.domain.user.entity.User;
import lombok.*;

import javax.persistence.*;

import static javax.persistence.FetchType.*;


@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "APPLICATION") //지원 테이블
public class Application extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long applicationIdx;

    @ManyToOne(fetch = LAZY) // N:1 단방향
    @JoinColumn(name = "userIdx")
    private User user; //지원한 유저 Idx

    @ManyToOne(fetch = LAZY) // N:1 단방향
    @JoinColumn(name = "groupIdx")
    private GroupInfo groupInfo; //지원자가 지원한 스터디 Idx

    @Column(columnDefinition = "TINYINT")
    @Builder.Default
    private Integer status = 0; //0: 승인 대기 1: 승인 2: 반려 3:추방

    @Column(length = 200)
    private String applicationContent; //지원서에 작성한 내용

    public void changeStatus(Integer status){
        this.status = status;

    }



}
