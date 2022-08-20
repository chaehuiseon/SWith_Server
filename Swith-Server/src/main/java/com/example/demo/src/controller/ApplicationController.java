package com.example.demo.src.controller;


import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.dto.request.PostApplicationReq;
import com.example.demo.src.dto.response.GetApplicationManageRes;
import com.example.demo.src.entity.Application;
import com.example.demo.src.service.ApplicationService;
import com.example.demo.src.service.GroupInfoService;
import com.fasterxml.jackson.databind.ser.Serializers;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/application")
@Api(tags = {"Swith Application API"})
public class ApplicationController {

    private final ApplicationService applicationService;
    private final GroupInfoService groupInfoService;

    @Autowired
    public ApplicationController(ApplicationService applicationService, GroupInfoService groupInfoService) {
        this.applicationService = applicationService;
        this.groupInfoService = groupInfoService;
    }

    @ResponseBody
    @PostMapping("/apply/{groupIdx}/{applicationMethod}")
    public BaseResponse<Long> Apply(@PathVariable Long groupIdx, @PathVariable Integer applicationMethod,
                                    @RequestBody PostApplicationReq postApplicationReq){


        Integer limit = applicationService.getMemberLimit(groupIdx);
        Long NumOfApplicants = applicationService.findNumOfApplicants(groupIdx);
        System.out.println("지원자/limit =  " + NumOfApplicants + "/ " + limit);
        if(limit.equals(NumOfApplicants.intValue())){ //신청인원이 다 찻다.
            return new BaseResponse<>(BaseResponseStatus.FULL_NUM_OF_Applicants);
        }
        Long applicationIdx = applicationService.Apply(groupIdx, applicationMethod, postApplicationReq);
        if(applicationIdx == null){
            return new BaseResponse<>(BaseResponseStatus.FAIL_SAVED_APPLICATION);
        }

        return new BaseResponse<>(applicationIdx);

    }

//    @ResponseBody
//    @GetMapping("/manage/{groupIdx}/{status}") //status : Application 에 있는 status
//    public BaseResponse<List<GetApplicationManageRes>>
//    ShowApplicationManage(@PathVariable Long groupIdx, @PathVariable Integer status){
//
//        //그룹상태 -> 있긴 한지 + 종료된 그룹이 아닌지...
//
//        boolean existcheck = groupInfoService.existGroupIdx(groupIdx);
//        if(existcheck == false){ //존재하지 않는 스터디
//
//
//
//        }
//
//        Integer statuscheck = groupInfoService.statusOfGroupInfo(groupIdx);
//
//        if(statuscheck == 2 ){//종료된 스터디
//
//
//        }
//
//
//        //가지고 와야지.. 정보....
//
//
//
//
//
//    }



}
