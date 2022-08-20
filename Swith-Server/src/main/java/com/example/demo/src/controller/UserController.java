package com.example.demo.src.controller;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.auth.SessionUser;
import com.example.demo.security.AuthInfo;
import com.example.demo.security.Authenticated;
import com.example.demo.src.dto.request.GetUserInfoReq;
import com.example.demo.src.dto.request.PostSignInReq;
import com.example.demo.src.dto.request.PostSignUpReq;
import com.example.demo.src.dto.response.GetUserInfoRes;
import com.example.demo.src.dto.response.PostSignInRes;
import com.example.demo.src.dto.response.PostSignUpRes;
import com.example.demo.src.entity.User;
import com.example.demo.src.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.ui.Model;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static com.example.demo.config.BaseResponseStatus.ERROR_FIND_EMAIL;
import static com.example.demo.config.BaseResponseStatus.NOT_LOGIN;

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
                throw new BaseException(NOT_LOGIN);
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
    public BaseResponse<GetUserInfoRes> userInfo(@Valid @RequestBody GetUserInfoReq getUserInfoReq) {
        try{
            GetUserInfoRes getUserInfoRes = userService.userInfo(getUserInfoReq);
            return new BaseResponse<>(getUserInfoRes);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ApiOperation("회원 가입 폼 등록")
    @PostMapping("/register")
    public BaseResponse<PostSignUpRes> register(@Valid @RequestBody PostSignUpReq postSignUpReq) {
        try{
            PostSignUpRes postSignUpRes = userService.register(postSignUpReq);
            return new BaseResponse<>(postSignUpRes);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

//    @ApiOperation("로그인")
//    @PostMapping("/v1/signIn")
//    public BaseResponse<PostSignInRes> signIn(@Valid @RequestBody PostSignInReq postSignInReq){
//        try{
//            PostSignInRes postSignInRes = userService.signIn(postSignInReq.getEmail(), postSignInReq.getPassword());
//            return new BaseResponse<>(postSignInRes);
//        } catch (BaseException e) {
//            return new BaseResponse<>(e.getStatus());
//        }
//    }
//
//    @ApiOperation("로그아웃 -> 엑세스 토큰 사용")
//    @PostMapping("/v1/logout")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    public BaseResponse<String> logout(@Authenticated AuthInfo authInfo){
//        try {
//            userService.logout(authInfo.getToken(), authInfo.getEmail());
//            return new BaseResponse<>("logout success");
//        } catch (BaseException e) {
////            e.printStackTrace();
//            return new BaseResponse<>(e.getStatus());
//        }
//    }
}
