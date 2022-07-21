package com.example.demo.src.entity;


import lombok.*;

import javax.persistence.*;


@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "RATING")
public class Rating extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ratingIdx; //평가 Idx

    //평가자를 기준으로 조회할일은 없기에 매핑할 이유가 없음
    private Long raterIdx; //평가자

    @ManyToOne // N:1 단방향
    @JoinColumn(name = "rateeIdx")
    private UserEntity user; //피평가자

    @Column(columnDefinition = "TINYINT")
    private Integer star; //평점(1~5)


}
