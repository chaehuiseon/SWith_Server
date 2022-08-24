package com.example.demo.src.repository;

import com.example.demo.src.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    @Query("select a " +
            "from Application a " +
            "join fetch a.groupInfo " +
            "where a.user.userIdx = :userIdx")
    List<Application> findByUserWithGroup(Long userIdx);
}
