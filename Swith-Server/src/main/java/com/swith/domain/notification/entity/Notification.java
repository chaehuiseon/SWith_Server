package com.swith.domain.notification.entity;


import com.swith.domain.common.BaseTimeEntity;
import com.swith.domain.user.entity.User;
import lombok.*;

import javax.persistence.*;

import static javax.persistence.FetchType.*;


@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "NOTIFICATION")
public class Notification extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationIdx; // 알림 Idx

    @ManyToOne(fetch = LAZY) // N:1 단방향
    @JoinColumn(name = "userIdx")
    private User user; //알림 받는 사람 Idx

    @Column(length = 45)
    private String notificationContent; //알림 내용
}
