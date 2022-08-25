package com.example.demo.src.controller;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.dto.request.PatchAnnouncementReq;
import com.example.demo.src.dto.request.PostAnnouncementReq;
import com.example.demo.src.dto.response.GetAnnouncementRes;
import com.example.demo.src.service.AnnouncementService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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

    //그룹 Id가 유효한지, 공지사항이 있는지에 대한 예외처리 필요한지 고민이 필요
    @ApiOperation("그룹의 모든 공지사항 불러오기 - P4")
    @GetMapping("/{groupIdx}")
    public BaseResponse<List<GetAnnouncementRes>> loadAnnouncements
            (@PathVariable("groupIdx") Long groupIdx){
        try{
            List<GetAnnouncementRes> getAnnouncementResList = announcementService.loadAnnouncements(groupIdx);
            return new BaseResponse<>(getAnnouncementResList);
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ApiOperation("그룹 공지사항 생성 - P5")
    @PostMapping()
    public BaseResponse<Long> createAnnouncement (@RequestBody PostAnnouncementReq postAnnouncementReq){
        try{
            Long announcementIdx = announcementService.createAnnouncement(postAnnouncementReq);
            return new BaseResponse<>(announcementIdx);
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        } catch (IOException e){
            return new BaseResponse<>(BaseResponseStatus.INVALID_GROUP);
        }
    }

    @ApiOperation("그룹 공지사항 수정 - P6")
    @PatchMapping()
    public BaseResponse<String> updateAnnouncement (@RequestBody PatchAnnouncementReq patchAnnouncementReq){
        try{
            Long announcementIdx = announcementService.updateAnnouncement(patchAnnouncementReq);
            String result = "수정 완료했습니다. ID: " + announcementIdx;
            return new BaseResponse<>(result);
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ApiOperation("그룹 공지사항 삭제 - P7")
    @PatchMapping("{announcementIdx}/status")
    public BaseResponse<String> deleteAnnouncement (@PathVariable("announcementIdx") Long announcementIdx){
        try{
            announcementService.deleteAnnouncement(announcementIdx);
            String result = "삭제 완료했습니다. ID: " + announcementIdx;
            return new BaseResponse<>(result);
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }
}
