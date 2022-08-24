package com.example.demo.src.service;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.jwt.JwtTokenProvider;
import com.example.demo.jwt.TokenInfo;
import com.example.demo.src.dto.request.PostSignUpAndInReq;
import com.example.demo.src.dto.request.PostUserInfoReq;
import com.example.demo.src.dto.request.PostSignUpReq;
import com.example.demo.src.dto.response.PostUserInfoRes;
import com.example.demo.src.dto.response.PostSignUpRes;
import com.example.demo.src.entity.Interest;
import com.example.demo.src.entity.User;
import com.example.demo.src.enums.RoleType;
import com.example.demo.src.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.src.entity.GroupInfo;
import com.example.demo.src.repository.GroupInfoRepository;
import org.springframework.transaction.annotation.Transactional;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final GroupInfoRepository groupInfoRepository;

    @Autowired
    public UserService(UserRepository userRepository, JwtTokenProvider jwtTokenProvider, GroupInfoRepository groupInfoRepository) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.groupInfoRepository = groupInfoRepository;
    }

    // 회원 DB 조회
    public PostUserInfoRes userInfo(PostUserInfoReq postUserInfoReq) throws BaseException {
        User findUser = userRepository.findByUserIdx(postUserInfoReq.getUserIdx());
        if(findUser == null){
            throw new BaseException(NOT_EXIST_USER);
        }

        PostUserInfoRes postUserInfoRes = PostUserInfoRes.builder()
                .userIdx(findUser.getUserIdx())
                .email(findUser.getEmail())
                .nickname(findUser.getNickname())
                .profileImgUrl(findUser.getProfileImgUrl())
                .introduction(findUser.getIntroduction())
                .interestIdx1(findUser.getInterest1().getInterestIdx())
                .interestIdx2(findUser.getInterest2().getInterestIdx())
                .averageStar(findUser.getAverageStar())
                .role(findUser.getRole())
                .refreshToken(findUser.getRefreshToken())
                .status(findUser.getStatus())
                .build();

        return postUserInfoRes;
    }

    // 초기 회원 정보 등록
    public PostSignUpRes register(PostSignUpReq postSignUpReq) throws BaseException {
        User findUser = userRepository.findByEmail(postSignUpReq.getEmail());
        if(findUser == null){
            throw new BaseException(ERROR_FIND_EMAIL);
        }
        Interest interest1 = Interest.builder()
                .interestIdx(postSignUpReq.getInterest1())
                .build();
        Interest interest2 = Interest.builder()
                .interestIdx(postSignUpReq.getInterest2())
                .build();

        User user = findUser.update(postSignUpReq.getNickname(), interest1, interest2, postSignUpReq.getIntroduction());
        User savedUser = userRepository.save(user);

        PostSignUpRes postSignUpRes = getSignUpResponseDto(savedUser);

        return postSignUpRes;
    }

    // 회원가입 및 로그인
    public PostUserInfoRes signUpAndIn(PostSignUpAndInReq postSignUpAndInReq) throws BaseException {
        User findUser = userRepository.findByEmail(postSignUpAndInReq.getEmail());
        TokenInfo accessTokenDto = jwtTokenProvider.createJwtAccessToken(postSignUpAndInReq.getEmail());
        TokenInfo refreshTokenDto = jwtTokenProvider.createJwtRefreshToken(postSignUpAndInReq.getEmail());
        // 이메일 없는 경우 -> isSignUp -> true로 변경
        if(findUser == null){
            User user = buildUserForSignUp(postSignUpAndInReq, refreshTokenDto);

            Interest interest1 = Interest.builder()
                    .interestIdx(1)
                    .build();
            Interest interest2 = Interest.builder()
                    .interestIdx(2)
                    .build();

            User updateUser = user.updateInterest(interest1, interest2);

            User savedUser = userRepository.save(updateUser);
            PostUserInfoRes postUserInfoRes = PostUserInfoRes.builder()
                    .userIdx(savedUser.getUserIdx())
                    .email(savedUser.getEmail())
                    .nickname(savedUser.getNickname())
                    .profileImgUrl(savedUser.getProfileImgUrl())
                    .introduction(savedUser.getIntroduction())
                    .interestIdx1(1)
                    .interestIdx2(2)
                    .averageStar(savedUser.getAverageStar())
                    .role(savedUser.getRole())
                    .accessToken(accessTokenDto.getToken())
                    .refreshToken(refreshTokenDto.getToken())
                    .isSignUp(true)
                    .status(savedUser.getStatus())
                    .build();

            return postUserInfoRes;
        }
        else{
            // refreshToken DB에 저장
            User user = findUser.updateRefreshToken(refreshTokenDto.getToken());
            User savedUser = userRepository.save(user);

            PostUserInfoRes postUserInfoRes = getSignUpAndInResponseDto(savedUser, accessTokenDto, refreshTokenDto);

            return postUserInfoRes;
        }
    }

    public boolean isAdminOfGroup (Long userIdx, Long GroupIdx) throws BaseException{
        User user = userRepository.findById(userIdx)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_USER));
        GroupInfo groupInfo = groupInfoRepository.findById(GroupIdx)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_GROUP));
        if(groupInfo.getUser() == user){
            return true;
        }
        return false;
    }

    private PostUserInfoRes getSignUpAndInResponseDto(User savedUser, TokenInfo accessTokenDto, TokenInfo refreshTokenDto){
        PostUserInfoRes postUserInfoRes = PostUserInfoRes.builder()
                .userIdx(savedUser.getUserIdx())
                .email(savedUser.getEmail())
                .nickname(savedUser.getNickname())
                .profileImgUrl(savedUser.getProfileImgUrl())
                .introduction(savedUser.getIntroduction())
                .interestIdx1(savedUser.getInterest1().getInterestIdx())
                .interestIdx2(savedUser.getInterest2().getInterestIdx())
                .averageStar(savedUser.getAverageStar())
                .role(savedUser.getRole())
                .accessToken(accessTokenDto.getToken())
                .refreshToken(refreshTokenDto.getToken())
                .isSignUp(false)
                .status(savedUser.getStatus())
                .build();
        return postUserInfoRes;
    }

    private PostSignUpRes getSignUpResponseDto(User savedUser) {
        PostSignUpRes postSignUpRes = PostSignUpRes.builder()
                .email(savedUser.getEmail())
                .interestIdx1(savedUser.getInterest1().getInterestIdx())
                .interestIdx2(savedUser.getInterest2().getInterestIdx())
                .nickname(savedUser.getNickname())
                .introduction(savedUser.getIntroduction())
                .build();
        return postSignUpRes;
    }

<<<<<<< HEAD

=======
    // 로그아웃
    public void logout(String accessToken, String email) throws BaseException{
        User findUser = userRepository.findByEmail(email);
        // 유저 없는 경우
        if(findUser == null){
            throw new BaseException(NOT_EXIST_USER);
        }
        // 엑세스 토큰이 아닌 리프레시 토큰으로 로그아웃을 시도한경우
        if(accessToken.equals(findUser.getRefreshToken())){
            throw new BaseException(REFRESH_LOGOUT);
        }
        // 이미 로그아웃한경우
        if(findUser.getRefreshToken() == null){
            throw new BaseException(ALREADY_LOGOUT);
        }
        User user = findUser.updateRefreshToken(null);
        userRepository.save(user);
    }

    private User buildUserForSignUp(PostSignUpAndInReq postSignUpAndInReq, TokenInfo refreshTokenDto) {
        User user = User.builder()
                .email(postSignUpAndInReq.getEmail())
                .nickname(postSignUpAndInReq.getNickname())
                .profileImgUrl(postSignUpAndInReq.getProfileImgUrl())
                .role(RoleType.GUEST)
                .refreshToken(refreshTokenDto.getToken())
                .status(0)
                .build();
        return user;
    }
>>>>>>> 9f9569e7a9a816d777e5dfba03f6185d17685fbd
}
