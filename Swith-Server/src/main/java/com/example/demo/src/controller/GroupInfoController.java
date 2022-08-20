package com.example.demo.src.controller;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.dto.response.GetEachGroupInfoRes;
import com.example.demo.src.dto.response.GetHomeGroupInfoRes;
import com.example.demo.src.dto.PostGroupInfoReq;
import com.example.demo.src.dto.PostGroupInfoRes;
import com.example.demo.src.dto.request.GetGroupInfoSearchReq;
import com.example.demo.src.dto.response.GetGroupInfoSearchRes;
import com.example.demo.src.entity.GroupInfo;
import com.example.demo.src.service.GroupInfoService;
import com.example.demo.src.service.SessionService;
import com.example.demo.src.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

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
    public BaseResponse<List<GetHomeGroupInfoRes>> loadHomeData (@RequestParam(value = "userIdx") Long userIdx) {
        try {
            List<GetHomeGroupInfoRes> getGroupHomeData = groupInfoService.loadHomeData(userIdx);    //출석율 부분 수정 필요
            return new BaseResponse<>(getGroupHomeData);
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ApiOperation("그룹 생성")
    @PostMapping
    public BaseResponse<PostGroupInfoRes> createGroup(@RequestBody PostGroupInfoReq request){
        System.out.println(request.toString());
        System.out.println("들어가기전!");
        PostGroupInfoRes response =  groupInfoService.create(request);
        return new BaseResponse<>(response);
    }

    //@ResponseBody
    //@GetMapping("/search")
    @RequestMapping(path = "/search", method = RequestMethod.GET)
    @ResponseBody
    //public BaseResponse<Slice<GetGroupInfoSearchRes>> searchGroup(@RequestParam GetGroupInfoSearchReq getGroupInfoSearchReq, Pageable pageable){
    public BaseResponse<Slice<GetGroupInfoSearchRes>> searchGroup(@RequestParam(name = "title",required = false) String title, @RequestParam(name = "regionIdx",required = false) String regionIdx,
                                                                  @RequestParam(name = "interest1",required = false)Integer interest1, @RequestParam(name = "interest2",required = false)Integer interest2,
                                                                  @RequestParam(name = "groupIdx",required = false)Long groupIdx, @RequestParam(name = "sortCond") Integer sortCond,
                                                                  @RequestParam(name = "ClientTime",required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime ClientTime,
                                                                    Pageable pageable){
        //System.out.println("받은값"+getGroupInfoSearchReq.getInterest1()+getGroupInfoSearchReq.getInterest2());
        //System.out.println("page size : " + pageable.getPageSize());
        //System.out.println(getGroupInfoSearchReq.getClientTime());
        System.out.println("title : "+title+"regionIdx : "+ regionIdx +"groupIdx : "+groupIdx +
                "sortCond : "+sortCond + "ClientTime : "+ClientTime  );
        GetGroupInfoSearchReq getGroupInfoSearchReq = new GetGroupInfoSearchReq(
                title, regionIdx, interest1, interest2, groupIdx, sortCond, ClientTime
        );
        System.out.println(getGroupInfoSearchReq.getSortCond());
        System.out.println(getGroupInfoSearchReq.getClientTime());
        Slice<GetGroupInfoSearchRes> result = groupInfoService.searchGroup(getGroupInfoSearchReq,pageable);
        System.out.println("---------");
        System.out.println(result.isLast());
        return new BaseResponse<>(result);


    }


    @GetMapping("/search/{groupIdx}")
    @ResponseBody
    public BaseResponse<GetEachGroupInfoRes> selectEachGroupInfo(@PathVariable Long groupIdx){

        System.out.println(groupIdx);
        GetEachGroupInfoRes response = groupInfoService.selectEachGroupInfo(groupIdx);
        return new BaseResponse<>(response);
    }







    @GetMapping("/search/test")
    @ResponseBody
    public JPAQuery<Integer> searchtest(@RequestBody GetGroupInfoSearchReq getGroupInfoSearchReq, Pageable pageable) {
        System.out.println("들어오ㅏ?"+getGroupInfoSearchReq.getTitle());
        System.out.println(pageable.getPageSize()+"   "+pageable.toString());
        return groupInfoService.searchtestGroup(getGroupInfoSearchReq, pageable);
    }



}
