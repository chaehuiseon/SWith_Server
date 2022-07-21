package com.example.demo.src.dto.request;

import com.example.demo.src.entity.Interest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SignUpRequestDto {
    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String password;

    private Interest interest1; //관심 분류

    private Interest interest2; //관심 분류

    private String introduction; //소개글
}
