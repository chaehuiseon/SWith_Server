package com.example.demo.src.service;

import com.example.demo.config.BaseException;
import com.example.demo.jwt.JwtTokenProvider;
import com.example.demo.jwt.TokenInfo;
import com.example.demo.src.dto.request.SignUpRequestDto;
import com.example.demo.src.dto.response.SignInResponseDto;
import com.example.demo.src.dto.response.SignUpResponseDto;
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

    public SignUpResponseDto signUp(SignUpRequestDto signUpRequestDto) throws BaseException {
        if(userRepository.existsByEmail(signUpRequestDto.getEmail())){
            throw new BaseException(POST_USERS_EXISTS_EMAIL);
        }
        Interest interest1 = Interest.builder()
                .interestIdx(signUpRequestDto.getInterest1())
                .build();
        Interest interest2 = Interest.builder()
                .interestIdx(signUpRequestDto.getInterest2())
                .build();

        User user = buildUserForSignUp(signUpRequestDto, interest1, interest2);
        User savedUser = userRepository.save(user
//                new UserEntity(null, email, bCryptPasswordEncoder.encode(password), nickname, interest1, interest2, introduction, null, null, 0, null, null)
        );
        SignUpResponseDto signUpResponseDto = getSignUpResponseDto(savedUser);

        return signUpResponseDto;
    }

    public SignInResponseDto signIn(String email, String password){
        User user = userRepository.findByEmail(email);
        if(user == null){
            throw new UserNotFoundException("사용자를 찾을 수 없습니다.");
        }
        if(!password.equals(user.getPassword())){
            throw new PasswordIncorrectException("비밀번호가 일치하지 않습니다.");
        }

//        if(!bCryptPasswordEncoder.matches(password, user.getPassword())){
//            throw new PasswordIncorrectException("비밀번호가 일치하지 않습니다.");
//        }

        TokenInfo accessTokenDto = jwtTokenProvider.createJwtAccessToken(email);
        TokenInfo refreshTokenDto = jwtTokenProvider.createJwtRefreshToken(email);
        user.updateRefreshToken(refreshTokenDto.getToken());
        return new SignInResponseDto(user.getEmail(), user.getNickname(), user.getInterest1(), user.getInterest2(),
                accessTokenDto.getToken(), refreshTokenDto.getToken());
    }

    public boolean isAdminOfGroup (Long userIdx, Long GroupIdx){
        User user = userRepository.findById(userIdx).get();
//                .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_USERIDX));
        GroupInfo groupInfo = groupInfoRepository.findById(GroupIdx).get();
//                .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_GROUPIDX));
        if(groupInfo.getUser() == user){
            return true;
        }
        return false;
    }

    private User buildUserForSignUp(SignUpRequestDto signUpRequestDto, Interest interest1, Interest interest2) {
        User user = User.builder()
                .email(signUpRequestDto.getEmail())
                .password(signUpRequestDto.getPassword())
                .nickname(signUpRequestDto.getNickname())
                .interest1(interest1)
                .interest2(interest2)
                .introduction(signUpRequestDto.getIntroduction())
                .status(0)
                .build();
        return user;
    }

    private SignUpResponseDto getSignUpResponseDto(User savedUser) {
        SignUpResponseDto signUpResponseDto = SignUpResponseDto.builder()
                .email(savedUser.getEmail())
                .interestIdx1(savedUser.getInterest1().getInterestIdx())
                .interestIdx2(savedUser.getInterest2().getInterestIdx())
                .nickname(savedUser.getNickname())
                .introduction(savedUser.getIntroduction())
                .build();
        return signUpResponseDto;
    }
}
