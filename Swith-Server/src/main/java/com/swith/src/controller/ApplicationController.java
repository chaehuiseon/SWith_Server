//package com.swith.src.controller;
//
//import com.swith.global.error.exception.BaseException;
//import com.swith.api.common.dto.BaseResponse;
//import com.swith.api.common.constant.BaseResponseStatus;
//import com.swith.api.application.dto.PatchApplicationStatusReq;
//import com.swith.api.dto.request.PatchExpelUserReq;
//import com.swith.api.application.dto.PostApplicationReq;
//import com.swith.api.application.dto.GetApplicationManageRes;
//import com.swith.api.application.dto.PatchApplicationStatusRes;
//import com.swith.api.application.dto.getApplicationRes;
//import com.swith.domain.application.service.ApplicationService;
//import com.swith.domain.groupinfo.service.GroupInfoService;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import javax.validation.Valid;
//import java.util.List;
//
//@RestController
//@RequestMapping("/application")
//@Api(tags = {"Swith Application API"})
//public class ApplicationController {
//
//    private final ApplicationService applicationService;
//    private final GroupInfoService groupInfoService;
//
//    @Autowired
//    public ApplicationController(ApplicationService applicationService, GroupInfoService groupInfoService) {
//        this.applicationService = applicationService;
//        this.groupInfoService = groupInfoService;
//    }
//
//    //지원 api
//    @ResponseBody
//    @PostMapping("/apply/{groupIdx}/{applicationMethod}")
//    public BaseResponse<Long> Apply(@PathVariable Long groupIdx, @PathVariable Integer applicationMethod,
//                                    @RequestBody PostApplicationReq postApplicationReq) throws BaseException {
//
//        Long admin = applicationService.findAdminIdx(groupIdx);
//        if(admin == postApplicationReq.getUserIdx()){
//            return new BaseResponse<>(BaseResponseStatus.INVAILD_ADMIN_APPLICATION);
//        }
//
//
//
//        Integer limit = applicationService.getMemberLimit(groupIdx);
//        Long NumOfApplicants = applicationService.findNumOfApplicants(groupIdx);
//        System.out.println("지원자/limit =  " + NumOfApplicants + "/ " + limit);
//        if(limit.equals(NumOfApplicants.intValue())){ //신청인원이 다 찻다.
//            return new BaseResponse<>(BaseResponseStatus.FULL_NUM_OF_Applicants);
//        }
//        Long applicationIdx = applicationService.Apply(groupIdx, applicationMethod, postApplicationReq);
//        if(applicationIdx == null){
//            return new BaseResponse<>(BaseResponseStatus.FAIL_SAVED_APPLICATION);
//        }
//
//        return new BaseResponse<>(applicationIdx);
//
//    }
//
//    // 지원/목록 (즉, 유저관리 페이지) 불러오기 api
//    @ApiOperation("가입신청-지원/목록 불러오기 API")
//    @ResponseBody
//    @GetMapping("/manage/show/{groupIdx}/{status}") //status : Application 에 있는 status
//    public BaseResponse<List<GetApplicationManageRes>>
//    ShowApplicationManage(@PathVariable Long groupIdx, @PathVariable Integer status){
//
//        //그룹상태 -> 있긴 한지 + 종료된 그룹이 아닌지...
//
//        boolean existcheck = groupInfoService.existGroupIdx(groupIdx);
//        if(existcheck == false){ //존재하지 않는 스터디
//            System.out.println("종료된 스터디거나 오류임~~~~~~");
//            return new BaseResponse<>(BaseResponseStatus.FAIL_LOAD_GROUPINFO);
//        }
//
//        //groupInfo의 status를 check 종료된 스터디인지.
//        //exist에서 이 검사를 다 하니깐...이부분 없애도 될거같음 -> refactory대상.
//        Integer statuscheck = groupInfoService.statusOfGroupInfo(groupIdx);
//        if(statuscheck == 2 ){//종료된 스터디
//            return new BaseResponse<>(BaseResponseStatus.FAIL_CLOSED_GROUPINFO);
//        }
//
//        //가지고 와야지.. 정보....
//
//        List<GetApplicationManageRes> response =  applicationService.getApplicationList(groupIdx,status);
//
//        return new BaseResponse<>(response);
//
//
//
//    }
//
//    @ApiOperation("가입신청-승인/반려API")
//    @ResponseBody
//    @PatchMapping("/manage/resume/{groupIdx}/{status}")  //status : Application 에 있는 status
//    public BaseResponse<PatchApplicationStatusRes> ApproveOrRejectStudyEnrollment(
//            @PathVariable Long groupIdx, @PathVariable Integer status, @RequestBody PatchApplicationStatusReq patchApplicationStatusReq) throws BaseException {
//
//        System.out.println("승인시작 : ");
//        System.out.println("application 요청 status :  "+ patchApplicationStatusReq.getStatusOfApplication());
//        System.out.println("application Idx " + patchApplicationStatusReq.getApplicationIdx());
//        System.out.println("admin " +patchApplicationStatusReq.getAdminIdx());
//        //목록이 보여졌다는것은.. 스터디가 종료 및 삭제 되지 않았다는 검사를 앞에서 다 했다는 것으로 간주.
//
//        //jwt 유효성 검사 추가해야됨.
//
//        //상태 변경 권한이 있는지..즉, 스터디 개설자가 맞는지.
//        Long ReqAdminIdx = patchApplicationStatusReq.getAdminIdx();
//        boolean check = groupInfoService.IsAdmin(groupIdx,ReqAdminIdx);
//        if(check == false){//권한없음
//            System.out.println("권한이 없습니다.");
//            return new BaseResponse<>(BaseResponseStatus.NO_GROUP_LEADER);
//        }
//        Integer ReqStatus = patchApplicationStatusReq.getStatusOfApplication();
//        if(!(ReqStatus == 1 || ReqStatus == 2 )){//요청이 승인(1) 또는 반려(2)가 아니면 잘못된 값을 받은 것.
//            return new BaseResponse<>(BaseResponseStatus.INVALID_STATUS);
//        }
//        //권한 있음.
//        PatchApplicationStatusRes response = applicationService.changeApplicationStatus(groupIdx, status, patchApplicationStatusReq);
//
//        if(response == null) return new BaseResponse<>(BaseResponseStatus.RESPONSE_ERROR);
//
//        return new BaseResponse<>(response);
//
//
//    }
//
//    @ApiOperation("스터디 유저 추방 API")
//    @ResponseBody
//    @PatchMapping("/manage/expel/{groupIdx}/{status}")
//    public BaseResponse<Long> ExpelUserFromGroup (@PathVariable Long groupIdx, @PathVariable Integer status , @RequestBody @Valid PatchExpelUserReq patchExpelUserReq) throws BaseException {
//
//        System.out.println("groupIDx >> " + groupIdx);
//        System.out.println("user >> " +patchExpelUserReq.getUserIdx());
//        System.out.println("applicationIdx >> "+patchExpelUserReq.getApplicationIdx());
//        System.out.println("adminIDx >> " +patchExpelUserReq.getAdminIdx());
//        //jwt 유효성 검사 추가
//
//        //그룹 존재
//        boolean existgroup = groupInfoService.existGroupIdx(groupIdx);
//        if(!existgroup) return new BaseResponse<>(BaseResponseStatus.FAIL_LOAD_GROUPINFO);
//
//
//        //추방 권한 확인
//        Long ReqAdminIdx = patchExpelUserReq.getAdminIdx();
//        boolean check = groupInfoService.IsAdmin(groupIdx,ReqAdminIdx);
//        if(check == false){//권한없음
//            return new BaseResponse<>(BaseResponseStatus.NO_GROUP_LEADER);
//        }
//
//        if(!(status == 1)){ //가입 승인이 된 유저만 대상으로 추방을 할 수 있음.
//            return new BaseResponse<>(BaseResponseStatus.INVALID_STATUS);
//        }
//
//        //추방하기
//        Long result = applicationService.ExpelUserFromGroup(groupIdx, patchExpelUserReq);
//        if(result == -3L) return new BaseResponse<>(BaseResponseStatus.DO_NOT_EXECUTE_CHANGE);
//        return new BaseResponse<>(result);
//
//    }
//
//    @ApiOperation("프로필 탭 내 지원서 목록 조회 - K1")
//    @GetMapping("/user")
//    public BaseResponse<List<getApplicationRes>> getUserApplication(@RequestParam Long userIdx) {
//        try {
//            List<getApplicationRes> getApplicationResList = applicationService.getUserApplication(userIdx);
//            return new BaseResponse<>(getApplicationResList);
//        } catch (BaseException e) {
//            return new BaseResponse<>(e.getStatus());
//        }
//    }
//
////    @ApiOperation("유저 지원 취소 - K2")
////    @GetMapping
////    public BaseResponse<List<getApplicationRes>> getUserApplication(@RequestParam Long userIdx) {
////        try {
////            List<getApplicationRes> getApplicationResList = applicationService.getUserApplication(userIdx);
////            return new BaseResponse<>(getApplicationResList);
////        } catch (BaseException e) {
////            return new BaseResponse<>(e.getStatus());
////        }
////    }
//
//
//}
