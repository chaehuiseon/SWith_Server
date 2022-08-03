package com.example.demo.src.controller;

import com.example.demo.config.BaseResponse;
import com.example.demo.src.dto.GetAnnouncementRes;
import com.example.demo.src.service.AnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/groupinfo/announcement")
public class AnnouncementController {
    private final AnnouncementService announcementService;

    @Autowired
    public AnnouncementController(AnnouncementService announcementService) {
        this.announcementService = announcementService;
    }

    @GetMapping("/{groupIdx}")
    public BaseResponse<List<GetAnnouncementRes>> loadAnnouncements
            (@PathVariable("groupIdx") Long groupIdx){
        List<GetAnnouncementRes> getAnnouncementResList = announcementService.loadAnnouncements(groupIdx);
        return new BaseResponse<>(getAnnouncementResList);
    }
}
