package com.swith.domain.application.service;


import com.swith.api.application.dto.*;
import com.swith.api.common.dto.BaseResponse;
import com.swith.domain.register.service.RegisterService;
import com.swith.global.error.exception.BaseException;
import com.swith.global.error.BaseResponseStatus;
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

@Transactional
@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final GroupInfoRepository groupInfoRepository;
    private final UserRepository userRepository;
    private final RegisterRepository registerRepository;
    private final RegisterService registerService;

    @Autowired
    public ApplicationService(ApplicationRepository applicationRepository,
                              GroupInfoRepository groupInfoRepository,
                              UserRepository userRepository,
                                RegisterRepository registerRepository,
                              RegisterService registerService) {
        this.applicationRepository = applicationRepository;
        this.groupInfoRepository = groupInfoRepository;
        this.userRepository = userRepository;
        this.registerRepository = registerRepository;
        this.registerService = registerService;

    }

    public Integer getMemberLimit(Long groupIdx){
        Integer limit = groupInfoRepository.findMemberLimitBy(groupIdx);
        System.out.println(limit);
        return limit;
    }

    public Long findNumOfApplicants(Long groupIdx){

        Long NumOfApplicants = applicationRepository.findNumOfApplicants(groupIdx);

        return NumOfApplicants;
    }

    public Long Apply(Long groupIdx,Integer applicationMethod, PostApplicationReq postApplicationReq) throws BaseException {

        if(applicationMethod == 0){//0: 선착순
            Integer status = 1 ;//선착순이므로 바로 승인.

            Application data = Application.builder()
                    .user(userRepository.getOne(postApplicationReq.getUserIdx()))
                    .groupInfo(groupInfoRepository.getById(groupIdx))
                    .status(status)
                    .build();

            Application savedInfo = applicationRepository.save(data);
            System.out.println(savedInfo.toString());
            Long applicationIdx = savedInfo.getApplicationIdx();

            //application 가입 승인 -> Register 등록
            if ((savedInfo.getGroupInfo().getGroupIdx() == groupIdx) && (savedInfo.getStatus() == 1)) {
                try{
                    // toentity + 더티체크 적용 -> 봐야 앎.
                    registerService.RegisterUserInGroup(savedInfo);
                }catch (BaseException exception){
                    throw new BaseException(BaseResponseStatus.FAIL_REGISTER_SAVE);
                }
            }

            return applicationIdx;

        }else if(applicationMethod == 1){ // 1 : 지원
            Integer status = 0 ; //지원이기 때문에 승인 대기 상태.

            Application data = Application.builder()
                    .user(userRepository.getOne(postApplicationReq.getUserIdx()))
                    .groupInfo(groupInfoRepository.getById(groupIdx))
                    .status(status)
                    .applicationContent(postApplicationReq.getApplicationContent())
                    .build();

            Application savedInfo = applicationRepository.save(data);
            System.out.println(savedInfo.toString());
            Long applicationIdx = savedInfo.getApplicationIdx();

            return applicationIdx;
        }

        return null;



    }


    //지원 목록 가져오기.
    public List<GetApplicationManageRes> getApplicationList(Long groupIdx,Integer status){
        //IsAdmin 추가 해야하나


        //domain 서비스 분리 대상!!!!!!!!!!!!!
        List<Application> applications =  applicationRepository.getApplicationListBy(groupIdx,status);

        //dto로 잘 만들어서.. 반환....
        List<GetApplicationManageRes> results = applications.stream()
                .map(a -> GetApplicationManageRes.from(a))
                .collect(Collectors.toList());

        return results;


    }


    //스터지 지원에 대한 승인 또는 반려 상태 변경.
    public PatchApplicationStatusRes changeApplicationStatus(Long groupIdx, Integer status, PatchApplicationStatusReq request)
            throws BaseException {

        Integer check = 0;
        if (status == 0) { //변경전. 지원에 있음.
            Integer req_status = request.getStatusOfApplication(); //요구된 상태
            Long req_applicationIdx = request.getApplicationIdx(); //요구된 idx
            System.out.println("변경 시작 >>"+req_status+req_status);


            //변경
            try {
                //nativequery 수정 해야됨.
                applicationRepository.updateStatusOfApplication(req_status, req_applicationIdx, groupIdx,status);
            } catch (Exception exception) {
                throw new BaseException(BaseResponseStatus.FAIL_CHANGED_STATUS);
            }

            //확인
            Application changed = applicationRepository.findById(req_applicationIdx).get();

            //application 승인이 되었으면, register에 등록해야 됨.
            if ((changed.getApplicationIdx() == req_applicationIdx) &&
                    (changed.getGroupInfo().getGroupIdx() == groupIdx) && (req_status == changed.getStatus())) {

                if(changed.getStatus() == 1 ){  //Application 가입 승인 -> Register에 등록.
                    try{
                        //아래 엔티티 변환 + 더티 체킹 변환코드 많음 테스트 해봐야 됨.
                        registerService.RegisterUserInGroup(changed);
                    }catch (BaseException exception){
                        throw new BaseException(BaseResponseStatus.FAIL_REGISTER_SAVE);
                    }



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
////                        throw new BaseException(BaseResponseStatus.FAIL_REGISER);
////                    }
//
//                    boolean registerCheck = CheckRegisterStatus(changed,saved,0);
//                    if(!registerCheck) throw new BaseException(BaseResponseStatus.FAIL_REGISTER_SAVE);

                }

                //전송
//                PatchApplicationStatusRes results = PatchApplicationStatusRes.builder()
//                        .applicationIdx(changed.getApplicationIdx())
//                        .status(changed.getStatus())
//                        .build();
                System.out.println("변경확인되었습니다.");

                return PatchApplicationStatusRes.of(changed);
            }

        }else {
            throw new BaseException(BaseResponseStatus.DO_NOT_EXECUTE_CHANGE);
        }

        return null;


    }



    //Group에서 추방
    public Long ExpelUserFromGroup(Integer userstatus, Long groupIdx, PatchExpelUserReq patchExpelUserReq) throws BaseException {

        // 예전코드
        if(!(userstatus == 1)){ //가입 승인이 된 유저만 대상으로 추방을 할 수 있음.
            throw new BaseException(BaseResponseStatus.INVALID_STATUS);
        }

        Long req_applicationIdx = patchExpelUserReq.getApplicationIdx();

        try {
            applicationRepository.updateStatusOfApplication(3, req_applicationIdx, groupIdx,1);
        } catch (Exception exception) {
            throw new BaseException(BaseResponseStatus.FAIL_CHANGED_STATUS);
        }

        Application changed = applicationRepository.findById(req_applicationIdx).get();
        if ((changed.getApplicationIdx() == req_applicationIdx) &&
                (changed.getGroupInfo().getGroupIdx() == groupIdx) && (3 == changed.getStatus())) {
            System.out.println("추방확인 >> "+ changed.getApplicationIdx() );
            return changed.getApplicationIdx();
        }

        return -3L;


//        //새로 짠 코드 -> 아직 테스트 안했음..
//        Application application = applicationRepository.findById(patchExpelUserReq.getApplicationIdx()).orElseThrow(
//                () -> new IllegalArgumentException(String.valueOf(BaseResponseStatus.NOT_EXIST_USER))
//        );
//
//        if ( groupInfoRepository.findstatusOfGroupInfo(groupIdx) != 1 ){//진행중인 스터디에 한해서만 변경이 가능함..
//
//        }
//
//        application.changeStatus(3);












    }

    public List<GetApplicationRes> getUserApplication(Long userIdx) throws BaseException {
        List<Application> applicationList = applicationRepository.findByUserWithGroup(userIdx);
        if(applicationList.isEmpty())
            throw new BaseException(BaseResponseStatus.NO_APPLICATION_INFO);

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

    public Long findAdminIdx(Long groupIdx){
        return groupInfoRepository.findAdminIdxBy(groupIdx);
    }




}
