package com.swith.api.application.service;


import com.swith.api.application.dto.*;
import com.swith.domain.application.service.ApplicationService;
import com.swith.domain.groupinfo.service.GroupInfoService;
import com.swith.api.register.service.RegisterApiService;
import com.swith.domain.user.entity.User;
import com.swith.domain.user.service.UserService;
import com.swith.global.error.ErrorCode;
import com.swith.global.error.exception.BaseException;
import com.swith.domain.application.entity.Application;
import com.swith.domain.groupinfo.entity.GroupInfo;
import com.swith.domain.application.repository.ApplicationRepository;
import com.swith.domain.groupinfo.repository.GroupInfoRepository;
import com.swith.domain.user.repository.UserRepository;
import com.swith.domain.register.repository.RegisterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Transactional(readOnly = true)
@Service
public class ApplicationApiService {

    private final ApplicationRepository applicationRepository;
    private final GroupInfoRepository groupInfoRepository;
    private final UserRepository userRepository;
    private final RegisterRepository registerRepository;
    private final RegisterApiService registerApiService;

    private final GroupInfoService groupInfoService;
    private final ApplicationService applicationService;
    private final UserService userService;

    @Autowired
    public ApplicationApiService(ApplicationRepository applicationRepository,
                                 GroupInfoRepository groupInfoRepository,
                                 UserRepository userRepository,
                                 RegisterRepository registerRepository,
                                 RegisterApiService registerApiService,
                                 GroupInfoService groupInfoService,
                                 ApplicationService applicationService, UserService userService) {
        this.applicationRepository = applicationRepository;
        this.groupInfoRepository = groupInfoRepository;
        this.userRepository = userRepository;
        this.registerRepository = registerRepository;
        this.registerApiService = registerApiService;
        this.groupInfoService = groupInfoService;
        this.applicationService = applicationService;

        this.userService = userService;
    }


    //중복지원자 확인.
    public void AlreadyInGroup(Long groupIdx, Long userIdx){
        Application a = applicationRepository.findByGroupIdxAndUserIdx(groupIdx,userIdx);
        if( a != null && userIdx.equals(a.getUser().getUserIdx()) && groupIdx.equals(a.getGroupInfo().getGroupIdx())) {
            throw new BaseException(ErrorCode.ALREADY_APPLICATION);
        }

    }


    //신청 인원이 다 찾는지 확인.
    public void CheckFULL(Long groupIdx){
        Integer limit = getMemberLimit(groupIdx);
        Long NumOfApplicants = findNumOfApplicants(groupIdx);
        if(limit.equals(NumOfApplicants.intValue())){//신청인원이 다 채워져서  신청 불가.
            throw new BaseException(ErrorCode.FULL_NUM_OF_Applicants);
        }

    }

    public Integer getMemberLimit(Long groupIdx){
        Integer limit = groupInfoRepository.findMemberLimitBy(groupIdx);
        return limit;
    }

    public Long findNumOfApplicants(Long groupIdx){

        Long NumOfApplicants = applicationRepository.findNumOfApplicants(groupIdx);
        return NumOfApplicants;
    }



    @Transactional
    public Long Apply(Long groupIdx, PostApplicationReq postApplicationReq) {


        // groupInfo 존재 하는지..
        GroupInfo groupInfo = groupInfoService.getOneGroupInfo(groupIdx);
        if(groupInfo.getStatus() != 0 ){//스터디 진행 예정인 경우만 신청을 할 수 있다.
            throw new BaseException(ErrorCode.IS_NOT_THE_APPlICATION_PERIOD);
        }
        //존재하는 회원인지.. jwt 검사할때 사용할 듯.
        User user = userService.getOneUser(postApplicationReq.getUserIdx());
        userService.isActiveUser(user);


        //중복 지원자인지 확인. -> 테스트 안한 상태.
        AlreadyInGroup(groupIdx,postApplicationReq.getUserIdx());

        // 가입 신청 인원이 다 찾는지 확인.
        CheckFULL(groupIdx);

        // 스터디 가입 신청자가 방장이면 가입 할 필요가 없음.
        groupInfoService.CheckIsAdmin(groupIdx, postApplicationReq.getUserIdx());

        //스터디 개설할 때, 입력한 정보에서 지원 방식( 선착순 or 지원 ) 가지고 온다.
        Integer applicationMethod = groupInfo.getApplicationMethod();



        if(applicationMethod == 0){//0: 선착순
            Integer status = 1 ;//선착순이므로 바로 승인.

            Application data = Application.builder()
                    .user(user)
                    .groupInfo(groupInfo)
                    .status(status)
                    .build();


            Application savedInfo = applicationService.save(data);

            Long applicationIdx = savedInfo.getApplicationIdx();

            if ((!groupIdx.equals(savedInfo.getGroupInfo().getGroupIdx())) || (!savedInfo.getStatus().equals(1))
                            || (!postApplicationReq.getUserIdx().equals(savedInfo.getUser().getUserIdx()))) {
                throw new BaseException(ErrorCode.FAIL_SAVED_APPLICATION);

            }

            //application 가입 승인 -> Register 등록
            registerApiService.RegisterUserInGroup(savedInfo);

            return applicationIdx;

        }else if(applicationMethod == 1){ // 1 : 지원
            Integer status = 0 ; //지원이기 때문에 승인 대기 상태.

            Application data = Application.builder()
                    .user(user)
                    .groupInfo(groupInfo)
                    .status(status)
                    .applicationContent(postApplicationReq.getApplicationContent())
                    .build();

            Application savedInfo = applicationService.save(data);
            Long applicationIdx = savedInfo.getApplicationIdx();

            return applicationIdx;
        }else{
            //지원 방식에 대한 잘못된 요청.
            throw new BaseException(ErrorCode.BADREQUEST);
        }



    }


    //지원 목록 가져오기.
    public List<GetApplicationManageRes> getApplicationList(Long groupIdx,Integer status){

        if(!(status == 0 || status == 1)){//승인대기(0)거나 승인(1) 상태가 아닌 목록의 요청은 잘못된 요청.
            throw new BaseException(ErrorCode.BADREQUEST);
        }

        //가지고 온다..
        List<Application> applications =  applicationService.getApplicationListByStatus(groupIdx,status);

        // 가입 신청자 또는 가입 승인된 사람이 없으면, null(isEmpty)상태 일 수 있기 때문에 에러는 아님..
        //성공으로 예외를,,, 해야하나?... stream은 비용이 비싼데..

        //dto로 잘 만들어서.. 반환....
        List<GetApplicationManageRes> results = applications.stream()
                .map(a -> GetApplicationManageRes.from(a))
                .collect(Collectors.toList());

        return results;


    }

    public void validationStatus(PatchApplicationStatusReq request, Integer status ){
        // 올바른 요청이 맞는지에 대한 검사
        Integer ReqStatus = request.getStatusOfApplication();
        if(!(ReqStatus == 1 || ReqStatus == 2 )){//요청이 승인(1) 또는 반려(2)가 아니면 잘못된 값을 받은 것.
            throw new BaseException(ErrorCode.INVALID_STATUS);
        }

        Integer findStatus = applicationService.getApplicationStatus(request.getApplicationIdx());
        if(findStatus == null ){ //지원자를 찾을 수 없음.
            throw new BaseException(ErrorCode.NO_APPLICATION_INFO);
        }
        //이미 바뀐적이 있거나 요청을 잘못한 경우.
        if(ReqStatus.equals(findStatus)){
            throw new BaseException(ErrorCode.DO_NOT_EXECUTE_CHANGE);
        }


    }


    //스터지 지원에 대한 승인 또는 반려 상태 변경.
    @Transactional
    public PatchApplicationStatusRes changeApplicationStatus(Long groupIdx, Integer status, PatchApplicationStatusReq request) {





        Integer check = 0;
        if (status == 0) { //변경전. 지원에 있음.
            Integer req_status = request.getStatusOfApplication(); //요구된 상태
            Long req_applicationIdx = request.getApplicationIdx(); //요구된 idx
            System.out.println("변경 시작 >>"+req_status);


            //변경
            // -> clearAutomatically = true 를 했기 때문에, update 쿼리 실행후, select쿼리가 한번 더 나감
            // 벌크 연산은 1차 캐시를 포함한  1차 캐시를 포함한 영속성 컨텍스트를 무시하고 바로 Query를 실행하기 때문에
            // 영속성 컨텍스트는 데이터 변경을 알 수가 없음. -> 그럼, 뒤에서 조회를 하면 변경 내용 모르고 1차 캐시에서 조회 -> 불일치
            // 이런 이유로.. clearAutomatically = true 해서 영속성 컨텍스트를 지움.
            // 조회를 실행하면 1차캐시에 해당 엔티티가 존재하지 않기 때문에 DB 조회 쿼리를 실행하게 됩니다.
            applicationRepository.updateStatusOfApplication(req_status, req_applicationIdx, groupIdx,status);


            //확인
            //아까, 위에서 말한 조회가 여기임.
            Application changed = applicationService.getOneApplication(req_applicationIdx);

            //application 승인이 되었으면, register에 등록해야 됨.
            if ((req_applicationIdx.equals(changed.getApplicationIdx())) &&
                    (groupIdx.equals(changed.getGroupInfo().getGroupIdx())) && (req_status.equals(changed.getStatus()))) {

                if(changed.getStatus() == 1 ){  //Application 가입 승인 -> Register에 등록.
                    registerApiService.RegisterUserInGroup(changed);




                    //                    Register register = Register.builder()
//                            .user(userRepository.getOne(changed.getUser().getUserIdx()))
//                            .groupInfo(groupInfoRepository.getOne(changed.getGroupInfo().getGroupIdx()))
//                            .status(0)//Register status 0 = 승인
//                            .build();
//                    Register saved = registerRepository.save(register);
//                    //등록이 잘 되었는지 확인.
////                    System.out.println(">>>>"+saved.getUser().getStatus()+
////                            saved.getUser().getUserIdx()
////                            +saved.getGroupInfo().getGroupIdx());
////                    if( !((saved.getStatus() == 0 ) && (saved.getUser().getUserIdx() == changed.getUser().getUserIdx() )
////                        && (saved.getGroupInfo().getGroupIdx() == changed.getGroupInfo().getGroupIdx() ) )){
////                        throw new BaseException(ErrorCode.FAIL_REGISER);
////                    }
//
//                    boolean registerCheck = CheckRegisterStatus(changed,saved,0);
//                    if(!registerCheck) throw new BaseException(ErrorCode.FAIL_REGISTER_SAVE);

                }

                //전송
//                PatchApplicationStatusRes results = PatchApplicationStatusRes.builder()
//                        .applicationIdx(changed.getApplicationIdx())
//                        .status(changed.getStatus())
//                        .build();
                System.out.println("변경확인되었습니다.");

                return PatchApplicationStatusRes.of(changed);
            }else {
                throw new BaseException(ErrorCode.DO_NOT_EXECUTE_CHANGE);
            }

        }else {

            throw new BaseException(ErrorCode.RESPONSE_ERROR);
        }


    }



    //Group에서 추방
    @Transactional
    public Long ExpelUserFromGroup( Long groupIdx, PatchExpelUserReq patchExpelUserReq) throws BaseException {

        // 예전코드
//        if(!(userstatus == 1)){ //가입 승인이 된 유저만 대상으로 추방을 할 수 있음.
//            throw new BaseException(ErrorCode.INVALID_STATUS);
//        }
//
//        Long req_applicationIdx = patchExpelUserReq.getApplicationIdx();
//
//        try {
//            applicationRepository.updateStatusOfApplication(3, req_applicationIdx, groupIdx,1);
//        } catch (Exception exception) {
//            throw new BaseException(ErrorCode.FAIL_CHANGED_STATUS);
//        }
//
//        Application changed = applicationRepository.findById(req_applicationIdx).get();
//        if ((changed.getApplicationIdx() == req_applicationIdx) &&
//                (changed.getGroupInfo().getGroupIdx() == groupIdx) && (3 == changed.getStatus())) {
//            System.out.println("추방확인 >> "+ changed.getApplicationIdx() );
//            return changed.getApplicationIdx();
//        }
//
//        return -3L;


        //새로 짠 코드 -> 아직 테스트 안했음..


        //가지고와서
        Application application = applicationService.getOneApplication(patchExpelUserReq.getApplicationIdx());


        //가입 승인이 된 유저만 대상으로 추방을 할 수 있음.
        Integer status = application.getStatus();
        if(!(status.equals(1))){
            throw new BaseException(ErrorCode.IS_NOT_THE_MEMBER);
        }

        // 현재, 승인상태면 바로 추방 가능하게 만들어놨다함. (즉, 현재 요구사항에선 스터디 진행중인거 고려할 필요가 없음...)
//        if ( groupInfoRepository.findstatusOfGroupInfo(groupIdx) != 1 ){//진행중인 스터디에 한해서만 변경이 가능함..
//
//
//        }


        //상태변경 - 추방
        application.changeStatus(3);


        return application.getApplicationIdx();
        // +) Register에서 상태변경(추방)..해야하는거 짜야 됨 .............



    }

    public List<GetApplicationRes> getUserApplication(Long userIdx) throws BaseException {
        List<Application> applicationList = applicationRepository.findByUserWithGroup(userIdx);
        if(applicationList.isEmpty())
            throw new BaseException(ErrorCode.NO_APPLICATION_INFO);

        List<GetApplicationRes> getApplicationResList = new ArrayList<>();
        for (Application application : applicationList) {
            GroupInfo groupInfo = application.getGroupInfo();
            GetApplicationRes res = GetApplicationRes.builder()
                    .applicationIdx(application.getApplicationIdx())
                    .applicationContent(application.getApplicationContent())
                    .status(application.getStatus())
                    .createdAt(application.getCreatedAt())
                    .title(groupInfo.getTitle())
                    .regionIdx1(groupInfo.getRegionIdx1())
                    .regionIdx2(groupInfo.getRegionIdx2())
                    .online(groupInfo.getOnline())
                    .build();
            getApplicationResList.add(res);
        }

        return getApplicationResList;
    }




}
