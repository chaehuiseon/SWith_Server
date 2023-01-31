package com.swith.domain.application.service;


import com.swith.domain.application.entity.Application;
import com.swith.domain.application.repository.ApplicationRepository;
import com.swith.global.error.BaseResponseStatus;
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
                () -> new IllegalArgumentException(String.valueOf(BaseResponseStatus.FAIL_LOAD_APPLICATION))
        );

    }


    public List<Application> getApplicationListByStatus(Long groupIdx, Integer status ){
        return applicationRepository.getApplicationListBy(groupIdx,status);

    }
}
