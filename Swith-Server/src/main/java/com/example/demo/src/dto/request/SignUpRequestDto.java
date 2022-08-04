package com.example.demo.src.dto.request;

import com.example.demo.src.entity.Interest;
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
public class SignUpRequestDto {
    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String password;

    @NotBlank
    private String nickname; // 닉네임

    //Dto에 Entity를 넣으신 이유
    private Integer interest1; //관심 분류

    private Integer interest2; //관심 분류

    private String introduction; //소개글
}
