package com.example.demo.src.service;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.dto.request.PatchAttendanceReq;
import com.example.demo.src.dto.response.GetGroupAttendanceRes;
import com.example.demo.src.dto.response.GetUserAttendanceRes;
import com.example.demo.src.dto.response.UserAttendanceInfo;
import com.example.demo.src.entity.Attendance;
import com.example.demo.src.entity.GroupInfo;
import com.example.demo.src.entity.Session;
import com.example.demo.src.entity.User;
import com.example.demo.src.repository.AttendanceRepository;
import com.example.demo.src.repository.GroupInfoRepository;
import com.example.demo.src.repository.SessionRepository;
import com.example.demo.src.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class AttendanceService {
    private final AttendanceRepository attendanceRepository;
    private final SessionRepository sessionRepository;
    private final GroupInfoRepository groupInfoRepository;
    private final UserRepository userRepository;

    public AttendanceService(AttendanceRepository attendanceRepository, SessionRepository sessionRepository, GroupInfoRepository groupInfoRepository, UserRepository userRepository) {
        this.attendanceRepository = attendanceRepository;
        this.sessionRepository = sessionRepository;
        this.groupInfoRepository = groupInfoRepository;
        this.userRepository = userRepository;
    }

    public GetGroupAttendanceRes getGroupAttendance(Long groupIdx) throws BaseException {
        GroupInfo groupInfo = groupInfoRepository.findById(groupIdx)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_GROUP));

        List<UserAttendanceInfo> userTotalAttendance = attendanceRepository.getUserTotalAttendance(groupIdx);
        if (userTotalAttendance.isEmpty())
            throw new BaseException(BaseResponseStatus.NO_GROUP_ATTENDANCE);

        List<GetUserAttendanceRes> getUserAttendanceResList = new ArrayList<>();
        UserAttendanceInfo first = userTotalAttendance.get(0);
        Long prevId = first.getUserIdx();
        String nickname = first.getNickname();
        int totalAttendance = 0, validAttendance = 0;

        for (UserAttendanceInfo attend : userTotalAttendance) {
            Integer status = attend.getStatus();

            if (!attend.getUserIdx().equals(prevId)) {
                int attendanceRate;
                if (totalAttendance == 0)        //status 가 0(예정)인 출석 정보만 있을 경우
                    attendanceRate = -1;
                else
                    attendanceRate = validAttendance * 100 / totalAttendance;


                GetUserAttendanceRes userAttendanceRes = GetUserAttendanceRes.builder()
                        .nickname(nickname)
                        .attendanceRate(attendanceRate)
                        .userIdx(prevId)
                        .build();

                getUserAttendanceResList.add(userAttendanceRes);

                prevId = attend.getUserIdx();
                nickname = attend.getNickname();
                totalAttendance = 0;
                validAttendance = 0;
            }
            if (attend.equals(userTotalAttendance.get(userTotalAttendance.size() - 1))) {
                if (status == 1) {    //유효한 출석일 경우
                    totalAttendance++;
                    validAttendance++;
                } else if (status != 0) { // 2 혹은 3일 경우
                    totalAttendance++;
                }
                int attendanceRate;
                if (totalAttendance == 0)        //status 가 0(예정)인 출석 정보만 있을 경우
                    attendanceRate = -1;
                else
                    attendanceRate = validAttendance * 100 / totalAttendance;


                GetUserAttendanceRes userAttendanceRes = GetUserAttendanceRes.builder()
                        .nickname(nickname)
                        .attendanceRate(attendanceRate)
                        .userIdx(prevId)
                        .build();

                getUserAttendanceResList.add(userAttendanceRes);
            }

            if (status == 1) {    //유효한 출석일 경우
                totalAttendance++;
                validAttendance++;
            } else if (status != 0) { // 2 혹은 3일 경우
                totalAttendance++;
            }
        }


        String attendanceValidTime = groupInfo.getAttendanceValidTime() + "분";
        GetGroupAttendanceRes getGroupAttendanceRes = GetGroupAttendanceRes.builder()
                .attendanceValidTime(attendanceValidTime)
                .getUserAttendanceResList(getUserAttendanceResList)
                .build();
        return getGroupAttendanceRes;
    }

    public Long updateAttendance(Long userIdx, Long sessionIdx) throws BaseException {
        User user = userRepository.findById(sessionIdx)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_USER));
        Session session = sessionRepository.findByIdWithGroup(sessionIdx)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_SESSION));
        //출석정보가 없으면 만든다.
        Attendance attendance = attendanceRepository.findByUserAndSession(userIdx, sessionIdx)
                .orElseGet(() -> makeAttendanceInfo(user, session));
        //지금 시간과 출석 유효시간을 고려해 상태를 결정
        Integer status = decideStatus(session);
        //이미 출석정보가 있으면 오류(일반 사용자의 접근이기 때문)
        if (attendance.getStatus() != 0)
            throw new BaseException(BaseResponseStatus.ALREADY_ATTENDED);


        attendance.setStatus(status);
        Attendance save = attendanceRepository.save(attendance);

        return save.getAttendanceIdx();
    }

    private Attendance makeAttendanceInfo(User user, Session session) {

        Attendance attendance = Attendance.builder()
                .session(session)
                .groupInfo(session.getGroupInfo())
                .user(user)
                .status(0)
                .build();
        Attendance save = attendanceRepository.save(attendance);

        return save;
    }

    private Integer decideStatus(Session session) throws BaseException {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = session.getSessionStart();
        LocalDateTime end = session.getSessionEnd();
        Integer validTime = session.getGroupInfo().getAttendanceValidTime();
        Integer status = 0;
        if (now.isBefore(start))
            //지금시간 < 시작시간
            throw new BaseException(BaseResponseStatus.FAIL_ATTEND);
        else if (now.isAfter(start) && now.isBefore(start.plusMinutes(validTime)))
            //시작시간 < 지금시간 < 시작시간 + 유효시간
            status = 1; //정상출석
        else if (now.isAfter(start.plusMinutes(validTime)) && now.isBefore(end))
            //시작시간 + 유효시간 < 지금시간 < 끝 시간
            status = 2; //지각
        else if (now.isAfter(end))
            //끝 시간 < 지금 시간
            status = 3; //결석
        return status;
    }


    public Integer modifyAttendance(List<PatchAttendanceReq> patchAttendanceReqList) throws BaseException {
        Integer count = 0;
        for (PatchAttendanceReq patchAttendanceReq : patchAttendanceReqList) {
            count += attendanceRepository.modifyStatus(patchAttendanceReq.getAttendanceIdx(), patchAttendanceReq.getStatus());
        }
        return count;
    }
}
