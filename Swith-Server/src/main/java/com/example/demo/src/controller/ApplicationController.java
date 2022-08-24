package com.example.demo.src.controller;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.dto.response.getApplicationRes;
import com.example.demo.src.service.ApplicationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/groupinfo/Application")
@Api(tags = {"Swith Application API"})
public class ApplicationController {
    private final ApplicationService applicationService;

    @Autowired
    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @ApiOperation("프로필 탭 내 지원서 목록 조회 - K1")
    @GetMapping
    public BaseResponse<List<getApplicationRes>> getUserApplication(@RequestParam Long userIdx) {
        try {
            List<getApplicationRes> getApplicationResList = applicationService.getUserApplication(userIdx);
            return new BaseResponse<>(getApplicationResList);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }


}
