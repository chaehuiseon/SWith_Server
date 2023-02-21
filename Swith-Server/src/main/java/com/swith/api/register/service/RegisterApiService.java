package com.swith.api.register.service;

import com.swith.domain.register.service.RegisterService;
import com.swith.global.error.exception.BaseException;
import com.swith.global.error.ErrorCode;
import com.swith.domain.application.entity.Application;
import com.swith.domain.register.entity.Register;
import com.swith.domain.groupinfo.repository.GroupInfoRepository;
import com.swith.domain.user.repository.UserRepository;
import com.swith.domain.register.repository.RegisterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RegisterApiService {
    private final RegisterRepository registerRepository;
    private final UserRepository userRepository;
    private final GroupInfoRepository groupInfoRepository;
    private final RegisterService registerService;

    @Autowired
    public RegisterApiService(RegisterRepository registerRepository, UserRepository userRepository, GroupInfoRepository groupInfoRepository, RegisterService registerService) {
        this.registerRepository = registerRepository;
        this.userRepository = userRepository;
        this.groupInfoRepository = groupInfoRepository;
        this.registerService = registerService;
    }



    public List<Register> findRegistrationInfo (Long userIdx) throws BaseException {
        Integer status = 0; //0은 가입 상태
        List<Register> registerList = registerRepository
                .findByUser_UserIdxAndStatusEquals(userIdx, status);
        if(registerList.isEmpty())
            throw  new BaseException(ErrorCode.NO_REGISTRATION_INFO);
        return registerList;
    }

    @Transactional
    public void RegisterUserInGroup(Application changed)  {
//        Register register = Register.builder()
//                .user(userRepository.getOne(changed.getUser().getUserIdx()))
//                .groupInfo(groupInfoRepository.getOne(changed.getGroupInfo().getGroupIdx()))
//                .status(0)//Register status 0 = 승인
//                .build();


        // 아래 변경 코드임.. 테스트 해봐야 함....
        Register register = Register.toEntity(changed,0); // 승인 : 0
        Register savedregisted = registerService.save(register);
        //등록이 잘 되었는지 확인.
        if ((!changed.getGroupInfo().getGroupIdx().equals(savedregisted.getGroupInfo().getGroupIdx()))
                || (!changed.getUser().getUserIdx().equals(savedregisted.getUser().getUserIdx()))) {
            if(!savedregisted.getStatus().equals(0))
                throw new BaseException(ErrorCode.FAIL_SAVED_APPLICATION);

        }

    }

    public boolean CheckRegisterStatus(Application application, Register register, Integer status) throws BaseException{
        if(! ((register.getStatus() == status ) && (register.getUser().getUserIdx() == application.getUser().getUserIdx() )
                && (register.getGroupInfo().getGroupIdx() == register.getGroupInfo().getGroupIdx() ) ) ){
            return false;
            //throw new BaseException(ErrorCode.FAIL_REGISER);
        }
        return true;
    }









}
