package com.swith.api.user.dto;

import com.swith.domain.user.constant.RoleType;
import com.swith.domain.user.entity.User;
import com.swith.global.jwt.TokenInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PostUserInfoRes {
    @ApiModelProperty(notes = "유저 인덱스",example = "53")
    private Long userIdx;

    @ApiModelProperty(notes = "유저 이메일",example = "test1@naver.com")
    private String email;

    @ApiModelProperty(notes = "유저 닉네임",example = "ym123")
    private String nickname;

    @ApiModelProperty(notes = "프로필 이미지 링크",example = "https://lh3.googleusercontent.com/a/AItbvmmZTEhJKpZdLsPHSnT9XH2q469L0kulNTIFqjm2=s96-c")
    private String profileImgUrl;

    @ApiModelProperty(notes = "소개글",example = "안녕하세요")
    private String introduction;

    @ApiModelProperty(notes = "관심 분류 인덱스1",example = "1")
    private Integer interestIdx1;

    @ApiModelProperty(notes = "관심 분류 인덱스2",example = "2")
    private Integer interestIdx2;

    @ApiModelProperty(notes = "활동지경",example = "서울 강남구")
    private String region;

    @ApiModelProperty(notes = "평점", example = "2.5")
    private Double averageStar; // 평점

    @ApiModelProperty(notes = "소셜로그인 - role", example = "GUEST")
    private RoleType role;

    @ApiModelProperty(notes = "엑세스토큰",example = "token")
    private String accessToken;

    @ApiModelProperty(notes = "리프레시토큰",example = "token")
    private String refreshToken;

    @ApiModelProperty(notes = "안드로이드 고유 기기 알람 토큰",example = "token")
    private String fcmtoken;

    @ApiModelProperty(notes = "처음 가입하는지 여부",example = "false")
    private Boolean isSignUp;

    private Integer status;

    public static PostUserInfoRes toDetailUser(User user){
        return PostUserInfoRes.builder()
                .userIdx(user.getUserIdx())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .profileImgUrl(user.getProfileImgUrl())
                .introduction(user.getIntroduction())
                .interestIdx1(user.getInterest1().getInterestIdx())
                .interestIdx2(user.getInterest2().getInterestIdx())
                .region(user.getRegion())
                .averageStar(user.getAverageStar())
                .role(user.getRole())
                .refreshToken(user.getRefreshToken())
                .fcmtoken(user.getFcmtoken())
                .status(user.getStatus())
                .build();
    }

    public static PostUserInfoRes from(User user, Integer interestIdx1, Integer interestIdx2, TokenInfo accessTokenDto, TokenInfo refreshTokenDto){
        return PostUserInfoRes.builder()
                .userIdx(user.getUserIdx())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .profileImgUrl(user.getProfileImgUrl())
                .introduction(user.getIntroduction())
                .interestIdx1(interestIdx1)
                .interestIdx2(interestIdx2)
                .region(user.getRegion())
                .averageStar(user.getAverageStar())
                .role(user.getRole())
                .accessToken(accessTokenDto.getToken())
                .refreshToken(refreshTokenDto.getToken())
                .fcmtoken(user.getFcmtoken())
                .isSignUp(false)
                .status(user.getStatus())
                .build();
    }
}
