package com.swith.api.user.dto;

import com.swith.domain.user.entity.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PostSignUpRes {
    @ApiModelProperty(notes = "유저 이메일",example = "test1@naver.com")
    private String email;

    @ApiModelProperty(notes = "유저 닉네임",example = "ym123")
    private String nickname;

    @ApiModelProperty(notes = "관심 분류 인덱스1",example = "1")
    private Integer interestIdx1;

    @ApiModelProperty(notes = "관심 분류 인덱스2",example = "2")
    private Integer interestIdx2;

    @ApiModelProperty(notes = "소개글",example = "hello")
    private String introduction;

    @ApiModelProperty(notes = "지역",example = "서울 강남구")
    private String region; //지역

    public static PostSignUpRes toDetailUser(User user){
        return PostSignUpRes.builder()
                .email(user.getEmail())
                .interestIdx1(user.getInterest1().getInterestIdx())
                .interestIdx2(user.getInterest2().getInterestIdx())
                .nickname(user.getNickname())
                .introduction(user.getIntroduction())
                .region(user.getRegion())
                .build();
    }
}
