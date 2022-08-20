package com.example.demo.src.service;


import com.example.demo.config.BaseResponse;
import com.example.demo.src.dto.request.PostApplicationReq;
import com.example.demo.src.dto.response.GetApplicationManageRes;
import com.example.demo.src.entity.Application;
import com.example.demo.src.repository.ApplicationRepository;
import com.example.demo.src.repository.GroupInfoRepository;
import com.example.demo.src.repository.UserRepository;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Transactional
@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final GroupInfoRepository groupInfoRepository;
    private final UserRepository userRepository;

    @Autowired
    public ApplicationService(ApplicationRepository applicationRepository,
                              GroupInfoRepository groupInfoRepository,
                              UserRepository userRepository) {
        this.applicationRepository = applicationRepository;
        this.groupInfoRepository = groupInfoRepository;
        this.userRepository = userRepository;
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

    public Long Apply(Long groupIdx,Integer applicationMethod, PostApplicationReq postApplicationReq){

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


    //public List<GetApplicationManageRes> getApplicationList(Long groupIdx)


}
