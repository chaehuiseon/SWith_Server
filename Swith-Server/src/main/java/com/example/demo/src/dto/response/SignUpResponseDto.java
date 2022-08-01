package com.example.demo.src.dto.response;

import com.example.demo.src.entity.Interest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class SignUpResponseDto {
    private String email;

    private String nickname;

    private Interest interest1;

    private Interest interest2;

    private String introduction;
}
