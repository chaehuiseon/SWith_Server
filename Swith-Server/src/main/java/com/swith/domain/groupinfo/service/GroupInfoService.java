package com.swith.domain.groupinfo.service;

import com.swith.api.groupinfo.dto.*;
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
import com.swith.global.error.BaseResponseStatus;
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
            throw new BaseException(BaseResponseStatus.NO_REGISTRATION_INFO);

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


    public PostGroupInfoRes create(PostGroupInfoReq request)  {
        System.out.println("스터디 개설 시작");
        //입력 정보를 DB에 저장하기 위해, Entity로 변환한다.
        GroupInfo groupInfo = toEntityForCreating(request);
        //저장
        GroupInfo savedgroupInfo = groupInfoRepository.save(groupInfo);
        //스터디를 개설한 유저를, 개설한 스터디에 등록하기 위해, 입력 정보를 바탕으로 REGISTER 생성.
        Register register = toEnityForCreating(savedgroupInfo);
        //저장
        Register savedregister = registerRepository.save(register);
        //로직이 정상 처리 되었는지 확인.
        // 개설한 스터디 정보(GroupInfo)의 groupIdx 와 스터디 유저 관리(Register)의 groupIdx가 일치하는 가.
        if(savedregister.getGroupInfo().getGroupIdx() != savedgroupInfo.getGroupIdx()) {
            throw new BaseException(BaseResponseStatus.FAIL_REGISTER_SAVE);
        }

        return PostGroupInfoRes.from(savedgroupInfo.getGroupIdx());


    }

    public Slice<GetGroupInfoSearchRes> searchGroup(GetGroupInfoSearchReq getGroupInfoSearchReq, Pageable pageable){

        System.out.println("서비스 진입");
        return groupInfoRepository.searchGroupInfo(getGroupInfoSearchReq,pageable);


    }

    public JPAQuery<Integer> searchtestGroup(GetGroupInfoSearchReq getGroupInfoSearchReq, Pageable pageable) {
        return groupInfoRepository.searchtestGroup(getGroupInfoSearchReq, pageable);
    }

    public GetEachGroupInfoRes selectEachGroupInfo(Long groupIdx){
        //찾고자 하는 groupInfo가 있는지. -> 아래 코드로 대체 되었음. 테스트 아직 안함.
        //GroupInfo groupInfo = groupInfoRepository.findByGroupIdx(groupIdx);

        //존재 하니깐 groupId 를 통해, groupInfo 정보 가지고 온다. 존재에 대한 예외처리를 아래서 하고 있음..
        GroupInfo groupInfo = groupInfoRepository.findById(groupIdx).orElseThrow(
                () -> new IllegalArgumentException(String.valueOf(BaseResponseStatus.FAIL_LOAD_GROUPINFO))
        );


        //스터디 신청 승인된 인원 찾기
        Long NumOfApplicants = 0L;
        NumOfApplicants = applicationRepository.findNumOfApplicants(groupIdx);

        //스터디 정보 반환
//        GetEachGroupInfoRes groupinfoRes = GetEachGroupInfoRes.of(groupInfo, NumOfApplicants);
        return GetEachGroupInfoRes.of(groupInfo, NumOfApplicants);


    }

    //스터디 그룹 존재 여부 체크
    public boolean existGroupIdx(Long groupIdx){
        //boolean check = groupInfoRepository.existsById(groupIdx);
        //상태 확인 코드 추가해야됨.

        Integer check = groupInfoRepository.findStatusOfGroupInfo(groupIdx);
        // 상태가 0(진행예정)또는 1(진행중) 이면 존재 ->true
        if(check == 0 || check == 1) return true;
        //상태가 2(종료)면 존재하지 않음 -> false
        else if(check == 2) return false;

        return false;


    }

    public Integer statusOfGroupInfo(Long groupIdx){

        Integer status = groupInfoRepository.findstatusOfGroupInfo(groupIdx);

        return status;

    }


    public void CheckIsAdmin(Long groupIdx, Long adminIdx){
        Long FoundAdminIdx = findAdminIdx(groupIdx);
        if(!adminIdx.equals(FoundAdminIdx)){ // admin이 아니기 때문에, 권한이 없다.
            throw new BaseException(BaseResponseStatus.NO_GROUP_LEADER);

        }
    }


    public Long findAdminIdx(Long groupIdx){
        return groupInfoRepository.findAdminIdxBy(groupIdx);
    }



    //스터디 정보 변경 로직
    @Transactional
    public Long  ModifyGroupInformation(Long groupIdx, PatchGroupInfoReq request){

//        // 변경하려는 그룹 존재해?
//        if(!existGroupIdx(groupIdx)){
//            return -1L;
//        }

        //존재 하니깐 groupId 를 통해, groupInfo 정보 가지고 온다. 존재에 대한 예외처리를 아래서 하고 있음..
        GroupInfo groupInfo = groupInfoRepository.findById(groupIdx).orElseThrow(
                () -> new IllegalArgumentException(String.valueOf(BaseResponseStatus.FAIL_LOAD_GROUPINFO))
        );

        Long check = groupInfo.getGroupIdx();
        //변경 가능한 상태의 group인지...
        if( !(check == 0 || check == 1 )) return -1L;
        //제대로 원하는 값을 가지고 왔는지 확인.
        if(check != groupIdx) return -2L;

        //변경 시작
        groupInfo.setGroupImgUrl(request.getGroupImgUrl());
        groupInfo.setTitle(request.getTitle());
        groupInfo.setMeet(request.getMeet());
        groupInfo.setFrequency(request.getFrequency());
        groupInfo.setPeriods(request.getPeriods());
        groupInfo.setOnline(request.getOnline());
        groupInfo.setRegionIdx1(request.getRegionIdx1());
        groupInfo.setRegionIdx2(request.getRegionIdx2());
        Integer originInterest = groupInfo.getInterest().getInterestIdx();

//        groupInfo.setInterest(ReqInterest);

        if(request.getInterest() != originInterest){ // interest 다른 경우만 바꾸겠다.
            //유효 검사해야함.............
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





        //GroupInfo save = groupInfoRepository.save(groupInfo);
        //System.out.println(">>>>>>>>>>>>"+save.getGroupIdx().toString());


        // 더티 체크 하려고 위에거 일단 삭제 했음. 아직 테스트 안해봄. 에러 해결되면 테스트 해봐야됨
        return groupInfo.getGroupIdx();


    }


    @Transactional
    public changeEndStatus EndGroup(Long groupIdx,Long adminIdx) {

//        if(!existGroupIdx(groupIdx)){ //존재하지 않음.
//            return -1L;
//        }

        //Integer status = groupInfoRepository.findStatusOfGroupInfo(groupIdx);
        //groupInfoRepository.changeGroupInfoStatusEnd(2,groupIdx,adminIdx);

        //새로운 변경 로직... 더티 체킹

        GroupInfo groupInfo = groupInfoRepository.findById(groupIdx).orElseThrow(
                () -> new IllegalArgumentException(String.valueOf(BaseResponseStatus.FAIL_LOAD_GROUPINFO))
        );


        if(groupInfo.getGroupIdx() != 1){ // 진행중인 스터디에 대해 종료해야하는데 아니면..
            throw new BaseException(BaseResponseStatus.BAD_REQUEST_GROUPINFO);
        }

        //종료 상태로 변경..
        groupInfo.changeStatus(2);


        return changeEndStatus.from(groupInfo.getGroupIdx(), groupInfo.getStatus(), groupInfo.getTitle());


    }


    public Long pushEndNotification(changeEndStatus endGroup) throws IOException {


        // 종료 알림을 받을 유저 list
        ArrayList<Long> pushEndAlramToUsers =registerRepository.findUserByGroup2(endGroup.getGroupIdx());
//            ArrayList<Long> pushEndAlramToUsers = groupInfoRepository.findUsersInGroup(groupIdx,1);
        System.out.println(">>>>>>> user : " +pushEndAlramToUsers);
        ArrayList<String> pushUserToken = groupInfoRepository.findUserToken(pushEndAlramToUsers);
        System.out.println(">>>>> token : "+ pushUserToken);
        //List<User> users = registerRepository.findRegisterUser(groupIdx);
        // 종료 groupIdx,종료 group title, 종료 알림 내용, 종료 날짜
        String title = endGroup.getTitle();
        String phrases = "스터디가 종료되었습니다!";
        //LocalDateTime now = LocalDateTime.now();
        String content =  phrases + "//" + endGroup.getGroupIdx() + "//" + "스터디종료" ;
        // 보냄
        for (String token : pushUserToken) {
            fcmService.sendMessageTo(token,title,content);
        }

        return endGroup.getGroupIdx();



    }



    public GroupInfo findGroup(Long groupIdx){
        return groupInfoRepository.findByGroupIdx(groupIdx);
    }



    private GroupInfo toEntityForCreating (PostGroupInfoReq body) {
        return GroupInfo.builder()
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
    }

    private Register toEnityForCreating(GroupInfo savedgroupInfo ){
        return   Register.builder()
                .user(userRepository.getOne(savedgroupInfo.getUser().getUserIdx()))
                .groupInfo(groupInfoRepository.getOne(savedgroupInfo.getGroupIdx()))
                .status(0)//Register status 0 = 승인
                .build();


    }












}
