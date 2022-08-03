package com.example.demo.src.service;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.entity.GroupInfo;
import com.example.demo.src.entity.User;
import com.example.demo.src.repository.GroupInfoRepository;
import com.example.demo.src.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final GroupInfoRepository groupInfoRepository;

    @Autowired
    public UserService(UserRepository userRepository, GroupInfoRepository groupInfoRepository) {
        this.userRepository = userRepository;
        this.groupInfoRepository = groupInfoRepository;
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

}
