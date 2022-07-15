package com.example.demo.src.entity;


import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity // 디비에 테이블을 생성
@Table(name = "INTEREST")
public class Interest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int interestIdx;

    @Column(nullable = false, length = 45)
    private String interestContent; //분류의 이름(카테고리)





}
