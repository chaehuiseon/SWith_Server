package com.example.demo.src.service;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.entity.Register;
import com.example.demo.src.repository.RegisterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class RegisterService {
    private final RegisterRepository registerRepository;

    @Autowired
    public RegisterService(RegisterRepository registerRepository) {
        this.registerRepository = registerRepository;
    }

    public List<Register> findRegistrationInfo (Long userIdx) throws BaseException {
        Integer status = 0; //0은 가입 상태
        List<Register> registerList = registerRepository
                .findByUser_UserIdxAndStatusEquals(userIdx, status);
        if(registerList.isEmpty())
            throw  new BaseException(BaseResponseStatus.NO_REGISTRATION_INFO);
        return registerList;
    }
}
