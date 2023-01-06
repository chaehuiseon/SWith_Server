package com.swith.src.controller;

import com.swith.config.BaseException;
import com.swith.config.BaseResponse;
import com.swith.config.auth.SessionUser;
import com.swith.security.AuthInfo;
import com.swith.security.Authenticated;
import com.swith.src.dto.request.PostSignUpAndInReq;
import com.swith.src.dto.request.PostUserInfoReq;
import com.swith.src.dto.request.PostSignUpReq;
import com.swith.src.dto.response.PostUserInfoRes;
import com.swith.src.dto.response.PostSignUpRes;
import com.swith.src.service.UserService;
import com.swith.config.BaseResponseStatus;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.ui.Model;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Api(tags = {"Swith User API"})
public class UserController {
    private final UserService userService;
    private final HttpSession httpSession;

    @GetMapping("/")
    @ApiOperation("서버 시간")
    public String time(){
        return LocalDateTime.now().toString();
    }

    @ApiOperation("로그인 성공 시 세션에 SessionUser 저장")
    @GetMapping("/success")
    @ResponseBody
    public Map login(Model model){
        model.addAttribute("userName", "test");
        // userName을 사용할 수 있게 model에 저장
        SessionUser user = (SessionUser) httpSession.getAttribute("user");
        if(user != null){
            model.addAttribute("userName",user.getName());
        }

        Map result = new HashMap<String, Object>();
        result.put("nickname", user.getName());
        result.put("email", user.getEmail());
        result.put("picture", user.getPicture());
        return result;
    }

    @ApiOperation("소셜 로그인 정보")
    @GetMapping("/oauthInfo")
    @ResponseBody
    public BaseResponse<Map> oauthLoginInfo(Authentication authentication, @AuthenticationPrincipal OAuth2User oAuth2UserPrincipal){
        try{
            // 로그인 안한 경우
            if(authentication == null){
                throw new BaseException(BaseResponseStatus.NOT_LOGIN);
            }
            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
            Map<String, Object> attributes = oAuth2User.getAttributes();
            return new BaseResponse<>(attributes);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ApiOperation("회원 DB 정보 조회")
    @PostMapping("/userInfo")
    public BaseResponse<PostUserInfoRes> userInfo(@Valid @RequestBody PostUserInfoReq postUserInfoReq) {
        try{
            PostUserInfoRes postUserInfoRes = userService.userInfo(postUserInfoReq);
            return new BaseResponse<>(postUserInfoRes);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ApiOperation("초기 회원 정보 등록")
    @PostMapping("/register")
    public BaseResponse<PostSignUpRes> register(@Valid @RequestBody PostSignUpReq postSignUpReq) {
        try{
            PostSignUpRes postSignUpRes = userService.register(postSignUpReq);
            return new BaseResponse<>(postSignUpRes);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ApiOperation("회원가입 및 로그인")
    @PostMapping("/signUpAndIn")
    public BaseResponse<PostUserInfoRes> signUpAndIn(@Valid @RequestBody PostSignUpAndInReq postSignUpAndInReq){
        try{
            PostUserInfoRes postUserInfoRes = userService.signUpAndIn(postSignUpAndInReq);
            return new BaseResponse<>(postUserInfoRes);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

//    @ApiOperation("로그아웃 -> 액세스토큰 사용")
//    @PostMapping("/v1/logout")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    public void logout(@Authenticated AuthInfo authInfo) {
//        userService.logout(authInfo.getToken(), authInfo.getEmail());
//    }

    @ApiOperation("로그아웃 -> 엑세스 토큰 사용")
    @PostMapping("/logout")
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