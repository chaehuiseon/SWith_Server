package com.example.demo.src.service;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.jwt.JwtTokenProvider;
import com.example.demo.src.dto.request.PostSignUpReq;
import com.example.demo.src.dto.response.PostSignUpRes;
import com.example.demo.src.entity.Interest;
import com.example.demo.src.entity.User;
import com.example.demo.src.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.src.entity.GroupInfo;
import com.example.demo.src.repository.GroupInfoRepository;
import org.springframework.transaction.annotation.Transactional;

import static com.example.demo.config.BaseResponseStatus.ERROR_FIND_EMAIL;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final GroupInfoRepository groupInfoRepository;
//    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, JwtTokenProvider jwtTokenProvider, GroupInfoRepository groupInfoRepository) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.groupInfoRepository = groupInfoRepository;
    }

    // 회원 정보 업데이트
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

//    // 회원 가입
//    public PostSignUpRes signUp(PostSignUpReq postSignUpReq) throws BaseException {
//        // 이메일 중복 시
//        if(userRepository.existsByEmail(postSignUpReq.getEmail())){
//            throw new BaseException(POST_USERS_EXISTS_EMAIL);
//        }
//        Interest interest1 = Interest.builder()
//                .interestIdx(postSignUpReq.getInterest1())
//                .build();
//        Interest interest2 = Interest.builder()
//                .interestIdx(postSignUpReq.getInterest2())
//                .build();
//
//        User user = buildUserForSignUp(postSignUpReq, interest1, interest2);
//        User savedUser = userRepository.save(user);
//        PostSignUpRes postSignUpRes = getSignUpResponseDto(savedUser);
//
//        return postSignUpRes;
//    }

//    // 로그인
//    public PostSignInRes signIn(String email, String password) throws BaseException {
//        User user = userRepository.findByEmail(email);
//        // 이메일 없는 경우
//        if(user == null){
//            throw new BaseException(ERROR_FIND_EMAIL);
//        }
//        // 비밀번호 틀린 경우
//        if(!password.equals(user.getPassword())){
//            throw new BaseException(POST_USERS_INVALID_PASSWORD);
//        }
////        if(!bCryptPasswordEncoder.matches(password, user.getPassword())){
////            throw new PasswordIncorrectException("비밀번호가 일치하지 않습니다.");
////        }
//
//        TokenInfo accessTokenDto = jwtTokenProvider.createJwtAccessToken(email);
//        TokenInfo refreshTokenDto = jwtTokenProvider.createJwtRefreshToken(email);
//        user.updateRefreshToken(refreshTokenDto.getToken());
//
//        PostSignInRes postSignInRes = PostSignInRes.builder()
//                .email(user.getEmail())
//                .nickname(user.getNickname())
//                .interest1(user.getInterest1())
//                .interest2(user.getInterest2())
//                .accessToken(accessTokenDto.getToken())
//                .refreshToken(refreshTokenDto.getToken())
//                .status(user.getStatus())
//                .build();
//
//        return postSignInRes;
//    }
//
//    // todo : 로그아웃 보완 필요
//    public void logout(String accessToken, String email) throws BaseException{
//        User user = userRepository.findByEmail(email);
//        // 유저 없는 경우
//        if(user == null){
//            throw new BaseException(NOT_EXIST_USER);
//        }
//        // 엑세스 토큰이 아닌 리프레시 토큰으로 로그아웃을 시도한경우
//        if(accessToken.equals(user.getRefreshToken())){
//            throw new BaseException(REFRESH_LOGOUT);
//        }
//        // 이미 로그아웃한경우
//        if(user.getRefreshToken() == null){
//            throw new BaseException(ALREADY_LOGOUT);
//        }
//        user.updateRefreshToken(null);
//        System.out.println("logout success!!!");
//    }

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

//    private User buildUserForSignUp(PostSignUpReq postSignUpReq, Interest interest1, Interest interest2) {
//        User user = User.builder()
//                .email(postSignUpReq.getEmail())
////                .password(postSignUpReq.getPassword())
////                .password((bCryptPasswordEncoder.encode(postSignUpReq.getPassword())))
//                .nickname(postSignUpReq.getNickname())
//                .interest1(interest1)
//                .interest2(interest2)
//                .introduction(postSignUpReq.getIntroduction())
//                .status(0)
//                .build();
//        return user;
//    }

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
