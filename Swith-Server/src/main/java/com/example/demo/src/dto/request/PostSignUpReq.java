package com.example.demo.src.dto.request;

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
public class PostSignUpReq {
    @Email
    @NotBlank
    @ApiModelProperty(notes = "유저 이메일",example = "test1@naver.com")
    private String email;

    @NotBlank
    @ApiModelProperty(notes = "유저 닉네임",example = "ym123")
    private String nickname; // 닉네임

    @ApiModelProperty(notes = "관심 분류 인덱스1",example = "1")
    private Integer interest1; //관심 분류

    @ApiModelProperty(notes = "관심 분류 인덱스2",example = "2")
    private Integer interest2; //관심 분류

    @ApiModelProperty(notes = "소개글",example = "hello")
    private String introduction; //소개글
}
