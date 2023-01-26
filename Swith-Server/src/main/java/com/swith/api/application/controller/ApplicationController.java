package com.swith.api.application.controller;


import com.swith.global.error.BaseResponseStatus;
import com.swith.api.common.dto.BaseResponse;
import com.swith.api.application.dto.PatchApplicationStatusReq;
import com.swith.api.application.dto.PatchExpelUserReq;
import com.swith.api.application.dto.PostApplicationReq;
import com.swith.api.application.dto.GetApplicationManageRes;
import com.swith.api.application.dto.PatchApplicationStatusRes;
import com.swith.api.application.dto.GetApplicationRes;
import com.swith.global.error.exception.BaseException;
import com.swith.domain.application.service.ApplicationService;
import com.swith.domain.groupinfo.service.GroupInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

    @ApiOperation("가입신청 API")
    @ResponseBody
    @PostMapping("/apply/{groupIdx}")
    public ResponseEntity<Long> Apply(@PathVariable Long groupIdx,
                                      @RequestBody PostApplicationReq postApplicationReq)  {

        //중복 지원자인지 확인. -> 테스트 안한 상태.
        applicationService.AlreadyInGroup(groupIdx,postApplicationReq.getUserIdx());

        // 가입 신청 인원이 다 찾는지 확인.
        applicationService.CheckFULL(groupIdx);

        // 스터디 가입 신청자가 방장이면 가입 할 필요가 없음.
        groupInfoService.CheckIsAdmin(groupIdx, postApplicationReq.getUserIdx());

        //신청 시작.
        Long applicationIdx = applicationService.Apply(groupIdx, postApplicationReq);

        return ResponseEntity.ok(applicationIdx);

    }

    // 1차 리펙토리 ok(dto 변경)
    // 지원/목록 (즉, 유저관리 페이지) 불러오기 api
    @ApiOperation("가입신청-지원/목록 불러오기 API")
    @ResponseBody
    @GetMapping("/manage/show/{groupIdx}/{status}") //status : Application 에 있는 status
    public ResponseEntity<List<GetApplicationManageRes>>
    ShowApplicationManage(@PathVariable Long groupIdx, @PathVariable Integer status){

        //그룹상태 -> 있긴 한지 + 종료된 그룹이 아닌지...
        boolean existcheck = groupInfoService.existGroupIdx(groupIdx);
        if(existcheck == false){ //존재하지 않거나 이미 종료된 스터디
            throw new BaseException(BaseResponseStatus.FAIL_LOAD_GROUPINFO);
        }

        if(!(status == 0 || status == 1)){//승인대기(0)거나 승인(1) 상태가 아닌 목록의 요청은 잘못된 요청.
            throw new BaseException(BaseResponseStatus.BADREQUEST);
        }


        List<GetApplicationManageRes> response =  applicationService.getApplicationList(groupIdx,status);

        return ResponseEntity.ok(response);



    }

    //2차 리펙토리
    @ApiOperation("가입신청-승인/반려API")
    @ResponseBody
    @PatchMapping("/manage/resume/{groupIdx}/{status}")  //status : Application 에 있는 status
    public ResponseEntity<PatchApplicationStatusRes> ApproveOrRejectStudyEnrollment(
            @PathVariable Long groupIdx, @PathVariable Integer status, @RequestBody PatchApplicationStatusReq patchApplicationStatusReq) {

        System.out.println("승인시작 : ");
        System.out.println("application 요청 status :  "+ patchApplicationStatusReq.getStatusOfApplication());
        System.out.println("application Idx " + patchApplicationStatusReq.getApplicationIdx());
        System.out.println("admin " +patchApplicationStatusReq.getAdminIdx());
        //목록이 보여졌다는것은.. 스터디가 종료 및 삭제 되지 않았다는 검사를 앞에서 다 했다는 것으로 간주.

        //jwt 유효성 검사 추가해야됨.

        //상태 변경 권한이 있는지..즉, 스터디 개설자가 맞는지.
        groupInfoService.CheckIsAdmin(groupIdx,patchApplicationStatusReq.getAdminIdx());


        // 올바른 요청이 맞는지에 대한 검사
        Integer ReqStatus = patchApplicationStatusReq.getStatusOfApplication();
        if(!(ReqStatus == 1 || ReqStatus == 2 )){//요청이 승인(1) 또는 반려(2)가 아니면 잘못된 값을 받은 것.
            throw new BaseException(BaseResponseStatus.INVALID_STATUS);
        }


        //권한 있음. 승인 또는 반려 상태 변경.
        PatchApplicationStatusRes response = applicationService.changeApplicationStatus(groupIdx, status, patchApplicationStatusReq);

        if(response == null) throw new BaseException(BaseResponseStatus.RESPONSE_ERROR);

        return ResponseEntity.ok(response);


    }

    @ApiOperation("스터디 유저 추방 API")
    @ResponseBody
    @PatchMapping("/manage/expel/{groupIdx}/{status}")
    public ResponseEntity<Long> ExpelUserFromGroup (@PathVariable Long groupIdx, @PathVariable Integer status , @RequestBody @Valid PatchExpelUserReq patchExpelUserReq) throws BaseException {

//        System.out.println("groupIDx >> " + groupIdx);
//        System.out.println("user >> " +patchExpelUserReq.getUserIdx());
//        System.out.println("applicationIdx >> "+patchExpelUserReq.getApplicationIdx());
//        System.out.println("adminIDx >> " +patchExpelUserReq.getAdminIdx());
        //jwt 유효성 검사 추가


        //추방 권한 확인
        groupInfoService.CheckIsAdmin(groupIdx,patchExpelUserReq.getAdminIdx());


        if(!(status.equals(1))){ //가입 승인이 된 유저만 대상으로 추방을 할 수 있음.
            throw new BaseException(BaseResponseStatus.INVALID_STATUS);
        }


        //추방하기
        Long result = applicationService.ExpelUserFromGroup(status, groupIdx, patchExpelUserReq);
        //if(result == -3L) throw new BaseException(BaseResponseStatus.DO_NOT_EXECUTE_CHANGE);
        return ResponseEntity.ok(result);

    }

    @ApiOperation("프로필 탭 내 지원서 목록 조회 - K1")
    @GetMapping("/user")
    public BaseResponse<List<GetApplicationRes>> getUserApplication(@RequestParam Long userIdx) {
        try {
            List<GetApplicationRes> getApplicationResList = applicationService.getUserApplication(userIdx);
            return new BaseResponse<>(getApplicationResList);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

//    @ApiOperation("유저 지원 취소 - K2")
//    @GetMapping
//    public BaseResponse<List<getApplicationRes>> getUserApplication(@RequestParam Long userIdx) {
//        try {
//            List<getApplicationRes> getApplicationResList = applicationService.getUserApplication(userIdx);
//            return new BaseResponse<>(getApplicationResList);
//        } catch (BaseException e) {
//            return new BaseResponse<>(e.getStatus());
//        }
//    }


}
