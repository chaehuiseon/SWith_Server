package com.example.demo.src.service;


import com.example.demo.src.dto.request.PostRatingReq;
import com.example.demo.src.dto.request.PostRatingStarReq;
import com.example.demo.src.dto.request.Start;
import com.example.demo.src.dto.response.PostRatingRes;
import com.example.demo.src.entity.Rating;
import com.example.demo.src.entity.Register;
import com.example.demo.src.entity.User;
import com.example.demo.src.repository.GroupInfoRepository;
import com.example.demo.src.repository.RatingRepository;
import com.example.demo.src.repository.RegisterRepository;
import com.example.demo.src.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
public class RatingService {
    private final RatingRepository ratingRepository;
    private final RegisterRepository registerRepository;
    private final UserRepository userRepository;

    @Autowired
    public RatingService( RegisterRepository registerRepository,
                         RatingRepository ratingRepository,
                          UserRepository userRepository) {
        this.registerRepository = registerRepository;
        this.ratingRepository = ratingRepository;
        this.userRepository = userRepository;
    }

    public List<PostRatingRes> RatingUserSinceGroupEnd(Long groupIdx, PostRatingReq postRatingReq) {
        List<Register> find = registerRepository.findUser(groupIdx);
        List<PostRatingRes> results = new ArrayList<PostRatingRes>();
        for (Register r : find) {
            Long userIdx = r.getUser().getUserIdx();
//            System.out.println(userIdx);
//            System.out.println(postRatingReq.getUserIdx());
//            System.out.println(userIdx.equals(postRatingReq.getUserIdx()));
            if(userIdx.equals(postRatingReq.getUserIdx())){
                //System.out.println("무시");
                continue;
            }
            String Nickname = r.getUser().getNickname();
            //System.out.println(">>>>>"+ userIdx + Nickname);
            PostRatingRes result = PostRatingRes.builder()
                    .userIdx(userIdx)
                    .nickname(Nickname)
                    .build();
            results.add(result);

        }
        return results;


    }

    public String RatingStart(PostRatingStarReq postRatingStarReq) {
        Long raterIdx = postRatingStarReq.getRaterIdx();
        List<Start> star = postRatingStarReq.getStart();
        for(Start s : star){
            calculateStar(s.getRateeIdx(), s.getStar());
            Rating r = Rating.builder()
                    .raterIdx(raterIdx)
                    .user(userRepository.getOne(s.getRateeIdx()))
                    .star(s.getStar())
                    .build();
            Rating saved = ratingRepository.save(r);

        }
        return "평가완료";

    }

    // 별점 계산 로직
    public double calculateStar(Long rateeIdx, Integer star){
        User user = userRepository.findByUserIdx(rateeIdx);
        // 스타 계산 후 저장
        double beforeRatedCnt = user.getRatedCnt() ; // 새로 추가됐으므로
        // todo : 처음 평점 생성할때 0, ratedCnt 0
        double averageStar = (user.getAverageStar() * beforeRatedCnt) + star / beforeRatedCnt + 1;
        averageStar = Math.round(averageStar * 10 / 10); // 반올림
        System.out.println(averageStar);
        User updateUser = user.updateRating(averageStar, user.getRatedCnt() + 1);
        userRepository.save(updateUser);

        return averageStar;
    }


//    //10분마다 동작
//    //rating update
//    @Scheduled(cron = "0 0/10 * * * *")
//    public void schedulyUpdateAvgStar() {
//
//        System.out.println("regular avgStar Calculation updating routine started");
//        userRepository.findUserStar()
//
//
//
//    }










}
