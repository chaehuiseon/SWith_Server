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
@Table(name = "ANNOUNCEMENT")
@Entity // 디비에 테이블을 생성
public class Announcement extends BaseTimeEntity{ //공지사항

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long announcementIdx;

    @ManyToOne // N:1 단방향
    @JoinColumn(name = "groupIdx")
    private GroupInfo groupInfo;

    @Column(length = 200)
    private String announcementContent;

    @Column(columnDefinition = "TINYINT")
    @Builder.Default
    private Integer status = 0; //0:활성화 1:비활성화


//    //-> Timestamp 형과 비교해봐야됨.
//    @CreationTimestamp // INSERT 시 자동으로 값을 채워줌
//    private LocalDateTime createdAt = LocalDateTime.now();
//
//    @UpdateTimestamp // UPDATE 시 자동으로 값을 채워줌
//    private LocalDateTime updatedAt = LocalDateTime.now();




}
