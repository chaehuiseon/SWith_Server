package com.example.demo.src.entity;


import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;


@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ANNOUNCEMENT") //공지사항 테이블
public class Announcement extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long announcementIdx; //공지사항 Idx

    @ManyToOne // N:1 단방향
    @JoinColumn(name = "groupIdx")
    private GroupInfo groupInfo; //그룹 Idx

    @Column(length = 200)
    private String announcementContent; //공지사항 내용

    @Column(columnDefinition = "TINYINT")
    @Builder.Default
    private Integer status = 0; //0:활성화 1:비활성화



}
