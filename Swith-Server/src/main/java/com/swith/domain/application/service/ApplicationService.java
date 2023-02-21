package com.swith.domain.application.service;


import com.swith.domain.register.service.RegisterService;
import com.swith.global.error.exception.BaseException;
import com.swith.global.error.ErrorCode;
import com.swith.api.application.dto.PatchApplicationStatusReq;
import com.swith.api.application.dto.PatchExpelUserReq;
import com.swith.api.application.dto.PostApplicationReq;
import com.swith.api.application.dto.GetApplicationManageRes;
import com.swith.api.application.dto.PatchApplicationStatusRes;
import com.swith.api.application.dto.GetApplicationRes;
import com.swith.domain.application.entity.Application;
import com.swith.domain.groupinfo.entity.GroupInfo;
import com.swith.domain.application.repository.ApplicationRepository;
import com.swith.global.error.ErrorCode;
import com.swith.global.error.exception.BaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@Service
public class ApplicationService {
    private final ApplicationRepository applicationRepository;

    @Autowired
    public ApplicationService(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    public Application save(Application application){
        return applicationRepository.save(application);
    }


    public Application getOneApplication(Long applicationIdx){
        return applicationRepository.findById(applicationIdx).orElseThrow(
                () -> new BaseException(ErrorCode.FAIL_LOAD_APPLICATION)
        );

    }


    public List<Application> getApplicationListByStatus(Long groupIdx, Integer status ){
        return applicationRepository.getApplicationListBy(groupIdx,status);

    }

    public Integer getApplicationStatus(Long applicationIdx){
        return applicationRepository.findStatusOfApplication(applicationIdx);

    }
}
