package com.swith.api.announcement.controller;

import com.swith.global.error.exception.BaseException;
import com.swith.api.common.dto.BaseResponse;
import com.swith.global.error.ErrorCode;
import com.swith.api.announcement.dto.PatchAnnouncementReq;
import com.swith.api.announcement.dto.PostAnnouncementReq;
import com.swith.api.announcement.dto.GetAnnouncementRes;
import com.swith.domain.announcement.service.AnnouncementService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<GetAnnouncementRes>> loadAnnouncements
            (@PathVariable("groupIdx") Long groupIdx){
            List<GetAnnouncementRes> getAnnouncementResList = announcementService.loadAnnouncements(groupIdx);
            return ResponseEntity.ok(getAnnouncementResList);
    }

    @ApiOperation("그룹 공지사항 생성 - P5")
    @PostMapping()
    public BaseResponse<Long> createAnnouncement (@RequestBody PostAnnouncementReq postAnnouncementReq){
            Long announcementIdx = announcementService.createAnnouncement(postAnnouncementReq);
            return new BaseResponse<>(announcementIdx);
    }

    @ApiOperation("그룹 공지사항 수정 - P6")
    @PatchMapping()
    public ResponseEntity<String> updateAnnouncement (@RequestBody PatchAnnouncementReq patchAnnouncementReq){
            Long announcementIdx = announcementService.updateAnnouncement(patchAnnouncementReq);
            String result = "수정 완료했습니다. ID: " + announcementIdx;
            return ResponseEntity.ok(result);
    }

    @ApiOperation("그룹 공지사항 삭제 - P7")
    @PatchMapping("{announcementIdx}/status")
    public ResponseEntity<String> deleteAnnouncement (@PathVariable("announcementIdx") Long announcementIdx){
            announcementService.deleteAnnouncement(announcementIdx);
            String result = "삭제 완료했습니다. ID: " + announcementIdx;
            return ResponseEntity.ok(result);
    }
}
