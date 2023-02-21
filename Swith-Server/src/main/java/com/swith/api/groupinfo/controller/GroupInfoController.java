package com.swith.api.groupinfo.controller;


import com.querydsl.jpa.impl.JPAQuery;
import com.swith.api.groupinfo.dto.*;
import com.swith.api.user.service.UserApiService;
import com.swith.domain.groupinfo.service.GroupInfoService;
import com.swith.global.error.ErrorCode;
import com.swith.api.common.dto.BaseResponse;
import com.swith.domain.user.service.UserService;
import com.swith.global.error.exception.BaseException;
import com.swith.api.groupinfo.service.GroupInfoApiService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/groupinfo")
@Api(tags = {"Swith GroupInfo API"})
public class GroupInfoController {

    private final GroupInfoApiService groupInfoApiService;
    private final GroupInfoService groupInfoService;
    private final UserApiService userApiService;

    @Autowired
    public GroupInfoController(GroupInfoApiService groupInfoApiService, GroupInfoService groupInfoService, UserApiService userApiService) {
        this.groupInfoApiService = groupInfoApiService;
        this.groupInfoService = groupInfoService;
        this.userApiService = userApiService;
    }


    @ApiOperation("홈화면 정보 불러오기 - P1")
    @GetMapping("/home")
    public BaseResponse<List<GetHomeGroupInfoRes>> loadHomeData(@RequestParam(value = "userIdx") Long userIdx) {
            List<GetHomeGroupInfoRes> getGroupHomeData = groupInfoApiService.loadHomeData(userIdx);    //출석율 부분 수정 필요
            return new BaseResponse<>(getGroupHomeData);
    }


    @ApiOperation("그룹 생성") // 리팩토리 2차.
    @PostMapping
    public ResponseEntity<PostGroupInfoRes> createGroup(@RequestBody PostGroupInfoReq request) {

        PostGroupInfoRes response = groupInfoApiService.create(request);
        //return new ErrorCodee<>(response);
        return ResponseEntity.ok(response);
    }

    //@ResponseBody -> 나중에 할 예정.. VO 적용하고 리팩토리 범위가 큼.
    //@GetMapping("/search")
    @ApiOperation("스터디 검색")
    @RequestMapping(path = "/search", method = RequestMethod.GET)
    @ResponseBody
    //public BaseResponse<Slice<GetGroupInfoSearchRes>> searchGroup(@RequestParam GetGroupInfoSearchReq getGroupInfoSearchReq, Pageable pageable){
    public BaseResponse<Slice<GetGroupInfoSearchRes>> searchGroup(
            @RequestParam(name = "title", required = false) String title,
            @RequestParam(name = "regionIdx", required = false) String regionIdx,
            @RequestParam(name = "interest1", required = false) Integer interest1, @RequestParam(name = "interest2", required = false) Integer interest2,
            @RequestParam(name = "groupIdx", required = false) Long groupIdx, @RequestParam(name = "sortCond") Integer sortCond,
            @RequestParam(name = "ClientTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime ClientTime,
            Pageable pageable) {
        //System.out.println("받은값"+getGroupInfoSearchReq.getInterest1()+getGroupInfoSearchReq.getInterest2());
        //System.out.println("page size : " + pageable.getPageSize());
        //System.out.println(getGroupInfoSearchReq.getClientTime());
        System.out.println("title : " + title + "regionIdx : " + regionIdx + "groupIdx : " + groupIdx +
                "sortCond : " + sortCond + "ClientTime : " + ClientTime);
        GetGroupInfoSearchReq getGroupInfoSearchReq = new GetGroupInfoSearchReq(
                title, regionIdx, interest1, interest2, groupIdx, sortCond, ClientTime
        );
        System.out.println(getGroupInfoSearchReq.getSortCond());
        System.out.println(getGroupInfoSearchReq.getClientTime());
        Slice<GetGroupInfoSearchRes> result = groupInfoApiService.searchGroup(getGroupInfoSearchReq, pageable);
        System.out.println("---------");
        System.out.println(result.isLast());
        return new BaseResponse<>(result);

    }


    @ApiOperation("스터디 정보 상세 보기")
    @GetMapping("/search/{groupIdx}")
    @ResponseBody
    public ResponseEntity<GetEachGroupInfoRes> selectEachGroupInfo(@PathVariable Long groupIdx) {
        GetEachGroupInfoRes response = groupInfoApiService.selectEachGroupInfo(groupIdx);
        //return new ErrorCodee<>(response);
        return ResponseEntity.ok(response);
    }

    //리팩토리 1차완 -> 더티체크 되는지를 테스트 아직 안함.
    @ApiOperation("스터디 정보 수정")
    @PatchMapping("/modify/{groupIdx}")
    @ResponseBody
    public ResponseEntity<Long> ModifyGroupInformation(@PathVariable Long groupIdx, @RequestBody PatchGroupInfoReq patchGroupInfoReq)  {

        //jwt 유효성 검사 추가해야됨.


        //검사 : 상태 변경 권한이 있는지..즉, 스터디 개설자가 맞는지.
        groupInfoService.CheckIsAdminForAdminToManage(groupIdx, patchGroupInfoReq.getAdminIdx());

        Long result = groupInfoApiService.ModifyGroupInformation(groupIdx, patchGroupInfoReq);

        return ResponseEntity.ok(result);

    }

    @ApiOperation("스터디 종료 API")
    @ResponseBody
    @PatchMapping("/end")
    public ResponseEntity<Long> EndGroup(@RequestBody PatchEndGroupReq patchEndGroupReq) throws IOException {
        Long groupIdx = patchEndGroupReq.getGroupIdx();
        Long adminIdx = patchEndGroupReq.getAdminIdx();
        System.out.println("end 받은 값 > " + groupIdx + adminIdx);

        //종료 권한이 있는지 체크
        groupInfoService.CheckIsAdminForAdminToManage(groupIdx, adminIdx);
        //jwt 유효성 검사 추가해야됨 ..

        //종료 상태로 변경.
        changeEndStatus result = groupInfoApiService.EndGroup(groupIdx);

//        //그룹 존재하지 않아서 실패
//        if (result == -1L) return new ErrorCodee<>(ErrorCode.FAIL_LOAD_GROUPINFO);
//        //삭제가 실패
//        if (result == -2L) return new ErrorCodee<>(ErrorCode.FAIL_CHANGED_STATUS);

        // 유저에게 알림 보내기.
        Long complete = groupInfoApiService.pushEndNotification(result);


        return ResponseEntity.ok(complete);

    }


    @GetMapping("/search/test")
    @ResponseBody
    public JPAQuery<Integer> searchtest(@RequestBody GetGroupInfoSearchReq getGroupInfoSearchReq, Pageable pageable) {
        System.out.println("들어오ㅏ?" + getGroupInfoSearchReq.getTitle());
        System.out.println(pageable.getPageSize() + "   " + pageable.toString());
        return groupInfoApiService.searchtestGroup(getGroupInfoSearchReq, pageable);
    }


}
