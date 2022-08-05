package com.example.demo.src.controller;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.dto.request.PostSignInReq;
import com.example.demo.src.dto.request.PostSignUpReq;
import com.example.demo.src.dto.response.PostSignInRes;
import com.example.demo.src.dto.response.PostSignUpRes;
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
    public BaseResponse<PostSignUpRes> signUp(@Valid @RequestBody PostSignUpReq postSignUpReq) {
        try{
            PostSignUpRes postSignUpRes = userService.signUp(postSignUpReq);
            return new BaseResponse<>(postSignUpRes);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ApiOperation("로그인")
    @PostMapping("/v1/signIn")
    public BaseResponse<PostSignInRes> signIn(@Valid @RequestBody PostSignInReq postSignInReq){
//        return userService.signIn(postSignInReq.getEmail(), postSignInReq.getPassword());
        try{
            PostSignInRes postSignInRes = userService.signIn(postSignInReq.getEmail(), postSignInReq.getPassword());
            return new BaseResponse<>(postSignInRes);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
}
