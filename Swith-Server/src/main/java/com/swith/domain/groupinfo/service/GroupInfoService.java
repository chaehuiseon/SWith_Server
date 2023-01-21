package com.swith.domain.groupinfo.service;

import com.swith.domain.announcement.entity.Announcement;
import com.swith.domain.announcement.repository.AnnouncementRepository;
import com.swith.domain.application.repository.ApplicationRepository;
import com.swith.domain.attendance.entity.Attendance;
import com.swith.domain.attendance.repository.AttendanceRepository;
import com.swith.domain.groupinfo.entity.GroupInfo;
import com.swith.domain.groupinfo.repository.GroupInfoRepository;
import com.swith.domain.interest.entity.Interest;
import com.swith.domain.interest.repository.InterestRepository;
import com.swith.domain.register.entity.Register;
import com.swith.domain.register.repository.RegisterRepository;
import com.swith.domain.register.service.RegisterService;
import com.swith.domain.session.entity.Session;
import com.swith.domain.user.repository.UserRepository;
import com.swith.domain.session.repository.SessionRepository;
import com.swith.global.error.exception.BaseException;
import com.swith.global.error.ErrorCode;
import com.swith.api.groupinfo.dto.PatchGroupInfoReq;
import com.swith.api.groupinfo.dto.GetEachGroupInfoRes;
import com.swith.api.groupinfo.dto.GetHomeGroupInfoRes;
import com.swith.api.groupinfo.dto.PostGroupInfoReq;
import com.swith.api.groupinfo.dto.PostGroupInfoRes;
import com.swith.api.groupinfo.dto.GetGroupInfoSearchReq;
import com.swith.api.groupinfo.dto.GetGroupInfoSearchRes;
import com.swith.external.firebase.FirebaseCloudMessageService;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    private final ApplicationRepository applicationRepository;
    private final FirebaseCloudMessageService fcmService;

    @Autowired
    public GroupInfoService(GroupInfoRepository groupInfoRepository, RegisterRepository registerRepository, RegisterService registerService,
                            InterestRepository interestRepository, AnnouncementRepository announcementRepository, ApplicationRepository applicationRepository,
                            SessionRepository sessionRepository, AttendanceRepository attendanceRepository, UserRepository userRepository,
                            FirebaseCloudMessageService fcmService) {
        this.groupInfoRepository = groupInfoRepository;
        this.registerRepository = registerRepository;
        this.interestRepository = interestRepository;
        this.announcementRepository = announcementRepository;
        this.sessionRepository = sessionRepository;
        this.attendanceRepository = attendanceRepository;
        this.userRepository = userRepository;
        this.applicationRepository = applicationRepository;
        this.fcmService = fcmService;
    }

    public List<GetHomeGroupInfoRes> loadHomeData(Long userIdx) throws BaseException {
        List<GroupInfo> groupInfos = registerRepository.findGroupInfoByUserIdx(userIdx);
        if (groupInfos.isEmpty())
            throw new BaseException(ErrorCode.NO_REGISTRATION_INFO);

        List<GetHomeGroupInfoRes> getHomeGroupInfoResList = new ArrayList<>();
        for (GroupInfo groupInfo : groupInfos) {
            //가장 최근에 업데이트된 공지 불러오기
            String announcementContent;
            List<Announcement> announcements = announcementRepository
                    .findByGroupInfo_GroupIdxOrderByModifiedAtDesc(groupInfo.getGroupIdx());
            Announcement announcement;
            if(announcements.isEmpty()) {
                announcement = Announcement
                        .builder()
                        .announcementContent("공지 사항이 없습니다.")
                        .build();
            } else {
                announcement = announcements.get(0);
            }


            announcementContent = announcement.getAnnouncementContent();
            //fetch join
            //근 시일내에 가장 빠르게 예정에 있는 회차 정보 불러오기
            Session session = sessionRepository
                    .findFirstByGroupInfo_GroupIdxAndSessionStartAfterAndStatusEqualsOrderBySessionNum
                            (groupInfo.getGroupIdx(), LocalDateTime.now(), 0)
                    .orElseGet(() -> Session
                            .builder()
                            .sessionContent("세션이 없습니다.")
                            .build());

            //해당 그룹에서 ( 쿼리 다시 짜기 )
            List<Attendance> attendanceList = attendanceRepository
                    .findByGroupIdxAndUserIdxAndStatusIsNot(groupInfo.getGroupIdx(), userIdx, (Integer) 0);
            Integer attendanceRate;


            int attendanceNum = 0;
            for (Attendance attendance : attendanceList) {
                if (attendance.getStatus().equals(1)) {
                    attendanceNum += 1;
                }
            }
            if (attendanceList.isEmpty())
                attendanceRate = null;
            else
                attendanceRate = attendanceNum * 100 / attendanceList.size();

            GetHomeGroupInfoRes getHomeGroupInfoRes = GetHomeGroupInfoRes.builder()
                    .groupIdx(groupInfo.getGroupIdx())
                    .title(groupInfo.getTitle())
                    .memberLimit(groupInfo.getMemberLimit())
                    .regionIdx1(groupInfo.getRegionIdx1())
                    .regionIdx2(groupInfo.getRegionIdx2())
                    .groupImgUrl(groupInfo.getGroupImgUrl())
                    .online(groupInfo.getOnline())
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

    public PostGroupInfoRes create(PostGroupInfoReq request) throws BaseException {
        System.out.println(request.toString());
        System.out.println(request.getGroupImgUrl());
        System.out.println("으악");
        PostGroupInfoReq body = request;

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
        //풀그 추가 가입 정보 생성
//        Register register = Register.builder()
//                .groupInfo(groupInfo)
//                .user(User.builder()
//                        .userIdx(request.getAdminIdx())
//                        .build())
//                .status(0)
//                .build();
//        registerRepository.save(register);


        GroupInfo savedgroupInfo = groupInfoRepository.save(groupInfo);
        Register register = Register.builder()
                .user(userRepository.getOne(savedgroupInfo.getUser().getUserIdx()))
                .groupInfo(groupInfoRepository.getOne(savedgroupInfo.getGroupIdx()))
                .status(0)//Register status 0 = 승인
                .build();
        Register saved = registerRepository.save(register);
        if(saved.getGroupInfo().getGroupIdx() != savedgroupInfo.getGroupIdx()) {
            throw new BaseException(ErrorCode.FAIL_REGISTER_SAVE);
        }


        long groupIdx = savedgroupInfo.getGroupIdx();
        return new PostGroupInfoRes(groupIdx);



    }

    public Slice<GetGroupInfoSearchRes> searchGroup(GetGroupInfoSearchReq getGroupInfoSearchReq, Pageable pageable){

        System.out.println("서비스 진입");
        return groupInfoRepository.searchGroupInfo(getGroupInfoSearchReq,pageable);


    }

    public JPAQuery<Integer> searchtestGroup(GetGroupInfoSearchReq getGroupInfoSearchReq, Pageable pageable) {
        return groupInfoRepository.searchtestGroup(getGroupInfoSearchReq, pageable);
    }

    public GetEachGroupInfoRes selectEachGroupInfo(Long groupIdx){
        GroupInfo groupInfo = groupInfoRepository.findByGroupIdx(groupIdx);
        System.out.println(groupInfo.toString());
        System.out.println(groupIdx);
        Long NumOfApplicants = 0L;
        NumOfApplicants = applicationRepository.findNumOfApplicants(groupIdx);
        System.out.println(">>>"+NumOfApplicants);
        GetEachGroupInfoRes data = GetEachGroupInfoRes.builder()
                .adminIdx(groupInfo.getUser().getUserIdx())
                .groupImgUrl(groupInfo.getGroupImgUrl())
                .title(groupInfo.getTitle())
                .meet(groupInfo.getMeet())
                .frequency(groupInfo.getFrequency())
                .periods(groupInfo.getPeriods())
                .online(groupInfo.getOnline())
                .regionIdx1(groupInfo.getRegionIdx1())
                .regionIdx2(groupInfo.getRegionIdx2())
                .interest(groupInfo.getInterest().getInterestIdx())
                .topic(groupInfo.getTopic())
                .memberLimit(groupInfo.getMemberLimit())
                .NumOfApplicants(NumOfApplicants)
                .applicationMethod(groupInfo.getApplicationMethod())
                .recruitmentEndDate(groupInfo.getRecruitmentEndDate())
                .groupStart(groupInfo.getGroupStart())
                .groupEnd(groupInfo.getGroupEnd())
                .attendanceValidTime(groupInfo.getAttendanceValidTime())
                .groupContent(groupInfo.getGroupContent())
                .build();

        System.out.println("방법 >>>>>>" + data.getApplicationMethod());
        System.out.println("interest >>>" + data.getInterest().toString());


        return data;



    }

    public boolean existGroupIdx(Long groupIdx){
        //boolean check = groupInfoRepository.existsById(groupIdx);
        //상태 확인 코드 추가해야됨.
        try{
            Integer check = groupInfoRepository.findStatusOfGroupInfo(groupIdx);
            // 상태가 0(진행예정)또는 1(진행중) 이면 존재 ->true
            if(check == 0 || check == 1) return true;
            //상태가 2(종료)면 존재하지 않음 -> false
            else if(check == 2) return false;
        }catch (Exception e){
            System.out.println("서버 error");
            return false;
        }
        //이상한 값...
        return false;


    }

    public Integer statusOfGroupInfo(Long groupIdx){

        Integer status = groupInfoRepository.findstatusOfGroupInfo(groupIdx);

        return status;

    }

    public boolean IsAdmin(Long groupIdx, Long adminIdx){
        Long FoundAdminIdx = groupInfoRepository.findAdminIdxBy(groupIdx);
        return FoundAdminIdx == adminIdx ;
    }


    public Long ModifyGroupInformation(Long groupIdx, PatchGroupInfoReq request){

        if(!existGroupIdx(groupIdx)){ //존재하지 않음.
            return -1L;
        }
        //존재함
        GroupInfo groupInfo = groupInfoRepository.findById(groupIdx).orElseThrow(
                () -> new IllegalArgumentException(String.valueOf(ErrorCode.FAIL_LOAD_GROUPINFO))
        );

        Long ReqgroupIdx = groupInfo.getGroupIdx();
        if(ReqgroupIdx != groupIdx) return -2L;//잘못된 값 읽음

        groupInfo.setGroupImgUrl(request.getGroupImgUrl());
        groupInfo.setTitle(request.getTitle());
        groupInfo.setMeet(request.getMeet());
        groupInfo.setFrequency(request.getFrequency());
        groupInfo.setPeriods(request.getPeriods());
        groupInfo.setOnline(request.getOnline());
        groupInfo.setRegionIdx1(request.getRegionIdx1());
        groupInfo.setRegionIdx2(request.getRegionIdx2());
        Integer originInterest = groupInfo.getInterest().getInterestIdx();
        if(request.getInterest() != originInterest){ //다른 경우만 바꾸겠다.
            Interest ReqInterest = interestRepository.getById(request.getInterest());
            groupInfo.setInterest(ReqInterest);
        }
        groupInfo.setTopic(request.getTopic());
        groupInfo.setMemberLimit(request.getMemberLimit());
        groupInfo.setApplicationMethod(request.getApplicationMethod());
        groupInfo.setRecruitmentEndDate(request.getRecruitmentEndDate());
        groupInfo.setGroupStart(request.getGroupStart());
        groupInfo.setGroupEnd(request.getGroupEnd());
        groupInfo.setAttendanceValidTime(request.getAttendanceValidTime());
        groupInfo.setGroupContent(request.getGroupContent());



        GroupInfo save = groupInfoRepository.save(groupInfo);
        System.out.println(">>>>>>>>>>>>"+save.getGroupIdx().toString());
        return save.getGroupIdx();


    }

    public Long EndGroup(Long groupIdx,Long adminIdx) throws IOException {

        if(!existGroupIdx(groupIdx)){ //존재하지 않음.
            return -1L;
        }

        //Integer status = groupInfoRepository.findStatusOfGroupInfo(groupIdx);
        groupInfoRepository.changeGroupInfoStatusEnd(2,groupIdx,adminIdx);

        GroupInfo check = groupInfoRepository.findByGroupIdx(groupIdx);
        if(check.getStatus() == 2){ //종료
            // 종료 알림을 받을 유저 list
            ArrayList<Long> pushEndAlramToUsers =registerRepository.findUserByGroup2(groupIdx);
//            ArrayList<Long> pushEndAlramToUsers = groupInfoRepository.findUsersInGroup(groupIdx,1);
            System.out.println(">>>>>>> user : " +pushEndAlramToUsers);
            ArrayList<String> pushUserToken = groupInfoRepository.findUserToken(pushEndAlramToUsers);
            System.out.println(">>>>> token : "+ pushUserToken);
            //List<User> users = registerRepository.findRegisterUser(groupIdx);
            // 종료 groupIdx,종료 group title, 종료 알림 내용, 종료 날짜
            String title = check.getTitle();
            String phrases = "스터디가 종료되었습니다!";
            //LocalDateTime now = LocalDateTime.now();
            String content =  phrases + "//" + groupIdx + "//" + "스터디종료" ;
            // 보냄
            for (String token : pushUserToken) {
                System.out.println("가즈아!");
                fcmService.sendMessageTo(token,title,content);
            }

            return check.getGroupIdx();
        }


        return -2L;


    }



    public GroupInfo findGroup(Long groupIdx){
        return groupInfoRepository.findByGroupIdx(groupIdx);
    }











}
