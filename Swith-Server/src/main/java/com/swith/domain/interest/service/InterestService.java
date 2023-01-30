package com.swith.domain.interest.service;


import com.swith.domain.interest.entity.Interest;
import com.swith.domain.interest.repository.InterestRepository;
import com.swith.global.error.BaseResponseStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class InterestService {

    private final InterestRepository interestRepository;

    @Autowired
    public InterestService(InterestRepository interestRepository) {
        this.interestRepository = interestRepository;
    }

    public Interest getOneInterest(Integer interestIdx){
        return interestRepository.findById(interestIdx).orElseThrow(
                () -> new IllegalArgumentException(String.valueOf(BaseResponseStatus.BAD_REQUEST_INTEREST))
        );

    }

}
