package com.example.demo.src.controller;

import com.example.demo.src.dto.request.SignUpRequestDto;
import com.example.demo.src.dto.response.SignUpResponseDto;
import com.example.demo.src.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@Api(tags = {"Swith API Test"})
public class UserController {
    private final UserService userService;

    @ApiOperation("서버 시간")
    @GetMapping("/")
    public String time(){
        return LocalDateTime.now().toString();
    }

    @ApiOperation("회원 가입")
    @PostMapping("/v1/signUp")
    public SignUpResponseDto signUp(@Valid @RequestBody SignUpRequestDto signUpRequestDto){
        return userService.signUp(signUpRequestDto.getEmail(), signUpRequestDto.getPassword(), signUpRequestDto.getNickname(),
                signUpRequestDto.getInterest1(), signUpRequestDto.getInterest2(),
                signUpRequestDto.getIntroduction());
    }


}
