package com.example.demo.src.service;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.jwt.JwtTokenProvider;
import com.example.demo.jwt.TokenInfo;
import com.example.demo.src.dto.request.PostSignUpReq;
import com.example.demo.src.dto.response.PostSignInRes;
import com.example.demo.src.dto.response.PostSignUpRes;
import com.example.demo.src.entity.Interest;
import com.example.demo.src.entity.User;
import com.example.demo.src.exception.userServiceException.PasswordIncorrectException;
import com.example.demo.src.exception.userServiceException.UserNotFoundException;
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

//    @Bean
//    public BCryptPasswordEncoder getPasswordEncoder() {
//        return new BCryptPasswordEncoder();
//    }

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final GroupInfoRepository groupInfoRepository;

    @Autowired
    public UserService(UserRepository userRepository, JwtTokenProvider jwtTokenProvider, GroupInfoRepository groupInfoRepository) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.groupInfoRepository = groupInfoRepository;
    }
//    private final PasswordEncoder bCryptPasswordEncoder;

    // 회원 가입
    public PostSignUpRes signUp(PostSignUpReq postSignUpReq) throws BaseException {
        // 이메일 중복 시
        if(userRepository.existsByEmail(postSignUpReq.getEmail())){
            throw new BaseException(POST_USERS_EXISTS_EMAIL);
        }
        Interest interest1 = Interest.builder()
                .interestIdx(postSignUpReq.getInterest1())
                .build();
        Interest interest2 = Interest.builder()
                .interestIdx(postSignUpReq.getInterest2())
                .build();

        User user = buildUserForSignUp(postSignUpReq, interest1, interest2);
        User savedUser = userRepository.save(user);
        PostSignUpRes postSignUpRes = getSignUpResponseDto(savedUser);

        return postSignUpRes;
    }

    // 로그인
    public PostSignInRes signIn(String email, String password) throws BaseException {
        User user = userRepository.findByEmail(email);
        // 이메일 없는 경우
        if(user == null){
            throw new BaseException(ERROR_FIND_EMAIL);
        }
        // 비밀번호 틀린 경우
        if(!password.equals(user.getPassword())){
            throw new BaseException(POST_USERS_INVALID_PASSWORD);
        }
//        if(!bCryptPasswordEncoder.matches(password, user.getPassword())){
//            throw new PasswordIncorrectException("비밀번호가 일치하지 않습니다.");
//        }

        TokenInfo accessTokenDto = jwtTokenProvider.createJwtAccessToken(email);
        TokenInfo refreshTokenDto = jwtTokenProvider.createJwtRefreshToken(email);
        user.updateRefreshToken(refreshTokenDto.getToken());

        PostSignInRes postSignInRes = PostSignInRes.builder()
                .email(user.getEmail())
                .nickname(user.getNickname())
                .interest1(user.getInterest1())
                .interest2(user.getInterest2())
                .accessToken(accessTokenDto.getToken())
                .refreshToken(refreshTokenDto.getToken())
                .status(user.getStatus())
                .build();

        return postSignInRes;
    }

    public boolean isAdminOfGroup (Long userIdx, Long GroupIdx) throws BaseException{
        User user = userRepository.findById(userIdx)
                .orElseThrow(() -> new BaseException(INVALID_USERIDX));
        GroupInfo groupInfo = groupInfoRepository.findById(GroupIdx)
                .orElseThrow(() -> new BaseException(INVALID_GROUPIDX));
        if(groupInfo.getUser() == user){
            return true;
        }
        return false;
    }

    private User buildUserForSignUp(PostSignUpReq postSignUpReq, Interest interest1, Interest interest2) {
        User user = User.builder()
                .email(postSignUpReq.getEmail())
                .password(postSignUpReq.getPassword())
                .nickname(postSignUpReq.getNickname())
                .interest1(interest1)
                .interest2(interest2)
                .introduction(postSignUpReq.getIntroduction())
                .status(0)
                .build();
        return user;
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
}
