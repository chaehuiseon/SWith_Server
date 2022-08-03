package com.example.demo.src.service;

import com.example.demo.jwt.JwtTokenProvider;
import com.example.demo.jwt.TokenInfo;
import com.example.demo.src.dto.request.SignInRequestDto;
import com.example.demo.src.dto.request.SignUpRequestDto;
import com.example.demo.src.dto.response.SignInResponseDto;
import com.example.demo.src.dto.response.SignUpResponseDto;
import com.example.demo.src.entity.Interest;
import com.example.demo.src.entity.UserEntity;
import com.example.demo.src.exception.userServiceException.ExistsEmailException;
import com.example.demo.src.exception.userServiceException.PasswordIncorrectException;
import com.example.demo.src.exception.userServiceException.UserNotFoundException;
import com.example.demo.src.repository.UserInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.transaction.Transactional;
import javax.validation.Valid;

@Service
@RequiredArgsConstructor
public class UserService {

//    @Bean
//    public BCryptPasswordEncoder getPasswordEncoder() {
//        return new BCryptPasswordEncoder();
//    }

    private final UserInfoRepository userInfoRepository;
    private final JwtTokenProvider jwtTokenProvider;
//    private final PasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public SignUpResponseDto signUp(String email, String password, String nickname, Interest interest1, Interest interest2, String introduction){
        if(userInfoRepository.existsByEmail(email)){
            throw new ExistsEmailException("이미 가입된 이메일 입니다.");
        }
        userInfoRepository.save(
//                new UserEntity(null, email, bCryptPasswordEncoder.encode(password), nickname, interest1, interest2, introduction, null, null, 0, null, null)
                  new UserEntity(null, email, password, nickname, interest1, interest2, introduction, null, null, 0, null, null)
        );

        return new SignUpResponseDto(email, nickname, interest1, interest2, introduction);
    }

    @Transactional
    public SignInResponseDto signIn(String email, String password){
        UserEntity user = userInfoRepository.findByEmail(email);
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
}
