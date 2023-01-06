package com.swith.src.service;

import com.swith.config.BaseException;
import com.swith.config.BaseResponseStatus;
import com.swith.src.dto.request.PatchAnnouncementReq;
import com.swith.src.dto.request.PostAnnouncementReq;
import com.swith.src.dto.response.GetAnnouncementRes;
import com.swith.src.entity.Announcement;
import com.swith.src.entity.GroupInfo;
import com.swith.src.firebase.FirebaseCloudMessageService;
import com.swith.src.repository.AnnouncementRepository;
import com.swith.src.repository.GroupInfoRepository;
import com.swith.src.repository.RegisterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class AnnouncementService {
    private final AnnouncementRepository announcementRepository;
    private final GroupInfoRepository groupInfoRepository;
    private final RegisterRepository registerRepository;
    private final FirebaseCloudMessageService fcmService;

    @Autowired
    public AnnouncementService(AnnouncementRepository announcementRepository,
                               GroupInfoRepository groupInfoRepository,
                               RegisterRepository registerRepository,
                               FirebaseCloudMessageService fcmService) {
        this.announcementRepository = announcementRepository;
        this.groupInfoRepository = groupInfoRepository;
        this.registerRepository = registerRepository;
        this.fcmService = fcmService;
    }

    public List<GetAnnouncementRes> loadAnnouncements(Long groupIdx) throws BaseException {
        if(!groupInfoRepository.existsById(groupIdx)){
            throw new BaseException(BaseResponseStatus.INVALID_GROUP);
        }
        List<Announcement> announcementList = announcementRepository.findByGroupIdx(groupIdx);
        List<GetAnnouncementRes> getAnnouncementResList = new ArrayList<>();

        for (Announcement announcement : announcementList) {
            GetAnnouncementRes result = getGetAnnouncementRes(announcement);
            getAnnouncementResList.add(result);
        }
        return getAnnouncementResList;
    }

    private GetAnnouncementRes getGetAnnouncementRes(Announcement announcement) {
        GetAnnouncementRes getAnnouncementRes = GetAnnouncementRes.builder()
                .announcementIdx(announcement.getAnnouncementIdx())
                .announcementContent(announcement.getAnnouncementContent())
                .createdAt(announcement.getCreatedAt())
                .build();
        return getAnnouncementRes;
    }

    public Long createAnnouncement(PostAnnouncementReq postAnnouncementReq) throws BaseException, IOException {
        Long groupIdx = postAnnouncementReq.getGroupIdx();
        GroupInfo groupInfo = groupInfoRepository.findById(groupIdx)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_GROUP));
        String title = groupInfo.getTitle();
        ArrayList<Long> userIdxByGroup = registerRepository.findUserIdxByGroup(groupIdx);
        ArrayList<String> pushUserToken = groupInfoRepository.findUserToken(userIdxByGroup);

        //파이어 베이스 알림 신청
        String announcementContent = postAnnouncementReq.getAnnouncementContent();
        String phrases = "새로운 공지사항 입니다.";
        //새로운 공지사항 입니다.//공지//1//
        String content =  phrases + "//" + "공지" + "//" + groupIdx + "//" ;
        for (String token : pushUserToken) {
            fcmService.sendMessageTo(token,title,content);
        }

        Announcement announcement = Announcement.builder()
                .announcementContent(announcementContent)
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
                ,patchAnnouncementReq.getAnnouncementContent(), LocalDateTime.now());
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
