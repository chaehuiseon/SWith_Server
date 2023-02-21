package com.swith.domain.memo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.swith.domain.common.BaseTimeEntity;
import com.swith.domain.session.entity.Session;
import com.swith.domain.user.entity.User;
import lombok.*;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "MEMO")
public class Memo extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long memoIdx;

    private String memoContent;

    @Builder.Default
    Integer status = 0;

    @ManyToOne(fetch = LAZY) // N:1 단방향
    @JoinColumn(name = "userIdx")
    private User user;

    @ManyToOne(fetch = LAZY) // N:1 단방향
    @JoinColumn(name = "sessionIdx")
    private Session session;
}
