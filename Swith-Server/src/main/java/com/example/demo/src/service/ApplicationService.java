package com.example.demo.src.service;


import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.dto.request.PatchApplicationStatusReq;
import com.example.demo.src.dto.request.PatchExpelUserReq;
import com.example.demo.src.dto.request.PostApplicationReq;
import com.example.demo.src.dto.response.GetApplicationManageRes;
import com.example.demo.src.dto.response.PatchApplicationStatusRes;
import com.example.demo.src.dto.response.getApplicationRes;
import com.example.demo.src.entity.Application;
import com.example.demo.src.entity.GroupInfo;
import com.example.demo.src.entity.Register;
import com.example.demo.src.repository.ApplicationRepository;
import com.example.demo.src.repository.GroupInfoRepository;
import com.example.demo.src.repository.RegisterRepository;
import com.example.demo.src.repository.UserRepository;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

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

    public Long Apply(Long groupIdx,Integer applicationMethod, PostApplicationReq postApplicationReq) throws BaseException{

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
                    boolean registerCheck = registerService.RegisterUserInGroup(savedInfo);
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


    public List<GetApplicationManageRes> getApplicationList(Long groupIdx,Integer status){
        Long adminIdx = groupInfoRepository.findAdminIdxBy(groupIdx);
        List<Application> applications =  applicationRepository.getApplicationListBy(groupIdx,status);
        List<GetApplicationManageRes> results = applications.stream()
                .map(a -> new GetApplicationManageRes(a))
                .collect(Collectors.toList());

        System.out.println(results.toString());

        return results;


    }


    public PatchApplicationStatusRes changeApplicationStatus(Long groupIdx, Integer status, PatchApplicationStatusReq request)
            throws BaseException {
        Integer check = 0;
        if (status == 0) { //변경전. 지원에 있음.
            Integer req_status = request.getStatusOfApplication(); //요구된 상태
            Long req_applicationIdx = request.getApplicationIdx(); //요구된 idx
            System.out.println("변경 시작 >>"+req_status+req_status);
            //변경
            try {
                applicationRepository.updateStatusOfApplication(req_status, req_applicationIdx, groupIdx,status);
            } catch (Exception exception) {
                throw new BaseException(BaseResponseStatus.FAIL_CHANGED_STATUS);
            }

            //확인
            Application changed = applicationRepository.findById(req_applicationIdx).get();
            if ((changed.getApplicationIdx() == req_applicationIdx) &&
                    (changed.getGroupInfo().getGroupIdx() == groupIdx) && (req_status == changed.getStatus())) {
                if(changed.getStatus() == 1 ){  //Application 가입 승인 -> Register에 등록
                    try{
                        boolean registerCheck = registerService.RegisterUserInGroup(changed);
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
                PatchApplicationStatusRes results = PatchApplicationStatusRes.builder()
                        .applicationIdx(changed.getApplicationIdx())
                        .status(changed.getStatus())
                        .build();
                System.out.println("변경확인되었습니다.");

                return results;
            }

        }else {
            throw new BaseException(BaseResponseStatus.DO_NOT_EXECUTE_CHANGE);
        }

        return null;


    }



    //Group에서 추방
    public Long ExpelUserFromGroup(Long groupIdx, PatchExpelUserReq patchExpelUserReq) throws BaseException {

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



    }

    public List<getApplicationRes> getUserApplication(Long userIdx) throws BaseException {
        List<Application> applicationList = applicationRepository.findByUserWithGroup(userIdx);
        if(applicationList.isEmpty())
            throw new BaseException(BaseResponseStatus.NO_APPLICATION_INFO);

        List<getApplicationRes> getApplicationResList = new ArrayList<>();
        for (Application application : applicationList) {
            GroupInfo groupInfo = application.getGroupInfo();
            getApplicationRes res = getApplicationRes.builder()
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
