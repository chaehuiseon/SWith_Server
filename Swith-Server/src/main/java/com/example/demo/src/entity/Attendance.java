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
@Entity // 디비에 테이블을 생성
@Table(name = "ATTENDANCE")
public class Attendance extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer attendanceIdx;

    @ManyToOne // N:1 단방향
    @JoinColumn(name = "userIdx")
    private User user;

    @ManyToOne // N:1 양방향
    @JoinColumn(name = "sessionIdx")
    private Session session;


    @Column(columnDefinition = "TINYINT")
    @Builder.Default
    private Integer status = 0; //0:예정 1:출석 2:지각 3:결석


//    //출석 시각
//    //-> Timestamp 형과 비교해봐야됨.
//    @CreationTimestamp // INSERT 시 자동으로 값을 채워줌
//    private LocalDateTime createdAt = LocalDateTime.now();
//
//
//    //출석 상태 변경 시각
//    @UpdateTimestamp // UPDATE 시 자동으로 값을 채워줌
//    private LocalDateTime updatedAt = LocalDateTime.now();



}
