package com.swith.domain.groupinfo.service;


import com.swith.domain.groupinfo.entity.GroupInfo;
import com.swith.domain.groupinfo.repository.GroupInfoRepository;

import com.swith.global.error.exception.BaseException;
import com.swith.domain.interest.entity.Interest;
import com.swith.domain.interest.repository.InterestRepository;
import com.swith.domain.register.entity.Register;
import com.swith.domain.register.repository.RegisterRepository;
import com.swith.domain.register.service.RegisterService;
import com.swith.domain.session.entity.Session;
import com.swith.domain.user.repository.UserRepository;
import com.swith.domain.session.repository.SessionRepository;
import com.swith.global.error.exception.BaseException;
import com.swith.global.error.ErrorCode;
import com.swith.api.groupinfo.dto.PatchGroupInfoReq;
import com.swith.api.groupinfo.dto.GetEachGroupInfoRes;
import com.swith.api.groupinfo.dto.GetHomeGroupInfoRes;
import com.swith.api.groupinfo.dto.PostGroupInfoReq;
import com.swith.api.groupinfo.dto.PostGroupInfoRes;
import com.swith.api.groupinfo.dto.GetGroupInfoSearchReq;
import com.swith.api.groupinfo.dto.GetGroupInfoSearchRes;
import com.swith.external.firebase.FirebaseCloudMessageService;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Transactional
@Service
public class GroupInfoService {

    private final GroupInfoRepository groupInfoRepository;

    @Autowired
    public GroupInfoService(GroupInfoRepository groupInfoRepository){
        this.groupInfoRepository = groupInfoRepository;

    }

    public GroupInfo getOneGroupInfo(Long groupIdx){
        return groupInfoRepository.findById(groupIdx).orElseThrow(
                () -> new BaseException(ErrorCode.FAIL_LOAD_GROUPINFO)

        );


    }

    public void CheckIsAdmin(Long groupIdx, Long adminIdx){
        Long FoundAdminIdx = findAdminIdx(groupIdx);
        System.out.println("찾은거 : " +adminIdx + FoundAdminIdx);
        if(adminIdx.equals(FoundAdminIdx)){ // Admin이면 접근 권한 없다.
            throw new BaseException(ErrorCode.INVAILD_ADMIN_APPLICATION);

        }
    }

    public void CheckIsAdminForAdminToManage(Long groupIdx, Long adminIdx){
        Long FoundAdminIdx = findAdminIdx(groupIdx);
        if(!adminIdx.equals(FoundAdminIdx)){ // admin이 아니기 때문에, 권한이 없다.
            throw new BaseException(ErrorCode.NO_GROUP_LEADER);

        }
    }

    public Long findAdminIdx(Long groupIdx){
        return groupInfoRepository.findAdminIdxBy(groupIdx);
    }



    //스터디 그룹 존재 여부 체크. 존재 여부만 체크하고 정보까지 가지고 올 필요가 없을 때, 사용.
    public void existGroupIdx(Long groupIdx){

        Integer check = groupInfoRepository.findStatusOfGroupInfo(groupIdx);
        // 상태가 0(진행예정)또는 1(진행중) 이면 존재 ->true
        if( !(check == 0 || check == 1)) {
            // 2(종료) 되었거나 다른 문제
            throw new BaseException(ErrorCode.FAIL_LOAD_GROUPINFO);
        }

    }

    //매일 아침 6시 미다 실행
    @Scheduled(cron = "0 0 06 * * *",zone = "Asia/Seoul")
    //@Scheduled(cron = "0 56 04 * * *",zone = "Asia/Seoul") -> 테스트함..
    public void startGroup(){
        //현재 날짜 구해서
        LocalDateTime now =  LocalDateTime.now();

        //그 날에 시작하는 그룹의 그룹 상태를 진행중으로 변경.
        String nows = now.toString().split("T")[0];
        System.out.println(nows);
        groupInfoRepository.updateGroupStatusSetStart(nows);
        System.out.println(nows + " 에 시작하는 스터디 그룹의 상태에 대해 진행중으로 변경 ");

        //push 알림.
    }









}
