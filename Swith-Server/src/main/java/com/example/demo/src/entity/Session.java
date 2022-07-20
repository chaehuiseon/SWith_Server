package com.example.demo.src.entity;


import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;



@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "SESSION")
@Entity // 디비에 테이블을 생성
public class Session extends BaseTimeEntity {//회차 테이블

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer sessionIdx;

    @ManyToOne // N:1 단방향
    @JoinColumn(name = "groupIdx")
    private GroupInfo groupInfo;

    private Integer sessionNum;

    @Temporal(TemporalType.TIMESTAMP)
    private Date sessionStart; //회차 진행 시작 시각
    @Temporal(TemporalType.TIMESTAMP)
    private Date sessionEnd; //회차 진행 종료 시각


    @Column(columnDefinition = "TINYINT")
    private Integer online;


    @Column(length = 45)
    private String place;

    @Column(columnDefinition = "TINYINT")
    @Builder.Default
    private Integer status = 0; //0:오프라인 1:온라인

    @Column(length = 500)
    private String sessionContent; //해당 회차의 학습 상세 내용

    //N:1 양방향
    @Builder.Default
    @OneToMany(mappedBy = "session")
    private List<Attendance> attendances = new ArrayList<>();


//    //-> Timestamp 형과 비교해봐야됨.
//    @CreationTimestamp // INSERT 시 자동으로 값을 채워줌
//    private LocalDateTime createdAt = LocalDateTime.now();
//
//    @UpdateTimestamp // UPDATE 시 자동으로 값을 채워줌
//    private LocalDateTime updatedAt = LocalDateTime.now();






}
