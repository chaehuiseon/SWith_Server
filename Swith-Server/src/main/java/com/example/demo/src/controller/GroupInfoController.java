package com.example.demo.src.controller;

import com.example.demo.config.BaseResponse;
import com.example.demo.src.dto.PostGroupInfoReq;
import com.example.demo.src.dto.PostGroupInfoRes;
import com.example.demo.src.entity.GroupInfo;
import com.example.demo.src.service.GroupInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/groupinfo")
public class GroupInfoController {
    @Autowired
    private GroupInfoService groupInfoService;


    @PostMapping("")
    @ResponseBody
    public BaseResponse<PostGroupInfoRes> createGroup(@RequestBody PostGroupInfoReq request){
        System.out.println(request.toString());
        System.out.println("들어가기전!");
        PostGroupInfoRes response =  groupInfoService.create(request);
        return new BaseResponse<>(response);
    }


}
