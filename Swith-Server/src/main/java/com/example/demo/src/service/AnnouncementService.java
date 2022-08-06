package com.example.demo.src.service;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.dto.request.PostAnnouncementReq;
import com.example.demo.src.dto.response.GetAnnouncementRes;
import com.example.demo.src.entity.Announcement;
import com.example.demo.src.entity.GroupInfo;
import com.example.demo.src.repository.AnnouncementRepository;
import com.example.demo.src.repository.GroupInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class AnnouncementService {
    private final AnnouncementRepository announcementRepository;
    private final GroupInfoRepository groupInfoRepository;

    @Autowired
    public AnnouncementService(AnnouncementRepository announcementRepository, GroupInfoRepository groupInfoRepository) {
        this.announcementRepository = announcementRepository;
        this.groupInfoRepository = groupInfoRepository;
    }

    public List<GetAnnouncementRes> loadAnnouncements(Long groupIdx) {
        List<Announcement> announcementList = announcementRepository.findByGroupIdx(groupIdx);
        List<GetAnnouncementRes> getAnnouncementResList = new ArrayList<>();
        for (Announcement announcement : announcementList) {
            GetAnnouncementRes getAnnouncementRes = GetAnnouncementRes.builder()
                    .announcementIdx(announcement.getAnnouncementIdx())
                    .announcementContent(announcement.getAnnouncementContent())
                    .createdAt(announcement.getCreatedAt())
                    .build();
            getAnnouncementResList.add(getAnnouncementRes);
        }
        return getAnnouncementResList;
    }

    public Long createAnnouncement(PostAnnouncementReq postAnnouncementReq) throws BaseException {
        GroupInfo groupInfo = groupInfoRepository.findById(postAnnouncementReq.getGroupIdx())
                .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_GROUPIDX));

        Announcement announcement = Announcement.builder()
                .announcementContent(postAnnouncementReq.getAnnouncementContent())
                .groupInfo(groupInfo)
                .build();

        Announcement savedAnnouncement = announcementRepository.save(announcement);

        return savedAnnouncement.getAnnouncementIdx();
    }
}
