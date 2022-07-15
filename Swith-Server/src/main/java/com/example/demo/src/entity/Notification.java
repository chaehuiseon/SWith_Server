package com.example.demo.src.entity;


import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity // 디비에 테이블을 생성
@Table(name = "NOTIFICATION")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int notificationIdx;

    @ManyToOne
    @JoinColumn(name = "userIdx")
    private User user; //알림 받는 사람 Idx

    @Column(nullable = false, length = 45)
    private String notificationContent; //내용




}
