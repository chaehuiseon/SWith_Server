package com.example.demo.src.dto.response;

import com.example.demo.src.entity.Interest;
import com.example.demo.src.enums.RoleType;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class GetUserInfoRes {
    @ApiModelProperty(notes = "유저 이메일",example = "test1@naver.com")
    private String email;

    @ApiModelProperty(notes = "유저 닉네임",example = "ym123")
    private String nickname;

    @ApiModelProperty(notes = "소개글",example = "안녕하세요")
    private String introduction;

    @ApiModelProperty(notes = "관심 분류 인덱스1",example = "1")
    private Integer interestIdx1;

    @ApiModelProperty(notes = "관심 분류 인덱스2",example = "2")
    private Integer interestIdx2;

    @ApiModelProperty(notes = "평점", example = "2.5")
    private Double averageStar; // 평점

    @ApiModelProperty(notes = "소셜로그인 - role", example = "GUEST")
    private RoleType role;

    @ApiModelProperty(notes = "토큰",example = "token")
    private String refreshToken;

    private Integer status;
}
