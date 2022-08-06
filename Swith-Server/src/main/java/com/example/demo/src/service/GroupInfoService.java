package com.example.demo.src.service;

import com.example.demo.config.BaseException;
import com.example.demo.src.dto.response.GetHomeGroupInfoRes;
import com.example.demo.src.dto.PostGroupInfoReq;
import com.example.demo.src.dto.PostGroupInfoRes;
import com.example.demo.src.entity.*;
import com.example.demo.src.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@Transactional
@Service
public class GroupInfoService {
    private final GroupInfoRepository groupInfoRepository;
    private final RegisterRepository registerRepository;
    private final InterestRepository interestRepository;
    private final AnnouncementRepository announcementRepository;
    private final SessionRepository sessionRepository;
    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;

    @Autowired
    public GroupInfoService(GroupInfoRepository groupInfoRepository, RegisterRepository registerRepository, RegisterService registerService, InterestRepository interestRepository, AnnouncementRepository announcementRepository, SessionRepository sessionRepository, AttendanceRepository attendanceRepository, UserRepository userRepository) {
        this.groupInfoRepository = groupInfoRepository;
        this.registerRepository = registerRepository;
        this.interestRepository = interestRepository;
        this.announcementRepository = announcementRepository;
        this.sessionRepository = sessionRepository;
        this.attendanceRepository = attendanceRepository;
        this.userRepository = userRepository;
    }

    public List<GetHomeGroupInfoRes> loadHomeData(Long userIdx) throws BaseException {
        List<GroupInfo> groupInfos = registerRepository.findGroupInfoByUserIdx(userIdx);
        if(groupInfos.isEmpty())
            throw  new BaseException(NO_REGISTRATION_INFO);

        List<GetHomeGroupInfoRes> getHomeGroupInfoResList = new ArrayList<>();
        for(GroupInfo groupInfo : groupInfos){
            //가장 최근에 작성된 공지 불러오기
            String announcementContent;
            try {            Announcement announcement = announcementRepository
                    .findByGroupInfo_GroupIdxOrderByCreatedAtDesc(groupInfo.getGroupIdx())
                    .get(0);
                announcementContent = announcement.getAnnouncementContent();
            } catch (NullPointerException e){
                announcementContent = "작성된 공지가 없습니다.";
                System.out.println("e = " + e);
            }
            //fetch join
            //근 시일내에 가장 빠르게 예정에 있는 회차 정보 불러오기
                Session session = sessionRepository
                        .findFirstByGroupInfo_GroupIdxAndSessionStartAfterOrderBySessionNum
                                (groupInfo.getGroupIdx(), LocalDateTime.now()).get();

            //해당 그룹에서 ( 쿼리 다시 짜기 )
            List<Attendance> attendanceList = attendanceRepository
                    .findByGroupInfo_GroupIdxAndUser_UserIdxAndStatusIsNot(groupInfo.getGroupIdx(), userIdx, (Integer) 0);
            int attendanceNum = 0;
            for (Attendance attendance : attendanceList){
                if(attendance.getStatus().equals(1)){
                    attendanceNum += 1;
                }
            }
            int attendanceRate = attendanceNum * 100 / attendanceList.size();

            GetHomeGroupInfoRes getHomeGroupInfoRes = GetHomeGroupInfoRes.builder()
                    .groupIdx(groupInfo.getGroupIdx())
                    .title(groupInfo.getTitle())
                    .memberLimit(groupInfo.getMemberLimit())
                    .interestContent(groupInfo.getInterest().getInterestContent())
                    .announcementContent(announcementContent)
                    .sessionContent(session.getSessionContent())
                    .sessionNum(session.getSessionNum())
                    .sessionStart(session.getSessionStart())
                    .attendanceRate(attendanceRate)
                    .build();
            getHomeGroupInfoResList.add(getHomeGroupInfoRes);
        }
        return getHomeGroupInfoResList;

    }

    public PostGroupInfoRes create(PostGroupInfoReq request){
        System.out.println(request.toString());
        System.out.println("으악");
        PostGroupInfoReq body = request;
        long regionimsi1 = 123;
        long regionimsi2 = 456;
        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분 ss초");
        // 문자열 -> Date
        //LocalDateTime date = LocalDateTime.parse(dateStr, formatter);

        GroupInfo groupInfo = GroupInfo.builder()
                .user(userRepository.getOne(body.getAdminIdx()))
                .groupImgUrl(body.getGroupImgUrl())
                .title(body.getTitle())
                .meet(body.getMeet())
                .frequency(body.getFrequency())
                .periods(body.getPeriods())
                .online(body.getOnline())
                .regionIdx1(body.getRegionIdx1())
                .regionIdx2(body.getRegionIdx2())
                .interest(interestRepository.getOne(body.getInterest()))
                .topic(body.getTopic())
                .memberLimit(body.getMemberLimit())
                .applicationMethod(body.getApplicationMethod())
                .recruitmentEndDate(body.getRecruitmentEndDate())
                .groupStart(body.getGroupStart())
                .groupEnd(body.getGroupEnd())
                .attendanceValidTime(body.getAttendanceValidTime())
                .groupContent(body.getGroupContent())
                .build();

        GroupInfo savedgroupInfo = groupInfoRepository.save(groupInfo);
        long groupIdx = savedgroupInfo.getGroupIdx();
        return new PostGroupInfoRes(groupIdx);



    }




}
