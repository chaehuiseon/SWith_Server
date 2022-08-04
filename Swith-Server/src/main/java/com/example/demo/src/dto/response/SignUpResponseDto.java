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
public class SignUpResponseDto {
    private String email;

    private String nickname;

    private Integer interestIdx1;

    private Integer interestIdx2;

    private String introduction;
}
