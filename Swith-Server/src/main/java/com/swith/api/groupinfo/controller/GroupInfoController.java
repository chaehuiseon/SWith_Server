package com.swith.api.groupinfo.controller;


import com.querydsl.jpa.impl.JPAQuery;
import com.swith.global.error.ErrorCode;
import com.swith.api.common.dto.BaseResponse;
import com.swith.api.groupinfo.dto.PostGroupInfoReq;
import com.swith.api.groupinfo.dto.PostGroupInfoRes;
import com.swith.api.groupinfo.dto.GetGroupInfoSearchReq;
import com.swith.api.groupinfo.dto.PatchEndGroupReq;
import com.swith.api.groupinfo.dto.PatchGroupInfoReq;
import com.swith.api.groupinfo.dto.GetEachGroupInfoRes;
import com.swith.api.groupinfo.dto.GetGroupInfoSearchRes;
import com.swith.api.groupinfo.dto.GetHomeGroupInfoRes;
import com.swith.domain.user.service.UserService;
import com.swith.global.error.exception.BaseException;
import com.swith.domain.groupinfo.service.GroupInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/groupinfo")
@Api(tags = {"Swith GroupInfo API"})
public class GroupInfoController {

    private final GroupInfoService groupInfoService;
    private final UserService userService;

    @Autowired
    public GroupInfoController(GroupInfoService groupInfoService, UserService userService) {
        this.groupInfoService = groupInfoService;
        this.userService = userService;
    }

    @ApiOperation("홈화면 정보 불러오기 - P1")
    @GetMapping("/home")
    public BaseResponse<List<GetHomeGroupInfoRes>> loadHomeData(@RequestParam(value = "userIdx") Long userIdx) {
        try {
            List<GetHomeGroupInfoRes> getGroupHomeData = groupInfoService.loadHomeData(userIdx);    //출석율 부분 수정 필요
            return new BaseResponse<>(getGroupHomeData);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ApiOperation("그룹 생성")
    @PostMapping
    public BaseResponse<PostGroupInfoRes> createGroup(@RequestBody PostGroupInfoReq request) throws BaseException {
        System.out.println(request.toString());
        PostGroupInfoRes response = groupInfoService.create(request);
        return new BaseResponse<>(response);
    }

    //@ResponseBody
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
        Slice<GetGroupInfoSearchRes> result = groupInfoService.searchGroup(getGroupInfoSearchReq, pageable);
        System.out.println("---------");
        System.out.println(result.isLast());
        return new BaseResponse<>(result);

    }


    @ApiOperation("스터디 정보 상세 보기")
    @GetMapping("/search/{groupIdx}")
    @ResponseBody
    public BaseResponse<GetEachGroupInfoRes> selectEachGroupInfo(@PathVariable Long groupIdx) {

        System.out.println(groupIdx);
        GetEachGroupInfoRes response = groupInfoService.selectEachGroupInfo(groupIdx);
        return new BaseResponse<>(response);
    }

    @ApiOperation("스터디 정보 수정")
    @PatchMapping("/modify/{groupIdx}")
    @ResponseBody
    public BaseResponse<Long> ModifyGroupInformation(@PathVariable Long groupIdx, @RequestBody PatchGroupInfoReq patchGroupInfoReq) throws BaseException {
        System.out.println(patchGroupInfoReq.getAdminIdx());
        System.out.println(patchGroupInfoReq.getGroupEnd());
        System.out.println(patchGroupInfoReq.getInterest().toString());
        Long ReqAdminIdx = patchGroupInfoReq.getAdminIdx();
        //jwt 유효성 검사 추가해야됨.

        //상태 변경 권한이 있는지..즉, 스터디 개설자가 맞는지.
        boolean check = groupInfoService.IsAdmin(groupIdx, ReqAdminIdx);
        if (check == false) {//권한없음
            return new BaseResponse<>(ErrorCode.NO_GROUP_LEADER);
        }

        Long result = groupInfoService.ModifyGroupInformation(groupIdx, patchGroupInfoReq);

        return new BaseResponse<>(result);

    }

    @ApiOperation("스터디 종료 API")
    @ResponseBody
    @PatchMapping("/end")
    public BaseResponse<Long> EndGroup(@RequestBody PatchEndGroupReq patchEndGroupReq) throws IOException {
        Long groupIdx = patchEndGroupReq.getGroupIdx();
        Long adminIdx = patchEndGroupReq.getAdminIdx();
        System.out.println("end 받은 값 > " + groupIdx + adminIdx);
        boolean check = groupInfoService.IsAdmin(groupIdx, adminIdx);
        //jwt 유효성 검사 추가해야됨 ..

        if (check == false) {//권한없음
            return new BaseResponse<>(ErrorCode.NO_GROUP_LEADER);
        }

        Long result = groupInfoService.EndGroup(groupIdx, adminIdx);
        System.out.println("종료 >>>>" + result);
        //그룹 존재하지 않아서 실패
        if (result == -1L) return new BaseResponse<>(ErrorCode.FAIL_LOAD_GROUPINFO);
        //삭제가 실패
        if (result == -2L) return new BaseResponse<>(ErrorCode.FAIL_CHANGED_STATUS);
        return new BaseResponse<>(result);

    }


    @GetMapping("/search/test")
    @ResponseBody
    public JPAQuery<Integer> searchtest(@RequestBody GetGroupInfoSearchReq getGroupInfoSearchReq, Pageable pageable) {
        System.out.println("들어오ㅏ?" + getGroupInfoSearchReq.getTitle());
        System.out.println(pageable.getPageSize() + "   " + pageable.toString());
        return groupInfoService.searchtestGroup(getGroupInfoSearchReq, pageable);
    }


}
