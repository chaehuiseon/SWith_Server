package com.example.demo.src.service;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.dto.request.PatchAnnouncementReq;
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

    public List<GetAnnouncementRes> loadAnnouncements(Long groupIdx) throws BaseException{
        if(!groupInfoRepository.existsById(groupIdx)){
            throw new BaseException(BaseResponseStatus.INVALID_GROUP);
        }
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
                .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_GROUP));

        Announcement announcement = Announcement.builder()
                .announcementContent(postAnnouncementReq.getAnnouncementContent())
                .groupInfo(groupInfo)
                .build();

        Announcement savedAnnouncement = announcementRepository.save(announcement);

        return savedAnnouncement.getAnnouncementIdx();
    }

    public Long updateAnnouncement(PatchAnnouncementReq patchAnnouncementReq)  throws BaseException {
        Announcement announcement = announcementRepository.findById(patchAnnouncementReq.getAnnouncementIdx())
                .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_ANNOUNCEMENT));
        //삭제된 상태의 경우
        if(announcement.getStatus() == 1)
            throw new BaseException(BaseResponseStatus.ALREADY_DELETED_ANNOUNCEMENT);

        //내용 수정 작업
        Integer count = announcementRepository.updateById(patchAnnouncementReq.getAnnouncementIdx()
                , patchAnnouncementReq.getAnnouncementContent());
        if(count != 1)
            throw new BaseException(BaseResponseStatus.MODIFY_FAIL_ANNOUNCEMENT);
        return announcement.getAnnouncementIdx();
    }

    public Integer deleteAnnouncement(Long announcementIdx) throws BaseException {
        Announcement announcement = announcementRepository.findById(announcementIdx)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_ANNOUNCEMENT));

        //삭제된 상태의 경우
        if(announcement.getStatus() == 1)
            throw new BaseException(BaseResponseStatus.ALREADY_DELETED_ANNOUNCEMENT);

        //삭제 작업
        Integer count = announcementRepository.deleteByIdx(announcementIdx);
        if(count != 1)
            throw new BaseException(BaseResponseStatus.DELETE_FAIL_ANNOUNCEMENT);
        return count;
    }
}
