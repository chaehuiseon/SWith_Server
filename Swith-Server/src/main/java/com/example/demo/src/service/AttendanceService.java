package com.example.demo.src.service;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.dto.response.GetGroupAttendanceRes;
import com.example.demo.src.dto.response.GetUserAttendanceRes;
import com.example.demo.src.dto.response.UserAttendanceInfo;
import com.example.demo.src.entity.GroupInfo;
import com.example.demo.src.repository.AttendanceRepository;
import com.example.demo.src.repository.GroupInfoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class AttendanceService {
    private final AttendanceRepository attendanceRepository;
    private final GroupInfoRepository groupInfoRepository;

    public AttendanceService(AttendanceRepository attendanceRepository, GroupInfoRepository groupInfoRepository) {
        this.attendanceRepository = attendanceRepository;
        this.groupInfoRepository = groupInfoRepository;
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
}
