package com.example.demo.src.entity;


import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;


@DynamicInsert //insert시에, null인 filed 제외시켜줌. -> createapi에서 설정할건지 봐야함 이거 지울거면 아래 초기화 값도 지워야됨.
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "SESSION")
@Entity // 디비에 테이블을 생성
public class Session {//회차 테이블

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int sessionIdx;

    @ManyToOne
    @JoinColumn(name = "groupIdx")
    private Study study;

    @Column(nullable = false)
    private int sessionNum;

    //회차 진행 시작 시각 sessiontStart

    //회차 진행 종료 시각 sessiontEnd

    @Column(nullable = false)
    private Byte online; //0:오프라인 1:온라인

    @Column(nullable = false,length = 45)
    private String place;

    @Column
    private Byte status = 0; //0:활성화 1:비활성화

    @Column(nullable = false,length = 500)
    private String sessionContent; //해당 회차의 학습 상세 내용


    //-> Timestamp 형과 비교해봐야됨.
    @CreationTimestamp // INSERT 시 자동으로 값을 채워줌
    private LocalDateTime createdAt = LocalDateTime.now();

    @UpdateTimestamp // UPDATE 시 자동으로 값을 채워줌
    private LocalDateTime updatedAt = LocalDateTime.now();






}
