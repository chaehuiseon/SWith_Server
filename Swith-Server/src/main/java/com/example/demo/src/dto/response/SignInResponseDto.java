package com.example.demo.src.dto.response;

import com.example.demo.src.entity.Interest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class SignInResponseDto {
    private String email;
    private String nickname;
    private Interest interest1;
    private Interest interest2;
    private String accessToken;
    private String refreshToken;
}
