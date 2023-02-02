package com.swith.domain.application.repository;

import com.swith.domain.application.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application,Long> {


    @Query("select count(a) from Application a "+
            "where a.groupInfo.groupIdx = :groupIdx "+
            "and a.status = 1")
    Long findNumOfApplicants(@Param("groupIdx")Long groupIdx);


    @Query("select a " +
            "from Application a " +
            "where a.groupInfo.groupIdx = :groupIdx " +
            "and a.user.userIdx = :userIdx ")
    Application findByGroupIdxAndUserIdx(@Param("groupIdx")Long groupIdx, @Param("userIdx")Long userIdx);


    @Query("select a "+
            "from Application a "+
            "join fetch a.user u "+
            "where a.groupInfo.groupIdx = :groupIdx and a.status = :status ")
    List<Application> getApplicationListBy(@Param("groupIdx")Long groupIdx, @Param("status")Integer status);



    //nativequery는 진짜 SQL 문법기준으로 작성해야됨..+ nativeQuery쓴이유 : 특정 컬럼만 업데이트하려고 상태만 변경해야하는데 위험해서 (더티체킹어려운상황)
    @Modifying(clearAutomatically = true)
    @Query(value = "update APPLICATION a " +
            "set a.status = :reqstatus " +
            "where a.applicationIdx = :applicationIdx and a.groupIdx = :groupIdx and a.status = :status ",nativeQuery = true)
    Integer updateStatusOfApplication(@Param("reqstatus")Integer reqstatus, @Param("applicationIdx")Long applicationIdx,
                                      @Param("groupIdx")Long groupIdx, @Param("status") Integer status);

    @Query("select a " +
            "from Application a " +
            "join fetch a.groupInfo " +
            "where a.user.userIdx = :userIdx")
    List<Application> findByUserWithGroup(Long userIdx);

    @Query("select a.status " +
            "from Application a " +
            "where a.applicationIdx = :applicationIdx ")
    Integer findStatusOfApplication(@Param("applicationIdx")Long applicationIdx);
}
