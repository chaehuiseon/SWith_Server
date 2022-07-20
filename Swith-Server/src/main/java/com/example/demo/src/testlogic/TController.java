package com.example.demo.src.testlogic;


import com.example.demo.src.config.BaseResponse;
import com.example.demo.src.entity.GroupInfo;
import com.example.demo.src.entity.Notification;
import com.example.demo.src.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.swing.*;
import java.util.List;

@RestController
public class TController {

    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final InterestRepository interestRepository;
    @Autowired
    private final NotificationRepository notificationRepository;


    public TController(UserRepository userRepository, InterestRepository interestRepository, NotificationRepository notificationRepository) {
        this.userRepository = userRepository;
        this.interestRepository = interestRepository;
        this.notificationRepository = notificationRepository;
    }

    @ResponseBody
    @PostMapping("/test/user")
    public BaseResponse<String> testPostUser(@RequestBody PostUserReq postUserReq){

        System.out.println(postUserReq.toString());
        User user = User.builder().email(postUserReq.getEmail())
                .password(postUserReq.getPassword())
                .interest1(interestRepository.getOne(postUserReq.getInterest1()))
                .interest2(interestRepository.getOne(postUserReq.getInterest2()))
                .introduction(postUserReq.getIntroduction())
                .profileImgUrl(postUserReq.getProfileImgUrl())
                .averageStar(postUserReq.getAverageStar())
                .build();
        User savedUser =userRepository.save(user);
        System.out.println(savedUser.toString());

        return new BaseResponse<>("확인됨");


    }
    @ResponseBody //notification 생성
    @PostMapping("/test/post/notification")
    //public BaseResponse<String> testGetNotification(@RequestBody GetNotiReq)
    public BaseResponse<String> testPostNotification(@RequestBody PostNotiReq postNotiReq){
        System.out.println(postNotiReq.toString());
        Notification noti = Notification.builder().user(userRepository.getOne(postNotiReq.getUserIdx()))
                .notificationContent(postNotiReq.getNotificationContent()).build();

        Notification savedNoti = notificationRepository.save(noti);
        System.out.println(savedNoti.toString());

        return new BaseResponse<>("noti 저장완료");


    }

    @ResponseBody // 연결되어 있는거 전부 다 가지고옴
    @GetMapping("/test/get/notification")
    //public BaseResponse<String> testGetNotification(@RequestBody GetNotiReq)
    public  List<Notification> testGetNotification(@RequestParam Long userIdx){
        return notificationRepository.findAllByUser(userIdx);

    }

    @ResponseBody //notification만 가지고옴
    @GetMapping("/test/get2/notification")
    public List<String> test2GetNotification(@RequestParam Long userIdx){
        return notificationRepository.findAllByUser2(userIdx);
    }

//    @ResponseBody
//    @PostMapping("/test/post/groupinfo")
//    public String createGroupInfo(@RequestBody PostGroupReq postGroupReq){
//        System.out.println(postGroupReq.toString());
//        GroupInfo gi = GroupInfo.builder().user(userRepository.getOne(postGroupReq.getAdminIx()))
//                .groupImgUrl(postGroupReq.getGroupImgUrl())
//                .title(postGroupReq.getTitle())
//                .meettime(postGroupReq.getMeettime())
//                .frequency(postGroupReq.getFrequency())
//                .periods(postGroupReq.getPeriods())
//                .online(postGroupReq.getOnline())
//                .regionIdx1(postGroupReq.getRegionIdx1())
//                .regionIdx2(postGroupReq.getRegionIdx2())
//                .interest(interestRepository.getOne())
//
//        return postGroupReq.toString();
//    }




}
