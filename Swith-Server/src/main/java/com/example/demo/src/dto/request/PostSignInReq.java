package com.example.demo.src.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PostSignInReq {
    @Email
    @NotBlank
    @ApiModelProperty(notes = "유저 이메일",example = "test1@naver.com")
    private String email;
    @NotBlank
    @ApiModelProperty(notes = "유저 비밀번호",example = "pwd123")
    private String password;
}
