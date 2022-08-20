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
public class GetUserInfoReq {
//    @Email
//    @NotBlank
//    @ApiModelProperty(notes = "유저 이메일",example = "test1@naver.com")
//    private String email;

    @ApiModelProperty(notes = "유저 고유 idx",example = "53")
    private Long userIdx;

}
