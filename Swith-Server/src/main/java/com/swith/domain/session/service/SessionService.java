package com.swith.domain.session.service;

import com.swith.domain.announcement.entity.Announcement;
import com.swith.domain.announcement.repository.AnnouncementRepository;
import com.swith.domain.attendance.entity.Attendance;
import com.swith.domain.attendance.repository.AttendanceRepository;
import com.swith.domain.groupinfo.entity.GroupInfo;
import com.swith.domain.groupinfo.repository.GroupInfoRepository;
import com.swith.domain.memo.entity.Memo;
import com.swith.domain.memo.repository.MemoRepository;
import com.swith.domain.register.repository.RegisterRepository;
import com.swith.domain.session.entity.Session;
import com.swith.domain.session.repository.SessionRepository;
import com.swith.domain.user.entity.User;
import com.swith.global.error.exception.BaseException;
import com.swith.global.error.ErrorCode;
import com.swith.api.session.dto.PatchSessionReq;
import com.swith.api.session.dto.GetGroupInfoRes;
import com.swith.api.session.dto.GetSessionRes;
import com.swith.api.session.dto.PostSessionReq;
import com.swith.api.session.dto.GetSessionTabRes;
import com.swith.api.session.dto.SessionAttendanceInfo;
import com.swith.api.groupinfo.service.GroupInfoApiService;
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
    private final MemoRepository memoRepository;
    private final RegisterRepository registerRepository;


    @Autowired
    public SessionService(SessionRepository sessionRepository, GroupInfoApiService groupInfoApiService, GroupInfoRepository groupInfoRepository, AnnouncementRepository announcementRepository, AttendanceRepository attendanceRepository, MemoRepository memoRepository, RegisterRepository registerRepository) {
        this.sessionRepository = sessionRepository;
        this.groupInfoRepository = groupInfoRepository;
        this.announcementRepository = announcementRepository;
        this.attendanceRepository = attendanceRepository;
        this.memoRepository = memoRepository;
        this.registerRepository = registerRepository;
    }


    public Long createSession(PostSessionReq postSessionReq, Integer sessionNum) {
        GroupInfo groupInfo = groupInfoRepository.findById(postSessionReq.getGroupIdx())
                .orElseThrow(() -> new BaseException(ErrorCode.INVALID_GROUP));

        Session session = Session.builder()
                .sessionContent(postSessionReq.getSessionContent())
                .sessionNum(sessionNum)
                .sessionStart(postSessionReq.getSessionStart())
                .sessionEnd(postSessionReq.getSessionEnd())
                .groupInfo(groupInfo)
                .online(postSessionReq.getOnline())
                .place(postSessionReq.getPlace())
                .build();
        List<Attendance> attendanceList = session.getAttendances();
        //출석 만들기
        List<User> userByGroup = registerRepository.findUserByGroup(postSessionReq.getGroupIdx());
        for (User user : userByGroup) {
            Attendance attendance = Attendance.builder()
                    .user(user)
                    .session(session)
                    .groupInfo(groupInfo)
                    .build();
            attendanceList.add(attendance);
        }

        Session session1 = sessionRepository.save(session);
        return session1.getSessionIdx();
    }


    public Integer findAppropriateSessionNum(Long groupIdx, LocalDateTime sessionStart)  {
        if (LocalDateTime.now().isAfter(sessionStart))
            throw new BaseException(ErrorCode.START_TIME_ERROR);
        Integer sessionNum = sessionRepository.findAppropriateSessionNum(groupIdx, sessionStart);
        return (sessionNum + 1);
    }


    public Integer adjustSessionNum(Integer sessionNum, Long groupIdx) {
        return sessionRepository.updateSessionNumPlusOne(sessionNum, groupIdx);
    }

    public GetGroupInfoRes loadGroupInfoAndSession(Long groupIdx, boolean isAdmin) {
        //그룹정보 찾기
        GroupInfo groupInfo = groupInfoRepository.findById(groupIdx)
                .orElseThrow(() -> new BaseException(ErrorCode.INVALID_GROUP));

        //가장 최근의 공지사항 가져오기
        Announcement announcement;
        List<Announcement> announcementList = announcementRepository
                .findByGroupInfo_GroupIdxOrderByModifiedAtDesc(groupIdx);
        if (announcementList.isEmpty()) {
            announcement = Announcement
                    .builder()
                    .announcementContent("공지사항이 없습니다.")
                    .build();
        } else
            announcement = announcementList.get(0);


        //sessionList 가져오기 <- fetch join으로 연관된 Attendance도 모두 가져오도록
        List<Session> getSessionList = sessionRepository.getSessionAndAttendanceByGroupIdx(groupIdx);

        List<GetSessionRes> getSessionResList = new ArrayList<>();

        //루프를 돌며 각 세션의 정보를 할당
        for (Session session : getSessionList) {
            List<Attendance> attendanceList = session.getAttendances();
            int attendanceRate, attendances = 0;

            //해당 세션이 '예정'상태가 아닌 경우 출석율을, 맞다면 -1을 attendanceRate 변수에 할당한다.
            if (LocalDateTime.now().isAfter(session.getSessionEnd()) && !attendanceList.isEmpty()) {
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
                .isAdmin(isAdmin)
                .title(groupInfo.getTitle())
                .groupImgUrl(groupInfo.getGroupImgUrl())
                .attendanceValidTime(groupInfo.getAttendanceValidTime())
                .announcementDate(announcement.getCreatedAt())
                .announcementContent(announcement.getAnnouncementContent())
                .getSessionResList(getSessionResList)
                .build();

        return getGroupInfoRes;
    }

    public GetSessionTabRes getSessionInfo(Long userIdx, Long sessionIdx)  {
        Session session = sessionRepository.findByIdWithGroup(sessionIdx)
                .orElseThrow(() -> new BaseException(ErrorCode.INVALID_SESSION));
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


        //메모를 찾고 메모정보가 없으면 Idx는 -1, 내용은 NULL을 반환
        Memo memo = memoRepository.findByUserAndSession(userIdx, sessionIdx)
                .orElseGet(() -> Memo.builder()
                        .build());

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
                .memoIdx(memo.getMemoIdx())
                .userMemo(memo.getMemoContent())
                .build();

        return getSessionTabRes;
    }

    public Long modifySession(PatchSessionReq patchSessionReq) {
        LocalDateTime start = patchSessionReq.getSessionStart();
        LocalDateTime end = patchSessionReq.getSessionEnd();
        LocalDateTime now = LocalDateTime.now();
        Long sessionIdx = patchSessionReq.getSessionIdx();
//        if (start.isBefore(now))
//            throw new BaseException(ErrorCode.INAPPROPRIATE_START_TIME);
        Session session = sessionRepository.findByIdWithGroup(sessionIdx)
                .orElseThrow(() -> new BaseException(ErrorCode.INVALID_SESSION));
        Long groupIdx = session.getGroupInfo().getGroupIdx();

        if (sessionRepository.existsOverlappedSession(groupIdx, sessionIdx, start, end))
            throw new BaseException(ErrorCode.TIME_OVERLAPPED);
        session.setOnline(patchSessionReq.getOnline());
        if (patchSessionReq.getOnline() == 1)
            session.setPlace("");
        else
            session.setPlace(patchSessionReq.getPlace());

        session.setSessionContent(patchSessionReq.getSessionContent());
        session.setSessionStart(start);
        session.setSessionEnd(end);
        sessionRepository.save(session);
        List<Session> sessionList = sessionRepository.findByGroupIdx(groupIdx);
        if (sessionList.isEmpty())
            throw new BaseException(ErrorCode.NO_SESSION_INFO);
        adjustSessionNum(sessionList);

        return sessionIdx;
    }

    public Long deleteSession(Long sessionIdx) {
        Session session = sessionRepository.findByIdWithGroup(sessionIdx)
                .orElseThrow(() -> new BaseException(ErrorCode.NO_SESSION_INFO));
        if (session.getStatus() == 1)
            throw new BaseException(ErrorCode.ALREADY_DELETED_SESSION);
        if (session.getSessionStart().isBefore(LocalDateTime.now()))
            throw new BaseException(ErrorCode.DELETE_FAIL_SESSION);

        sessionRepository.deleteSession(sessionIdx);

        //회차 수 재조정
        List<Session> sessionList = sessionRepository.findByGroupIdx(session.getGroupInfo().getGroupIdx());
        if (sessionList.isEmpty())
            throw new BaseException(ErrorCode.NO_SESSION_INFO);
        adjustSessionNum(sessionList);

        return sessionIdx;
    }

    private void adjustSessionNum(List<Session> sessionList) {
        for (int i = 1; i < sessionList.size() + 1; i++) {
            Session session1 = sessionList.get(i - 1);
            if (session1.getSessionNum() != i) {
                session1.setSessionNum(i);
                sessionRepository.save(session1);
            }
        }
    }

    public boolean existsOverlappedSession(Long groupIdx, LocalDateTime sessionStart, LocalDateTime sessionEnd) {
        return sessionRepository.existsOverlappedSession(groupIdx, 0L, sessionStart, sessionEnd);
    }
}
