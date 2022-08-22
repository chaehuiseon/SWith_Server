package com.example.demo.src.controller;


import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.dto.request.PatchApplicationStatusReq;
import com.example.demo.src.dto.request.PostApplicationReq;
import com.example.demo.src.dto.response.GetApplicationManageRes;
import com.example.demo.src.dto.response.PatchApplicationStatusRes;
import com.example.demo.src.service.ApplicationService;
import com.example.demo.src.service.GroupInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.RESPONSE_ERROR;

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

    //지원 api
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

    // 지원/목록 (즉, 유저관리 페이지) 불러오기 api
    @ApiOperation("가입신청-지원/목록 불러오기 API")
    @ResponseBody
    @GetMapping("/manage/show/{groupIdx}/{status}") //status : Application 에 있는 status
    public BaseResponse<List<GetApplicationManageRes>>
    ShowApplicationManage(@PathVariable Long groupIdx, @PathVariable Integer status){

        //그룹상태 -> 있긴 한지 + 종료된 그룹이 아닌지...

        boolean existcheck = groupInfoService.existGroupIdx(groupIdx);
        if(existcheck == false){ //존재하지 않는 스터디
            return new BaseResponse<>(BaseResponseStatus.FAIL_LOAD_GROUPINFO);
        }

        //groupInfo의 status를 check 종료된 스터디인지.
        Integer statuscheck = groupInfoService.statusOfGroupInfo(groupIdx);
        if(statuscheck == 2 ){//종료된 스터디
            return new BaseResponse<>(BaseResponseStatus.FAIL_CLOSED_GROUPINFO);
        }

        //가지고 와야지.. 정보....

        List<GetApplicationManageRes> response =  applicationService.getApplicationList(groupIdx,status);

        return new BaseResponse<>(response);



    }

    @ApiOperation("가입신청-승인/반려API")
    @ResponseBody
    @PatchMapping("/manage/resume/{groupIdx}/{status}")  //status : Application 에 있는 status
    public BaseResponse<PatchApplicationStatusRes> ApproveOrRejectStudyEnrollment(
            @PathVariable Long groupIdx, @PathVariable Integer status, @RequestBody PatchApplicationStatusReq patchApplicationStatusReq) throws BaseException {

        System.out.println(patchApplicationStatusReq.getStatusOfApplication());
        System.out.println(patchApplicationStatusReq.getApplicationIdx());
        System.out.println(patchApplicationStatusReq.getAdminIdx());
        //목록이 보여졌다는것은.. 스터디가 종료 및 삭제 되지 않았다는 검사를 앞에서 다 했다는 것으로 간주.

        //jwt 유효성 검사 추가해야됨.

        //상태 변경 권한이 있는지..즉, 스터디 개설자가 맞는지.
        Long ReqAdminIdx = patchApplicationStatusReq.getAdminIdx();
        boolean check = groupInfoService.IsAdmin(groupIdx,ReqAdminIdx);
        if(check == false){//권한없음
            return new BaseResponse<>(BaseResponseStatus.NO_GROUP_LEADER);
        }
        Integer ReqStatus = patchApplicationStatusReq.getStatusOfApplication();
        if(!(ReqStatus == 1 || ReqStatus == 2)){
            return new BaseResponse<>(BaseResponseStatus.INVALID_STATUS);
        }
        //권한 있음.
        PatchApplicationStatusRes response = applicationService.changeApplicationStatus(groupIdx, status, patchApplicationStatusReq);

        if(response == null) return new BaseResponse<>(RESPONSE_ERROR);

        return new BaseResponse<>(response);


    }






}
