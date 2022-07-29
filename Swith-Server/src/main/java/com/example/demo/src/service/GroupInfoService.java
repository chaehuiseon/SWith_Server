package com.example.demo.src.service;


import com.example.demo.src.dao.GroupInfoRepository;
import com.example.demo.src.dao.InterestRepository;
import com.example.demo.src.dao.UserRepository;
import com.example.demo.src.dto.PostGroupInfoReq;
import com.example.demo.src.dto.PostGroupInfoRes;
import com.example.demo.src.entity.GroupInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class GroupInfoService {
    @Autowired
    private GroupInfoRepository groupInfoRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private InterestRepository interestRepository;
//
//    public GroupInfoService(GroupInfoRepository groupInfoRepository, UserRepository userRepository, InterestRepository interestRepository) {
//        this.groupInfoRepository = groupInfoRepository;
//        this.userRepository = userRepository;
//        this.interestRepository = interestRepository;
//    }

    public PostGroupInfoRes create(PostGroupInfoReq request){
        System.out.println(request.toString());
        System.out.println("으악");
        PostGroupInfoReq body = request;
        long regionimsi1 = 123;
        long regionimsi2 = 456;
        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분 ss초");
        // 문자열 -> Date
        //LocalDateTime date = LocalDateTime.parse(dateStr, formatter);

        GroupInfo groupInfo = GroupInfo.builder()
                .user(userRepository.getOne(body.getAdminIdx()))
                .groupImgUrl(body.getGroupImgUrl())
                .title(body.getTitle())
                .meet(body.getMeet())
                .frequency(body.getFrequency())
                .periods(body.getPeriods())
                .online(body.getOnline())
                .regionIdx1(body.getRegionIdx1())
                .regionIdx2(body.getRegionIdx2())
                .interest(interestRepository.getOne(body.getInterest()))
                .topic(body.getTopic())
                .memberLimit(body.getMemberLimit())
                .applicationMethod(body.getApplicationMethod())
                .recruitmentEndDate(body.getRecruitmentEndDate())
                .groupStart(body.getGroupStart())
                .groupEnd(body.getGroupEnd())
                .attendanceValidTime(body.getAttendanceValidTime())
                .groupContent(body.getGroupContent())
                .build();

        GroupInfo savedgroupInfo = groupInfoRepository.save(groupInfo);
        long groupIdx = savedgroupInfo.getGroupIdx();
        return new PostGroupInfoRes(groupIdx);



    }
}
