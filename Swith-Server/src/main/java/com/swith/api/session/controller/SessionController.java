package com.swith.api.session.controller;

import com.swith.global.error.exception.BaseException;
import com.swith.api.common.dto.BaseResponse;
import com.swith.global.error.ErrorCode;
import com.swith.api.session.dto.PatchSessionReq;
import com.swith.api.session.dto.GetGroupInfoRes;
import com.swith.api.session.dto.PostSessionReq;
import com.swith.api.session.dto.GetSessionTabRes;
import com.swith.domain.user.service.UserService;
import com.swith.domain.session.service.SessionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/groupinfo/session")
@Api(tags = {"Swith Session API"})
public class SessionController {
    private final SessionService sessionService;
    private final UserService userService;

    @Autowired
    public SessionController(SessionService sessionService, UserService userService) {
        this.sessionService = sessionService;
        this.userService = userService;
    }

    @ApiOperation("스터디탭-그룹 정보 불러오기 - P2")
    @GetMapping
    public BaseResponse<GetGroupInfoRes> loadGroupData(@RequestParam(value = "userIdx") Long userIdx,
                                                       @RequestParam(value = "groupIdx") Long groupIdx) {
        try {
            boolean isAdmin = userService.isAdminOfGroup(userIdx, groupIdx);
            GetGroupInfoRes getGroupInfoRes = sessionService.loadGroupInfoAndSession(groupIdx, isAdmin);
            return new BaseResponse<>(getGroupInfoRes);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }

    }


    @ApiOperation("회차탭 - 개요, 출석, 메모 정보 불러오기 - P10")
    @GetMapping("/info")
    public BaseResponse<GetSessionTabRes> getSessionTabInfo(@RequestParam(value = "userIdx") Long userIdx,
                                                         @RequestParam(value = "sessionIdx") Long sessionIdx) {
        try {
            GetSessionTabRes getSessionTabRes = sessionService.getSessionInfo(userIdx, sessionIdx);
            return new BaseResponse<>(getSessionTabRes);
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ApiOperation("회차 생성 - P3")
    @PostMapping            //@Valid 추가 <-회차 만들 때 디폴트로 출석정보를 추가하도록 할지 고민 필요
    public BaseResponse<Long> createSession(@RequestBody PostSessionReq postSessionReq) {
        try {
            //요청한 유저가 관리자인지 검증
            if (!userService.isAdminOfGroup(postSessionReq.getUserIdx(), postSessionReq.getGroupIdx()))
                return new BaseResponse<>(ErrorCode.POST_SESSION_NOT_ADMIN);
            //요청된 회차의 시간대가 겹치지 않는 지를 검증
            if(sessionService.existsOverlappedSession(postSessionReq.getGroupIdx(),
                    postSessionReq.getSessionStart(),postSessionReq.getSessionEnd()))
                throw new BaseException(ErrorCode.TIME_OVERLAPPED);

            //회차 시작시간, 끝시간을 기준으로 회차의 적절한 sessionNum 을 찾는다.
            Integer sessionNum = sessionService.findAppropriateSessionNum(postSessionReq.getGroupIdx(), postSessionReq.getSessionStart());

            //찾은 sessionNum 이상의 회차들의 sessionNum 에 1을 더한다.
            sessionService.adjustSessionNum(sessionNum, postSessionReq.getGroupIdx());

            //찾은 sessionNum 에 해당하는 회차 레코드를 생성한다.
            Long sessionIdx = sessionService.createSession(postSessionReq, sessionNum);

            //그룹내에 가입 한 모든 유저를 불러오고 해당 회차에 해당하는 출석정보를 전부 생성한다.
            //multirow insert 써보기
            return new BaseResponse<>(sessionIdx);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ApiOperation("관리자탭 - 회차정보 수정 - P13")
    @PatchMapping ("/admin")
    public BaseResponse<Long> modifySession(@RequestBody PatchSessionReq patchSessionReq) {
        try {
            Long sessionIdx = sessionService.modifySession(patchSessionReq);
            return new BaseResponse<>(sessionIdx);
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ApiOperation("관리자탭 - 회차 삭제 - P14")
    @PatchMapping ("/admin/{sessionIdx}/status")
    public BaseResponse<Long> modifySession(@PathVariable Long sessionIdx) {
        try {
            Long session = sessionService.deleteSession(sessionIdx);
            return new BaseResponse<>(session);
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }
}
