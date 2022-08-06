package com.example.demo.src.controller;

import com.example.demo.config.BaseResponse;
import com.example.demo.src.dto.request.PostAnnouncementReq;
import com.example.demo.src.dto.response.GetAnnouncementRes;
import com.example.demo.src.service.AnnouncementService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/groupinfo/announcement")
@Api(tags = {"Swith Announcement API"})
public class AnnouncementController {
    private final AnnouncementService announcementService;

    @Autowired
    public AnnouncementController(AnnouncementService announcementService) {
        this.announcementService = announcementService;
    }

    @ApiOperation("그룹의 모든 공지사항 불러오기")
    @GetMapping("/{groupIdx}")
    public BaseResponse<List<GetAnnouncementRes>> loadAnnouncements
            (@PathVariable("groupIdx") Long groupIdx){
        List<GetAnnouncementRes> getAnnouncementResList = announcementService.loadAnnouncements(groupIdx);
        return new BaseResponse<>(getAnnouncementResList);
    }

    @ApiOperation("그룹 공지사항 생성")
    @PostMapping()
    public BaseResponse<Long> createAnnouncement (@RequestBody PostAnnouncementReq postAnnouncementReq){

        return new BaseResponse<>(0L);
    }
}
