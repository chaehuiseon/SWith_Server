package com.swith.src.service;


import com.swith.src.dto.request.PostRatingReq;
import com.swith.src.dto.request.PostRatingStarReq;
import com.swith.src.dto.request.Start;
import com.swith.src.dto.response.PostRatingRes;
import com.swith.src.entity.Rating;
import com.swith.src.entity.Register;
import com.swith.src.entity.User;
import com.swith.src.repository.RatingRepository;
import com.swith.src.repository.RegisterRepository;
import com.swith.src.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
        System.out.println(postRatingStarReq.getStar());
        List<Start> star = postRatingStarReq.getStar();
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
        double averageStar;
        if(beforeRatedCnt == 0)
            averageStar = star;
        else averageStar = (user.getAverageStar() * beforeRatedCnt + star) / (beforeRatedCnt + 1);
        // 소수점 첫째자리까지 반올림
        String str = String.format("%.1f", averageStar);
        averageStar = Double.parseDouble(str);
        User updateUser = user.updateRating(averageStar, user.getRatedCnt() + 1);
        userRepository.save(updateUser);

        return averageStar;
    }

}
