package com.example.demo.src.controller;

import com.example.demo.config.BaseResponse;
import com.example.demo.src.dto.GetGroupInfoRes;
import com.example.demo.src.dto.GetHomeGroupInfoRes;
import com.example.demo.src.service.GroupInfoService;
import com.example.demo.src.service.SessionService;
import com.example.demo.src.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/groupinfo")
public class GroupInfoController {

    private final GroupInfoService groupInfoService;
    private final UserService userService;

    @Autowired
    public GroupInfoController(GroupInfoService groupInfoService, UserService userService, SessionService sessionService, UserService userService1) {
        this.groupInfoService = groupInfoService;
        this.userService = userService1;
    }

    @GetMapping("/test")
    public BaseResponse<String> test(){

        String string = "테스트 컨트롤러입니다.";
        return new BaseResponse<>(string);
    }

    @ResponseBody
    @GetMapping("/home")
    public BaseResponse<List<GetHomeGroupInfoRes>> loadHomeData (@RequestParam(value = "userIdx") Long userIdx){
        List<GetHomeGroupInfoRes> getGroupHomeData = groupInfoService.loadHomeData(userIdx);    //출석율 부분 수정 필요
        return new BaseResponse<>(getGroupHomeData);
    }



}
