package com.example.demo.src.controller;

import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.dto.GetGroupInfoRes;
import com.example.demo.src.dto.PostSessionReq;
import com.example.demo.src.service.SessionService;
import com.example.demo.src.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/groupinfo/session")
public class SessionController {
    private final SessionService sessionService;
    private final UserService userService;

    @Autowired
    public SessionController(SessionService sessionService, UserService userService) {
        this.sessionService = sessionService;
        this.userService = userService;
    }

    @PostMapping            //@Valid 추가 <-회차 만들 때 디폴트로 출석정보를 추가하도록 할지 고민 필요
    public BaseResponse<Long> createSession(@RequestBody PostSessionReq postSessionReq){
        if(!userService.isAdminOfGroup(postSessionReq.getUserIdx(), postSessionReq.getGroupIdx())){
            return new BaseResponse<>(BaseResponseStatus.POST_SESSION_NOT_ADMIN);
        }
        //회차 시작시간, 끝시간을 기준으로 회차의 적절한 sessionNum 을 찾는다.
        Integer sessionNum = sessionService.findAppropriateSessionNum(postSessionReq);

        //찾은 sessionNum 이상의 회차들의 sessionNum 에 1을 더한다.
        sessionService.adjustSessionNum(sessionNum, postSessionReq.getGroupIdx());

        //찾은 sessionNum 에 해당하는 회차 레코드를 생성한다.
        Long sessionIdx = sessionService.createSession(postSessionReq, sessionNum);
        return new BaseResponse<>(sessionIdx);
    }

    @ResponseBody
    @GetMapping
    public BaseResponse<GetGroupInfoRes> loadGroupData (@RequestParam(value = "userIdx") Long userIdx
            , @RequestParam(value = "groupIdx") Long groupIdx ){
        boolean isAdmin = userService.isAdminOfGroup(userIdx, groupIdx);
        GetGroupInfoRes getGroupInfoRes = sessionService.loadGroupInfoAndSession(groupIdx, isAdmin);

        return new BaseResponse<>(getGroupInfoRes);
    }
}
