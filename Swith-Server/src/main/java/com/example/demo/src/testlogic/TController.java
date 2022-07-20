package com.example.demo.src.testlogic;


import com.example.demo.src.config.BaseResponse;
import com.example.demo.src.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TController {

    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final InterestRepository interestRepository;

    public TController(UserRepository userRepository, InterestRepository interestRepository) {
        this.userRepository = userRepository;
        this.interestRepository = interestRepository;
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
}
