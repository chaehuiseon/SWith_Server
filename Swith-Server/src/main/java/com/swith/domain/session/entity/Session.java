package com.swith.domain.session.entity;


import com.swith.domain.attendance.entity.Attendance;
import com.swith.domain.groupinfo.entity.GroupInfo;
import com.swith.domain.common.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "SESSION")
public class Session extends BaseTimeEntity {//회차 테이블

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sessionIdx;

    @ManyToOne(fetch = FetchType.LAZY) // N:1 단방향
    @JoinColumn(name = "groupIdx")
    private GroupInfo groupInfo; //회차가 속한 스터디 그룹 Idx

    private Integer sessionNum; //몇회차인지 순서를 나타냄

    private LocalDateTime sessionStart; //회차 진행 시작 시각

    private LocalDateTime sessionEnd; //회차 진행 종료 시각

    @Column(columnDefinition = "TINYINT")
    private Integer online; //0:오프라인 1:온라인

    @Column(length = 45)
    private String place; //스터디 진행 장소

    @Column(columnDefinition = "TINYINT")
    @Builder.Default
    private Integer status = 0; //0:활성화 1:비활성화

    @Column(length = 500)
    private String sessionContent; //해당 회차의 학습 상세 내용

    //N:1 양방향   //fetch join 을 위해서 사용
    @Builder.Default
    @OneToMany(mappedBy = "session", cascade = CascadeType.PERSIST)
    private List<Attendance> attendances = new ArrayList<>();
}
