package com.example.demo.src.controller;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.dto.GetGroupInfoRes;
import com.example.demo.src.dto.GetHomeGroupInfoRes;
import com.example.demo.src.dto.PostGroupInfoReq;
import com.example.demo.src.dto.PostGroupInfoRes;
import com.example.demo.src.dto.request.GetGroupInfoSearchReq;
import com.example.demo.src.dto.response.GetGroupInfoSearchRes;
import com.example.demo.src.entity.GroupInfo;
import com.example.demo.src.service.GroupInfoService;
import com.example.demo.src.service.SessionService;
import com.example.demo.src.service.UserService;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/groupinfo")
public class GroupInfoController {

    private final GroupInfoService groupInfoService;
    private final UserService userService;

    @Autowired
    public GroupInfoController(GroupInfoService groupInfoService, UserService userService) {
        this.groupInfoService = groupInfoService;
        this.userService = userService;
    }

    @ResponseBody
    @GetMapping("/home")
    public BaseResponse<List<GetHomeGroupInfoRes>> loadHomeData (@RequestParam(value = "userIdx") Long userIdx) {
        try{
            List<GetHomeGroupInfoRes> getGroupHomeData = groupInfoService.loadHomeData(userIdx);    //출석율 부분 수정 필요
            return new BaseResponse<>(getGroupHomeData);
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    @PostMapping("")
    @ResponseBody
    public BaseResponse<PostGroupInfoRes> createGroup(@RequestBody PostGroupInfoReq request){
        System.out.println(request.toString());
        System.out.println("들어가기전!");
        PostGroupInfoRes response =  groupInfoService.create(request);
        return new BaseResponse<>(response);
    }

    @ResponseBody
    @GetMapping("/search")
    public BaseResponse<Slice<GetGroupInfoSearchRes>> searchGroup(@RequestBody GetGroupInfoSearchReq getGroupInfoSearchReq, Pageable pageable){
        System.out.println("받은값"+getGroupInfoSearchReq.getInterest1()+getGroupInfoSearchReq.getInterest2());
        System.out.println("page size : " + pageable.getPageSize());
        System.out.println(getGroupInfoSearchReq.getClientTime());
        Slice<GetGroupInfoSearchRes> result = groupInfoService.searchGroup(getGroupInfoSearchReq,pageable);
        return new BaseResponse<>(result);


    }

    @GetMapping("/search/test")
    @ResponseBody
    public JPAQuery<Integer> searchtest(@RequestBody GetGroupInfoSearchReq getGroupInfoSearchReq, Pageable pageable) {
        System.out.println("들어오ㅏ?"+getGroupInfoSearchReq.getTitle());
        System.out.println(pageable.getPageSize()+"   "+pageable.toString());
        return groupInfoService.searchtestGroup(getGroupInfoSearchReq, pageable);
    }



}
