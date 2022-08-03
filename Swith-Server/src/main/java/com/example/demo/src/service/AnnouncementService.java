package com.example.demo.src.service;

import com.example.demo.src.dto.GetAnnouncementRes;
import com.example.demo.src.entity.Announcement;
import com.example.demo.src.repository.AnnouncementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class AnnouncementService {
    private final AnnouncementRepository announcementRepository;

    @Autowired
    public AnnouncementService(AnnouncementRepository announcementRepository) {
        this.announcementRepository = announcementRepository;
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
}
