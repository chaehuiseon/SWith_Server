package com.example.demo.src.controller;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.dto.GetGroupInfoRes;
import com.example.demo.src.dto.GetHomeGroupInfoRes;
import com.example.demo.src.dto.PostGroupInfoReq;
import com.example.demo.src.dto.PostGroupInfoRes;
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

    @ResponseBody
    @GetMapping("/home")
    public BaseResponse<List<GetHomeGroupInfoRes>> loadHomeData (@RequestParam(value = "userIdx") Long userIdx) throws BaseException {
        List<GetHomeGroupInfoRes> getGroupHomeData = groupInfoService.loadHomeData(userIdx);    //출석율 부분 수정 필요
        return new BaseResponse<>(getGroupHomeData);
    }

    @PostMapping("")
    @ResponseBody
    public BaseResponse<PostGroupInfoRes> createGroup(@RequestBody PostGroupInfoReq request){
        System.out.println(request.toString());
        System.out.println("들어가기전!");
        PostGroupInfoRes response =  groupInfoService.create(request);
        return new BaseResponse<>(response);
    }



}
