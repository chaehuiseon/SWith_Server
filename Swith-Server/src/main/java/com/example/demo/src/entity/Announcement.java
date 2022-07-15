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
@Table(name = "ANNOUNCEMENT")
@Entity // 디비에 테이블을 생성
public class Announcement { //공지사항

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int announcementIdx;

    @ManyToOne
    @JoinColumn(name = "groupIdx")
    private Study study;

    @Column(nullable = false,length = 200)
    private String announcementContent;

    @Column
    private Byte status = 0;     //0:활성화 1:비활성화

    //-> Timestamp 형과 비교해봐야됨.
    @CreationTimestamp // INSERT 시 자동으로 값을 채워줌
    private LocalDateTime createdAt = LocalDateTime.now();

    @UpdateTimestamp // UPDATE 시 자동으로 값을 채워줌
    private LocalDateTime updatedAt = LocalDateTime.now();








}
