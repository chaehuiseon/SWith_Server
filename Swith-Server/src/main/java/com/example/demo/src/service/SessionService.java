package com.example.demo.src.service;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.dto.response.GetGroupInfoRes;
import com.example.demo.src.dto.response.GetSessionRes;
import com.example.demo.src.dto.request.PostSessionReq;
import com.example.demo.src.dto.response.GetSessionTabRes;
import com.example.demo.src.dto.response.SessionAttendanceInfo;
import com.example.demo.src.entity.Announcement;
import com.example.demo.src.entity.Attendance;
import com.example.demo.src.entity.GroupInfo;
import com.example.demo.src.entity.Session;
import com.example.demo.src.repository.AnnouncementRepository;
import com.example.demo.src.repository.AttendanceRepository;
import com.example.demo.src.repository.GroupInfoRepository;
import com.example.demo.src.repository.SessionRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class SessionService {
    private final SessionRepository sessionRepository;
    private final GroupInfoRepository groupInfoRepository;
    private final AnnouncementRepository announcementRepository;
    private final AttendanceRepository attendanceRepository;


    @Autowired
    public SessionService(SessionRepository sessionRepository, GroupInfoService groupInfoService, GroupInfoRepository groupInfoRepository, AnnouncementRepository announcementRepository, AttendanceRepository attendanceRepository) {
        this.sessionRepository = sessionRepository;
        this.groupInfoRepository = groupInfoRepository;
        this.announcementRepository = announcementRepository;
        this.attendanceRepository = attendanceRepository;
    }


    public Long createSession(PostSessionReq postSessionReq, Integer sessionNum) throws BaseException {
        GroupInfo groupInfo = groupInfoRepository.findById(postSessionReq.getGroupIdx())
                .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_GROUP));
        Session session = Session.builder()
                .sessionContent(postSessionReq.getSessionContent())
                .sessionNum(sessionNum)
                .sessionStart(postSessionReq.getSessionStart())
                .sessionEnd(postSessionReq.getSessionEnd())
                .groupInfo(groupInfo)
                .online(postSessionReq.getOnline())
                .place(postSessionReq.getPlace())
                .build();
        Session session1 = sessionRepository.save(session);
        return session1.getSessionIdx();
    }


    public Integer findAppropriateSessionNum(@NotNull PostSessionReq postSessionReq) {
        Integer sessionNum = sessionRepository.findAppropriateSessionNum(postSessionReq.getGroupIdx(), postSessionReq.getSessionStart());
        return (sessionNum + 1);
    }


    public Integer adjustSessionNum(Integer sessionNum, Long groupIdx) {
        return sessionRepository.updateSessionNumPlusOne(sessionNum, groupIdx);
    }

    public GetGroupInfoRes loadGroupInfoAndSession(Long groupIdx, boolean isAdmin) throws BaseException {
        //그룹정보 찾기
        GroupInfo groupInfo = groupInfoRepository.findById(groupIdx)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_GROUP));

        //가장 최근의 공지사항 가져오기
        Announcement announcement = announcementRepository
                .findByGroupInfo_GroupIdxOrderByCreatedAtDesc(groupIdx).get(0);

        //sessionList 가져오기 <- fetch join으로 연관된 Attendance도 모두 가져오도록
        List<Session> getSessionList = sessionRepository.getSessionInfoByGroupIdx(groupIdx);

        List<GetSessionRes> getSessionResList = new ArrayList<>();

        //루프를 돌며 각 세션의 정보를 할당
        for (Session session : getSessionList) {
            List<Attendance> attendanceList = session.getAttendances();
            int attendanceRate, attendances = 0;

            //해당 세션이 '예정'상태가 아닌 경우 출석율을, 맞다면 -1을 attendanceRate 변수에 할당한다.
            if(LocalDateTime.now().isAfter(session.getSessionEnd()) && !attendanceList.isEmpty()) {
                for (Attendance attendance : attendanceList) {
                    if (attendance.getStatus() == 1)
                        attendances++;
                }
                attendanceRate = attendances * 100 / attendanceList.size();
            } else
                attendanceRate = -1;

            GetSessionRes getSessionRes = GetSessionRes.builder()
                    .sessionIdx(session.getSessionIdx())
                    .sessionNum(session.getSessionNum())
                    .sessionStart(session.getSessionStart())
                    .sessionEnd(session.getSessionEnd())
                    .sessionContent(session.getSessionContent())
                    .online(session.getOnline())
                    .place(session.getPlace())
                    .attendanceRate(attendanceRate)
                    .build();
            getSessionResList.add(getSessionRes);
        }


        GetGroupInfoRes getGroupInfoRes = GetGroupInfoRes.builder()
                .title(groupInfo.getTitle())
                .isAdmin(isAdmin)
                .announcementDate(announcement.getCreatedAt())
                .announcementContent(announcement.getAnnouncementContent())
                .getSessionResList(getSessionResList)
                .build();

        return getGroupInfoRes;
    }

    public GetSessionTabRes getSessionInfo(Long userIdx, Long sessionIdx) throws BaseException{
        Session session = sessionRepository.findByIdWithGroup(sessionIdx)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_SESSION));
        List<Attendance> attendanceList = attendanceRepository.findBySession(sessionIdx);

        List<SessionAttendanceInfo> getAttendanceList = new ArrayList<>();

        for (Attendance attendance : attendanceList) {
            SessionAttendanceInfo attendanceInfo = SessionAttendanceInfo.builder()
                    .userIdx(attendance.getUser().getUserIdx())
                    .nickname(attendance.getUser().getNickname())
                    .status(attendance.getStatus())
                    .build();
            getAttendanceList.add(attendanceInfo);
        }


        GetSessionTabRes getSessionTabRes = GetSessionTabRes.builder()
                .sessionIdx(session.getSessionIdx())
                .sessionNum(session.getSessionNum())
                .sessionStart(session.getSessionStart())
                .sessionEnd(session.getSessionEnd())
                .online(session.getOnline())
                .place(session.getPlace())
                .sessionContent(session.getSessionContent())
                .getAttendanceList(getAttendanceList)
                .groupImgUrl(session.getGroupInfo().getGroupImgUrl())
                .attendanceValidTime(session.getGroupInfo().getAttendanceValidTime())
                .userMemo("미완성")
                .build();

        return getSessionTabRes;
    }
}
