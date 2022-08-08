package com.example.demo.src.controller;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.auth.SessionUser;
import com.example.demo.security.AuthInfo;
import com.example.demo.security.Authenticated;
import com.example.demo.src.dto.request.PostSignInReq;
import com.example.demo.src.dto.request.PostSignUpReq;
import com.example.demo.src.dto.response.PostSignInRes;
import com.example.demo.src.dto.response.PostSignUpRes;
import com.example.demo.src.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.ui.Model;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@Api(tags = {"Swith User API"})
public class UserController {
    private final UserService userService;
    private final HttpSession httpSession;

    @ApiOperation("로그인 성공 시 세션에 SessionUser 저장")
    @GetMapping("/")
    public String index(Model model)
    {
        // userName을 사용할 수 있게 model에 저장
        SessionUser user = (SessionUser) httpSession.getAttribute("user");
        if(user != null){
            model.addAttribute("userName",user.getName());
        }
        return "index";
    }

//    @ApiOperation("서버 시간")
//    public String time(){
//        return LocalDateTime.now().toString();
//    }

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
        try{
            PostSignInRes postSignInRes = userService.signIn(postSignInReq.getEmail(), postSignInReq.getPassword());
            return new BaseResponse<>(postSignInRes);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ApiOperation("로그아웃 -> 엑세스 토큰 사용")
    @PostMapping("/v1/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public BaseResponse<String> logout(@Authenticated AuthInfo authInfo){
        try {
            userService.logout(authInfo.getToken(), authInfo.getEmail());
            return new BaseResponse<>("logout success");
        } catch (BaseException e) {
//            e.printStackTrace();
            return new BaseResponse<>(e.getStatus());
        }
    }
}
