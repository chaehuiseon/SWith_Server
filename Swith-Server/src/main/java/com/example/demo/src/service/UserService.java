package com.example.demo.src.service;

import com.example.demo.src.dto.response.SignUpResponseDto;
import com.example.demo.src.entity.Interest;
import com.example.demo.src.entity.UserEntity;
import com.example.demo.src.exception.userServiceException.ExistsEmailException;
import com.example.demo.src.repository.UserInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserInfoRepository userInfoRepository;

    @Transactional
    public SignUpResponseDto signUp(String email, String password, Interest interest1, Interest interest2, String introduction){
        if(userInfoRepository.existsByEmail(email)){
            throw new ExistsEmailException("이미 가입된 이메일 입니다.");
        }
        userInfoRepository.save(
                new UserEntity(null, email, "test", interest1, interest2, introduction, null, null, 0, null)
        );

        return new SignUpResponseDto(email, interest1, interest2, introduction);
    }
}
