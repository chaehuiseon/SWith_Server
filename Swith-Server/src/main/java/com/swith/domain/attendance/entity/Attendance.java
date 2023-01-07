package com.swith.domain.attendance.entity;


import com.swith.domain.common.BaseTimeEntity;
import com.swith.domain.groupinfo.entity.GroupInfo;
import com.swith.domain.session.entity.Session;
import com.swith.domain.user.entity.User;
import lombok.*;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity // 디비에 테이블을 생성
@Table(name = "ATTENDANCE") //출석 테이블
public class Attendance extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attendanceIdx; //출석 Idx

    @ManyToOne(fetch = LAZY) // N:1 단방향
    @JoinColumn(name = "userIdx")
    private User user; //출석할, 한 유저 Idx

    @ManyToOne(fetch = LAZY) // N:1 단방향
    @JoinColumn(name = "groupIdx")
    private GroupInfo groupInfo; //

    @ManyToOne(fetch = LAZY)// N:1 양방향 //session과 생명 주기가 같으므로 cascade 고려
    @JoinColumn(name = "sessionIdx")
    private Session session; //회차 Idx

    @Column(columnDefinition = "TINYINT")
    @Builder.Default
    private Integer status = 0; //0:예정 1:출석 2:지각 3:결석

}
