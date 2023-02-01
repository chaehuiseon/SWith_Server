package com.swith.domain.user.service;


import com.swith.domain.user.entity.User;
import com.swith.domain.user.repository.UserRepository;
import com.swith.global.error.BaseResponseStatus;
import com.swith.global.error.exception.BaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getOneUser(Long userIdx){
        return userRepository.findById(userIdx)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_USER));

    }
}
