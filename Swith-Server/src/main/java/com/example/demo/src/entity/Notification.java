package com.example.demo.src.entity;


import lombok.*;

import javax.persistence.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity // 디비에 테이블을 생성
@Table(name = "NOTIFICATION")
public class Notification extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer notificationIdx; // 알림 Idx

    @ManyToOne
    @JoinColumn(name = "userIdx")
    private User user; //알림 받는 사람 Idx

    @Column(length = 45)
    private String notificationContent; //내용




}
