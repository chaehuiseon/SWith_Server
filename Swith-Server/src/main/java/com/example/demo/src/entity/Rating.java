package com.example.demo.src.entity;


import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity // 디비에 테이블을 생성
@Table(name = "RATING")
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ratingIdx;


    @ManyToOne
    @JoinColumn(name = "raterIdx")
    private User user1;

    @ManyToOne
    @JoinColumn(name = "rateeIdx")
    private User user2;



    //-> Timestamp 형과 비교해봐야됨.
    @CreationTimestamp // INSERT 시 자동으로 값을 채워줌
    private LocalDateTime createdAt = LocalDateTime.now(); //스터디 종료후 평가 완료 시 생성
}
