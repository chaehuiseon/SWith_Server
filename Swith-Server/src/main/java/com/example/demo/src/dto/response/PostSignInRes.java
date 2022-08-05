package com.example.demo.src.dto.response;

import com.example.demo.src.entity.Interest;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PostSignInRes {
    @ApiModelProperty(notes = "유저 이메일",example = "test1@naver.com")
    private String email;

    @ApiModelProperty(notes = "유저 닉네임",example = "ym123")
    private String nickname;

    @ApiModelProperty(notes = "관심 분류 인덱스1",example = "1")
    private Interest interest1;

    @ApiModelProperty(notes = "관심 분류 인덱스2",example = "2")
    private Interest interest2;

    @ApiModelProperty(notes = "토큰",example = "token")
    private String accessToken;

    @ApiModelProperty(notes = "토큰",example = "token")
    private String refreshToken;

    private Integer status;
}
