package com.swith.api.user.service;

import com.swith.global.error.exception.BaseException;
import com.swith.global.error.ErrorCode;
import com.swith.global.jwt.JwtTokenProvider;
import com.swith.global.jwt.TokenInfo;
import com.swith.api.user.dto.PostSignUpAndInReq;
import com.swith.api.user.dto.PostUserInfoReq;
import com.swith.api.user.dto.PostSignUpReq;
import com.swith.api.user.dto.PostUserInfoRes;
import com.swith.api.user.dto.PostSignUpRes;
import com.swith.domain.interest.entity.Interest;
import com.swith.domain.user.entity.User;
import com.swith.domain.user.constant.RoleType;
import com.swith.domain.user.repository.UserRepository;
import com.swith.domain.groupinfo.repository.GroupInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.swith.domain.groupinfo.entity.GroupInfo;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserApiService {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final GroupInfoRepository groupInfoRepository;

    @Autowired
    public UserApiService(UserRepository userRepository, JwtTokenProvider jwtTokenProvider, GroupInfoRepository groupInfoRepository) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.groupInfoRepository = groupInfoRepository;
    }



    // 회원 DB 조회
    public PostUserInfoRes userInfo(PostUserInfoReq postUserInfoReq){
        User findUser = userRepository.findByUserIdx(postUserInfoReq.getUserIdx());
        if(findUser == null){
            throw new BaseException(ErrorCode.NOT_EXIST_USER);
        }
        return PostUserInfoRes.toDetailUser(findUser);
    }

    // 초기 회원 정보 등록
    public PostSignUpRes register(PostSignUpReq postSignUpReq){
        User findUser = userRepository.findByEmail(postSignUpReq.getEmail());
        if(findUser == null){
            throw new BaseException(ErrorCode.ERROR_FIND_EMAIL);
        }
        Interest interest1 = buildInterest(postSignUpReq.getInterest1());
        Interest interest2 = buildInterest(postSignUpReq.getInterest2());

        User user = findUser.update(postSignUpReq.getNickname(), interest1, interest2, postSignUpReq.getIntroduction(), postSignUpReq.getRegion());
        User savedUser = userRepository.save(user);

        PostSignUpRes postSignUpRes = getSignUpResponseDto(savedUser);

        return postSignUpRes;
    }

    // 회원가입 및 로그인
    public PostUserInfoRes signUpAndIn(PostSignUpAndInReq postSignUpAndInReq){
        User findUser = userRepository.findByEmail(postSignUpAndInReq.getEmail());
        TokenInfo accessTokenDto = jwtTokenProvider.createJwtAccessToken(postSignUpAndInReq.getEmail());
        TokenInfo refreshTokenDto = jwtTokenProvider.createJwtRefreshToken(postSignUpAndInReq.getEmail());
        // 이메일 없는 경우 -> isSignUp -> true로 변경
        if(findUser == null){
            User user = buildUserForSignUp(postSignUpAndInReq, refreshTokenDto);
            Interest interest1 = buildInterest(1);
            Interest interest2 = buildInterest(2);

            User updateUser = user.updateInterest(interest1, interest2);
            User savedUser = userRepository.save(updateUser);
            return PostUserInfoRes.from(savedUser, 1, 2, accessTokenDto, refreshTokenDto);
        }
        else{
            // refreshToken DB에 저장
            User user = findUser.updateRefreshToken(refreshTokenDto.getToken());
            User savedUser = userRepository.save(user);

            return PostUserInfoRes.from(savedUser,savedUser.getInterest1().getInterestIdx(),
                    savedUser.getInterest2().getInterestIdx(), accessTokenDto, refreshTokenDto);
        }
    }

    public boolean isAdminOfGroup(Long userIdx, Long GroupIdx) {
        User user = userRepository.findById(userIdx)
                .orElseThrow(() -> new BaseException(ErrorCode.INVALID_USER));
        GroupInfo groupInfo = groupInfoRepository.findById(GroupIdx)
                .orElseThrow(() -> new BaseException(ErrorCode.INVALID_GROUP));
        if(groupInfo.getUser() == user){
            return true;
        }
        return false;
    }

    private PostSignUpRes getSignUpResponseDto(User savedUser) {
        return PostSignUpRes.toDetailUser(savedUser);
    }

    // 로그아웃
    public void logout(String accessToken, String email){
        User findUser = userRepository.findByEmail(email);
        // 유저 없는 경우
        if(findUser == null){
            throw new BaseException(ErrorCode.NOT_EXIST_USER);
        }
        // 엑세스 토큰이 아닌 리프레시 토큰으로 로그아웃을 시도한경우
        if(accessToken.equals(findUser.getRefreshToken())){
            throw new BaseException(ErrorCode.REFRESH_LOGOUT);
        }
        // 이미 로그아웃한경우
        if(findUser.getRefreshToken() == null){
            throw new BaseException(ErrorCode.ALREADY_LOGOUT);
        }
        User user = findUser.updateRefreshToken(null);
        userRepository.save(user);
    }

    private User buildUserForSignUp(PostSignUpAndInReq postSignUpAndInReq, TokenInfo refreshTokenDto) {
        User user = User.builder()
                .email(postSignUpAndInReq.getEmail())
                .nickname(postSignUpAndInReq.getNickname())
                .averageStar(0.0)
                .ratedCnt(0L)
                .profileImgUrl(postSignUpAndInReq.getProfileImgUrl())
                .role(RoleType.GUEST)
                .refreshToken(refreshTokenDto.getToken())
                .fcmtoken(postSignUpAndInReq.getToken())
                .status(0)
                .build();
        return user;
    }

    private Interest buildInterest(Integer interestIdx){
        Interest interest = Interest.builder()
                .interestIdx(interestIdx)
                .build();
        return interest;
    }
}