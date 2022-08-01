package com.example.demo.src.service;

import com.example.demo.src.dto.request.SignUpRequestDto;
import com.example.demo.src.dto.response.SignUpResponseDto;
import com.example.demo.src.entity.Interest;
import com.example.demo.src.entity.UserEntity;
import com.example.demo.src.exception.userServiceException.ExistsEmailException;
import com.example.demo.src.repository.UserInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.transaction.Transactional;
import javax.validation.Valid;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserInfoRepository userInfoRepository;

    @Transactional
    public SignUpResponseDto signUp(String email, String password, String nickname, Interest interest1, Interest interest2, String introduction){
        if(userInfoRepository.existsByEmail(email)){
            throw new ExistsEmailException("이미 가입된 이메일 입니다.");
        }
        userInfoRepository.save(
                new UserEntity(null, email, password, nickname, interest1, interest2, introduction, null, null, 0, null)
        );

        return new SignUpResponseDto(email, nickname, interest1, interest2, introduction);
    }
}
