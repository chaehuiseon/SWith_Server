package com.example.demo.src.controller;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.dto.request.SignInRequestDto;
import com.example.demo.src.dto.request.SignUpRequestDto;
import com.example.demo.src.dto.response.SignInResponseDto;
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
import java.util.logging.LoggingPermission;

@RestController
@RequiredArgsConstructor
@Api(tags = {"Swith User API"})
public class UserController {
    private final UserService userService;

    @ApiOperation("서버 시간")
    @GetMapping("/")
    public String time(){
        return LocalDateTime.now().toString();
    }

    @ApiOperation("회원 가입")
    @PostMapping("/v1/signUp")
    public BaseResponse<SignUpResponseDto> signUp(@Valid @RequestBody SignUpRequestDto signUpRequestDto) {
        try{
            SignUpResponseDto signUpResponseDto = userService.signUp(signUpRequestDto);
            return new BaseResponse<>(signUpResponseDto);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ApiOperation("로그인")
    @PostMapping("/v1/signIn")
    public SignInResponseDto signIn(@Valid @RequestBody SignInRequestDto signInRequestDto){
        return userService.signIn(signInRequestDto.getEmail(), signInRequestDto.getPassword());
    }

}
