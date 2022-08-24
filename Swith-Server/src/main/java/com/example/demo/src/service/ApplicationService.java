package com.example.demo.src.service;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.dto.response.getApplicationRes;
import com.example.demo.src.entity.Application;
import com.example.demo.src.entity.GroupInfo;
import com.example.demo.src.repository.ApplicationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ApplicationService {

    private final ApplicationRepository applicationRepository;

    public ApplicationService(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    public List<getApplicationRes> getGroupAttendance(Long userIdx) throws BaseException {
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
}
