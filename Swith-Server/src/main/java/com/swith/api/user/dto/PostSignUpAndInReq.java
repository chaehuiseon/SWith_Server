package com.swith.api.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PostSignUpAndInReq {
    @Email
    @NotBlank
    @ApiModelProperty(notes = "유저 이메일",example = "test1@naver.com")
    private String email;

    @NotBlank
    @ApiModelProperty(notes = "유저 닉네임",example = "ym123")
    private String nickname; // 닉네임

    @ApiModelProperty(notes = "프로필 이미지 url",example = "https://lh3.googleusercontent.com/a/AItbvmmZTEhJKpZdLsPHSnT9XH2q469L0kulNTIFqjm2=s96-c")
    private String profileImgUrl; // 프로필 이미지 url

    @ApiModelProperty(notes = "알람 토큰",example = "token")
    private String token; // 프로필 이미지 url
}
