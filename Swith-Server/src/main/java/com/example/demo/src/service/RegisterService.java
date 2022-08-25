package com.example.demo.src.service;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.entity.Application;
import com.example.demo.src.entity.Register;
import com.example.demo.src.repository.GroupInfoRepository;
import com.example.demo.src.repository.RegisterRepository;
import com.example.demo.src.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class RegisterService {
    private final RegisterRepository registerRepository;
    private final UserRepository userRepository;
    private final GroupInfoRepository groupInfoRepository;

    @Autowired
    public RegisterService(RegisterRepository registerRepository, UserRepository userRepository,
                           GroupInfoRepository groupInfoRepository) {
        this.registerRepository = registerRepository;
        this.userRepository = userRepository;
        this.groupInfoRepository = groupInfoRepository;
    }

    public List<Register> findRegistrationInfo (Long userIdx) throws BaseException {
        Integer status = 0; //0은 가입 상태
        List<Register> registerList = registerRepository
                .findByUser_UserIdxAndStatusEquals(userIdx, status);
        if(registerList.isEmpty())
            throw  new BaseException(BaseResponseStatus.NO_REGISTRATION_INFO);
        return registerList;
    }

    public boolean RegisterUserInGroup(Application changed) throws BaseException {
        Register register = Register.builder()
                .user(userRepository.getOne(changed.getUser().getUserIdx()))
                .groupInfo(groupInfoRepository.getOne(changed.getGroupInfo().getGroupIdx()))
                .status(0)//Register status 0 = 승인
                .build();
        Register saved = registerRepository.save(register);
        //등록이 잘 되었는지 확인.
        boolean registerCheck = CheckRegisterStatus(changed,saved,0);
        if(!registerCheck) throw new BaseException(BaseResponseStatus.FAIL_REGISTER_SAVE);
        return true;
    }

    public boolean CheckRegisterStatus(Application application, Register register, Integer status) throws BaseException{
        if(! ((register.getStatus() == status ) && (register.getUser().getUserIdx() == application.getUser().getUserIdx() )
                && (register.getGroupInfo().getGroupIdx() == register.getGroupInfo().getGroupIdx() ) ) ){
            return false;
            //throw new BaseException(BaseResponseStatus.FAIL_REGISER);
        }
        return true;
    }






}
